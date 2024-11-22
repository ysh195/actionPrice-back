package com.example.actionprice.security.jwt.refreshToken;


import com.example.actionprice.redis.TemporaryEntities;
import com.example.actionprice.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

import lombok.*;

/**
 * 리프레시 토큰 객체
 * @value tokenId
 * @value refreshToken : 리프레시 토큰값
 * @value username : 발급 받은 사용자 이름
 * @value expiresAt : 토큰 만료 시간
 * @value blocked : 이용 금지 처분 여부. true면 금지, false면 정상
 * @author 연상훈
 * @created 2024-10-19 오후 4:43
 * @updated 2024-10-20 오후 12:32 : 1. 단순 set 메서드가 너무 많아서 그냥 @Setter 사용함. 2. User 객체와 연결함
 */
@Entity
@Table(name = "refresh_token")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "refresh_token_id")
  private Long tokenId;

  @Column(nullable = false, unique = true)
  private String refreshToken;

  @OneToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "username", referencedColumnName = "username", nullable = true)
  private User user;

  @Column(nullable = false)
  @Builder.Default
  private LocalDateTime expiresAt =
      LocalDateTime.now().plusSeconds(TemporaryEntities.REFRESH_TOKEN.getTtl());

  @Builder.Default
  private boolean blocked = false;

  public void resetExpiresAt() {
    this.expiresAt =
        LocalDateTime.now().plusSeconds(TemporaryEntities.REFRESH_TOKEN.getTtl());
  }
}
