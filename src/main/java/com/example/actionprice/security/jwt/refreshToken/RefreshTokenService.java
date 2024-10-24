package com.example.actionprice.security.jwt.refreshToken;

import com.example.actionprice.user.User;
import java.util.Map;

/**
 * @author 연상훈
 * @created 2024-10-19 오후 5:24
 */
public interface RefreshTokenService {
  User issueRefreshToken(String username);
  User checkRefreshFirst(String username);
}
