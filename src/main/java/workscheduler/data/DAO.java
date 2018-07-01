package workscheduler.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

abstract class DAO implements ConnectionBuilder{

}

interface ConnectionBuilder {

    String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    /*Change these for your database*/
    String DB_NAME = "u04ts4";
    String DB_URL = "jdbc:mysql://127.0.0.1/u04ts4?&useSSL=false&serverTimezone=UTC";
    String DB_USER = "root";
    String DB_PASS = "";

    default Connection createConnection() throws ClassNotFoundException, SQLException {
        Connection conn;

        Class.forName(DB_DRIVER);
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        System.out.println("Connected to database : " + DB_NAME);

        return conn;
    }
}
