package main.data;

import java.sql.*;

class DbConnection {

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    /*Change these for your database*/
    private static final String DB_NAME = "u04ts4";
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/" + DB_NAME + "?&useSSL=false&serverTimezone=" + Database.ZONE_ID_DB;
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    Connection getConnection() throws SQLException, ClassNotFoundException {
        Connection conn;

        Class.forName(DB_DRIVER);
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        System.out.println("Connected to database : " + DB_NAME);

        return conn;
    }
}