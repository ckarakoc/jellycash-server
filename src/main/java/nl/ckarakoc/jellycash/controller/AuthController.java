package nl.ckarakoc.jellycash.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.ckarakoc.jellycash.config.AppConstants;
import nl.ckarakoc.jellycash.dto.auth.AuthLoginRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthLoginResponseDto;
import nl.ckarakoc.jellycash.dto.auth.AuthLogoutRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthLogoutResponseDto;
import nl.ckarakoc.jellycash.dto.auth.AuthRefreshRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthRefreshResponseDto;
import nl.ckarakoc.jellycash.dto.auth.AuthRegisterRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthRegisterResponseDto;
import nl.ckarakoc.jellycash.dto.auth.AuthStatusDto;
import nl.ckarakoc.jellycash.dto.LoggedInUserDto;
import nl.ckarakoc.jellycash.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;
  // TODO: /auth/forgot-password | /auth/reset-password ( | /auth/verify-email | /auth/verify-phone | /auth/resend-verification )

  @PostMapping("/login")
  public ResponseEntity<AuthLoginResponseDto> login(
      @CookieValue(name = AppConstants.JwtCookieNames.ACCESS_TOKEN, required = false) String accessToken,
      @RequestBody @Valid AuthLoginRequestDto authLoginRequestDto) {
    log.info("Access token: {}", accessToken);
    return ResponseEntity.ok(authService.login(authLoginRequestDto, accessToken));
  }

  @PostMapping("/logout")
  public ResponseEntity<AuthLogoutResponseDto> logout(
      @RequestBody @Valid AuthLogoutRequestDto authLogoutRequestDto,
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
      authService.logout(new AuthLogoutRequestDto(refreshCookie.getValue()));
    }
    // Deletes refresh token from database (client)
    return ResponseEntity.ok(authService.logout(authLogoutRequestDto));
  }

  @PostMapping("/register")
  public ResponseEntity<Void> register(
      @RequestBody @Valid AuthRegisterRequestDto authRegisterRequestDto,
      HttpServletResponse response) {
    AuthRegisterResponseDto tokens = authService.register(authRegisterRequestDto);
    ResponseCookie accessCookie = ResponseCookie.from(AppConstants.JwtCookieNames.ACCESS_TOKEN,
            tokens.getAccessToken())
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(AppConstants.JwtTokenExpiry.ACCESS_TOKEN_EXPIRY)
        .sameSite("Strict")
        .build();
    ResponseCookie refreshCookie = ResponseCookie.from(AppConstants.JwtCookieNames.REFRESH_TOKEN,
            tokens.getRefreshToken())
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(AppConstants.JwtTokenExpiry.REFRESH_TOKEN_EXPIRY)
        .sameSite("Strict")
        .build();

    response.addHeader("Set-Cookie", accessCookie.toString());
    response.addHeader("Set-Cookie", refreshCookie.toString());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthRefreshResponseDto> refresh(
      @RequestBody @Valid AuthRefreshRequestDto authRefreshRequestDto,
      HttpServletRequest request,
      HttpServletResponse response) {
    Cookie refreshCookie = WebUtils.getCookie(request, AppConstants.JwtCookieNames.REFRESH_TOKEN);
    if (refreshCookie == null) {
      // We go the Header based authentication route
      return ResponseEntity.ok(authService.refresh(authRefreshRequestDto));
    }

    AuthRefreshResponseDto tokens = authService.refresh(
        new AuthRefreshRequestDto(refreshCookie.getValue()));
    ResponseCookie accessCookie = ResponseCookie.from(AppConstants.JwtCookieNames.ACCESS_TOKEN,
            tokens.getAccessToken())
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
    return ResponseEntity.ok(authService.getLoggedInUserInfo());
  }

  @GetMapping("/status")
  public ResponseEntity<AuthStatusDto> status(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, AppConstants.JwtCookieNames.ACCESS_TOKEN);
    if (cookie == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthStatusDto(false));
    }

    AuthStatusDto status = authService.checkAuthenticationStatus(cookie.getValue());
    if (!status.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(status);
    }
    return ResponseEntity.ok(status);
  }

}
