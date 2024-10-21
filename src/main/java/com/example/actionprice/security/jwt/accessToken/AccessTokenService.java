package com.example.actionprice.security.jwt.accessToken;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AccessTokenService {
  String issueJwt(String username);
  String issueAccessToken(String username);
  Map<String, Object> validateAccessTokenInReqeust(HttpServletRequest request);
  Map<String,Object> checkAccessToken(String accessToken);
}
