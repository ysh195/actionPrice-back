package com.example.actionprice.exception;

/**
 * 리프레시 토큰 에러
 * @author : 연상훈
 * @created : 2024-10-06 오후 2:58
 * @updated 2024-10-19 오후 5:17 : 블랙리스트 기능 구현을 위해 TOKEN_ERROR에 BLOCKED 추가
 * @updated 2024-11-11 오전 5:16 : 불필요한 응답 방식 제거
 */
public class RefreshTokenException extends RuntimeException {

  private TokenErrors tokenErrors;
  private String username;

  public RefreshTokenException(TokenErrors tokenErrors) {
    super(tokenErrors.name());
    this.tokenErrors = tokenErrors;
  }

  public RefreshTokenException(TokenErrors tokenErrors, String username) {
    super(tokenErrors.name());
    this.tokenErrors = tokenErrors;
    this.username = username;
  }

  public TokenErrors getTokenErrors() {
    return tokenErrors;
  }

  public String getUsername() {
    return username;
  }

}
