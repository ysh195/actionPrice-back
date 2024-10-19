package com.example.actionprice.security.jwt;

import com.example.actionprice.exception.RefreshTokenException;
import com.example.actionprice.user.User;
import com.example.actionprice.user.UserRepository;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 리프레시 토큰 서비스
 * @value accessTokenValidityInMinutes : 엑세스 토큰 유효 시간 : 60(분)
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

  private final int accessTokenValidityInMinutes = 60; // 60분. 분단위
  private final int refreshTokenValidityInMinutes = 60*3; // 180분. 분단위

  // 리프레시 토큰 필터에서 사용됨. 그러니 엑세스 토큰만 발급하고, 리프레시 토큰에 문제 있으면 바로 예외 처리
  @Override
  public String issueAccessToken(String username) {
    // 혹시 사용자가 아닐 수도 있음
    User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("유저[" + username + "]가 존재하지 않습니다."));

    // 기존 사용자인지 찾아보고 아니면 예외 처리
    RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByUsername(username)
        .orElseThrow(() -> new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH));

    // 블록된 유저인지 확인
    if (refreshTokenEntity.isBlocked()) {
      throw new RefreshTokenException(RefreshTokenException.ErrorCase.BLOCKED);
    }

    // 그 외 각종 토큰 검사 실시
    checkRefreshToken(refreshTokenEntity.getRefreshToken());

    // 엑세스 토큰 발급. 엑세스 토큰에 대한 검증은 리프레시 토큰 필터에서 진행함
    String accessToken = jwtUtil.generateToken(Map.of("username", username), accessTokenValidityInMinutes);

    return returnWithJson(accessToken, username);
  }

  // 로그인 석세스 핸들러에서 사용됨. 로그인이 성공했다는 것은 확실히 우리 사용자라는 의미니까 사용자에 대한 검사는 필요 없음
  @Override
  public String issueJwt(String username) {

    // 해당 사용자에게 이미 리프레시 토큰이 있는지 체크하고 없으면 새로 생성
    Map<String, Object> claim = Map.of("username", username);
    RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByUsername(username)
        .orElseGet(() -> createRefreshTokenEntity(claim, username));

    // 리프레시 토큰 재발급 받으려고 하거나 아직 발급 받은 적 없는 사람일 수 있으니, 블록 여부만 확인.
    if (refreshTokenEntity.isBlocked()) {
      throw new RefreshTokenException(RefreshTokenException.ErrorCase.BLOCKED);
    }

    String accessToken = jwtUtil.generateToken(claim, accessTokenValidityInMinutes);

    // 토큰이 만료되기 60분 전이면 재발급
    if (refreshTokenEntity.getExpiresAt().isBefore(LocalDateTime.now().plusMinutes(60))) {
      refreshTokenEntity.setRefreshToken(jwtUtil.generateToken(Map.of("username", username), refreshTokenValidityInMinutes));
      refreshTokenEntity.setExpiresAt(LocalDateTime.now().plusMinutes(refreshTokenValidityInMinutes));
    }

    refreshTokenRepository.save(refreshTokenEntity);

    return returnWithJson(accessToken, username);
  }

  @Override
  public void discardJwt(String username) {
    // 혹시 사용자가 아닐 수도 있음
    User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("유저[" + username + "]가 존재하지 않습니다."));

    // 해당 유저에게 리프레시 토큰이 발급된 적 있는지 확인하고 아니면 예외 처리
    RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByUsername(username)
        .orElseThrow(() -> new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH));

    // 블록된 유저인지 확인
    if (refreshTokenEntity.isBlocked()) {
      throw new RefreshTokenException(RefreshTokenException.ErrorCase.BLOCKED);
    }

    // 그 외 각종 토큰 검사 실시
    checkRefreshToken(refreshTokenEntity.getRefreshToken());

    refreshTokenRepository.delete(refreshTokenEntity);
  }

  @Override
  public Map<String, Object> validateJwtToken(String token) {
    return jwtUtil.validateToken(token);
  }

  private void checkRefreshToken(String refreshToken) throws RefreshTokenException {
    try {
      jwtUtil.validateToken(refreshToken);
    } catch (ExpiredJwtException e) {
      throw new RefreshTokenException(RefreshTokenException.ErrorCase.OLD_REFRESH);
    } catch (MalformedJwtException e) {
      throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
    } catch (Exception e) {
      throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
    }
  }

  private RefreshTokenEntity createRefreshTokenEntity(Map<String, Object> claim, String username){
    return RefreshTokenEntity.builder()
        .refreshToken(jwtUtil.generateToken(claim, refreshTokenValidityInMinutes))
        .username(username)
        .expiresAt(LocalDateTime.now().plusMinutes(refreshTokenValidityInMinutes))
        .build();
  }

  private String returnWithJson(String accessToken, String username){
    return new Gson().toJson(Map.of("access_token", accessToken, "username", username));
  }
}
