package nl.ckarakoc.jellycash.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.ckarakoc.jellycash.config.AppConstants;
import nl.ckarakoc.jellycash.dto.ApiError;
import nl.ckarakoc.jellycash.exception.ApiException;
import nl.ckarakoc.jellycash.security.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    String path = request.getRequestURI();
    for (Pattern noAuthRequiredEndpoint : AppConstants.NO_AUTH_ENDPOINTS) {
      if (noAuthRequiredEndpoint.matcher(path).matches()) {
        filterChain.doFilter(request, response);
        return;
      }
    }

    Cookie cookie = WebUtils.getCookie(request, AppConstants.JwtCookieNames.ACCESS_TOKEN);

    if (cookie == null) {
      filterChain.doFilter(request, response);
      return;
    }

    final String accessToken = cookie.getValue();

    try {
      final String userEmail = jwtService.extractUsername(accessToken);
      if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        if (jwtService.isTokenValid(accessToken, userDetails)) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    } catch (ApiException e) {
      ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), "Bad Request",
          e.getMessage());
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.setContentType("application/json");
      objectMapper.writeValue(response.getWriter(), apiError);
    } catch (UsernameNotFoundException e) {
      ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized",
          "User not found");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      objectMapper.writeValue(response.getWriter(), apiError);
    }
    filterChain.doFilter(request, response);
  }
}
