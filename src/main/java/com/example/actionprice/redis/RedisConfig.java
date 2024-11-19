package com.example.actionprice.redis;

import com.example.actionprice.redis.accessToken.AccessTokenRepository;
import com.example.actionprice.redis.loginFailureCounter.LoginFailureCounterRepository;
import com.example.actionprice.redis.sendEmail.VerificationEmailRepository;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
@EnableRedisRepositories(basePackageClasses = {
    VerificationEmailRepository.class,
    LoginFailureCounterRepository.class,
    AccessTokenRepository.class
})
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