package nl.ckarakoc.jellycash.config;


import java.time.Duration;

public class AppConstants {

  private AppConstants() {
  }

  /**
   * Contains constants representing the names of cookies used for storing JWT tokens.
   * These constants are used for identifying cookies that store access
   * and refresh tokens, which are employed for authentication and session management.
   */
  public static class JwtCookieNames {

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
  }

  /**
   * Contains constants related to JWT token prefixes.
   * This class defines commonly used token prefix values
   * to standardize the formatting of JWT tokens, ensuring consistency
   * across the application.
   */
  public static class JwtTokenPrefix {

    public static final String BEARER = "Bearer ";
  }

  /**
   * Represents constants for JWT token expiry durations.
   * This utility class defines the standard durations for access tokens
   * and refresh tokens used in the application.
   *
   * ACCESS_TOKEN_EXPIRY specifies the duration after which an access token expires.
   * REFRESH_TOKEN_EXPIRY specifies the duration after which a refresh token expires.
   *
   * These durations are used to ensure secure and consistent token management.
   */
  public static class JwtTokenExpiry {

    public static final Duration ACCESS_TOKEN_EXPIRY = Duration.ofMinutes(15);
    public static final Duration REFRESH_TOKEN_EXPIRY = Duration.ofDays(7);
  }

  /**
   * Defines constants for HTTP headers commonly used in the application.
   * These constants aim to standardize the usage of header names,
   * particularly for authentication and authorization purposes.
   */
  public static class Header {

    public static final String AUTHORIZATION = "Authorization";
  }

  public static class ApiPaths {
    public static final String API_V1 = "/api/v1";
    public static final String Users = API_V1 + "/users";
    public static final String Pots = API_V1 + "/pots";
  }

  /* Endpoints that don't require authentication */
  public static String[] NO_AUTH_ENDPOINTS = new String[]{"/auth/"};
}
