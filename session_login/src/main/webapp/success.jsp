<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%
  HttpSession httpSession = request.getSession(false);

  if(httpSession != null && httpSession.getAttribute("userid") != null) {
    String userid = (String)httpSession.getAttribute("userid");

    // Authorization(인가)
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
  } else {

  }
%>