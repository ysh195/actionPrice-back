package com.example.actionprice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // 이게 없으면 @CreatedDate 등이 작동을 안 함
@EnableCaching // 캐싱 기능 활성화
public class ActionPriceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ActionPriceApplication.class, args);
  }

}
