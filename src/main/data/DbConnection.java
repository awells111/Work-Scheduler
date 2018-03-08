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
    public static final int QUERY_ERROR = -1;
    public static final int QUERY_SUCCESS = 1;

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_NAME = "U04TS4";
    private static final String DB_URL = "jdbc:mysql://52.206.157.109/U04TS4";
    private static final String DB_USER = "U04TS4";
    private static final String DB_PASS = "53688339332";

    private ConnectedUser connectedUser; //Will only be set AFTER userLogin TODO do we even need this

    private Connection getConnection() throws SQLException, ClassNotFoundException {
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

        return QUERY_ERROR;
    }

    /*User is set after userLogin*/
    public void setConnectedUser(ConnectedUser connectedUser) {
        this.connectedUser = connectedUser;
    }

    public ArrayList<Customer> getCustomers() throws SQLException, ClassNotFoundException {
        ArrayList<Customer> customers = new ArrayList<>();

        Connection conn = getConnection();

        /*Select all customers*/
        PreparedStatement custStmt = conn.prepareStatement(SELECT_CUSTOMERS);

        ResultSet custRS = custStmt.executeQuery();

        HashMap<Integer, Integer> hashMap = new HashMap<>(); //Will hold the index of an object in the arraylist
        int arrayListIndex = 0;

        while (custRS.next()) { //For each result
            int rsCustId = custRS.getInt(COLUMN_CUSTOMER_ID);
            String rsCustName = custRS.getString(COLUMN_CUSTOMER_NAME);

            hashMap.put(rsCustId, arrayListIndex++);

            customers.add(new Customer(rsCustId, rsCustName, "", ""));
        }

        /*Select all addresses (For the previous customers)*/
        PreparedStatement addrStmt = conn.prepareStatement(SELECT_ADDRESSES);

        ResultSet addrRS = addrStmt.executeQuery();

        while (addrRS.next()) { //For each result
            int rsAddrId = addrRS.getInt(COLUMN_ADDRESS_ID);
            String rsAddrName = addrRS.getString(COLUMN_ADDRESS_NAME);
            String rsAddrPhone = addrRS.getString(COLUMN_ADDRESS_PHONE);

            Customer current = customers.get(hashMap.get(rsAddrId));
            current.setAddress(rsAddrName);
            current.setPhone(rsAddrPhone);
        }

        return customers;
    }



    //todo on failure reset
    public int insertCustomer(Customer customer) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); //By setting AutoCommit to false, the we can cancel the first statement if the second one fails
            try {
                /*Insert the customer first*/
                PreparedStatement custStmt = conn.prepareStatement(INSERT_CUSTOMER);
                custStmt.setInt(1, customer.getId());
                custStmt.setString(2, customer.getName());
                custStmt.setInt(3, customer.getId());

                custStmt.executeUpdate();


                /*Insert the address*/
                PreparedStatement addrStmt = conn.prepareStatement(INSERT_ADDRESS);
                addrStmt.setInt(1, customer.getId());
                addrStmt.setString(2, customer.getAddress());
                addrStmt.setString(3, customer.getPhone());

                addrStmt.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Hiya buddy");
                throw e;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return QUERY_ERROR;
        }

        return QUERY_SUCCESS;
    }

    //    public int insertCustomer(Customer customer) throws SQLException, ClassNotFoundException {
    //
    //        Connection conn = getConnection();
    //
    //        /*Insert the customer first*/
    //        PreparedStatement custStmt = conn.prepareStatement(INSERT_CUSTOMER);
    //        custStmt.setInt(1, customer.getId());
    //        custStmt.setString(2, customer.getName());
    //        custStmt.setInt(3, customer.getId());
    //
    //        custStmt.executeUpdate();
    //
    //
    //        /*Insert the address*/
    //        PreparedStatement addrStmt = conn.prepareStatement(INSERT_ADDRESS);
    ////        addrStmt.setInt(1, customer.getId());
    //        addrStmt.setInt(1, 1);
    //        addrStmt.setString(2, customer.getAddress());
    //        addrStmt.setString(3, customer.getPhone());
    //
    //        return addrStmt.executeUpdate();
    //    }
//              Test Usernames  Test Passwords
//              "test"          "test"
//              "t"             "t"
}
