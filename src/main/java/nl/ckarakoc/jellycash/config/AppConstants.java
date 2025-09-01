package nl.ckarakoc.jellycash.config;


import java.time.Duration;

public class AppConstants {

  private AppConstants() {
  }

  public static class JwtCookieNames {

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
  }

  public static class JwtTokenPrefix {

    public static final String BEARER = "Bearer ";
  }

  public static class JwtTokenExpiry {

    public static final Duration ACCESS_TOKEN_EXPIRY = Duration.ofMinutes(15);
    public static final Duration REFRESH_TOKEN_EXPIRY = Duration.ofDays(7);
  }

  public static class Header {

    public static final String AUTHORIZATION = "Authorization";
  }

  /* Endpoints that don't require authentication */
  public static String[] NO_AUTH_ENDPOINTS = new String[]{"/auth/"};
}
