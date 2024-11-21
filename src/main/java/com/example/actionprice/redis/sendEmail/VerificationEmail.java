package com.example.actionprice.redis.sendEmail;

import com.example.actionprice.redis.TemporaryEntities;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

/**
 * 회원 가입 시 이메일로 인증코드를 발송하고, 그것을 이후에 대조하기 위해 DB에 저장하는 객체
 * @author : 연상훈
 * @created : 2024-10-06 오후 7:15
 * @value email
 * @value verificationCode
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "verification_email")
public class VerificationEmail {

  @Id
  private String email;

  @Column(nullable=false, length = 8)
  private String verificationCode;

  @TimeToLive
  @Builder.Default
  private long ttl = TemporaryEntities.VERIFICATION_EMAIL.getTtl();
}
