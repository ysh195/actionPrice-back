package com.example.actionprice.security.jwt.refreshToken;

import com.example.actionprice.user.User;
import java.util.Map;

/**
 * @author 연상훈
 * @created 2024-10-19 오후 5:24
 */
public interface RefreshTokenService {
  User issueRefreshTokenOnLoginSuccess(String username);
  boolean setBlockUserByUsername(String username);
  void resetRefreshToken(String username);
  void validateRefreshToken(String username);
}
