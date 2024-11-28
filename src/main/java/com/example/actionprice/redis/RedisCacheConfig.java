package com.example.actionprice.redis;

import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 데이터 캐싱할 때 저장되는 위치를 레디스로 설정
 * @author 연상훈
 * @created 2024-11-28 오후 6:09
 * @deprecated 본래 홈페이지 이미지가 자주 사용되면서 고정적이니까 캐싱해두고 쓰려고 했는데,
 * 생각해보니 그럴 바에 프론트에서만 관리하는 게 훨씬 효율적이라서 홈페이지 이미지 관련 기능은 프론트로 이전해버림
 * 결국 캐싱 기능을 따로 안 쓰게 되었는데, 혹시 나중에 쓰게 될 지도 모르니까 일단은 그대로 둠.
 */
@Configuration
@EnableCaching
public class RedisCacheConfig {

  private final long ttl = 10L;

  @Bean
  public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        .disableCachingNullValues() // null값은 캐싱하지 않음
        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
        .entryTtl(Duration.ofMinutes(ttl)); // 캐싱된 데이터의 유효시간(분)

    return RedisCacheManager.builder(redisConnectionFactory)
        .cacheDefaults(redisCacheConfiguration)
        .build();
  }
}