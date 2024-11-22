package com.example.actionprice.security;

import com.example.actionprice.exception.AccessTokenException;
import com.example.actionprice.exception.RefreshTokenException;
import com.example.actionprice.exception.TokenErrors;
import com.example.actionprice.redis.TemporaryEntities;
import com.example.actionprice.redis.accessToken.AccessTokenService;
import com.example.actionprice.security.jwt.refreshToken.RefreshTokenService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
  public String tokenRefresh(@RequestHeader("Authorizaion") String access_token) {
    try{
      accessTokenService.validateAccessTokenAndExtractUsername(access_token);

      return access_token;
    } catch (AccessTokenException e) {
      if (e.getTokenErrors().name().equals(TokenErrors.EXPIRED.name())){
        // 엑세스 토큰이 만료되었다면
        String username = e.getUsername();

        validateRefreshToken(username);

        // 리프레시 토큰 검증 후 아무 일 없었다면
        Map<String, String> map = accessTokenService.issueAccessToken(username);
        String newAccessToken = map.get(TemporaryEntities.ACCESS_TOKEN.getGlobalName());

        return newAccessToken;
      }

      throw e;
    }
  }

  private void validateRefreshToken(String username){
    try{
      refreshTokenService.validateRefreshToken(username);
    }
    catch(RefreshTokenException e){
      if(e.getTokenErrors().name().equals(TokenErrors.EXPIRED.name())){
        refreshTokenService.issueRefreshTokenOnLoginSuccess(username);
      } else {
        throw e;
      }
    }
  }

}