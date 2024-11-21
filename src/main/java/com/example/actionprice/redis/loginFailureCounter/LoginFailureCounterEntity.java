package com.example.actionprice.redis.loginFailureCounter;

import com.example.actionprice.redis.TemporaryEntities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "login_failure_counter")
public class LoginFailureCounterEntity {

  @Id
  private String username;

  @Builder.Default
  private int failureCount = 0;

  @TimeToLive
  @Builder.Default
  private long ttl = TemporaryEntities.LOGIN_FAILURE_COUNTER.getTtl();

  public int addOnePointToFailureCount() {
    this.failureCount = this.failureCount + 1;
    return this.failureCount;
  }

  public void resetTtl() {
    this.ttl = TemporaryEntities.LOGIN_FAILURE_COUNTER.getTtl();
  }
}