package pack.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "mySuperSecretKey12345678901234567890123456789012";
    private final long EXPIRATION_TIME = 1000 * 60 * 16;
    
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }
    
    // JWT 생성
    public String generateToken(String sabun) {
        return Jwts.builder()
                .setSubject(sabun)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }
    
    // JWT에서 Claim 추출
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }
    
    // JWT 유효성 검사 : token이 유효하면 true, 아니면 false 반환
    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);
            // 토큰의 만료시간이 현재시간 이전이면 <- 만료되지 않은 경우에 true를 반환
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            // 예외상황 : 토큰 서명이 잘못된 경우, JWT 구조가 잘못된 경우, 디코딩이 안되는 경우, 기타 예상하지 못한 상황
            return false;
        }
    }
}
