package com.example.actionprice.redis.loginFailureCounter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class LoginFailureCounterServiceImpl implements LoginFailureCounterService {

  private final LoginFailureCounterRepository repository;

  @Override
  public LoginFailureCounterEntity getCounterEntity(String username) {
    return repository.findById(username).orElse(null);
  }

  @Override
  public void addOnePoint(String username) {
    LoginFailureCounterEntity entity = getOrCreateCounterEntity(username);

    if (entity.getFailureCount() < 5){
      entity.addOnePointToFailureCount();
      entity.resetTtl();

      repository.save(entity);
    }
  }

  @Override
  public void deleteCounterEntity(String username) {
    LoginFailureCounterEntity entity = repository.findById(username).orElse(null);
    if (entity == null){
      return;
    }

    repository.delete(entity);
  }

  private LoginFailureCounterEntity getOrCreateCounterEntity(String username) {
    LoginFailureCounterEntity entity = repository.findById(username).orElse(null);

    if (entity == null){
      entity = LoginFailureCounterEntity.builder()
          .username(username)
          .build();

      entity = repository.save(entity);
    }

    return entity;
  }
}
