<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%
  Cookie jwtCookie = new Cookie("jwt", "");
  jwtCookie.setPath("/");
  jwtCookie.setMaxAge(0);
  response.addCookie(jwtCookie);

  response.sendRedirect("login.html");
%>