package chatroom.TextMessages;

import java.sql.Timestamp;

public class TextMessages {
    private int id;
    private int senderId;
    private Timestamp timestamp;
    private String message;

    public TextMessages(int id, int senderId, Timestamp timestamp, String message) {
        this.id = id;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.message = message;
    }

    public TextMessages() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
