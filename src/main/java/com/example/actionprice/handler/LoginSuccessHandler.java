package com.example.actionprice.handler;

import com.example.actionprice.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;

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

  private final JWTUtil jwtUtil;

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

    log.info(authentication + " : " + authentication.getName());

    Map<String, Object> claim = Map.of("username", authentication.getName()); // 키 : username, 밸류 : 인증된 사용자 네임
    log.info("claim : " + claim);
    String accessToken = jwtUtil.generateToken(claim, 60); // 생성한 클레임(맵)으로 엑세스토큰 발행
    String refreshToken = jwtUtil.generateToken(claim, 60 * 3); // 리프레쉬 토큰 발행

    Gson gson = new Gson();

    Map<String, String> keyMap = Map.of("access_token", accessToken,"refresh_token", refreshToken, "username", authentication.getName()); // map 형태로 토큰 정보를 저장

    String jsonStr = gson.toJson(keyMap); // json에 토큰 정보를 전달

    log.info("Login Success Handler : " + jsonStr);

    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().println(jsonStr); // response(json 형태)의 writer에 토큰 정보 추가
  }
}
