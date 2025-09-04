package nl.ckarakoc.jellycash.config;


import java.time.Duration;
import java.util.regex.Pattern;

/**
 * A utility class that provides constant values grouped into nested static classes for organization and clarity.
 */
public class AppConstants {

  private AppConstants() {
  }

  /**
   * Contains constants representing the names of cookies used for storing JWT tokens.
   */
  public static class JwtCookieNames {

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
  }

  /**
   * Contains constants related to JWT token prefixes. This class defines commonly used token prefix values to standardize the formatting of JWT tokens.
   */
  public static class JwtTokenPrefix {

    public static final String BEARER = "Bearer ";
  }

  /**
   * Represents constants for JWT token expiry durations.
   */
  public static class JwtTokenExpiry {

    public static final Duration ACCESS_TOKEN_EXPIRY = Duration.ofMinutes(15);
    public static final Duration REFRESH_TOKEN_EXPIRY = Duration.ofDays(7);
  }

  /**
   * Defines constants for HTTP headers commonly used in the application.
   */
  public static class Header {

    public static final String AUTHORIZATION = "Authorization";
  }

  /**
   * Provides API endpoint path constants for version 1 of the API. This class serves as a centralized location to define and manage the paths for various entities in the API.
   */
  public static class ApiPaths {

    public static final String AUTH = "/auth";
    public static final String API_V1 = "/api/v1";
    public static final String Users = API_V1 + "/users";
    public static final String Pots = API_V1 + "/pots";
  }

  /* Endpoints that don't require authentication */
  public static final Pattern[] NO_AUTH_ENDPOINTS = new Pattern[]{
      Pattern.compile("^/auth/(?!logout).*")
  };
}
