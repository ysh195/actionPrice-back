package com.example.actionprice.redis;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 레디스 설정
 * @author 연상훈
 * @created 2024-11-21 오후 8:08
 * @info 참고로 redis repository에 사용되는 entity는 @Id를 사용할 때
 * [jakarta.persistence.Id] 대신 [org.springframework.data.annotation.Id] 를 사용해야 함
 */
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
@EnableRedisRepositories(basePackages = "com.example.actionprice.redis")
@Setter
public class RedisConfig {

  private String host;
  private int port;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(host, port);
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());

    // 일반적인 key:value의 경우 시리얼라이저
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());

    // Hash를 사용할 경우 시리얼라이저
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(new StringRedisSerializer());

    // 모든 경우
    redisTemplate.setDefaultSerializer(new StringRedisSerializer());

    return redisTemplate;
  }
}