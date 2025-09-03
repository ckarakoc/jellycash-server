package nl.ckarakoc.jellycash.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.ckarakoc.jellycash.config.AppConstants;
import nl.ckarakoc.jellycash.dto.LoggedInUserDto;
import nl.ckarakoc.jellycash.dto.auth.AuthLoginRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthMessageResponseDto;
import nl.ckarakoc.jellycash.dto.auth.AuthRefreshRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthRegisterRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthStatusDto;
import nl.ckarakoc.jellycash.dto.auth.AuthTokenResponseDto;
import nl.ckarakoc.jellycash.exception.NotImplementedException;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

  @Operation(
      summary = "Login",
      description = "Logs user in to the application and sets the authentication cookies: access_token and refresh_token",
      responses = {
          @ApiResponse(responseCode = "204", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad request: Validation Error"),
          @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication Error"),
      }
  )
  @PostMapping("/login")
  public ResponseEntity<Void> login(
      @RequestBody @Valid AuthLoginRequestDto authLoginRequestDto,
      HttpServletResponse response) {
    AuthTokenResponseDto tokens = authService.login(authLoginRequestDto);

    setAuthCookies(tokens, response);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Logout",
      description = "Clears the authentication cookies: access_token and refresh_token",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success, but user was not logged in anyways"),
          @ApiResponse(responseCode = "204", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad request: Validation Error"),
      }
  )
  @PostMapping("/logout")
  public ResponseEntity<AuthMessageResponseDto> logout(
      @AuthenticationPrincipal User user,
      HttpServletResponse response) {
    System.out.println("Logout: " + user);

    // Deletes tokens from client
    clearAuthCookies(response);

    // Deletes refresh token from database
    AuthMessageResponseDto logout = authService.logout(user);
    if (!logout.isSuccess()) {
      return ResponseEntity.ok().body(logout);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  @Operation(
      summary = "Register",
      description = "Registers a new user and sets the authentication cookies: access_token and refresh_token",
      responses = {
          @ApiResponse(responseCode = "204", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad request: Validation Error"),
          @ApiResponse(responseCode = "409", description = "Conflict: User already exists"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error: Role User does not exist"), // should not happen
      }
  )
  @PostMapping("/register")
  public ResponseEntity<Void> register(
      @RequestBody @Valid AuthRegisterRequestDto authRegisterRequestDto,
      HttpServletResponse response) {
    AuthTokenResponseDto tokens = authService.register(authRegisterRequestDto);

    setAuthCookies(tokens, response);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Refresh",
      description = "Refreshes the access token and sets the authentication cookies: access_token",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad request: Validation Error"),
          @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication Error"),
      }
  )
  @PostMapping("/refresh")
  public ResponseEntity<Void> refresh(
      HttpServletRequest request,
      HttpServletResponse response) {
    Cookie refreshCookie = WebUtils.getCookie(request, AppConstants.JwtCookieNames.REFRESH_TOKEN);
    if (refreshCookie == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    AuthTokenResponseDto tokens = authService.refresh(new AuthRefreshRequestDto(refreshCookie.getValue()));
    ResponseCookie accessCookie = ResponseCookie.from(AppConstants.JwtCookieNames.ACCESS_TOKEN,
            tokens.getAccessToken())
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(AppConstants.JwtTokenExpiry.ACCESS_TOKEN_EXPIRY)
        .sameSite("Strict")
        .build();
    response.addHeader("Set-Cookie", accessCookie.toString());
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Logged in user info",
      description = "Gets all the info of the logged in user",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication Error"),
          @ApiResponse(responseCode = "403", description = "Forbidden: User is not logged in"),
      }
  )
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/me")
  public ResponseEntity<LoggedInUserDto> loggedInUserInfo() {
    return ResponseEntity.ok(authService.getLoggedInUserInfo());
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<Void> forgotPassword() {
    throw new NotImplementedException();
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Void> resetPassword() {
    throw new NotImplementedException();
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

  private void setAuthCookies(AuthTokenResponseDto tokens, HttpServletResponse response) {
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
  }

  private void clearAuthCookies(HttpServletResponse response) {
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
  }

}
