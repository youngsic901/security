package pack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShopController {
    @GetMapping("/shop")
    public String shop() {
        return "/shop";
    }

    @GetMapping("/shop/buy")
    public String buy(
            @RequestParam(name = "goodscode")int goodscode,
            @RequestParam(name = "amount")int amount,
            Model model
    ) {
        String msg = goodscode + "번 상품 " + amount + "개 주문";
        model.addAttribute("msg", msg);
        return "buy";
    }
}
