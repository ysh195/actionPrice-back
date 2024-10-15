package com.example.actionprice.security;

import com.example.actionprice.user.User;
import com.example.actionprice.user.UserRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author : 연상훈
 * @created : 2024-10-06 오후 1:14
 * @updated : 2024-10-011 오후 11:32
 * - user의 권한 객체를 Set<String>으로 바꾸면서 Set<GrantedAuthority>를 생성하는 로직 추가.
 * - [2024-10-14 오전 5:42] : 리멤버미 필드를 추가한 CustomUserDetails를 리턴하도록 변경
 * @info :
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      log.info("[class] CustomUserDetailService - [method] loadUserByUsername > 시작");

      // id = username
      User user = userRepository.findById(username)
          .orElseThrow(() -> new UsernameNotFoundException("유저[" + username + "]가 존재하지 않습니다."));

      log.info("[class] CustomUserDetailService - [method] loadUserByUsername > : " + user.toString());

      // 여기서 유저의 유저네임이나 리프레시 토큰으로 블랙리스트에서 조회하고,
      // 그 결과에 따라 CustomUserDetails의 active 값을 바꿔주면 로그인 못하게 막을 수 있음

      // Set<String> authorities를 Set<GrantedAuthority>로 변환
      Set<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
          .map(role -> new SimpleGrantedAuthority(role))
          .collect(Collectors.toSet());

      return  new org.springframework.security.core.userdetails.User(
              user.getUsername(),
              user.getPassword(),
              grantedAuthorities);
  }
}
