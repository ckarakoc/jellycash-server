package nl.ckarakoc.jellycash.manager.impl;

import lombok.RequiredArgsConstructor;
import nl.ckarakoc.jellycash.dto.*;
import nl.ckarakoc.jellycash.exception.AuthenticationException;
import nl.ckarakoc.jellycash.manager.AuthManager;
import nl.ckarakoc.jellycash.model.RefreshToken;
import nl.ckarakoc.jellycash.model.Role;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.model.enums.AppRole;
import nl.ckarakoc.jellycash.repository.RefreshTokenRepository;
import nl.ckarakoc.jellycash.security.service.JwtService;
import nl.ckarakoc.jellycash.service.RoleService;
import nl.ckarakoc.jellycash.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthManagerImpl implements AuthManager {
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final UserService userService;
	private final RoleService roleService;
	private final AuthenticationManager authenticationManager;
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	@Override
	public AuthRegisterResponseDto register(AuthRegisterRequestDto requestDto) {
		if (userService.existsByEmail(requestDto.getEmail())) throw new AuthenticationException("Email already exists");

		User user = modelMapper.map(requestDto, User.class);
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Role userRole = roleService.getRole(AppRole.USER);
		user.setRoles(Set.of(userRole));
		User created = userService.save(user);

		String accessToken = jwtService.generateToken(user);
		RefreshToken refreshToken = jwtService.generateRefreshToken(user);
		refreshTokenRepository.save(refreshToken);

		AuthRegisterResponseDto response = modelMapper.map(created, AuthRegisterResponseDto.class);
		response.setAccessToken(accessToken);
		response.setRefreshToken(refreshToken.getToken());
		return response;
	}

	@Override
	public AuthLoginResponseDto login(AuthLoginRequestDto requestDto, String accessToken) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));
		User user = (User) authentication.getPrincipal();

		if (!user.isEnabled()) throw new AuthenticationException("User is disabled");
		if (!user.isAccountNonExpired()) throw new AuthenticationException("User account has expired");
		if (!user.isAccountNonLocked()) throw new AuthenticationException("User account is locked");
		if (!user.isCredentialsNonExpired()) throw new AuthenticationException("User credentials have expired");

		if (accessToken == null) // no cookies
			accessToken = jwtService.generateToken(user);

		RefreshToken updated = refreshTokenRepository.findByUser(user)
			.orElse(new RefreshToken());
		RefreshToken newToken = jwtService.generateRefreshToken(user);

		updated.setToken(newToken.getToken());
		updated.setUser(user);
		updated.setExpiryDate(newToken.getExpiryDate());
		refreshTokenRepository.save(updated);

		return new AuthLoginResponseDto(accessToken, updated.getToken());
	}

	@Override
	public AuthRefreshResponseDto refresh(AuthRefreshRequestDto requestDto) {
		String refreshToken = requestDto.getRefreshToken();

		if (refreshToken == null) throw new AuthenticationException("Refresh token is null");
		if (!jwtService.isTokenValid(refreshToken)) throw new AuthenticationException("Refresh token is invalid");

		String email = jwtService.extractUsername(refreshToken);
		User user = userService.findByEmail(email);

		if (!refreshTokenRepository.existsByTokenAndUser(refreshToken, user)) {
			throw new AuthenticationException("Refresh token expired");
		}

		return new AuthRefreshResponseDto(jwtService.generateToken(user), refreshToken);
	}

	@Override
	public AuthLogoutResponseDto logout(AuthLogoutRequestDto requestDto) {
		String refreshToken = requestDto.getRefreshToken();
		if (refreshToken == null) throw new AuthenticationException("Refresh token is required for logout");
		refreshTokenRepository.deleteByToken(refreshToken);
		SecurityContextHolder.clearContext();

		return new AuthLogoutResponseDto(true);
	}

	private void deleteRefreshToken(String refreshToken) {
		refreshTokenRepository.deleteByToken(refreshToken);
	}

	@Override
	public LoggedInUserDto getLoggedInUserInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
			throw new AuthenticationException("No user authenticated");

		String email = authentication.getName();

		User user = userService.findByEmail(email);
		return modelMapper.map(user, LoggedInUserDto.class);
	}
}
