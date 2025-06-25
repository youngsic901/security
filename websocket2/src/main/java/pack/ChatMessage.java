package pack;

import lombok.Data;

@Data
public class ChatMessage {
    private String sender;
    private String content;
    private MessageType type;

    public enum MessageType { // 메세지 유형 정의
        CHAT,
        JOIN,
        LEAVE
    }
}
