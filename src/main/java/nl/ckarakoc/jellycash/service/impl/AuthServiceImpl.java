package nl.ckarakoc.jellycash.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import nl.ckarakoc.jellycash.dto.LoggedInUserDto;
import nl.ckarakoc.jellycash.dto.auth.AuthLoginRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthMessageResponseDto;
import nl.ckarakoc.jellycash.dto.auth.AuthRefreshRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthRegisterRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthStatusDto;
import nl.ckarakoc.jellycash.dto.auth.AuthTokenResponseDto;
import nl.ckarakoc.jellycash.exception.AuthenticationConflictException;
import nl.ckarakoc.jellycash.exception.AuthenticationException;
import nl.ckarakoc.jellycash.exception.AuthenticationForbiddenException;
import nl.ckarakoc.jellycash.model.AppRole;
import nl.ckarakoc.jellycash.model.RefreshToken;
import nl.ckarakoc.jellycash.model.Role;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.repository.RefreshTokenRepository;
import nl.ckarakoc.jellycash.repository.RoleRepository;
import nl.ckarakoc.jellycash.repository.UserRepository;
import nl.ckarakoc.jellycash.security.service.JwtService;
import nl.ckarakoc.jellycash.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @Transactional
  @Override
  public AuthTokenResponseDto register(AuthRegisterRequestDto requestDto) {
    if (userRepository.existsByEmail(requestDto.getEmail())) {
      throw new AuthenticationConflictException("Email already exists");
    }

    User user = modelMapper.map(requestDto, User.class);
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    Role userRole = roleRepository.findByRole(AppRole.USER)
        .orElseThrow(() -> new RuntimeException("Role not found"));

    user.setRoles(Set.of(userRole));
    userRepository.save(user);

    String accessToken = jwtService.generateToken(user);
    RefreshToken refreshToken = jwtService.generateRefreshToken(user);
    refreshTokenRepository.save(refreshToken);

    return AuthTokenResponseDto.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken.getToken())
        .build();
  }

  @Override
  public AuthTokenResponseDto login(AuthLoginRequestDto requestDto) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));
    User user = (User) authentication.getPrincipal();

    if (!user.isEnabled()) {
      throw new AuthenticationException("User is disabled");
    }
    if (!user.isAccountNonExpired()) {
      throw new AuthenticationException("User account has expired");
    }
    if (!user.isAccountNonLocked()) {
      throw new AuthenticationException("User account is locked");
    }
    if (!user.isCredentialsNonExpired()) {
      throw new AuthenticationException("User credentials have expired");
    }

    String accessToken = jwtService.generateToken(user);
    RefreshToken newToken = jwtService.generateRefreshToken(user);

    RefreshToken fromDb = refreshTokenRepository.findByUser(user)
        .orElse(new RefreshToken());

    fromDb.setToken(newToken.getToken());
    fromDb.setUser(user);
    fromDb.setExpiryDate(newToken.getExpiryDate());
    refreshTokenRepository.save(fromDb);

    return new AuthTokenResponseDto(accessToken, fromDb.getToken());
  }

  @Override
  public AuthTokenResponseDto refresh(AuthRefreshRequestDto requestDto) {
    String refreshToken = requestDto.getRefreshToken();

    if (refreshToken == null) {
      throw new AuthenticationException("Refresh token is null");
    }

    if (!jwtService.isTokenValid(refreshToken)) {
      throw new AuthenticationException("Refresh token is invalid");
    }

    String email = jwtService.extractUsername(refreshToken);
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new AuthenticationException("User not found"));

    if (!refreshTokenRepository.existsByTokenAndUser(refreshToken, user)) {
      throw new AuthenticationException("Refresh token expired");
    }

    return new AuthTokenResponseDto(jwtService.generateToken(user), refreshToken);
  }

  @Override
  public AuthMessageResponseDto logout(User user) {
    if (user == null) {
      return new AuthMessageResponseDto(false, "User is not logged in");
    }
    refreshTokenRepository.deleteByUser(user);
    SecurityContextHolder.clearContext();
    return new AuthMessageResponseDto(true, "Logout successful");
  }

  @Override
  public LoggedInUserDto getLoggedInUserInfo() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      throw new AuthenticationForbiddenException("No user authenticated");
    }

    String email = authentication.getName();

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new AuthenticationException("User not found"));
    return modelMapper.map(user, LoggedInUserDto.class);
  }

  /**
   * Lightweight check whether the user is authenticated or not.
   *
   * @param token the Jwt token
   * @return the authentication status
   */
  @Override
  public AuthStatusDto checkAuthenticationStatus(String token) {
    if (jwtService.isTokenValid(token)) {
      return new AuthStatusDto(true);
    }
    return new AuthStatusDto(false);
  }

}
