package chatroom.TextMessages;

import chatroom.database.DatabaseQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TextMessagesService {

    private DatabaseQuery databaseQuery;

    public TextMessagesService(DatabaseQuery databaseQuery) {
        this.databaseQuery = databaseQuery;
    }

    public TextMessages getMessageById(int id) throws SQLException {
        String query = "SELECT * from TextMessages where id=?";
        ResultSet resultSet = databaseQuery.executeQuery(query, id);
        TextMessages messages = resultSetToTextMessagesObject(resultSet);
        return messages;
    }

    public void addMessage(TextMessages message) throws SQLException {
        String query = "insert into TextMessages(sender_id, sendTime, message) values (?, ?, ?)";
        this.databaseQuery.executeUpdate(query, message.getSenderId(), message.getTimestamp(), message.getMessage());
    }

    public List<TextMessages> getAllMessages() throws SQLException {
        List<TextMessages> messagesList = new ArrayList<>();
        String query = "SELECT * FROM TextMessages";
        ResultSet resultSet = databaseQuery.executeQuery(query);

        while (resultSet.next()) {
            TextMessages message = resultSetToTextMessagesObject(resultSet);
            messagesList.add(message);
        }

        return messagesList;
    }

    private TextMessages resultSetToTextMessagesObject(ResultSet result) throws SQLException {
        int id = result.getInt("id");
        int senderId = result.getInt("sender_id");
        Timestamp timestamp = result.getTimestamp("sendTime");
        String messageText = result.getString("message");

        return new TextMessages(id, senderId, timestamp, messageText);
    }
}
