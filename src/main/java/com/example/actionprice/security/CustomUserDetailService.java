package com.example.actionprice.security;

import com.example.actionprice.user.User;
import com.example.actionprice.user.UserRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author : 연상훈
 * @created : 2024-10-06 오후 1:14
 * @updated : 2024-10-06 오후 1:14
 */
@Service
@Log4j2
public class CustomUserDetailService implements UserDetailsService {

  @Autowired
  UserRepository userRepository;

  @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      log.info("[class] CustomUserDetailService - [method] loadUserByUsername > 시작");

      // id = username
      User user = userRepository.findById(username)
          .orElseThrow(() -> new UsernameNotFoundException("유저[" + username + "]가 존재하지 않습니다."));

      log.info("[class] CustomUserDetailService - [method] loadUserByUsername > : " + user.toString());

    Set<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
        .map(role -> {return new SimpleGrantedAuthority(role);})
        .collect(Collectors.toSet());  // String을 GrantedAuthority로 변환

      return new org.springframework.security.core.userdetails.User(
          user.getUsername(),
          user.getPassword(),
          grantedAuthorities);
  }
}
