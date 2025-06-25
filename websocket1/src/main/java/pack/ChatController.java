package pack;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @MessageMapping("/message")
    @SendTo("/topic/messages") // /app/message로 전송한 메세지 처리. 처리된 메세지는 "/topic/messages"로 Broadcast 한다.
    public String sendMessage(String message) {
        return message; // 사용자로부터 받은 메세지를 그대로 반환
        // 메세지는 자동으로 /topic/messages 경로를 구독하고 있는 모든 사용자에게 전달
    }
}
