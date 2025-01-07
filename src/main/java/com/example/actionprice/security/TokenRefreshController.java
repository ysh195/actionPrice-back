package com.example.actionprice.security;

import com.example.actionprice.exception.AccessTokenException;
import com.example.actionprice.exception.RefreshTokenException;
import com.example.actionprice.redis.TemporaryEntities;
import com.example.actionprice.redis.accessToken.AccessTokenService;
import com.example.actionprice.security.jwt.refreshToken.RefreshTokenService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Log4j2
@RequiredArgsConstructor
public class TokenRefreshController {

  private final AccessTokenService accessTokenService;
  private final RefreshTokenService refreshTokenService;

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/refresh")
  public ResponseEntity<String> tokenRefresh(@RequestHeader("Authorization") String bearerToken) {
    log.info("엑세스 토큰에 이상 있음?");

    // bearerToken 값에서 "Bearer " 부분 떼어서 엑세스 토큰 값 추출
    String access_token = bearerToken.substring(7);

    try{
      accessTokenService.validateAccessTokenAndExtractUsername(access_token);

      log.info("엑세스 토큰에 이상 없음");

      return ResponseEntity.ok(access_token);
    } catch (AccessTokenException e) {
      log.info("엑세스 토큰에 이상 있음!");

      if (e.getTokenErrors().getStatus().value() == 418){
        log.info("엑세스 토큰 만료됨");
        // 엑세스 토큰이 만료되었다면
        String username = e.getUsername();

        validateRefreshToken(username);

        // 리프레시 토큰 검증 후 아무 일 없었다면
        Map<String, String> map = accessTokenService.issueAccessToken(username);
        access_token = map.get(TemporaryEntities.ACCESS_TOKEN.getGlobalName());

        return ResponseEntity.ok(access_token);
      }

      log.info("그 외 엑세스 토큰 문제");

      throw e;
    }
  }

  private void validateRefreshToken(String username){
    log.info("리프레시 토큰 만료?");
    try{
      refreshTokenService.validateRefreshToken(username);
    }
    catch(RefreshTokenException e){
      if(e.getTokenErrors().getStatus().value() == 418){
        log.info("리프레시 토큰 만료 O");
        refreshTokenService.issueRefreshTokenOnLoginSuccess(username);
      } else {
        log.info("리프레시 토큰 만료 X");
        throw e;
      }
    }
  }

}