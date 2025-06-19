package pack.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pack.util.JwtUtil;

@Controller
public class AuthController {
    // 인증 처리를 위한 AuthenticationManager 인스턴스를 필드로 선언
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // 생성자로 AuthenticationManager를 주입 받음
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/auth/login")
    public String login() {
        return "login";
    }

    @PostMapping("/auth/login")
    public String login(@RequestParam(name="sabun")String sabun, 
                        @RequestParam(name="irum")String irum, 
                        Model model,
                        HttpServletResponse response
    ) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(sabun, irum);
            Authentication authentication = authenticationManager.authenticate(token);
            
            // JWT 생성
            String jwt = jwtUtil.generateToken(sabun);
            
            // 쿠키에 JWT를 추가
            Cookie jwtCookie = new Cookie("JWT", jwt);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false); // https 로만 사용할 경우에는 true
            jwtCookie.setPath("/"); // 모든 경로 허용
            jwtCookie.setMaxAge(60 * 60);
            response.addCookie(jwtCookie);
            
            model.addAttribute("username", authentication.getName());
            return "success";
        } catch (AuthenticationException e) {
            model.addAttribute("error", "로그인 실패");
            return "login";
        }
    }

    @GetMapping("/auth/success")
    public String success(Model model, Authentication authentication) {
        if(authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
            return "success";
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/auth/gugu")
    public String gugu(Model model, Authentication authentication) {
        if(authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        model.addAttribute("username", authentication.getName());
        return "gugu";
    }

    @PostMapping("/auth/gugu")
    public String gugu(@RequestParam(name = "num")int num, 
                       Model model,
                       Authentication authentication
    ) {
        if(authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        model.addAttribute("username", authentication.getName());
        model.addAttribute("num", num);
        return "guguresult";
    }

    @GetMapping("/auth/logout")
    public String logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("JWT", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);
        
        // SecurityContext 초기화
        SecurityContextHolder.clearContext();
        
        return "redirect:/auth/login";
    }
}