package repository.dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseConnection {
    private static Connection instance;
    public static Connection getInstance() throws SQLException {
        if(instance == null){
            String constr = "jdbc:postgresql://kdb.sh:6082/ckawka";
            String user = "ckawka";
            String name = "ckawka@kdb.sh";
            String password = "ckawkafpj24";
            Properties props = new Properties();
            props.setProperty("name",name);
            props.setProperty("user", user);
            props.setProperty("password", password);
            instance = DriverManager.getConnection(constr, props);
        }
        return instance;
    }
}