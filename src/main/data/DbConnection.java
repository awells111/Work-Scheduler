package main.data;

import main.model.ConnectedUser;
import main.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import static main.data.SQLStatements.*;
import static main.data.SQLStatements.COLUMN_USER_ID;
import static main.data.SQLStatements.COLUMN_USER_PASSWORD;
import static main.data.SQLStatements.COLUMN_USER_USERNAME;

public class DbConnection {

    //todo use connection pool
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_NAME = "U04TS4";
    private static final String DB_URL = "jdbc:mysql://52.206.157.109/U04TS4";
    private static final String DB_USER = "U04TS4";
    private static final String DB_PASS = "53688339332";

    private ConnectedUser connectedUser; //Will only be set AFTER userLogin TODO do we even need this

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Connection conn;

        Class.forName(DB_DRIVER);
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        System.out.println("Connected to database : " + DB_NAME);

        return conn;
    }

    public int userLogin(String user, String pass) throws SQLException, ClassNotFoundException {

        Connection conn = getConnection();

        /*Select the user with the given name and password*/
        PreparedStatement stmt = conn.prepareStatement(SELECT_USER);
        stmt.setString(1, user);
        stmt.setString(2, pass);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) { //For each result
            int rsId = rs.getInt(COLUMN_USER_ID);
            String rsUser = rs.getString(COLUMN_USER_USERNAME);
            String rsPass = rs.getString(COLUMN_USER_PASSWORD);

            System.out.println(rsId); //print user id
            System.out.println(rsUser); //print user username
            System.out.println(rsPass); //print user password

            if (rsUser.equals(user) && rsPass.equals(pass)) {
                return rsId;
            }
        }

        return DAO.CODE_ERROR;
    }

    /*User is set after userLogin*/
    public void setConnectedUser(ConnectedUser connectedUser) {
        this.connectedUser = connectedUser;
    }

    //              Test Usernames  Test Passwords
    //              "test"          "test"
    //              "t"             "t"
}
