package pack.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pack.util.JwtUtil;

import java.io.IOException;

// JWT 인증 처리용 필터로, 요청이 들어올 때 마다 실행
// 요청의 헤더 또는 쿠키에서 JWT 토큰을 추출한 후 토큰이 유효하면 사용자 정보를 로드하여 인증 객체 생성

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // http 요청 당 한 번만 수행되는 필터를 구현할 때 사용 - 사용자 정의 필터를 작성할 때 효과적
    // 동일한 요청 내에서 중복 호출방지를 보장하여 불필요한 작업을 막음

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 필터의 핵심로직 작성 메소드

        String token = null;
        // Authorization 헤더에서 토큰 추출
        String authHeader = request.getHeader("Authorization");
        System.out.println("authHeader" + authHeader);

        // 참고 : authHeader 값이 null인지, "Bearer "라는 문자열로 시작하는지 확인
        // "Bearer "는 인증방식이 Bearer Token임을 나타내는 접두사이다.
        // 토큰을 얻는 방법 2가지를 사용하는 이유는 다양한 클라이언트 환경과 요구사항에 대응하기 위함
        // 브라우저 기반 어플리케이션을 사용 할 경우: 쿠키 사용
        // 모바일 앱 등을 사용 할 경우: Authorization 헤더 사용
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // 문자열의 7번째 인덱스부터 끝까지 잘라내기(접두사를 떼고 실제 토큰 얻기)
        } else {
            Cookie[] cookies = request.getCookies();
            if(cookies != null) {
                for(Cookie cookie : cookies) {
                    if("JWT".equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
            }
        }

        // 토큰 검증 및 인증 설정 ==========
        if(token != null && jwtUtil.validateToken(token)) {
            String sabun = jwtUtil.extractUsername(token); // 토큰에서 sabun 추출
            UserDetails userDetails = userDetailsService.loadUserByUsername(sabun);

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // userDetails : 사용자 인증 정보 객체
            // password는 null : 이미 인증이 되어 있으므로 비밀번호를 제공하지 않음
            // userDetails.getAuthorities() : 사용자 권한 목록 (ex: ROLE_USER 등)

            // 요청 처리 동안 인증된 사용자 정보를 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // filterChain은 여러 개의 필터가 순차적으로 실행되는 구조
        // 각 필터는 http 요청을 처리한 후 다음 필터로 요청을 전달하기 위해 사용함
        filterChain.doFilter(request, response); // JWT를 사용하기 때문에 세션상태를 유지할 필요 없음 (Stateless)
    }
}
