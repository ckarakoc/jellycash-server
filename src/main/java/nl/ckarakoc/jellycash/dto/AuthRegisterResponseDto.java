package nl.ckarakoc.jellycash.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRegisterResponseDto {

  private String accessToken;
  private String refreshToken;
  private String email;
}
