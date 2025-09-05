package nl.ckarakoc.jellycash.config;

import io.jsonwebtoken.Claims;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.ckarakoc.jellycash.model.AppRole;
import nl.ckarakoc.jellycash.model.Pot;
import nl.ckarakoc.jellycash.model.RefreshToken;
import nl.ckarakoc.jellycash.model.Role;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.repository.BudgetRepository;
import nl.ckarakoc.jellycash.repository.PotRepository;
import nl.ckarakoc.jellycash.repository.RefreshTokenRepository;
import nl.ckarakoc.jellycash.repository.RoleRepository;
import nl.ckarakoc.jellycash.repository.UserRepository;
import nl.ckarakoc.jellycash.security.service.JwtService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * The DataBaseInitializer class is used to initialize the database with default data at application startup.
 */
@Slf4j
@RequiredArgsConstructor
@Component
@Profile({"!test", "!prod"})
public class DataBaseInitializer implements CommandLineRunner {

  private final RefreshTokenRepository refreshTokenRepository;
  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final PotRepository potRepository;
  private final BudgetRepository budgetRepository;

  @Override
  public void run(String... args) {
    log.debug("Initializing database data...");

    log.debug("Inserting roles...");
    Set<Role> allRoles = new HashSet<>();
    for (AppRole role : AppRole.values()) {
      boolean exist = roleRepository.existsByRole(role);
      if (!exist) {
        Role newRole = new Role();
        newRole.setRole(role);
        allRoles.add(roleRepository.save(newRole));
        log.debug("Inserted role: {}", role);
      }
    }

    User superUser = User.builder()
        .email("mark@rutte.nl")
        .password(passwordEncoder.encode("geenHerinneringen$44"))
        .roles(allRoles)
        .firstName("Mark")
        .lastName("Rutte")
        .avatar("assets/images/avatars/mark-rutte.jpg")
        .balance(new BigDecimal("4836.00"))
        .income(new BigDecimal("3814.25"))
        .expenses(new BigDecimal("1700.50"))
        .build();
    userRepository.save(superUser);

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXJrQHJ1dHRlLm5sIiwiaWF0IjoxNzU3MDAyNDEzLCJleHAiOjE3NTc2MDcyMTN9.BQtQsHHafrTprN3vPxNQ9ERGlhavGb0p04ZmsBxnTXI";
    Date expiry = jwtService.extractClaim(token, Claims::getExpiration);
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUser(superUser);
    refreshToken.setToken(token);
    refreshToken.setExpiryDate(expiry);
    refreshTokenRepository.save(refreshToken);

    log.debug("Inserted user: {} and refresh token: {}", superUser.getEmail(), refreshToken.getToken());

    createMockUsers();
    createMockPots(superUser);
    createMockBudgets(superUser);
  }

  private void createMockUsers() {
    log.debug("Inserting mock users...");
    userRepository.save(User.builder()
        .email("emma.richardson@example.com")
        .password(passwordEncoder.encode("geenHerinneringen$44"))
        .roles(Set.of(roleRepository.findByRole(AppRole.USER)
            .orElseThrow(() -> new RuntimeException("Role not found"))))
        .firstName("Emma")
        .lastName("Richardson")
        .avatar("assets/images/avatars/emma-richardson.jpg")
        .build());

    userRepository.save(User.builder()
        .email("daniel.carter@example.com")
        .password(passwordEncoder.encode("geenHerinneringen$44"))
        .roles(Set.of(roleRepository.findByRole(AppRole.USER)
            .orElseThrow(() -> new RuntimeException("Role not found"))))
        .firstName("Daniel")
        .lastName("Carter")
        .avatar("assets/images/avatars/daniel-carter.jpg")
        .build());

    userRepository.save(User.builder()
        .email("sun.park@example.com")
        .password(passwordEncoder.encode("geenHerinneringen$44"))
        .roles(Set.of(roleRepository.findByRole(AppRole.USER)
            .orElseThrow(() -> new RuntimeException("Role not found"))))
        .firstName("Sun")
        .lastName("Park")
        .avatar("assets/images/avatars/sun-park.jpg")
        .build());

    userRepository.save(User.builder()
        .email("liam.hughes@example.com")
        .password(passwordEncoder.encode("geenHerinneringen$44"))
        .roles(Set.of(roleRepository.findByRole(AppRole.USER)
            .orElseThrow(() -> new RuntimeException("Role not found"))))
        .firstName("Liam")
        .lastName("Hughes")
        .avatar("assets/images/avatars/liam-hughes.jpg")
        .build());

    userRepository.save(User.builder()
        .email("lily.ramirez@example.com")
        .password(passwordEncoder.encode("geenHerinneringen$44"))
        .roles(Set.of(roleRepository.findByRole(AppRole.USER)
            .orElseThrow(() -> new RuntimeException("Role not found"))))
        .firstName("Lily")
        .lastName("Ramirez")
        .avatar("assets/images/avatars/lily-ramirez.jpg")
        .build());

    userRepository.save(User.builder()
        .email("ethan.clark@example.com")
        .password(passwordEncoder.encode("geenHerinneringen$44"))
        .roles(Set.of(roleRepository.findByRole(AppRole.USER)
            .orElseThrow(() -> new RuntimeException("Role not found"))))
        .firstName("Ethan")
        .lastName("Clark")
        .avatar("assets/images/avatars/ethan-clark.jpg")
        .build());

    userRepository.save(User.builder()
        .email("james.thompson@example.com")
        .password(passwordEncoder.encode("geenHerinneringen$44"))
        .roles(Set.of(roleRepository.findByRole(AppRole.USER)
            .orElseThrow(() -> new RuntimeException("Role not found"))))
        .firstName("James")
        .lastName("Thompson")
        .avatar("assets/images/avatars/james-thompson.jpg")
        .build());

    userRepository.save(User.builder()
        .email("ella.phillips@example.com")
        .password(passwordEncoder.encode("geenHerinneringen$44"))
        .roles(Set.of(roleRepository.findByRole(AppRole.USER)
            .orElseThrow(() -> new RuntimeException("Role not found"))))
        .firstName("Ella")
        .lastName("Phillips")
        .avatar("assets/images/avatars/ella-phillips.jpg")
        .build());

    userRepository.save(User.builder()
        .email("sofia.peterson@example.com")
        .password(passwordEncoder.encode("geenHerinneringen$44"))
        .roles(Set.of(roleRepository.findByRole(AppRole.USER)
            .orElseThrow(() -> new RuntimeException("Role not found"))))
        .firstName("Sofia")
        .lastName("Peterson")
        .avatar("assets/images/avatars/sofia-peterson.jpg")
        .build());

    userRepository.save(User.builder()
        .email("mason.martinez@example.com")
        .password(passwordEncoder.encode("geenHerinneringen$44"))
        .roles(Set.of(roleRepository.findByRole(AppRole.USER)
            .orElseThrow(() -> new RuntimeException("Role not found"))))
        .firstName("Mason")
        .lastName("Martinez")
        .avatar("assets/images/avatars/mason-martinez.jpg")
        .build());

    userRepository.save(User.builder()
        .email("sebastian.cook@example.com")
        .password(passwordEncoder.encode("geenHerinneringen$44"))
        .roles(Set.of(roleRepository.findByRole(AppRole.USER)
            .orElseThrow(() -> new RuntimeException("Role not found"))))
        .firstName("Sebastian")
        .lastName("Cook")
        .avatar("assets/images/avatars/sebastian-cook.jpg")
        .build());

    userRepository.save(User.builder()
        .email("william.harris@example.com")
        .password(passwordEncoder.encode("geenHerinneringen$44"))
        .roles(Set.of(roleRepository.findByRole(AppRole.USER)
            .orElseThrow(() -> new RuntimeException("Role not found"))))
        .firstName("William")
        .lastName("Harris")
        .avatar("assets/images/avatars/william-harris.jpg")
        .build());

    userRepository.save(User.builder()
        .email("rina.sato@example.com")
        .password(passwordEncoder.encode("geenHerinneringen$44"))
        .roles(Set.of(roleRepository.findByRole(AppRole.USER)
            .orElseThrow(() -> new RuntimeException("Role not found"))))
        .firstName("Rina")
        .lastName("Sato")
        .avatar("assets/images/avatars/rina-sato.jpg")
        .build());

    userRepository.save(User.builder()
        .email("yuna.kim@example.com")
        .password(passwordEncoder.encode("geenHerinneringen$44"))
        .roles(Set.of(roleRepository.findByRole(AppRole.USER)
            .orElseThrow(() -> new RuntimeException("Role not found"))))
        .firstName("Yuna")
        .lastName("Kim")
        .avatar("assets/images/avatars/yuna-kim.jpg")
        .build());

    userRepository.save(User.builder()
        .email("harper.edwards@example.com")
        .password(passwordEncoder.encode("geenHerinneringen$44"))
        .roles(Set.of(roleRepository.findByRole(AppRole.USER)
            .orElseThrow(() -> new RuntimeException("Role not found"))))
        .firstName("Harper")
        .lastName("Edwards")
        .avatar("assets/images/avatars/harper-edwards.jpg")
        .build());
  }

  private void createMockPots(User user) {
    log.debug("Inserting mock pots...");
    potRepository.save(Pot.builder()
        .name("Savings")
        .balance(BigDecimal.valueOf(159))
        .maxBalance(BigDecimal.valueOf(2000))
        .user(user)
        .build());

    potRepository.save(Pot.builder()
        .name("Concert Ticket")
        .balance(BigDecimal.valueOf(110))
        .maxBalance(BigDecimal.valueOf(150))
        .user(user)
        .build());

    potRepository.save(Pot.builder()
        .name("Gift")
        .balance(BigDecimal.valueOf(110))
        .maxBalance(BigDecimal.valueOf(150))
        .user(user)
        .build());

    potRepository.save(Pot.builder()
        .name("New Laptop")
        .balance(BigDecimal.valueOf(10))
        .maxBalance(BigDecimal.valueOf(1000))
        .user(user)
        .build());

    potRepository.save(Pot.builder()
        .name("Holiday")
        .balance(BigDecimal.valueOf(531))
        .maxBalance(BigDecimal.valueOf(1440))
        .user(user)
        .build());
  }

  private void createMockBudgets(User superUser) {
    log.debug("Inserting mock budgets...");
    // TODO
  }
}
