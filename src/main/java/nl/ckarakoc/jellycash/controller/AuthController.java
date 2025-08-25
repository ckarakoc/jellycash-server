package nl.ckarakoc.jellycash.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.ckarakoc.jellycash.config.AppConstants;
import nl.ckarakoc.jellycash.dto.*;
import nl.ckarakoc.jellycash.manager.AuthManager;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthManager authManager;
	// todo: /auth/forgot-password | /auth/reset-password ( | /auth/verify-email | /auth/verify-phone | /auth/resend-verification )

	@PostMapping("/login")
	public ResponseEntity<AuthLoginResponseDto> login(
		@CookieValue(name = AppConstants.JwtCookieNames.ACCESS_TOKEN, required = false) String accessToken,
		@RequestBody @Valid AuthLoginRequestDto authLoginRequestDto) {
		log.info("Access token: {}", accessToken);
		return ResponseEntity.ok(authManager.login(authLoginRequestDto, accessToken));
	}

	@PostMapping("/logout")
	public ResponseEntity<AuthLogoutResponseDto> logout(@RequestBody @Valid AuthLogoutRequestDto authLogoutRequestDto,
	                                                    HttpServletRequest request,
	                                                    HttpServletResponse response) {
		Cookie refreshCookie = WebUtils.getCookie(request, AppConstants.JwtCookieNames.REFRESH_TOKEN);

		ResponseCookie clearAccess = ResponseCookie.from(AppConstants.JwtCookieNames.ACCESS_TOKEN, "")
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(0)
			.sameSite("Strict")
			.build();

		ResponseCookie clearRefresh = ResponseCookie.from(AppConstants.JwtCookieNames.REFRESH_TOKEN, "")
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(0)
			.sameSite("Strict")
			.build();

		response.addHeader("Set-Cookie", clearAccess.toString());
		response.addHeader("Set-Cookie", clearRefresh.toString());

		if (refreshCookie != null) {
			// Deletes refresh token from database (cookie)
			authManager.logout(new AuthLogoutRequestDto(refreshCookie.getValue()));
		}
		// Deletes refresh token from database (client)
		return ResponseEntity.ok(authManager.logout(authLogoutRequestDto));
	}

	@PostMapping("/register")
	public ResponseEntity<AuthRegisterResponseDto> register(@RequestBody @Valid AuthRegisterRequestDto authRegisterRequestDto,
	                                                        HttpServletResponse response) {
		AuthRegisterResponseDto tokens = authManager.register(authRegisterRequestDto);
		ResponseCookie accessCookie = ResponseCookie.from(AppConstants.JwtCookieNames.ACCESS_TOKEN, tokens.getAccessToken())
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(AppConstants.JwtTokenExpiry.ACCESS_TOKEN_EXPIRY)
			.sameSite("Strict")
			.build();
		ResponseCookie refreshCookie = ResponseCookie.from(AppConstants.JwtCookieNames.REFRESH_TOKEN, tokens.getRefreshToken())
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(AppConstants.JwtTokenExpiry.REFRESH_TOKEN_EXPIRY)
			.sameSite("Strict")
			.build();

		response.addHeader("Set-Cookie", accessCookie.toString());
		response.addHeader("Set-Cookie", refreshCookie.toString());
		return ResponseEntity.ok(tokens);
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthRefreshResponseDto> refresh(@RequestBody @Valid AuthRefreshRequestDto authRefreshRequestDto,
	                                                      HttpServletRequest request,
	                                                      HttpServletResponse response) {
		Cookie refreshCookie = WebUtils.getCookie(request, AppConstants.JwtCookieNames.REFRESH_TOKEN);
		if (refreshCookie == null) {
			// We go the Header based authentication route
			return ResponseEntity.ok(authManager.refresh(authRefreshRequestDto));
		}

		AuthRefreshResponseDto tokens = authManager.refresh(new AuthRefreshRequestDto(refreshCookie.getValue()));
		ResponseCookie accessCookie = ResponseCookie.from(AppConstants.JwtCookieNames.ACCESS_TOKEN, tokens.getAccessToken())
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(AppConstants.JwtTokenExpiry.ACCESS_TOKEN_EXPIRY)
			.sameSite("Strict")
			.build();
		response.addHeader("Set-Cookie", accessCookie.toString());
		return ResponseEntity.ok(tokens);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/me")
	public ResponseEntity<LoggedInUserDto> loggedInUserInfo() {
		return ResponseEntity.ok(authManager.getLoggedInUserInfo());
	}

}
