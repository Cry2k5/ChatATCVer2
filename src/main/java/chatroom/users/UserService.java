package chatroom.users;


import chatroom.database.DatabaseQuery;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    private DatabaseQuery database;

    public UserService(DatabaseQuery database) throws SQLException, IOException {
        this.database = database;
    }


    public User getUserById(int id) throws SQLException{
        String query ="select * from users where id=?";
            ResultSet result =this.database.executeQuery(query,id);
            return this.ResultSetToUserObject(result);
    }

    public User getUserByEmail(String email) throws SQLException{
        String query ="select * from users where email=?";
        ResultSet result =this.database.executeQuery(query,email);
        return this.ResultSetToUserObject(result);

    }



    public User ResultSetToUserObject(ResultSet set) throws SQLException{
        User user = new User();
        if(set.next()) {
            user.setId(set.getInt("id"));
            user.setname(set.getString("name"));
            user.setEmail(set.getString("email"));
            user.setPassword(set.getString("password"));
            user.setimage(set.getString("avatar"));
        }
        return user;
    }
}
