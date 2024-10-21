package com.example.actionprice.security.jwt.refreshToken;

import java.util.Map;

/**
 * @author 연상훈
 * @created 2024-10-19 오후 5:24
 */
public interface RefreshTokenService {
  void issueRefreshToken(String username, Map<String, Object> claim);
  void checkRefreshFirst(String username);
}
