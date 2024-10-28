package com.example.actionprice.security.jwt.refreshToken;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 연상훈
 * @created 2024-10-19 오후 5:24
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
  Optional<RefreshTokenEntity> findByUser_Username(String username);
}
