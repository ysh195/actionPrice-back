package com.example.actionprice.config;

import com.example.actionprice.security.CustomUserDetailService;
import com.example.actionprice.security.filter.LoginFilter;
import com.example.actionprice.security.filter.RefreshTokenFilter;
import com.example.actionprice.security.filter.TokenCheckFilter;
import com.example.actionprice.user.UserRepository;
import com.example.actionprice.util.JWTUtil;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * @author 연상훈
 * @created 24/10/01 13:46
 * @updated 24/10/14 05:26
 * - [24/10/14 05:26] : LoginSuccessHandler가 rememberMe 토큰 생성을 막고 있어서 제거. 그것을 대체하는 LoginFilter - successfulAuthentication 생성함
 * @info 토큰관련 로직이 추가로 더 필요하긴 함. 필요한 것만 보안을 열어뒀으니 상황에 따라 추가해야 함
 */
@SuppressWarnings("ALL")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Log4j2
public class CustomSecurityConfig {

    private final DataSource dataSource;
    private final JWTUtil jwtUtil;
    private final CustomUserDetailService userDetailsService;
    private final UserRepository userRepository;

    // method - @Bean
    /**
     * @author : 연상훈
     * @created : 2024-10-05 오후 9:27
     * @updated : 2024-10-10 오전 9:26
     * - [2024-10-14 오전 5:38] : 리멤버미 사용을 위해 일부 수정.
     * @see :
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        log.info("------------ security configuration --------------");

        // AuthenticationManager 설정. 얘가 일종의 매표소 직원. 출입하는 사람들의 권한 부여 및 확인 역할
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(
            AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        // Login Filter
        LoginFilter loginFilter = new LoginFilter("/api/user/login", userDetailsService, rememberMeServices(), jwtUtil, authenticationManager);

        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource())) // corsConfigurationSource
            .csrf((csrfconfig) -> csrfconfig.disable())
            .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/user/login").anonymous() // 로그인을 안 한 사람만 로그인 창으로 이동 가능
                        .requestMatchers("/api/user/logout").authenticated() // 로그인을 한 사람만 로그아웃 창으로 이동 가능
                        .requestMatchers(
                                "/",
                                "/api/user/register",
                                "/api/user/sendVerificationCode",
                                "/api/user/checkVerificationCode",
                                "/api/user/generate/refreshToken",
                                "/api/user/checkForDuplicateUsername"
                        ).permitAll()
                        .anyRequest().authenticated())
            .authenticationManager(authenticationManager)
            .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class) // 필터 순서에 주의
            .addFilterBefore(tokenCheckFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new RefreshTokenFilter("/api/user/generate/refreshToken", jwtUtil), TokenCheckFilter.class)
            .rememberMe(httpSecurityRememberMeConfigurer -> httpSecurityRememberMeConfigurer.rememberMeServices(rememberMeServices()))
            .formLogin((formLogin) -> formLogin.loginPage("/api/user/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .failureUrl("/api/user/login") // TODO failureForwardUrl는 기존 url을 유지하면서 이동. 거기에 failureHandler를 쓰면 로그인 실패 횟수를 체크할 수 있을 것 같은데?
                    .loginProcessingUrl("/api/user/login")
                    .defaultSuccessUrl("/", true))
            .logout((logout) -> logout.logoutUrl("/api/user/logout").logoutSuccessUrl("/api/user/login"));
        return http.build();

    }

    /**
     * 리멤버미 사용을 위해 필요한 Service.
     * @author : 연상훈
     * @created : 2024-10-14 오전 5:39
     * @updated : 2024-10-14 오전 5:39
     * @info : 학원에서 가르쳐준 것과 달리, 진짜 대충 만드는 거 아니면 이게 필요하다고 함
     */
    @Bean
    public RememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices("rememberMeKey" , userDetailsService, persistentTokenRepository());
        rememberMeServices.setParameter("rememberMe"); // 리멤버미 파라미터 이름 설정
        rememberMeServices.setCookieName("REMEMBERME"); // 리멤버미 쿠키 이름 설정
        rememberMeServices.setTokenValiditySeconds(3600); // 쿠키 유효 시간 설정 (초)
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    /**
     * @author 연상훈
     * @created 24/10/05 20:20
     * @updated 24/10/05 20:20
     * @see : 간편한 jwt 사용을 위한 레포지토리
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() { // persistentTokenRepository
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    /**
     * @author 연상훈
     * @created 24/10/04
     * @updated 24/10/04
     * @see : 리액트-스프링부트 연결을 위한 cors config
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() { // corsConfigurationSource
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // TokenCheckFilter 설정을 위한 메서드
    @Bean
    public TokenCheckFilter tokenCheckFilter(){
        return new TokenCheckFilter(userDetailsService, jwtUtil);
    }

}
