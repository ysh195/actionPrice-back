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
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @value jwtUtil
 * @value refreshTokenService
 * @author 연상훈
 * @created 2024-10-20 오후 4:25
 * @info 리프레시 토큰에 관한 것은 모두 refreshTokenService에서 처리하니까 여기서는 엑세스 토큰만 관리
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class AccessTokenServiceImpl implements AccessTokenService {

  private final UserRepository userRepository;
  private final JWTUtil jwtUtil;
  private final RedisTemplate<String, Object> redisTemplate;

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
        .orElseThrow(() -> new UserNotFoundException("user(" + username + ") does not exist"));

    // checkRefreshFirst에서 문제 없었으니 엑세스 토큰 발급
    String accessToken = jwtUtil.generateToken(user, TemporaryEntities.ACCESS_TOKEN.getTtl());

    TemporaryEntities temporaryEntities = TemporaryEntities.ACCESS_TOKEN;
    String key = joinKeyTypeAndKeyValue(temporaryEntities.getGlobalName(), accessToken);

    redisTemplate.opsForValue().set(key, username, temporaryEntities.getTtl(), TimeUnit.MILLISECONDS);

    boolean isAdmin = user.getAuthorities().contains(UserRole.ROLE_ADMIN.name());
    String role = isAdmin ? UserRole.ROLE_ADMIN.name() : UserRole.ROLE_USER.name();

    Map<String, String> map = new HashMap<>();
    map.put(temporaryEntities.getGlobalName(), accessToken);
    map.put("username", username);
    map.put("role", role);

    return map;
  }

  @Override
  public String getUsernameFromAccessToken(String accessToken) {
    String key = joinKeyTypeAndKeyValue(TemporaryEntities.ACCESS_TOKEN.getGlobalName(), accessToken);
    return (String) redisTemplate.opsForValue().get(key);
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

  private String joinKeyTypeAndKeyValue(String keyType, String keyValue) {
    return String.format("%s:%s", keyType, keyValue);
  }
}
