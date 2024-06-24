package chatroom.TextMessages;


import java.sql.Timestamp;


public class TextMessages {

    private int id;
    private int senderId;
    private Timestamp sendTime;
    private String message;

    public TextMessages() {
    }


    public TextMessages(int id, int senderId, Timestamp sendTime, String message) {
        this.id = id;
        this.senderId = senderId;
        this.sendTime = sendTime;
        this.message = message;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return this.senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }



    public Timestamp getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", senderId='" + getSenderId() + "'" +
            ", sendTime='" + getSendTime() + "'" +
            ", message='" + getMessage() + "'" +
            "}";
    }

}