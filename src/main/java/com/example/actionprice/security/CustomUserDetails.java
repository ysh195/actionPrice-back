package com.example.actionprice.security;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 기존의 UserDetails 객체에는 rememberMe 필드가 없어서 새롭게 생성
 * @author : 연상훈
 * @created : 2024-10-14 오전 5:43
 * @updated : 2024-10-14 오전 5:43
 * @info :
 * rememberMe 구현을 위해서는 UserDetails(= principal)에 rememberMe가 있어야 하는데, 기존의 UserDetails에는 그게 없어서 새로 생성
 * 어차피 final은 없지만 불변 객체라서 set 메서드는 있어봐야 의미가 없음. 하지만 rememberMe 값이 바뀔 때마다 다시 집어넣기 위해 set 생성
 */
@Getter
@ToString
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

  private String username;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;
  private boolean active;
  private boolean rememberMe;

  public void setRememberMe(boolean rememberMe) {
    this.rememberMe = rememberMe;
  }
}