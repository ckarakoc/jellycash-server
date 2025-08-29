package nl.ckarakoc.jellycash.service;

import nl.ckarakoc.jellycash.dto.*;

public interface AuthService {
	AuthRegisterResponseDto register(AuthRegisterRequestDto requestDto);

	AuthLoginResponseDto login(AuthLoginRequestDto requestDto, String accessToken);

	AuthRefreshResponseDto refresh( AuthRefreshRequestDto requestDto);

	AuthLogoutResponseDto logout(AuthLogoutRequestDto requestDto);

	LoggedInUserDto getLoggedInUserInfo();

	AuthStatusDto checkAuthenticationStatus(String token);
}
