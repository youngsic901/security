package pack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @GetMapping("/user/required_loginform")
    public String requiredForm() {
        return "user/required_loginform";
    }

    @GetMapping("/user/loginform")
    public String loginForm() {
        return "user/loginform";
    }

    @PostMapping("/user/login_success")
    public String loginSuccess() {
        return "user/login_success";
    }

    @PostMapping("/user/login_fail")
    public String loginFail() {
        return "user/login_fail";
    }

    @GetMapping("/user/denied")
    public String denied() {
        return "user/denied";
    }

    @GetMapping("/staff/user/list")
    public String userList() {
        return "user/list";
    }

    @GetMapping("/admin/user/manage")
    public String userManage() {
        return "user/manage";
    }

    @GetMapping("/user/expired")
    public String userExpired() {
        return "user/expired";
    }
}
