package com.example.actionprice.security.filter;

import com.example.actionprice.exception.RefreshTokenException;
import com.example.actionprice.security.jwt.accessToken.AccessTokenService;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 리프레시 토큰 체크 필터
 * @value refreshPath : freshToken 재발급 요청 url. 생성하면서 입력 받음. 현재 "/api/user/generate/refreshToken"
 * @value refreshTokenService
 * @author : 연상훈
 * @created : 2024-10-06 오후 3:05
 * @updated : 2024-10-06 오후 3:05
 * @updated 2024-10-19 오후 5:34 : jwtUtil을 refreshTokenService로 교체하면서 로직 간략화
 */
@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

  // SecurityConfig에서 생성하면서 주입 받을 것들
  private final String refreshPath;
  private final AccessTokenService accessTokenService;

  /**
   * 필터링 로직
   * @author : 연상훈
   * @created : 2024-10-06 오후 3:10
   * @updated : 2024-10-06 오후 3:10
   * @see : 책대로 함
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String path = request.getRequestURI();

    if(!path.equals(refreshPath)){
      log.info("리프레시 토큰 필터를 위한 경로가 아니므로 넘어갑니다.");
      filterChain.doFilter(request, response);
      return;
    }

    log.info("리프레시 토큰 필터 실행");

    // 전송된 json에서 accessToken과 refreshToken을 얻어온다
    Map<String, String> tokens = parseRequestJSON(request);
    if (tokens == null || !tokens.containsKey("access_token") || !tokens.containsKey("username")) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().println("Invalid request payload");
      return;
    }

    String access_token = tokens.get("access_token");
    String username = tokens.get("username");

    // 여기서 서비스 사용
    log.info("access_token : " + access_token);

    try {
      accessTokenService.checkAccessToken(access_token);

      String jsonStr = accessTokenService.issueAccessToken(username);

      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter().println(jsonStr);
    } catch (RefreshTokenException e) {
      e.sendResponseError(response);
    } catch (Exception e) {
      log.error("리프레시 토큰 처리 중 오류 발생: {}", e.getMessage());
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.getWriter().println("토큰 처리 실패");
    }
  }

  private Map<String, String> parseRequestJSON(HttpServletRequest request) {

    // json 데이터를 분석해서 username, mpw 전달 값을 Map으로 처리
    try(Reader reader = new InputStreamReader(request.getInputStream())){

      Gson gson = new Gson();

      return gson.fromJson(reader, Map.class);
    }
    catch(Exception e){
      log.error(e.getMessage());
    }

    return null;
  }

}
