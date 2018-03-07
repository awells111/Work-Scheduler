package main.data;

import java.sql.*;

import static main.model.ConnectedUser.COLUMN_USER_ID;
import static main.model.ConnectedUser.COLUMN_USER_PASSWORD;
import static main.model.ConnectedUser.COLUMN_USER_USERNAME;

public class TestConnection {
//todo delete in final version
    //userId userName password
    public static void main(String[] argv) throws ClassNotFoundException {
        Connection conn = null;
        String driver = "com.mysql.cj.jdbc.Driver";
        String db = "U04TS4";
        String url = "jdbc:mysql://52.206.157.109/" + db;
        String user = "U04TS4";
        String pass = "53688339332";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connected to database : " + db);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }

        Statement stmt;

        ResultSet rs;
        try {

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM user");

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


    //prints all column values and names

    //            int columnsNumber = rsmd.getColumnCount();
    //            ResultSetMetaData rsmd = rs.getMetaData();
//                while (rs.next()) {
//        for (int i = 1; i <= columnsNumber; i++) {
//            if (i > 1) System.out.print(",  ");
//            String columnValue = rs.getString(i);
//            System.out.println("value: " + columnValue);
//            System.out.print(columnValue + " " + rsmd.getColumnName(i));
//        }
//    }

//              Test Usernames  Test Passwords
//              "test"          "test"
//              "taskstream"    "100please"
}

