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
 * @info : user의 권한 객체를 Set<String>으로 바꾸면서 Set<GrantedAuthority>를 생성하는 로직 추가.
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

      // Set<String> authorities를 Set<GrantedAuthority>로 변환
      Set<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
          .map(role -> new SimpleGrantedAuthority(role))
          .collect(Collectors.toSet());

      return new org.springframework.security.core.userdetails.User(
          user.getUsername(),
          user.getPassword(),
          grantedAuthorities);
  }
}
