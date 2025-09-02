package nl.ckarakoc.jellycash.config;

import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import nl.ckarakoc.jellycash.model.AppRole;
import nl.ckarakoc.jellycash.model.RefreshToken;
import nl.ckarakoc.jellycash.model.Role;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.repository.RefreshTokenRepository;
import nl.ckarakoc.jellycash.repository.RoleRepository;
import nl.ckarakoc.jellycash.repository.UserRepository;
import nl.ckarakoc.jellycash.security.service.JwtService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Profile("!test")
public class DataBaseInitializer implements CommandLineRunner {

  private final RefreshTokenRepository refreshTokenRepository;
  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @Override
  public void run(String... args) throws Exception {
    System.out.println("Initializing database data...");

    System.out.println("Inserting roles...");
    Set<Role> allRoles = new HashSet<>();
    for (AppRole role : AppRole.values()) {
      boolean exist = roleRepository.existsByRole(role);
      if (!exist) {
        Role newRole = new Role();
        newRole.setRole(role);
        roleRepository.save(newRole);
        allRoles.add(newRole);
        System.out.println("Inserted role: " + role);
      }
    }

    User superUser = new User();
    superUser.setEmail("test@rutte.nl");
    superUser.setPassword(passwordEncoder.encode("geenHerinneringen$44"));
    superUser.setRoles(allRoles);
    superUser.setFirstName("Mark");
    superUser.setLastName("Rutte");
    userRepository.save(superUser);

    // Date expiryDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);
    // RefreshToken refreshToken = jwtService.generateRefreshToken(superUser, expiryDate);

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHJ1dHRlLm5sIiwiaWF0IjoxNzU2ODAyNzAwLCJleHAiOjE3NTc0MDc0OTl9.UgBYEeBEitATkLBJZzWnvoUBTAJbsMvJHoOvoRafuaY";
    Date expiry = jwtService.extractClaim(token, Claims::getExpiration);
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUser(superUser);
    refreshToken.setToken(token);
    refreshToken.setExpiryDate(expiry);
    refreshTokenRepository.save(refreshToken);

    System.out.println("Inserted user: " + superUser.getEmail() + " and refresh token: "
        + refreshToken.getToken());
  }
}
