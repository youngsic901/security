package pack.controller;

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

@Controller
@RequestMapping("/auth")
public class AuthController {
    // 인증 처리를 위한 AuthenticationManager 인스턴스를 필드로 선언
    private final AuthenticationManager authenticationManager;

    // 생성자로 AuthenticationManager를 주입 받음
    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(name="sabun")String sabun, @RequestParam(name="irum")String irum, Model model) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(sabun, irum);

            Authentication authentication = authenticationManager.authenticate(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return "redirect:/auth/success";
        } catch (AuthenticationException e) {
            model.addAttribute("error", "로그인 실패");
            return "login";
        }
    }

    @GetMapping("/success")
    public String success(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
        return "success";
    }

    @GetMapping("/gugu")
    public String gugu() {
        // 현재 인증된 사용자의 인증 정보를 읽음
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        return "gugu";
    }

    @PostMapping("/gugu")
    public String gugu(@RequestParam(name = "num")int num, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        model.addAttribute("num", num);
        return "guguresult";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/auth/login";
    }
}

// 처리 흐름
// 1) 사용자 요청 : /login ==> sabun, irum을 서버로 전달
// 2) 스프링 시큐리티는 UsernamePasswordAutenticationToken 생성
//              => Authentication.authentication() 를 호출
//              => 내부적으로 UserDetailService의 loadUserByUsername() 호출
//              => 사용자 정보 검증 및 인증을 수행
// 결과 처리
// 성공 : 인증 객체 저장 -> 성공 페이지로 이동
// 실패 : 예외 처리 -> 로그인 페이지로 이동
