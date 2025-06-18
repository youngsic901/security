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
}
