package com.example.actionprice.sendEmail;


import com.example.actionprice.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 가입 시 이메일로 인증코드를 발송하고, 그것을 이후에 대조하기 위해 DB에 저장하는 객체
 * @author : 연상훈
 * @created : 2024-10-06 오후 7:15
 * @value email
 * @value verificationCode
 */
@Data
@Builder
@Entity
@Table(name = "verification_email")
@AllArgsConstructor
@NoArgsConstructor
public class VerificationEmail extends BaseEntity {

  @Id
  private String email;

  @Column(nullable=false, length = 8)
  private String verificationCode;
}
