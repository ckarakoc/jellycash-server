package nl.ckarakoc.jellycash.security.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.ckarakoc.jellycash.config.AppConstants;
import nl.ckarakoc.jellycash.exception.ApiException;
import nl.ckarakoc.jellycash.model.RefreshToken;
import nl.ckarakoc.jellycash.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {
	@Value("${security.jwt.secret-key}")
	private String secretKey;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return buildToken(extraClaims, userDetails, new Date(System.currentTimeMillis() + AppConstants.JwtTokenExpiry.ACCESS_TOKEN_EXPIRY.toMillis()));
	}

	public <T extends User> RefreshToken generateRefreshToken(T userDetails) {
		return generateRefreshToken(userDetails, new Date(System.currentTimeMillis() + AppConstants.JwtTokenExpiry.REFRESH_TOKEN_EXPIRY.toMillis()));
	}

	public <T extends User> RefreshToken generateRefreshToken(T userDetails, Date expiryDate) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setUser(userDetails);
		refreshToken.setToken(buildToken(new HashMap<>(), userDetails, expiryDate));
		refreshToken.setExpiryDate(expiryDate);
		return refreshToken;
	}

	public boolean isTokenValid(String token) {
		parseJwts(token);
		return !isTokenExpired(token);
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return parseJwts(token)
			.getPayload();
	}

	private Jws<Claims> parseJwts(String token) {
		try {
			return Jwts
				.parser()
				.verifyWith(getSignInKey())
				.build()
				.parseSignedClaims(token);
		} catch (UnsupportedJwtException e) {
			log.error("unsupported jwt exception: {}", e.getMessage());
			throw new ApiException("the jwt argument does not represent a signed Claims JWT");
		} catch (JwtException e) {
			log.error("jwt exception: {}", e.getMessage());
			throw new ApiException("the jwt string cannot be parsed or validated as required");
		} catch (IllegalArgumentException e) {
			log.error("illegal argument exception: {}", e.getMessage());
			throw new ApiException("jwt string is null or empty or only whitespace");
		}
	}

	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, Date expiration) {
		return Jwts
			.builder()
			.claims(extraClaims)
			.subject(userDetails.getUsername())
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(expiration)
			.signWith(getSignInKey(), Jwts.SIG.HS256)
			.compact();
	}
}
