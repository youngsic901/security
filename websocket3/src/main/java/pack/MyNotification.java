package pack;

/**
 * @author : youngsic
 * @packageName : pack
 * @fileName : MyNotification
 * @date : 25. 6. 25.
 **/

public class MyNotification {
    private String type; // 알림 유형 : 친구 요청, 댓글, 좋아요 등
    private String message;

    public MyNotification() {

    }

    public MyNotification(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
