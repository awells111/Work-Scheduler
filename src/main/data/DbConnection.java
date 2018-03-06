package main.data;

import java.sql.*;

import static main.model.ConnectedUser.COLUMN_USER_ID;
import static main.model.ConnectedUser.COLUMN_USER_PASSWORD;
import static main.model.ConnectedUser.COLUMN_USER_USERNAME;

public class DbConnection {

    public static final int USER_NOT_FOUND = -1;

    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String DB = "U04TS4";
    public static final String URL = "jdbc:mysql://52.206.157.109/U04TS4";
    public static final String USER = "U04TS4";
    public static final String PASS = "53688339332";

//todo use connection pool
    //userId userName password
    public int userLogin (String user, String pass) throws ClassNotFoundException {

        Connection conn = null;

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connected to database : " + DB);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }

        ResultSet rs;
        try {

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE userName=? AND password=?");
            stmt.setString(1, user);
            stmt.setString(2, pass);

            rs = stmt.executeQuery();

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

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return USER_NOT_FOUND;
    }


    public void connect (String query) throws ClassNotFoundException {
        query = "SELECT * FROM user";

        Connection conn = null;

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connected to database : " + DB);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }

        Statement stmt;

        ResultSet rs;
        try {

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {

                System.out.println(rs.getString(COLUMN_USER_ID)); //print user id
                System.out.println(rs.getString(COLUMN_USER_USERNAME)); //print user username
                System.out.println(rs.getString(COLUMN_USER_PASSWORD)); //print user password

            }

            rs.close();
            stmt.close();
            conn.close();


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
