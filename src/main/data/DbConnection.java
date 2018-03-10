package main.data;

import java.sql.*;

public class DbConnection {

    //todo use connection pool
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_NAME = "U04TS4";
    private static final String DB_URL = "jdbc:mysql://52.206.157.109/U04TS4";
    private static final String DB_USER = "U04TS4";
    private static final String DB_PASS = "53688339332";

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Connection conn;

        Class.forName(DB_DRIVER);
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        System.out.println("Connected to database : " + DB_NAME);

        return conn;
    }

    //              Test Usernames  Test Passwords
    //              "test"          "test"
    //              "t"             "t"
}
