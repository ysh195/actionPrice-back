package com.example.actionprice.exception;

// favorite 갯수가 너무 많을 때(10개 이상)
public class TooManyFavoritesException extends RuntimeException {
  public TooManyFavoritesException(int maxSize) {
    super(String.format("max size of favorite : %d", maxSize));
  }
}
