package com.example.actionprice.redis.loginFailureCounter;

public interface LoginFailureCounterService {
  LoginFailureCounterEntity getOrCreateCounterEntity(String username);
  void addOnePoint(String username);
  void deleteCounterEntity(String username);
}
