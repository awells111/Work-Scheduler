package main.data;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestConnection {
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
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }


        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }

// Test Usernames
// "test" "test"
// "taskstream" "100please"
}

