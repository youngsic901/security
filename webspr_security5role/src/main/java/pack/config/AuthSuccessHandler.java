package pack.config;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;

public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private RequestCache requestCache = new HttpSessionRequestCache();

    //2) 생성자에서 부모 객체에 전달
    public AuthSuccessHandler() {
        super.setRequestCache(requestCache);
    }

    // 로그인 성공 이후 자동으로 호출되는 메소드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 추가로직 구현,  로깅 처리 .., 세션 처리 .. , 예외 처리 .. 등

        // 세션 유지 시간 설정
        HttpSession session=request.getSession();
        session.setMaxInactiveInterval(60*20);   // 초 단위로 설정

        //3) 로그인 성공 이후 미리 저장된 요청이 있었는지 읽어와서
        SavedRequest cashed=requestCache.getRequest(request, response);

        //4) 만일 미리 저장된 요청이 없다면 (로그인하지 않은 상태로 인증이 필요한 경로를 요청하지 않았다면)
        if(cashed==null) {

            //5) 로그인 환영 페이지로 foward 이동
            RequestDispatcher rd=request.getRequestDispatcher("/templates/user/login_success");
            rd.forward(request, response);
        } else {
            // 6) 원래 가려던 목적지 경로로 리다이렉트 시킨다 (GET 방식 요청 파라미터도 자동으로  같이 가지고 간다)
            // SecurityConfig 클래스에서 http.formLogin().successHandler(new CustomAuthenticationHandler())로 설정한다.
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
