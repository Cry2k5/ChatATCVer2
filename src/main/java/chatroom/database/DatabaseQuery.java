package chatroom.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseQuery {

    private DatabaseHandler connection;

    public DatabaseQuery(DatabaseHandler connection){

        this.connection = connection;
    }


    public ResultSet executeQuery(String query, Object... params) throws SQLException{
        PreparedStatement statement = this.connection.prepareStatement(query);
        setParametres(statement, params);
        return statement.executeQuery();
    };

    public void executeUpdate(String query, Object... params) throws SQLException{
        PreparedStatement statement = this.connection.prepareStatement(query);
        setParametres(statement, params);
        statement.executeUpdate();
            
        };
    //careful ignore id because auto increment
    private void setParametres(PreparedStatement statement , Object... params) throws SQLException{
        for (int i=0;i<params.length;i++){
            statement.setObject(i+1, params[i]);
        }
    }
}
