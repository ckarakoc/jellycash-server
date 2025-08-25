package nl.ckarakoc.jellycash.controller;

import jakarta.validation.Valid;
import nl.ckarakoc.jellycash.dto.*;
import nl.ckarakoc.jellycash.exception.NotImplementedException;
import nl.ckarakoc.jellycash.manager.AuthManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthManager authManager;

	public AuthController(AuthManager authManager) {
		this.authManager = authManager;
	}
	// todo: /auth/forgot-password | /auth/reset-password ( | /auth/verify-email | /auth/verify-phone | /auth/resend-verification )

	@PostMapping("/login")
	public ResponseEntity<AuthLoginResponseDto> login(@RequestBody @Valid AuthLoginRequestDto authLoginRequestDto) {
		return ResponseEntity.ok(authManager.login(authLoginRequestDto));
	}

	@PostMapping("/logout")
	public ResponseEntity<AuthLogoutResponseDto> logout(@RequestBody @Valid AuthLogoutRequestDto authLogoutRequestDto) {
		return ResponseEntity.ok(authManager.logout(authLogoutRequestDto));
	}

	@PostMapping("/register")
	public ResponseEntity<AuthRegisterResponseDto> register(@RequestBody @Valid AuthRegisterRequestDto authRegisterRequestDto) {
		return ResponseEntity.ok(authManager.register(authRegisterRequestDto));
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthRefreshResponseDto> refresh(@RequestBody @Valid AuthRefreshRequestDto authRefreshRequestDto) {
		return ResponseEntity.ok(authManager.refresh(authRefreshRequestDto));
	}
}
