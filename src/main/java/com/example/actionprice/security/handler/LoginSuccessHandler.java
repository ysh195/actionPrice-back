package com.example.actionprice.security.handler;

import com.example.actionprice.redis.accessToken.AccessTokenService;
import com.example.actionprice.security.jwt.refreshToken.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 로그인 성공 시 사용되는 핸들러
 * @author : 연상훈
 * @created : 2024-10-06 오후 3:26
 * @updated 2024-10-14 오후 12:07 : 없앴다가 다시 생성함
 * @updated 2024-10-17 오후 7:16 : 리멤버미 삭제
 * @value jwtUtil : 토큰 생성 및 검증용 컴포넌트
 */
@Log4j2
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final AccessTokenService accessTokenService;
  private final RefreshTokenService refreshTokenService;

  /**
   * 인증이 성공하면 진행되는 절차
   * @author : 연상훈
   * @created : 2024-10-06 오후 6:17
   * @updated : 2024-10-06 오후 6:17
   * @see : 토큰 유효기간 재설정 필요.
   */
  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) throws IOException
  {

    log.info("----------- Login Success Handler -----------");

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    String username = authentication.getName();
    log.info(authentication + " : " + username);

    refreshTokenService.issueRefreshTokenOnLoginSuccess(username);

    Map<String, String> map = accessTokenService.issueAccessToken(username);
    String jsonStr = accessTokenService.returnWithJson(map);

    log.info("Login Success Handler : " + jsonStr);

    // 인증 정보 저장
    SecurityContextHolder.getContext().setAuthentication(authentication);
    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().println(jsonStr); // response(json 형태)의 writer에 토큰 정보 추가
  }
}
