package nl.ckarakoc.jellycash.dto;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoggedInUserDto {

  private Long userId;
  private String email;
  private String firstName;
  private String lastName;
  private String avatar;
  private String currency;
  private Long balance;
  private Long income;
  private Long expenses;
  private boolean enabled;
  private Set<String> roles;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
