package helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbConnection {
    public   static Connection getConnect() {
          Connection connection = null;

        try {
             connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stock", "root", "123456789");
            /*Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from entry");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("REFERENCE")+" "+ resultSet.getString("DESIGNATION")+" "+resultSet.getString("PRIX")+"DH");
            }*/
        }  catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return  connection;
    }
}
