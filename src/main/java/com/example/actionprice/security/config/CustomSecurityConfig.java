package com.example.actionprice.security.config;

import com.example.actionprice.redis.loginFailureCounter.LoginFailureCounterService;
import com.example.actionprice.security.CustomUserDetailService;
import com.example.actionprice.security.UrlPathManager;
import com.example.actionprice.security.handler.LoginSuccessHandler;
import com.example.actionprice.security.filter.LoginFilter;
import com.example.actionprice.security.filter.JwtAuthenticationFilter;
import com.example.actionprice.redis.accessToken.AccessTokenService;
import com.example.actionprice.security.jwt.refreshToken.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * 보안 관련된 거의 모든 설정이 있는 곳
 * @author 연상훈
 * @created 24/10/01 13:46
 * @updated 2024-11-13 오전 1:23 [연상훈] : bean과 url path 정리. 지나치게 길고 많은 url들을 UrlPathManager에서 관리하도록 함
 */
@SuppressWarnings("ALL")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
@Log4j2
public class CustomSecurityConfig {

    @Value("${server.port}")
    private String port;

    @Value("${frontend.port:#{null}}")
    private String frontendPort;

    @Value("${backend.domain:#{null}}")
    private String backendDomain;

    @Value("${frontend.domain:#{null}}")
    private String frontendDomain;

    private final CustomUserDetailService userDetailsService;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final LoginFailureCounterService loginFailureCounterService;

    /**
     * @author : 연상훈
     * @created : 2024-10-05 오후 9:27
     * @info component나 configuration으로 등록하기엔 자잘한 것들을 여기서 bean으로 설정해서 관리함 
     * @info 요청 url path 설정에 주의
     * @info 토큰 필터 순서에 주의. jwtAuthentication 필터 > 로그인/로그아웃 필터 > UsernamePasswordAuthenticationFilter
     * @see : 설정 목록
     * [활성화]
     * 1. cors
     * [비활성화]
     * 1. 세션
     * 2. csrf
     * 3. formLogin(로그인페이지 접근 거절 시 리다이렉트 비활성화 / 프론트에서 알아서 처리함)
     * 4. accessDeniedHandler(권한 없음으로 인한 접근 거절 시 리다이렉트 비활성화 / 프론트에서 알아서 처리함)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

      log.info("------------ security configuration --------------");

      //유저 권한 password 검증
      AuthenticationManager authenticationManager = authenticationManager(http);

      http.sessionManagement(sessionPolicy -> sessionPolicy.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource())) // corsConfigurationSource
          .csrf(csrfconfig -> csrfconfig.disable())
          .exceptionHandling(exceptionHandler -> exceptionHandler
              .accessDeniedHandler(accessDeniedHandler()) // 인증 실패 후 리다이렉트하지 않도록 만들어서 계속 이상한 곳으로 요청 보내지 않도록 함
              .authenticationEntryPoint(new Http403ForbiddenEntryPoint())) // 사용자가 허락되지 않은 경로로 강제 이동 시의 처리를 진행
          .authorizeHttpRequests(authz -> {
            new UrlPathManager().configureAllEndpoints(authz);
          })
          .authenticationManager(authenticationManager)
          .addFilterBefore(new JwtAuthenticationFilter(userDetailsService, accessTokenService), UsernamePasswordAuthenticationFilter.class)
          .addFilterBefore(loginFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
          .addFilterBefore(logoutFilter(), UsernamePasswordAuthenticationFilter.class) // 필터 순서에 주의. 기본적으로 나중에 입력한 것일수록 뒤에 실행됨
          .formLogin(formLogin -> formLogin.disable());
      return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
      AuthenticationManagerBuilder authenticationManagerBuilder =
              http.getSharedObject(AuthenticationManagerBuilder.class);

      authenticationManagerBuilder.userDetailsService(userDetailsService)
              .passwordEncoder(passwordEncoder());

      return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }

    // 재사용도 안 하고 어디서 주입 받거나 주입할 일이 없는 것들은 모두 @bean이 아닌 priavte로 관리.

    /**
     * 로그인 필터. 로그인 절차를 임의로 수정하기 위해서 필요함
     * @author 연상훈
     * @created 2024-11-07 오후 11:37
     * @info 인증을 위해서 userDetailsService와 authenticationManager는 필수
     * @info 로그인 후의 로직을 임의로 구성하려면 loginSuccessHandler 필요
     */
    private LoginFilter loginFilter(AuthenticationManager authenticationManager) throws Exception {
      return new LoginFilter(
              "/api/user/login",
              userDetailsService,
              new LoginSuccessHandler(accessTokenService, refreshTokenService),
              loginFailureCounterService,
              authenticationManager
      );
    }

    private AccessDeniedHandler accessDeniedHandler(){
      return (request, response, accessDeniedException) -> {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
      };
    }

    /**
     * @author 연상훈
     * @created 24/10/04
     * @updated 24/10/04
     * @see : 리액트-스프링부트 연결을 위한 cors config
     */
    private CorsConfigurationSource corsConfigurationSource() { // corsConfigurationSource
        CorsConfiguration configuration = new CorsConfiguration();

        // 허용할 도메인 설정
        List<String> domainList = new ArrayList<>();
        domainList.add("http://localhost:3000");
        domainList.add("http://localhost:8080");
        if (backendDomain != null){
          String domain = String.format("%s:%s", backendDomain, port);
          domainList.add(domain);
        }
        if ((frontendDomain != null) && (frontendPort != null)) {
          String domain = String.format("%s:%s", frontendDomain, frontendPort);
          domainList.add(domain);
        }
        configuration.setAllowedOrigins(domainList);

        configuration.setAllowedMethods(Arrays.asList("*")); // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(Arrays.asList("*")); // 허용할 헤더
        configuration.setAllowCredentials(true); // 자격 증명 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // 모든 경로에 대해 CORS 설정 적용
        return source;
    }

    /**
     * 로그아웃 필터
     * @author 연상훈
     * @created 2024-11-08 오전 1:01
     * @info 스프링부트 세큐리티에서 로그아웃 필터는 기본적으로 거의 맨 앞에 배치함
     * > 지금 세션은 사용하지 않고 토큰으로만 인증하기 때문에 거의 맨 앞이면 토큰 인증을 못 받음
     * > 필터 순서를 토큰 필터 뒤로 옮기려고 생성한 필터
     * @info 로그아웃 후 별도의 리다이렉트가 없고, 인증 정보를 모두 삭제하도록 구성함
     */
    private LogoutFilter logoutFilter() {
      SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
      LogoutSuccessHandler logoutSuccessHandler = new LogoutSuccessHandler() {
        @Override
        public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
        ) throws IOException, ServletException {
          // 엑세스 토큰 삭제
          String username = authentication.getName();
          accessTokenService.deleteAccessToken(username);

          SecurityContextHolder.clearContext();
          log.info("----------------- 로그아웃 ----------------------");
          response.setStatus(HttpServletResponse.SC_OK);  // 200 OK 응답
          response.getWriter().write("logout success");  // JSON 응답
        }
      };
  
      LogoutFilter logoutFilter = new LogoutFilter(logoutSuccessHandler, logoutHandler);
      logoutFilter.setFilterProcessesUrl("/api/user/logout");
  
      return logoutFilter;
    }

}
