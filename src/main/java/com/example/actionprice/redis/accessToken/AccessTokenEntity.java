package com.example.actionprice.redis.accessToken;

import com.example.actionprice.redis.TemporaryEntities;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "access_token")
public class AccessTokenEntity {

  @Id
  private String accessToken;

  private String username;

  @TimeToLive
  @Builder.Default
  private long ttl = TemporaryEntities.ACCESS_TOKEN.getTtl();
}
