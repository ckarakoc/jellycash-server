package nl.ckarakoc.jellycash.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "The response containing jwt access and bearer tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthTokenResponseDto {

  private String accessToken;
  private String refreshToken;
}
