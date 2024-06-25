package chatroom.TextMessages;

import java.sql.Timestamp;

public class TextMessages {
    private String senderName;
    private String receiverName;
    private Timestamp timestamp;
    private String message;

    public TextMessages(String senderName, String receiverName, Timestamp timestamp, String message) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.timestamp = timestamp;
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
