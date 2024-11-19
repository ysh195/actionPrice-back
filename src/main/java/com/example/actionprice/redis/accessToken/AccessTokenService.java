package com.example.actionprice.redis.accessToken;

import java.util.Map;

public interface AccessTokenService {
  Map<String, String> issueAccessToken(String username);
  String getUsernameFromAccessToken(String accessToken);
  String validateAccessTokenAndExtractUsername(String accessToken);
  String returnWithJson(Map<String, String> map);
}
