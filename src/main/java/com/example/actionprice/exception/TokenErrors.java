package com.example.actionprice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 토큰 에러 설정
 * @author 연상훈
 * @created 2024-11-23 오전 3:08
 * @info 토큰 만료 시에는 토큰 재발급을 위해 상태 코드를 특수한 걸로 설정함.
 * 프론트에서 상태 코드를 보고 재발급 요청을 해야 할 테니
 */
@Getter
public enum TokenErrors {
  BADTYPE(HttpStatus.UNAUTHORIZED, "Token type must be Bearer"),
  EXPIRED(HttpStatus.I_AM_A_TEAPOT, "Expired Token"),
  MALFORM(HttpStatus.FORBIDDEN, "Malformed Token"),
  UNEXPECTED(HttpStatus.FORBIDDEN, "Unexpected claim"),
  BADSIGN(HttpStatus.FORBIDDEN, "Bad Signature Token"),
  BLOCKED(HttpStatus.FORBIDDEN, "Blocked"),
  NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found");

  private HttpStatus status;
  private String message;

  TokenErrors(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
