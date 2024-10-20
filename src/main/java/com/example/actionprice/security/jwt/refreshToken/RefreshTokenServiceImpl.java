package com.example.actionprice.security.jwt.refreshToken;

import com.example.actionprice.exception.RefreshTokenException;
import com.example.actionprice.exception.RefreshTokenException.ErrorCase;
import com.example.actionprice.security.jwt.JWTUtil;
import com.example.actionprice.user.User;
import com.example.actionprice.user.UserRepository;
import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 리프레시 토큰 서비스
 * @value refreshTokenValidityInMinutes : 리프레시 토큰 유효 시간 : 180(분)
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

  private final int refreshTokenValidityInMinutes = 60*3; // 180분. 분단위

  // public method
  /**
   * 로그인 석세스 핸들러에서 토큰 발급에 사용되 메서드
   * @param username
   * @author 연상훈
   * @created 2024-10-20 오후 1:02
   * @info 리프레시 토큰 발급 받으려고 로그인하는 거니까 리프레시 토큰에 대한 검증이 거의 필요 없음.
   * @info 토큰이 없으면(refreshToken = null) 새로 발급해주고, 만약 이미 있는 게 블록된 것인지만 체크하면 됨
   */
  @Override
  public void issueRefreshToken(String username, Map<String, Object> claim) {

    // 해당 사용자에게 이미 리프레시 토큰이 있는지 체크하고 없으면 새로 생성
    User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("유저[" + username + "]가 존재하지 않습니다."));
    RefreshTokenEntity refreshTokenEntity = user.getRefreshToken();

    // 토큰이 존재하는지 체크
    if(refreshTokenEntity == null) {
      // 로그인 로직이기 때문에 없으면 새로 발급해줘야 함
      refreshTokenEntity = RefreshTokenEntity.builder()
          .refreshToken(jwtUtil.generateToken(claim, refreshTokenValidityInMinutes))
          .expiresAt(LocalDateTime.now().plusMinutes(refreshTokenValidityInMinutes))
          .build();
    }

    // 리프레시 토큰 재발급 받으려고 하거나 아직 발급 받은 적 없는 사람일 수 있으니,
    // 블록 여부와 위변조 여부만 확인.
    checkRefreshToken_partially(refreshTokenEntity);

    // 토큰이 만료되기 60분 전이면 리프레시 토큰 재발급
    if (refreshTokenEntity.getExpiresAt().isBefore(LocalDateTime.now().plusMinutes(60))) {
      refreshTokenEntity.setRefreshToken(jwtUtil.generateToken(Map.of("username", username), refreshTokenValidityInMinutes));
      refreshTokenEntity.setExpiresAt(LocalDateTime.now().plusMinutes(refreshTokenValidityInMinutes));
    }

    // 모든 작업이 완료됐으니 서로 연결 시켜주고, 레포지토리에 반영
    user.setRefreshToken(refreshTokenEntity);
    refreshTokenEntity.setUser(user);
    refreshTokenRepository.save(refreshTokenEntity);
    userRepository.save(user);
  }

  /**
   * 리프레시 토큰 필터에서 엑세스 토큰 발급에 사용되는 메서드
   * @param username
   * @author 연상훈
   * @created 2024-10-20 오후 1:05
   * @info 엑세스 토큰에 대한 검증은 리프레시 토큰 필터에서 진행함
   * @info 이 메서드는 엑세스 토큰 발급만 처리해야 하고, 리프레시 토큰에 문제 있으면 바로 예외 처리 해야 함
   * @info 그래서 토큰 검사가 가장 엄격해야 함
   */
  public void checkRefreshFirst(String username) {

    User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("유저[" + username + "]가 존재하지 않습니다."));
    RefreshTokenEntity refreshTokenEntity = user.getRefreshToken();

    // 토큰이 존재하는지 체크
    if(refreshTokenEntity == null) {
      throw new RefreshTokenException(ErrorCase.NO_REFRESH);
    }

    // 그 외 모든 레프레시 토큰 검사 실시
    checkRefreshToken_all(refreshTokenEntity);
  }

  /**
   * 로그아웃 토큰 필터에서 로그아웃 성공 후 기존 토큰 삭제에 사용되는 메서드
   * @param username
   * @author 연상훈
   * @created 2024-10-20 오후 1:08
   * @info 어차피 리프레시 토큰을 삭제할 것이기 때문에 토큰이 존재하는지와 block 된 사용자가 토큰 삭제 시도 하는 게 아닌지만 확인하면 됨.
   * @info 로그인 중 리프레시 토큰 유효시간이 끝난 상태에서 로그아웃 하려는데, 토큰 만료라고 오류 떠서 로그아웃 못하면 안 되니까
   * @info block 된 사용자가 임의로 토큰을 삭제할 수 있으면 블랙리스트는 없는 거나 마찬가지니까 함부러 못 지우게 해야 함
   */
  @Override
  public void discardRefreshToken(String username) {
    User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("유저[" + username + "]가 존재하지 않습니다."));
    RefreshTokenEntity refreshTokenEntity = user.getRefreshToken();

    // 토큰이 존재하는지 체크
    if(refreshTokenEntity == null) {
      throw new RefreshTokenException(ErrorCase.NO_REFRESH);
    }

    // 리프레시 토큰 재발급 받으려고 하거나 아직 발급 받은 적 없는 사람일 수 있으니,
    // 블록 여부와 위변조 여부만 확인.
    checkRefreshToken_partially(refreshTokenEntity);

    // 리프레시 토큰 삭제를 각 레포지토리와 user 객체에 반영
    user.setRefreshToken(null);
    refreshTokenRepository.delete(refreshTokenEntity);
    userRepository.save(user);
  }

  // private method
  /**
   * 입력 받은 리프레시 토큰에 대해 토큰 관련 모든 유효성 검사를 진행하는 메서드
   * @param refreshTokenEntity
   * @author 연상훈
   * @created 2024-10-20 오후 3:08
   * @info 리프레시 토큰을 통한 엑세스 토큰 재발급 과정에서 사용하기 때문에 가장 엄격하게 검사함
   */
  public void checkRefreshToken_all(RefreshTokenEntity refreshTokenEntity) {

    if (refreshTokenEntity.isBlocked()) {
      throw new RefreshTokenException(ErrorCase.BLOCKED_REFRESH);
    }

    try {
      jwtUtil.validateToken(refreshTokenEntity.getRefreshToken());
    } catch (ExpiredJwtException e) {
      log.error("만료된 리프레시 토큰입니다.");
      throw new RefreshTokenException(ErrorCase.EXPIRED_REFRESH);
    } catch (UnsupportedJwtException e) {
      log.error("지원되지 않는 형식입니다. 위변조 가능성이 있는 리프레시 토큰입니다.");
      throw new RefreshTokenException(ErrorCase.INVALID_REFRESH);
    } catch (MalformedJwtException e) {
      log.error("잘못된 형식입니다. 위변조 가능성이 있는 리프레시 토큰입니다.");
      throw new RefreshTokenException(ErrorCase.INVALID_REFRESH);
    } catch (ClaimJwtException e) {
      log.error("잘못된 클레임입니다. 위변조 가능성이 있는 리프레시 토큰입니다.");
      throw new RefreshTokenException(ErrorCase.UNEXPECTED_REFRESH);
    } catch (SignatureException e) {
      log.error("잘못된 서명입니다. 위변조 가능성이 있는 리프레시 토큰입니다.");
      throw new RefreshTokenException(ErrorCase.BADSIGN_REFRESH);
    } catch (JwtException e){
      log.error("리프레시 토큰 검사 중 기타 오류가 발생하였습니다.");
      throw new RefreshTokenException(ErrorCase.NO_ACCESS);
    }
  }

  /**
   * 입력 받은 리프레시 토큰에 대해 토큰 관련 일부 유효성 검사를 진행하는 메서드
   * @param refreshTokenEntity
   * @author 연상훈
   * @created 2024-10-20 오후 3:08
   * @info 리프레시 토큰 발급/재발급 또는 삭제 시 사용하기 때문에 위변조 여부만 검사함
   * @info 위변조 된 리프레시 토큰 가져와서 멀쩡한 것으로 교환하려는 시도를 차단해야 하니까
   */
  public void checkRefreshToken_partially(RefreshTokenEntity refreshTokenEntity) {

    if (refreshTokenEntity.isBlocked()) {
      throw new RefreshTokenException(ErrorCase.BLOCKED_REFRESH);
    }

    try {
      jwtUtil.validateToken(refreshTokenEntity.getRefreshToken());
    } catch (UnsupportedJwtException e) {
      log.error("지원되지 않는 형식입니다. 위변조 가능성이 있는 리프레시 토큰입니다.");
      throw new RefreshTokenException(ErrorCase.INVALID_REFRESH);
    } catch (MalformedJwtException e) {
      log.error("잘못된 형식입니다. 위변조 가능성이 있는 리프레시 토큰입니다.");
      throw new RefreshTokenException(ErrorCase.INVALID_REFRESH);
    } catch (ClaimJwtException e) {
      log.error("잘못된 클레임입니다. 위변조 가능성이 있는 리프레시 토큰입니다.");
      throw new RefreshTokenException(ErrorCase.UNEXPECTED_REFRESH);
    } catch (SignatureException e) {
      log.error("잘못된 서명입니다. 위변조 가능성이 있는 리프레시 토큰입니다.");
      throw new RefreshTokenException(ErrorCase.BADSIGN_REFRESH);
    }
  }
}
