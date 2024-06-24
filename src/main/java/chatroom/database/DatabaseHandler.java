package chatroom.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseHandler {
    private Connection connection;

    public DatabaseHandler() throws SQLException, IOException {
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost/chatapplication", "root", "");
    }

    public PreparedStatement prepareStatement(String query) throws SQLException{
        return this.connection.prepareStatement(query);
    }
    public static void closeConnect(Connection c){
        try {
            c.close();
            System.out.println("Đóng kết nối csdl thành công!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
