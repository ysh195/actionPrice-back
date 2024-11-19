package com.example.actionprice.redis.sendEmail;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * @author : 연상훈
 * @created : 2024-10-06 오후 7:48
 */
public interface VerificationEmailRepository extends CrudRepository<VerificationEmail, String> {
  Optional<VerificationEmail> findById(String email); // id = email
}
