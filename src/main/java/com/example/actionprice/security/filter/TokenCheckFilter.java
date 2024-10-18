package com.example.actionprice.security.filter;

import com.example.actionprice.exception.AccessTokenException;
import com.example.actionprice.security.CustomUserDetailService;
import com.example.actionprice.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
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
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 토큰을 검사하는 필터
 * @author : 연상훈
 * @created : 2024-10-06 오후 2:43
 * @updated 2024-10-17 오후 7:23 : 토큰체크 경로 수정
 * @see : @RequiredArgsConstructor를 사용했기 때문에 나중에 생성할 때 넣어줘야 함
 */
@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {

  private final CustomUserDetailService userDetailService;
  private final JWTUtil jwtUtil;

  // 토큰 검사를 실행하는 경로
  private final String[] tokenCheckPath = {
    "/api/admin/**"
  };

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.info("[class] TokenCheckFilter - [method] doFilterInternal > 시작");


    if(shouldCheckToken(request)) {
      try{
        Map<String, Object> payload = validateAccessToken(request);
        log.info("Payload : " + payload);

        String username = (String)payload.get("username");
        log.info("username : " + username);

        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        log.info("UserDetails : " + userDetails);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
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

  private Map<String, Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException {
    String headerStr = request.getHeader("Authorization");
    log.info("headerStr : " + headerStr);

    if(headerStr == null || headerStr.length() < 8){
      throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
    }

    String tokenType = headerStr.substring(0,6);
    String tokenStr = headerStr.substring(7);
    log.info("tokenType : " + tokenType);
    log.info("tokenStr : " + tokenStr);


    if(tokenType.equalsIgnoreCase("Bearer") == false){
      // 엑세스 토큰에 Bearer가 빠져 있음. 형식이 이상함
      throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
    }

    log.info("this is bearer : " + tokenType);

    try{
      Map<String,Object> values = jwtUtil.validateToken(tokenStr);
      log.info("values : " + values);

      return values;
    }
    catch(MalformedJwtException e){
      log.error("-------- {} ------------", e.getClass().getSimpleName());
      throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
    }
    catch(SignatureException e){
      log.error("-------- {} ------------", e.getClass().getSimpleName());
      throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
    }
    catch(ExpiredJwtException e){
      log.error("-------- {} ------------", e.getClass().getSimpleName());
      throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
    }
  }

  /**
   * 인증이 필요한 경로인지 체크하는 메서드
   * @author 연상훈
   * @created 2024-10-08 오후 4:32
   * @updated 2024-10-17 오후 7:24 : 반환 값 수정. 토큰 체크가 필요한 경로면 true를 반환
   */
  private boolean shouldCheckToken(HttpServletRequest request) {
    String path = request.getRequestURI();
    // 예: 로그인 및 기타 공개 API는 인증 불필요
    for(String startWord : tokenCheckPath){
      // for문을 돌리면서 tokenCheckPath에 포함된 게 하나라도 있으면 = 토큰 체크를 해야 하는 경로에 해당하면
      if(path.startsWith(startWord)){
        //
        return true; // true를 반환
      }
    }
    // 포함된 게 아니었으니 for문 돌리면서 안 걸러지고 여기까지 옴.
    return false; // 그러니 false를 반환
  }
}
