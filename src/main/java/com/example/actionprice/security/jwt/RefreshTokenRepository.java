package com.example.actionprice.security.jwt;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 연상훈
 * @created 2024-10-19 오후 5:24
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
  Optional<RefreshTokenEntity> findByUsername(String username);
  void deleteByUsername(String username); // 사용자 삭제할 때 간단하게 하기 위해
}
