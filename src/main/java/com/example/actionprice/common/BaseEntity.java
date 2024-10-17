package com.example.actionprice.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * base entity
 * @author 연상훈
 * @created 2024-10-17 오후 8:03
 * @value createdAt : 자동생성, 변경 불가
 * @value updatedAt : 자동 생성, 변경 가능
 * @info post, comment, verificationEmail에서 사용 중입니다.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {

  @CreatedDate
  @Column(updatable = false, name = "createdAt")
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updatedAt")
  private LocalDateTime updatedAt;
}
