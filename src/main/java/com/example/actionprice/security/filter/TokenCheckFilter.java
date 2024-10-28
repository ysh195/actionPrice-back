package com.example.actionprice.security.filter;

import com.example.actionprice.exception.AccessTokenException;
import com.example.actionprice.security.CustomUserDetailService;
import com.example.actionprice.security.jwt.accessToken.AccessTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 토큰을 검사하는 필터
 * @value tokenCheckPath : 토큰 검사가 필요한 url 경로들의 배열.
 * @value userDetailService
 * @value refreshTokenService
 * @author : 연상훈
 * @created : 2024-10-06 오후 2:43
 * @updated 2024-10-17 오후 7:23 : 토큰체크 경로 수정
 * @updated 2024-10-19 오후 5:33 : jwtUtil을 refreshTokenService로 교체
 */
@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {

  private final CustomUserDetailService userDetailService;
  private final AccessTokenService accessTokenService;

  // 토큰 검사를 실행하는 경로
  private final String[] tokenCheckPath = {
      "/api/admin/**",
      "/api/user/login",
      "/api/user/logout"
  };

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.info("[class] TokenCheckFilter - [method] doFilterInternal > 시작");


    if(shouldCheckToken(request)) {
      try{
        Map<String, Object> payload = accessTokenService.validateAccessTokenInReqeust(request);
        log.info("Payload : " + payload);

        String username = (String)payload.get("username");
        log.info("username : " + username);

        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        log.info("UserDetails : " + userDetails);

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // 추가됨
        log.info("Authentication Token : " + authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("Security Context : " + SecurityContextHolder.getContext());

      }
      catch(AccessTokenException e){
        e.sendResponseError(response);
        return;
      }
    }

    filterChain.doFilter(request, response);
  }

  /**
   * 인증이 필요한 경로인지 체크하는 메서드
   * @author 연상훈
   * @created 2024-10-08 오후 4:32
   * @updated 2024-10-17 오후 7:24 : 반환 값 수정. 토큰 체크가 필요한 경로면 true를 반환
   */
  private boolean shouldCheckToken(HttpServletRequest request) {
    String path = request.getRequestURI();

    // for문을 돌리면서 tokenCheckPath에 포함된 게 하나라도 있으면 = 토큰 체크를 해야 하는 경로에 해당하면
    for(String startWord : tokenCheckPath){
      if(path.startsWith(startWord)){
        return true; // true를 반환
      }
    }
    // 포함된 게 아니었으니 for문 돌리면서 안 걸러지고 여기까지 옴.
    return false; // 그러니 false를 반환
  }
}
