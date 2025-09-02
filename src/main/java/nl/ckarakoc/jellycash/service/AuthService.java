package nl.ckarakoc.jellycash.service;

import nl.ckarakoc.jellycash.dto.auth.AuthLoginRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthLoginResponseDto;
import nl.ckarakoc.jellycash.dto.auth.AuthLogoutRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthLogoutResponseDto;
import nl.ckarakoc.jellycash.dto.auth.AuthRefreshRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthRefreshResponseDto;
import nl.ckarakoc.jellycash.dto.auth.AuthRegisterRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthRegisterResponseDto;
import nl.ckarakoc.jellycash.dto.auth.AuthStatusDto;
import nl.ckarakoc.jellycash.dto.LoggedInUserDto;

public interface AuthService {

  AuthRegisterResponseDto register(AuthRegisterRequestDto requestDto);

  AuthLoginResponseDto login(AuthLoginRequestDto requestDto, String accessToken);

  AuthRefreshResponseDto refresh(AuthRefreshRequestDto requestDto);

  AuthLogoutResponseDto logout(AuthLogoutRequestDto requestDto);

  LoggedInUserDto getLoggedInUserInfo();

  AuthStatusDto checkAuthenticationStatus(String token);
}
