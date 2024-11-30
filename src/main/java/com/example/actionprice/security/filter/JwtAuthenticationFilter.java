package com.example.actionprice.security.filter;

import com.example.actionprice.exception.AccessTokenException;
import com.example.actionprice.exception.TokenErrors;
import com.example.actionprice.redis.accessToken.AccessTokenEntity;
import com.example.actionprice.security.CustomUserDetailService;
import com.example.actionprice.redis.accessToken.AccessTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 토큰을 검사하는 필터
 * @value userDetailService
 * @value accessTokenService
 * @author : 연상훈
 * @created : 2024-10-06 오후 2:43
 * @updated 2024-10-17 오후 7:23 : 토큰체크 경로 수정
 * @updated 2024-10-19 오후 5:33 : jwtUtil을 refreshTokenService로 교체
 * @updated 2024-11-08 오후 12:39 : accessTokenService와 refreshTokenService를 분리하여 관심사 분리 및 로직 구체화
 * @info 거의 모든 요청에 대해 인증 정보를 제공하는 필터이기 때문에 인증 정보 생성을 위해 userDetailService 사용
 */
@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final CustomUserDetailService userDetailService;
  private final AccessTokenService accessTokenService;

  // url이 equals 일 때만 취급하고 있음. 필요하면 startWith로 바꿔야 함
  private final String[] URL_WITH_UNAVOIDABLE_REASON = {
      "/api/auth/refresh",
      "/api/user/logout"
  };

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    log.info("토큰 체크 필터 실행");

    String headerStr = request.getHeader("Authorization");
    log.info("url : {} | headerStr : {}", request.getRequestURI(), headerStr);

    // 토큰이 없거나 Bearer로 시작하지 않으면
    if(isHeaderWithoutToken(headerStr)) {
      log.info("익명 사용자로 처리");
      // 익명 사용자로 처리
      filterChain.doFilter(request, response);
      return;
    }

    // 정상적인 형태의 토큰이 존재한다면
    try{
      // 토큰에서 토큰의 내용을 추출함
      String tokenStr = extractTokenInHeaderStr(headerStr);
      String username = null;

      // 일단 레디스에 저장된 게 있는지 확인
      AccessTokenEntity accessTokenEntity = accessTokenService.getAccessToken(tokenStr);
      if (accessTokenEntity == null){
        log.info("레디스에 저장된 토큰이 없음");

        // 유효성 검증 중 토큰 만료 등의 문제가 발생하면 예외로 넘어감
        if(hasUnavoidableReason(request)){
          log.info("로그아웃 또는 토큰 재발급 중에는 토큰 만료에 대해 검증하지 않음");
          username =
              accessTokenService.validateAccessTokenAndExtractUsernameWithoutEXP(tokenStr);
        } else {
          username =
              accessTokenService.validateAccessTokenAndExtractUsername(tokenStr);
        }

      } else {
        log.info("레디스에 저장된 토큰이 있음");
        username = accessTokenEntity.getUsername();
      }

      log.info("username : " + username);

      // 인증 정보를 저장
      SecurityContextHolder.getContext().setAuthentication(getAuthenticationToken(username, request));
      log.info("Security Context : " + SecurityContextHolder.getContext());
    } catch(AccessTokenException e){
      log.info("엑세스 토큰 문제 발생");

      // 토큰 만료를 일부러 넘어가 주는 경로가 아닌 이상 토큰 만료 시 에러가 발생함.
      TokenErrors tokenErrors = e.getTokenErrors();

      // 만약 에러 코드가 418(토큰 만료)면
      if (tokenErrors.getStatus().value() == 418){
        // 여기서 로직을 중단하고 에러를 반환
        response.setStatus(418);
        response.getWriter().write(tokenErrors.getMessage());

        return;
      } else {
        // 에러를 인터셉터에 넘김
        request.setAttribute("filter.exception", e);
      }
    } catch (Exception e) {
      // 에러를 인터셉터에 넘김
      log.error("TokenCheckFilter 처리 중 예외 발생: {}", e.getMessage());
      request.setAttribute("filter.exception", e);
    }

    // 뭐가 어찌됐든 필터는 이어줌. 그래야 인터셉터에서 넘겨 받아서 어드바이스로 처리가 됨
    filterChain.doFilter(request, response);
  }

  /**
   * 헤더에서 토큰값 추출
   * @author 연상훈
   * @created 2024-11-26 오후 5:46
   */
  private String extractTokenInHeaderStr(String headerStr) {
    log.info("headerStr : " + headerStr);

    String tokenType = headerStr.substring(0,6);
    String tokenStr = headerStr.substring(7);
    log.info("tokenType : " + tokenType);
    log.info("tokenStr : " + tokenStr);

    // 엑세스 토큰에 대한 엄격한 검사 후 결과 반환
    return tokenStr;
  }

  /**
   * 인증 정보 생성
   * @author 연상훈
   * @created 2024-11-26 오후 5:47
   * @info 로직이 중요한 것도 아닌데 지저분해서 그냥 메서드로 뺌. 메서드로 안 빼도 상관 없음
   */
  private UsernamePasswordAuthenticationToken getAuthenticationToken(String username, HttpServletRequest request){
    // 인증 정보 생성
    UserDetails userDetails = userDetailService.loadUserByUsername(username);
    log.info("UserDetails : " + userDetails);

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );

    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    log.info("Authentication Token : " + authenticationToken);

    return authenticationToken;
  }

  /**
   * 헤더가 정상적인 토큰을 포함하고 있는지 체크
   * @author 연상훈
   * @created 2024-11-26 오후 5:47
   */
  private boolean isHeaderWithoutToken(String headerStr) {
    return (headerStr == null || headerStr.contains("undefined") || !headerStr.startsWith("Bearer "));
  }

  /**
   * 요청된 url이 토큰 만료 검사의 면제 대상인지 확인
   * @author 연상훈
   * @created 2024-11-26 오후 5:48
   * @info equals로 검사하는 것에 유의
   */
  private boolean hasUnavoidableReason(HttpServletRequest request){
    if (request.getMethod().equals("POST")){
      for(String url : URL_WITH_UNAVOIDABLE_REASON){
        if (request.getRequestURI().equals(url)){
          return true;
        }
      }
    }

    return false;
  }

}
