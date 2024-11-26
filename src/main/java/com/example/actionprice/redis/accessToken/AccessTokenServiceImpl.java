package com.example.actionprice.redis.accessToken;

import com.example.actionprice.exception.AccessTokenException;
import com.example.actionprice.exception.TokenErrors;
import com.example.actionprice.exception.UserNotFoundException;
import com.example.actionprice.redis.TemporaryEntities;
import com.example.actionprice.security.jwt.JWTUtil;
import com.example.actionprice.user.User;
import com.example.actionprice.user.UserRepository;
import com.example.actionprice.user.UserRole;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @value jwtUtil
 * @value refreshTokenService
 * @author 연상훈
 * @created 2024-10-20 오후 4:25
 * @info 리프레시 토큰에 관한 것은 모두 refreshTokenService에서 처리하니까 여기서는 엑세스 토큰만 관리
 */
@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class AccessTokenServiceImpl implements AccessTokenService {

  private final UserRepository userRepository;
  private final JWTUtil jwtUtil;
  private final AccessTokenRepository accessTokenRepository;

  /**
   * 리프레시 토큰 필터에서 엑세스 토큰만 발급함
   * @param username
   * @author 연상훈
   * @created 2024-10-20 오후 4:09
   * @info 사용자 검사, 리프레시 토큰 검사는 refreshTokenService에서 다 처리함
   * @info 그러니 여기서는 엑세스 토큰 발급만 하면 됨
   */
  @Override
  public Map<String, String> issueAccessToken(String username) {
    User user = userRepository.findById(username)
        .orElseThrow(() -> new UserNotFoundException(username));

    String accessToken = jwtUtil.generateToken(user, TemporaryEntities.ACCESS_TOKEN.getTtl());

    AccessTokenEntity accessTokenEntity = AccessTokenEntity.builder()
        .username(username)
        .accessToken(accessToken)
        .build();
    accessTokenRepository.save(accessTokenEntity);

    boolean isAdmin = user.getAuthorities().contains(UserRole.ROLE_ADMIN.name());
    String role = isAdmin ? UserRole.ROLE_ADMIN.name() : UserRole.ROLE_USER.name();

    Map<String, String> map = new HashMap<>();
    map.put(TemporaryEntities.ACCESS_TOKEN.getGlobalName(), accessToken);
    map.put("username", username);
    map.put("role", role);

    return map;
  }

  // 레디스에 저장된 게 있는지 확인해야 하기 때문에 예외처리 없이 null 반환
  @Override
  public AccessTokenEntity getAccessToken(String accessToken) {
    return accessTokenRepository.findById(accessToken).orElse(null);
  }

  /**
   * 엑세스 토큰을 엄격하게 검사 후 username을 추출함
   * @author 연상훈
   * @created 2024-11-08 오후 12:54
   */
  @Override
  public String validateAccessTokenAndExtractUsername(String accessToken) {
    try {
      String username = jwtUtil.validateToken(accessToken);
      return username;
    } catch (MalformedJwtException e) {
      log.error("잘못된 형식입니다. 위변조 가능성이 있는 엑세스 토큰입니다.");
      throw new AccessTokenException(TokenErrors.MALFORM);
    } catch (UnsupportedJwtException e) {
      log.error("잘못된 형식입니다. 위변조 가능성이 있는 엑세스 토큰입니다.");
      throw new AccessTokenException(TokenErrors.UNEXPECTED);
    } catch (SignatureException e) {
      log.error("잘못된 서명입니다. 위변조 가능성이 있는 엑세스 토큰입니다.");
      throw new AccessTokenException(TokenErrors.BADSIGN);
    } catch (ExpiredJwtException e) {
      log.error("만료된 엑세스 토큰입니다.");
      throw new AccessTokenException(TokenErrors.EXPIRED, e.getClaims().getSubject());
    }
  }

  @Override
  public String validateAccessTokenAndExtractUsernameWithoutEXP(String accessToken) {
    String username = null;
    try {
      username = jwtUtil.validateToken(accessToken);
    } catch (MalformedJwtException e) {
      log.error("잘못된 형식입니다. 위변조 가능성이 있는 엑세스 토큰입니다.");
      throw new AccessTokenException(TokenErrors.MALFORM);
    } catch (UnsupportedJwtException e) {
      log.error("잘못된 형식입니다. 위변조 가능성이 있는 엑세스 토큰입니다.");
      throw new AccessTokenException(TokenErrors.UNEXPECTED);
    } catch (SignatureException e) {
      log.error("잘못된 서명입니다. 위변조 가능성이 있는 엑세스 토큰입니다.");
      throw new AccessTokenException(TokenErrors.BADSIGN);
    } catch (ExpiredJwtException e) {
      username = e.getClaims().getSubject();
    }

    return username;
  }

  /**
   * 토큰 발급 후 결과를 json 형태로 반환
   * @author 연상훈
   * @created 2024-10-20 오후 1:27
   * @info 리프레시 토큰은 내부적으로만 관리하고, 반환하는 건 엑세스 토큰만
   */
  @Override
  public String returnWithJson(Map<String, String> map){
    return new Gson().toJson(map);
  }

  @Override
  public void deleteAccessToken(String username) {
    AccessTokenEntity accessTokenEntity = accessTokenRepository.findByUsername(username).orElse(null);
    if (accessTokenEntity == null) {
      return;
    }

    log.info("로그아웃 - 엑세스 토큰 삭제");

    accessTokenRepository.delete(accessTokenEntity);
  }
}
