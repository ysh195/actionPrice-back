package com.example.actionprice.admin;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserSimpleDTO {
  String username;
  String email;
  int postCount;
  int commentCount;
  String authorities;
  LocalDateTime tokenExpiresAt;
  boolean isBlocked;
  // favorite은 시스템적으로 사이즈 제한을 걸어뒀으니 굳이 확인하지 않음
}