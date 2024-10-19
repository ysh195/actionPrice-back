package com.example.actionprice.handler;

import com.example.actionprice.security.jwt.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * 로그아웃 석세스 핸들러
 * @author 연상훈
 * @created 2024-10-19 오후 5:22
 * @info 로그아웃 성공 시 기존에 발급한 jwt 토큰을 삭제함.
 * @see : 사용자의 로컬에 있는 토큰은 프론트에서 지워야 함.
 */
@Log4j2
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

  private final RefreshTokenService refreshTokenService;

  @Override
  public void onLogoutSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) throws IOException, ServletException
  {
    String username = authentication.getName();

    refreshTokenService.discardJwt(username);

    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().write("Logout successful");
  }

}
