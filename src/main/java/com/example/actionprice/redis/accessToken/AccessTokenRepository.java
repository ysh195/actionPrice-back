package com.example.actionprice.redis.accessToken;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AccessTokenRepository extends CrudRepository<AccessTokenEntity, String> {
  Optional<AccessTokenEntity> findById(String accessToken);
}
