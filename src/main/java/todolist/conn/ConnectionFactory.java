package todolist.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection() throws SQLException{
        String url = "jdbc:mysql://db:3306/todo_list"
                + "?useSSL=false"
                + "&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "root";

        return DriverManager.getConnection(url, username, password);
    }
}
