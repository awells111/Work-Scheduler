package main.data;

import java.sql.*;

import static main.data.ConnectionInfo.*;

public class DbConnection {

    //todo use connection pool
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

//    private static final String DB_NAME = "";
//    private static final String DB_URL = "";
//    private static final String DB_USER = "";
//    private static final String DB_PASS = "";

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Connection conn;

        Class.forName(DB_DRIVER);
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        System.out.println("Connected to database : " + DB_NAME);

        return conn;
    }
}