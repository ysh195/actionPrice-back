package com.example.actionprice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ActionPriceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ActionPriceApplication.class, args);
  }

}
