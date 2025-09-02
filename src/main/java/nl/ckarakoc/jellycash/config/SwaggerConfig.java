package nl.ckarakoc.jellycash.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openApi() {
    SecurityScheme bearerScheme = new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .description("JWT Bearer token");
    SecurityRequirement bearerRequirement = new SecurityRequirement()
        .addList("Bearer Authentication");

    SecurityScheme apiKeyScheme = new SecurityScheme()
        .type(SecurityScheme.Type.APIKEY)
        .in(SecurityScheme.In.HEADER)
        .name("X-API-KEY")
        .scheme("api-token")
        .description("API token");
    SecurityRequirement apiKeyRequirement = new SecurityRequirement()
        .addList("API Key");

    return new OpenAPI()
        .info(new Info()
            .title("JellyCash API")
            .version("1.0.0")
            .description("JellyCash REST API Documentation")
            .contact(new Contact()
                .name("API Support")
                .email("support@jellycash.nl")
                .url("https://jellycash.ckarakoc.nl")))
        .servers(List.of(new Server()
            .url("http://localhost:8080")
            .description("Development server")))
        .components(new Components()
            .addSecuritySchemes("Bearer Authentication", bearerScheme)
            .addSecuritySchemes("API Key", apiKeyScheme))
        .addSecurityItem(bearerRequirement)
        .addSecurityItem(apiKeyRequirement);
  }
}
