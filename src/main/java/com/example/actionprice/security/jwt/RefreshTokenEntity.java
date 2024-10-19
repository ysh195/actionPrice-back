package com.example.actionprice.security.jwt;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 리프레시 토큰 객체
 * @value tokenId
 * @value refreshToken : 리프레시 토큰값
 * @value username : 발급 받은 사용자 이름
 * @value expiresAt : 토큰 만료 시간
 * @value blocked : 이용 금지 처분 여부. true면 금지, false면 정상
 * @author 연상훈
 * @created 2024-10-19 오후 4:43
 * @info 관리의 편의를 위해 user와 연결되어 있지 않음. 하지만 차라리 연결을 하는 게 좋을 수도 있어서 고민 중
 * @info refreshToken, expiresAt, blocked만 set 메서드 있음
 */
@Entity
@Table(name = "refresh_token")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long tokenId;

  private String refreshToken;

  private String username;

  private LocalDateTime expiresAt;

  @Builder.Default
  private boolean blocked = false;

  public void setBlocked(boolean blocked) {
    this.blocked = blocked;
  }

  public void setExpiresAt(LocalDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
