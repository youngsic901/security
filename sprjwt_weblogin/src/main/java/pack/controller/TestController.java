package pack.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pack.service.JwtService;

@Controller
public class TestController {
    @Autowired
    private JwtService jwtService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("userid")String userid,
                        @RequestParam("password")String password, HttpServletResponse response) {
        String validId = "ok";
        String validPwd = "123";

        if(userid.equals(validId) && password.equals(validPwd)) {
            String token = jwtService.createToken(userid);
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/success";
        } else {
            return "redirect:/login?error";
        }
    }

    @GetMapping("/success")
    public String success(HttpServletRequest request, Model model) {
        String userid = getUserIdFromToken(request);
        if(userid == null) {
            return "redirect:/login";
        }

        model.addAttribute("myuser", userid);
        return "success";
    }

    // 요청에서 JWT를 추출하고 사용자id를 반환
    private String getUserIdFromToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("jwt")) {
                    String token = cookie.getValue();
                    return jwtService.getUserIdFromToken(token);
                }
            }
        }

        return null;
    }

    @GetMapping("/gugu")
    public String gugu(HttpServletRequest request) {
        String userid = getUserIdFromToken(request);

        if(userid == null) {
            return "redirect:/login";
        }
        return "gugu";
    }

    @PostMapping("/gugu")
    public String guguResult(@RequestParam(name="num")int num,
                       HttpServletRequest request,
                       Model model) {
        String userid = getUserIdFromToken(request);

        if(userid == null) {
            model.addAttribute("num", num);
            return "redirect:/login";
        }
        model.addAttribute("num", num);
        return "guguresult";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/login";
    }
}
