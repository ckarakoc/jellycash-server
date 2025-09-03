package nl.ckarakoc.jellycash.service;

import nl.ckarakoc.jellycash.dto.LoggedInUserDto;
import nl.ckarakoc.jellycash.dto.auth.AuthLoginRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthMessageResponseDto;
import nl.ckarakoc.jellycash.dto.auth.AuthRefreshRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthRegisterRequestDto;
import nl.ckarakoc.jellycash.dto.auth.AuthStatusDto;
import nl.ckarakoc.jellycash.dto.auth.AuthTokenResponseDto;
import nl.ckarakoc.jellycash.model.User;

public interface AuthService {

  AuthTokenResponseDto register(AuthRegisterRequestDto requestDto);

  AuthTokenResponseDto login(AuthLoginRequestDto requestDto);

  AuthTokenResponseDto refresh(AuthRefreshRequestDto requestDto);

  AuthMessageResponseDto logout(User user);

  LoggedInUserDto getLoggedInUserInfo();

  AuthStatusDto checkAuthenticationStatus(String token);
}
