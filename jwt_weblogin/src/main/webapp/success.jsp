<%@ page import="io.jsonwebtoken.Jws" %>
<%@ page import="io.jsonwebtoken.Claims" %>
<%@ page import="io.jsonwebtoken.Jwts" %>
<%@ page import="io.jsonwebtoken.security.Keys" %>
<%@ page import="java.util.Base64" %>
<%@ page import="java.security.Key" %>
<%@ page import="io.jsonwebtoken.ExpiredJwtException" %>
<%@ page import="io.jsonwebtoken.JwtException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%
  // 쿠키에서 JWT를 읽어 유효성 검사
  Cookie[] cookies = request.getCookies();
  String jwt = null;

  if(cookies != null) {
    for(Cookie cookie: cookies) {
      if(cookie.getName().equals("jwt")) {
        jwt = cookie.getValue();
        break;
      }
    }
  }
  if(jwt != null) {
    try {
      String encodedKey = (String) application.getAttribute("secretKey");
      byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
      Key secretKey = Keys.hmacShaKeyFor(decodedKey);

      // JWT 유효성 검사
      Jws<Claims> claims = Jwts.parserBuilder()
              .setSigningKey(secretKey) // jwt 생성시 비밀키와 동일
              .build()
              .parseClaimsJws(jwt); // 파싱된 jwt 문자열이 유효하면 Jws<Claims> 객체반환. 유효하지 않으면 JwtException 발생

      String userid = claims.getBody().getSubject();
%>
<html>
<head>
  <title>Title</title>
</head>
<body>
<h2>로그인 성공 페이지</h2>
<p>환영합니다. <%=userid%></p>
인증에 성공했으므로 제공된 자료 사용 가능
<br>
쇼핑 게시판, 방명록 등....
<hr>
<a href="logout.jsp">로그아웃</a>
</body>
</html>
<%
    } catch (ExpiredJwtException e) {
      System.out.println("만료된 토큰");
    } catch (JwtException e) {
      // jwt가 유효하지 않으므로
      response.sendRedirect("login.html");
    }
  } else {
    // jwt가 null인 경우
      response.sendRedirect("login.html");
  }
%>