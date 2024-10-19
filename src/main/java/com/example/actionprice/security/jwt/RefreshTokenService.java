package com.example.actionprice.security.jwt;

import java.util.Map;

/**
 * @author 연상훈
 * @created 2024-10-19 오후 5:24
 */
public interface RefreshTokenService {
  String issueJwt(String username);
  void discardJwt(String username);
  String issueAccessToken(String username);
  Map<String, Object> validateJwtToken(String token);
}
