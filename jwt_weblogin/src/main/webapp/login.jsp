<%@ page import="java.security.Key" %>
<%@ page import="io.jsonwebtoken.Jwts" %>
<%@ page import="io.jsonwebtoken.security.Keys" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Base64" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // JWT 자격 증명 확인 후 유효하면 JWT 생성 (JWT는 비밀키와 함께 서명되며, 만료시간도 설정)
    // 생성된 JWT를 쿠키에 저장하도록 클라이언트에 전송
    // 로그인에 성공하면 성공 페이지로 이동

    String id = request.getParameter("id");
    String password = request.getParameter("password");

    //자격 증명 확인
    String validId = "ok";
    String validPassword = "123";

    // Authentication(인증)
    if(id != null && password != null && id.equals(validId) && password.equals(validPassword)){
        // 자격 증명이 유효하면 JWT 생성
        // 고정된 비밀 키 사용 (예제용)  최소 256비트 길이의 비밀 키
        // String secretKeyString = "mySuperSecretKey12345678901234567890123456789012";
        // Key secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());

        // 서블릿 컨텍스트에서 Base64로 인코딩된 비밀 키 가져오기 java.util.Base64
        String encodedKey = (String) application.getAttribute("secretKey");
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        Key secretKey = Keys.hmacShaKeyFor(decodedKey);

         long expirationTime = 3600000; // 1시간

        // JWT 생성하기
        String jwt = Jwts.builder()
                        .setSubject(id) // sub 클레임 설정. 클레임은 사용자 id 또는 고유한 사용자 식별자
                        .setIssuedAt(new Date()) // 발행시간
                        .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 만료 시간 지정
                        .signWith(secretKey) // 서명 알고리즘과 비밀키를 부여해 토큰의 무결성 보장
                        .compact(); // 설정 내용을 기반으로 JWT 문자열 생성

        // 생성된 JWT 구조
        // 1) header : jwt의 메타데이터, 서명 알고리즘을 포함. 예시) { "alg": "HS256", "typ": "JWT" }
        // 2) payload : 토큰에 발행된 클레임 데이터 정보 예)  { "sub": 사용자id, "iat": 발행시간, "exo": 만료시간 ... }
        // 3) signature : header + payload를 암호화키(secretKey)를 사용해 생성한 서명

        // 쿠키에 JWT 저장
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true); // client는 접근 불가 (XSS 공격 방지 목적)
        jwtCookie.setPath("/"); // 모든 경로에서 쿠키 접근 가능
        // jwtCookie.setDomain("kbs.com"); // 특정 도메인만 허용
        response.addCookie(jwtCookie);

        response.sendRedirect("success.jsp");
    } else {
        // 자격증명이 유효하지 않은 경우
        out.println("<html><body>");
        out.println("<h3>로그인 실패!</h3>");
        out.println("<a href='login.html'>다시 로그인</a>");
        out.println("</body></html>");
    }
%>
