package com.example.actionprice.healthCheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

  @GetMapping("/")
  public String healthCheck() {
    return "서비스 작동 중";
  }

}
