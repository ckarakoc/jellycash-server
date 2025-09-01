package nl.ckarakoc.jellycash.service;

import nl.ckarakoc.jellycash.dto.AuthLoginRequestDto;
import nl.ckarakoc.jellycash.dto.AuthLoginResponseDto;
import nl.ckarakoc.jellycash.dto.AuthLogoutRequestDto;
import nl.ckarakoc.jellycash.dto.AuthLogoutResponseDto;
import nl.ckarakoc.jellycash.dto.AuthRefreshRequestDto;
import nl.ckarakoc.jellycash.dto.AuthRefreshResponseDto;
import nl.ckarakoc.jellycash.dto.AuthRegisterRequestDto;
import nl.ckarakoc.jellycash.dto.AuthRegisterResponseDto;
import nl.ckarakoc.jellycash.dto.AuthStatusDto;
import nl.ckarakoc.jellycash.dto.LoggedInUserDto;

public interface AuthService {

  AuthRegisterResponseDto register(AuthRegisterRequestDto requestDto);

  AuthLoginResponseDto login(AuthLoginRequestDto requestDto, String accessToken);

  AuthRefreshResponseDto refresh(AuthRefreshRequestDto requestDto);

  AuthLogoutResponseDto logout(AuthLogoutRequestDto requestDto);

  LoggedInUserDto getLoggedInUserInfo();

  AuthStatusDto checkAuthenticationStatus(String token);
}
