package com.example.actionprice.security.jwt.refreshToken;

import com.example.actionprice.exception.RefreshTokenException;
import com.example.actionprice.exception.TokenErrors;
import com.example.actionprice.exception.UserNotFoundException;
import com.example.actionprice.redis.TemporaryEntities;
import com.example.actionprice.security.jwt.JWTUtil;
import com.example.actionprice.user.User;
import com.example.actionprice.user.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 리프레시 토큰 서비스
 * @value refreshTokenRepository
 * @value userRepository
 * @value jwtUtil
 * @author 연상훈
 * @created 2024-10-19 오후 5:25
 * @info jwtUtil을 유일하게 관리하고 활용함.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class RefreshTokenServiceImpl implements RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;
  private final JWTUtil jwtUtil;

  @Override
  public User issueRefreshTokenOnLoginSuccess(String username) {
    User user = userRepository.findById(username)
        .orElseThrow(() -> new UserNotFoundException(username));

    RefreshTokenEntity refreshToken = user.getRefreshToken();

    // 신규 발급
    if (refreshToken == null){
      refreshToken = RefreshTokenEntity.builder()
          .refreshToken(jwtUtil.generateToken(user, TemporaryEntities.REFRESH_TOKEN.getTtl()))
          .user(user)
          .build();

      refreshToken = refreshTokenRepository.save(refreshToken);

      user.setRefreshToken(refreshToken);
      userRepository.save(user);

      return user;
    }

    // 이미 있다면 정지 당했는지 확인
    if (refreshToken.isBlocked()){
      throw new RefreshTokenException(TokenErrors.BLOCKED);
    }
    
    // 만료가 가깝거나 만료면 발급. 로그인이니까 그럼
    boolean isExpired = LocalDateTime.now().isAfter(refreshToken.getExpiresAt().minusMinutes(60));
    if (isExpired){
      refreshToken.resetExpiresAt();
      refreshToken.setRefreshToken(jwtUtil.generateToken(user, TemporaryEntities.REFRESH_TOKEN.getTtl()));

      refreshTokenRepository.save(refreshToken);
    }

    return user;
  }

  @Override
  public boolean setBlockUserByUsername(String username) {
    RefreshTokenEntity refreshToken = refreshTokenRepository.findByUser_Username(username)
        .orElseThrow(() -> new RefreshTokenException(TokenErrors.NOT_FOUND));

    boolean currentState = refreshToken.isBlocked();
    refreshToken.setBlocked(!currentState);

    refreshTokenRepository.save(refreshToken);

    return !currentState;
  }

  /**
   * 어드민페이지에서 토큰 재발급
   * @author 연상훈
   * @created 2024-10-28 오후 7:41
   * @info 어드민페이지에서 해주는 거라 검사도 없이 바로 재발급됨
   */
  @Override
  public void resetRefreshToken(String username) {
    RefreshTokenEntity refreshToken = refreshTokenRepository.findByUser_Username(username)
        .orElseThrow(() -> new RefreshTokenException(TokenErrors.NOT_FOUND));

    User user = refreshToken.getUser();

    refreshToken.resetExpiresAt();
    refreshToken.setRefreshToken(jwtUtil.generateToken(user, TemporaryEntities.REFRESH_TOKEN.getTtl()));

    refreshTokenRepository.save(refreshToken);
  }

  // private method
  /**
   * 입력 받은 리프레시 토큰에 대해 토큰 관련 모든 유효성 검사를 진행하는 메서드
   * @author 연상훈
   * @created 2024-10-20 오후 3:08
   * @info 리프레시 토큰을 통한 엑세스 토큰 재발급 과정에서 사용하기 때문에 가장 엄격하게 검사함
   */
  @Override
  public void validateRefreshToken(String username) {
    RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByUser_Username(username)
        .orElseThrow(() -> new RefreshTokenException(TokenErrors.NOT_FOUND));

    if (refreshTokenEntity.isBlocked()) {
      throw new RefreshTokenException(TokenErrors.BLOCKED);
    }

    try {
      jwtUtil.validateToken(refreshTokenEntity.getRefreshToken());
    } catch (UnsupportedJwtException e) {
      log.error("지원되지 않는 형식입니다. 위변조 가능성이 있는 리프레시 토큰입니다.");
      throw new RefreshTokenException(TokenErrors.UNEXPECTED);
    } catch (MalformedJwtException e) {
      log.error("잘못된 형식입니다. 위변조 가능성이 있는 리프레시 토큰입니다.");
      throw new RefreshTokenException(TokenErrors.MALFORM);
    } catch (SignatureException e) {
      log.error("잘못된 서명입니다. 위변조 가능성이 있는 리프레시 토큰입니다.");
      throw new RefreshTokenException(TokenErrors.BADSIGN);
    } catch (ExpiredJwtException e) {
      log.error("만료된 리프레시 토큰입니다.");
      throw new RefreshTokenException(TokenErrors.EXPIRED, e.getClaims().getSubject());
    }

    // 이미 만료였다면 위에서 ExpiredJwtException에 걸려서 예외로 던져버림.
    // 따라서 여기로 왔다면 만료는 아닌 것.
    // 그리고 현재 시간이 만료되기까지 60분도 안 남았다면
    boolean isExpired = LocalDateTime.now().isAfter(refreshTokenEntity.getExpiresAt().minusMinutes(60));

    // 만료는 아니지만, 만료까지 얼마 안 남았으면 이걸로 처리
    if (isExpired){
      User user = refreshTokenEntity.getUser();
      refreshTokenEntity.resetExpiresAt();
      refreshTokenEntity.setRefreshToken(jwtUtil.generateToken(user, TemporaryEntities.REFRESH_TOKEN.getTtl()));
    }
  }
}
