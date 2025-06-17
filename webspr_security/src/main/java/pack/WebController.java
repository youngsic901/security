package pack;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/kbs")
    public String kbs() {
        return "media";
    }

    @GetMapping({"/mbc", "/mbc/good", "/mbc/11/ok"})
    public String mbc() {
        return "media";
    }
}
