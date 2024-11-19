package com.example.actionprice.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TemporaryEntities {
  ACCESS_TOKEN("access_token", 1000*60*60),
  REFRESH_TOKEN("refresh_token", 1000*60*60*3),
  VERIFICATION_EMAIL("verification_email", 1000*60*5),
  LOGIN_FAILURE_COUNTER("login_failure_counter", 1000*60*5);

  private String globalName;
  private long ttl;
}
