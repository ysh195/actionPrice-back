package com.example.actionprice.redis.loginFailureCounter;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface LoginFailureCounterRepository extends CrudRepository<LoginFailureCounterEntity, String> {
  Optional<LoginFailureCounterEntity> findById(String username);

}