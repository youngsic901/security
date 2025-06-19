package pack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pack.filter.JwtAuthenticationFilter;
import pack.util.JwtUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // AuthenticationManager : 인증(Authentication)과 관련된 핵심 컴포넌트

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConf) throws Exception {
//        return authConf.getAuthenticationManager();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        httpSecurity
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth ->
            auth
            .requestMatchers("/","/auth/login", "/auth/logout", "/static/**").permitAll()
            .requestMatchers("/auth/success", "/auth/gugu", "auth/guguresult").authenticated()
        )
        // JWT 사용시 주의사항
        // 1) 세션관리 비활성화 : session 대신 STATELESS 보안을 사용
        // 2) JWT 필터 추가 : 요청마다 JWT 토큰을 확인하고 인증정보를 설정하기 위한 커스텀 필터 작성
        // 3) form 기반 인증 제거 : JWT 인증에서는 보통 로그인 페이지를 사용하지 않고 REST api로 로그인 처리를 하므로 form 로그인 설정을 하지 않음
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
//    .formLogin(formlogin ->
//        formlogin
//        .loginPage("/auth/login")
//        .loginProcessingUrl("/auth/login")
//        .usernameParameter("sabun")
//        .passwordParameter("irum")
//        .defaultSuccessUrl("/auth/success", true)
//        .permitAll()
//    )
//    .logout(logout ->
//        logout
//        .logoutUrl("/auth/logout")
//        .logoutSuccessUrl("/auth/login")
//        .invalidateHttpSession(true)  // 세션 무효화
//        .clearAuthentication(true)    // 인증 정보 제거
//        .deleteCookies("JSESSIONID") // 세션 쿠키 삭제
//        .permitAll()
//    );
//    return httpSecurity.build();
//}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
