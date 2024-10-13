package com.example.actionprice.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * rememberMe 구현을 위한 테이블 생성용 객체
 * @author : 연상훈
 * @created : 2024-10-12 오후 11:10
 * @updated : 2024-10-12 오후 11:10
 * @info : 리멤버미 토큰 정보를 저장하기 위한 테이블 생성용
 */
@Table(name = "persistent_logins")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersistentLogins {

  @Id
  @Column(name = "series", length = 64)
  private String series;

  @Column(name = "username", nullable = false, length = 64)
  private String username;

  @Column(name = "token", nullable = false, length = 64)
  private String token;

  @Column(name = "last_used", nullable = false, length = 64)
  private LocalDateTime lastUsed;
}