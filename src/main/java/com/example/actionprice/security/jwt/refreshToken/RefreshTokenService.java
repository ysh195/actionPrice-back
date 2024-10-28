package com.example.actionprice.security.jwt.refreshToken;

import com.example.actionprice.user.User;

/**
 * @author 연상훈
 * @created 2024-10-19 오후 5:24
 */
public interface RefreshTokenService {
  User issueRefreshToken(String username);
  User checkRefreshFirst(String username);
  boolean setBlockUserByUsername(String username);
}
