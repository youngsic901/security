<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String id = request.getParameter("id");
    String password = request.getParameter("password");

    //자격 증명 확인
    String validId = "ok";
    String validPassword = "123";

    // Authentication(인증)
    if(id != null && password != null && id.equals(validId) && password.equals(validPassword)){
        // 자격 증명이 유효하면 session 생성
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("userid", id);

        response.sendRedirect("success.jsp");
    } else {
        out.println("<html><body>");
        out.println("<h3>로그인 실패!</h3>");
        out.println("<a href='login.html'>다시 로그인</a>");
        out.println("</body></html>");
    }
%>
