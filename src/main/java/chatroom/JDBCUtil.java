package chatroom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
	public static Connection getConnection(){
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/chatapplication","root","");
			
			return connect;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;

	}
	public static void closeConnect(Connection c){
		try {
			c.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
