package com.example.actionprice.exception;

/**
 * 엑세스 토큰 에러
 * @author : 연상훈
 * @created : 2024-10-06 오후 2:57
 * @updated 2024-10-17 오후 7:42 : 상태코드 숫자 수정
 * @updated 2024-10-19 오후 5:17 : 블랙리스트 기능 구현을 위해 TOKEN_ERROR에 BLOCKED 추가
 * @updated 2024-11-11 오전 5:16 : 불필요한 응답 방식 제거
 */
public class AccessTokenException extends RuntimeException {

  private final TokenErrors tokenErrors;
  private String username;

  public AccessTokenException(TokenErrors tokenErrors) {
    super(tokenErrors.name());
    this.tokenErrors = tokenErrors;
  }

  public AccessTokenException(TokenErrors tokenErrors, String username) {
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
