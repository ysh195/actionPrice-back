package com.example.actionprice.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

// ttl은 초 단위. 60 = 1분 / 60*60 = 1시간
@Getter
@AllArgsConstructor
public enum TemporaryEntities {
  ACCESS_TOKEN("access_token", 60*60),
  REFRESH_TOKEN("refresh_token", 60*60*24),
  VERIFICATION_EMAIL("verification_email", 60*5),
  LOGIN_FAILURE_COUNTER("login_failure_counter", 60*5);

  private String globalName;
  private long ttl;
}
