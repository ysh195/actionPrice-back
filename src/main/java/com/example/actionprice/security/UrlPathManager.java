package com.example.actionprice.security;

import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

/**
 * url path만 관리하는 곳
 * @author 연상훈
 * @created 2024-11-13 오전 12:11
 * @info url path가 너무 많아서 여기서 따로 관리함
 * @info 되도록 각 컨트롤러별로 관리
 */
@NoArgsConstructor
public class UrlPathManager {
  
  public void configureAllEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authz) {
    basicEndpoints(authz);
    adminEndPoints(authz);
    userEndPoints(authz);
    myPageEndPoints(authz);
    favoriteEndPoints(authz);
    categoryEndPoints(authz);
    tokenRefreshEndPoints(authz);
    postEndPoints(authz);
    commentEndPoints(authz);
  }

  private void basicEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authz) {
    authz.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
    authz.requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll(); // 스웨거
  }
  
  private void adminEndPoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authz) {
    // 그냥 싹 다 어드민 권한 있어야 가능
    authz.requestMatchers("/api/admin/**").hasRole("ADMIN");
  }

  private void userEndPoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authz) {
    authz.requestMatchers("/api/user/logout").authenticated();
    authz.requestMatchers("/api/user/login").anonymous();
    authz.requestMatchers("/api/user/**").permitAll();
  }

  private void myPageEndPoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authz) {
    authz.requestMatchers("/api/myPage/**").authenticated();
  }

  private void favoriteEndPoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authz) {
    authz.requestMatchers("/api/category/favorite/**").authenticated();
  }

  private void tokenRefreshEndPoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authz) {
    authz.requestMatchers("/api/auth/**").authenticated();
  }

  private void categoryEndPoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authz) {
    authz.requestMatchers("/api/category/**").permitAll();
  }

  private void postEndPoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authz) {
    authz.requestMatchers(HttpMethod.POST,"/api/post").authenticated(); // 생성
    authz.requestMatchers(HttpMethod.GET,"/api/post/*").permitAll(); // 상세 조회 혹은 리스트 조회(게시글 번호가 들어가면 상세, list가 들어가면 리스트)
    authz.requestMatchers(HttpMethod.PATCH,"/api/post/*").authenticated(); // 수정
    authz.requestMatchers(HttpMethod.DELETE,"/api/post/*").authenticated(); // 삭제
    authz.requestMatchers(HttpMethod.GET,"/api/post/*/username/*").authenticated(); // 수정을 위한 조회
  }

  private void commentEndPoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authz) {
    authz.requestMatchers("/api/post/*/comment", "/api/post/*/comment/*").authenticated(); // 생성
    authz.requestMatchers("/api/post/comments").permitAll();
  }

}
