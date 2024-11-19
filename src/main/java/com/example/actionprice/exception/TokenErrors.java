package com.example.actionprice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

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
