package com.example.actionprice.exception;

public class UserNotFoundException extends RuntimeException{
  public UserNotFoundException(String message){
    super(message);
  }
}
