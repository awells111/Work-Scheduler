package main.data;

import main.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomerDAO extends DAO {

    /*
    Customer Table
    */
    private static final String TABLE_CUSTOMER = "customer";
    private static final String COLUMN_CUSTOMER_ID = "customerId";
    private static final String COLUMN_CUSTOMER_NAME = "customerName";
    private static final String COLUMN_CUSTOMER_ADDRESS_ID = "addressId"; //todo delete because the addressId is unnecessary

    /*Customer Table Statements*/
    private static final String QUERY_SELECT_CUSTOMERS = "SELECT * FROM " + TABLE_CUSTOMER;

    private static final String STATEMENT_INSERT_CUSTOMER = "INSERT INTO `" + TABLE_CUSTOMER + "`(`" +
            COLUMN_CUSTOMER_ID + "`, `" +
            COLUMN_CUSTOMER_NAME + "`, `" +
            COLUMN_CUSTOMER_ADDRESS_ID +
            "`) VALUES (?, ?, ?)";

    private static final String STATEMENT_UPDATE_CUSTOMER = "UPDATE " + TABLE_CUSTOMER + " SET " +
            COLUMN_CUSTOMER_NAME + " = ? WHERE " +
            COLUMN_CUSTOMER_ID + " = ?";

    private static final String STATEMENT_DELETE_CUSTOMER = "DELETE FROM " + TABLE_CUSTOMER +
            " WHERE " + COLUMN_CUSTOMER_ID + " = ?";

    /*
    Address Table
    */
    private static final String TABLE_ADDRESS = "address";
    private static final String COLUMN_ADDRESS_ID = "customerId"; //todo Change addressId to customerId when I create my own database
    private static final String COLUMN_ADDRESS_NAME = "address";
    private static final String COLUMN_ADDRESS_PHONE = "phone";

    /*Address Table Statements*/
    private static final String QUERY_SELECT_ADDRESSES = "SELECT * FROM " + TABLE_ADDRESS;

    private static final String STATEMENT_INSERT_ADDRESS = "INSERT INTO `" + TABLE_ADDRESS + "`(`" +
            COLUMN_ADDRESS_ID + "`, `" +
            COLUMN_ADDRESS_NAME + "`, `" +
            COLUMN_ADDRESS_PHONE + "`) VALUES (?, ?, ?)";

    private static final String STATEMENT_UPDATE_ADDRESS = "UPDATE " + TABLE_ADDRESS + " SET " +
            COLUMN_ADDRESS_NAME + " = ?, " +
            COLUMN_ADDRESS_PHONE + " = ? WHERE " +
            COLUMN_ADDRESS_ID + " = ?";

    /**
     * The tables required to modify a customer entity
     */
    private static final String[] CUSTOMER_TABLES = {TABLE_CUSTOMER, TABLE_ADDRESS};

    private DbConnection dbConnection;

    CustomerDAO(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    private DbConnection getDbConnection() {
        return dbConnection;
    }

    private int update(String[][] statements) {
        try {
            return super.update(getDbConnection().getConnection(), statements);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return CODE_ERROR;
        }
    }

    private ResultSet[] getResultSets(String[][] statements) {
        try {
            return super.getResultSets(getDbConnection().getConnection(), statements);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new ResultSet[0];
    }

    /**
     * Insert a {@link Customer} into the database
     *
     * @param newCustomer The {@link Customer} that will be inserted into the database
     * @return {@value CODE_SUCCESS} if successful, else {@value CODE_ERROR}
     */
    int insertEntity(Customer newCustomer) {
        String id = Integer.toString(newCustomer.getId()); //The same ID is used for both Address and Customer

        /*Build the statements required to insert a customer*/
        String[][] statements = emptyEntity(CUSTOMER_TABLES.length);

        /*Insert Customer Statement*/
        statements[0] = new String[]{
                STATEMENT_INSERT_CUSTOMER,
                id, //customerId
                newCustomer.getName(), //customerName
                id //addressId
        };

        /*Insert Address Statement*/
        statements[1] = new String[]{
                STATEMENT_INSERT_ADDRESS,
                id, //customerId
                newCustomer.getAddress(), //address
                newCustomer.getPhone() //phone
        };

        /*Execute the required statements*/
        return update(statements);
    }

    /**
     * Update an existing {@link Customer} in the database
     *
     * @param updatedCustomer The {@link Customer} that will be updated in the database
     * @return {@value CODE_SUCCESS} if successful, else {@value CODE_ERROR}
     */
    int updateEntity(Customer updatedCustomer) {
        String id = Integer.toString(updatedCustomer.getId()); //The same ID is used for both Address and Customer

        /*Build the statements required to delete a customer*/
        String[][] statements = emptyEntity(CUSTOMER_TABLES.length);

        /*Update Customer Statement*/
        statements[0] = new String[]{
                STATEMENT_UPDATE_CUSTOMER,
                updatedCustomer.getName(), //customerName
                Integer.toString(updatedCustomer.getId()) //customerId
        };

        /*Update Address Statement*/
        statements[1] = new String[]{
                STATEMENT_UPDATE_ADDRESS,
                updatedCustomer.getAddress(), //address
                updatedCustomer.getPhone(), //phone
                id //customerId
        };

        /*Execute the required statements*/
        return update(statements);
    }

    /**
     * Delete an existing {@link Customer} in the database
     *
     * @param selectedCustomer The {@link Customer} that will be deleted in the database
     * @return {@value CODE_SUCCESS} if successful, else {@value CODE_ERROR}
     */
    int deleteEntity(Customer selectedCustomer) {
        String id = Integer.toString(selectedCustomer.getId()); //The same ID is used for both Address and Customer

        /*Build the statements required to delete a customer*/
        String[][] statements = emptyEntity(1); //Only deleting the customer because the address will cascade delete.

        /*Delete Customer Statement*/
        statements[0] = new String[]{
                STATEMENT_DELETE_CUSTOMER,
                id //customerId
        };

        /*Execute the required statements*/
        return update(statements);
    }

    ArrayList getEntities() {
        ArrayList<Customer> customers = new ArrayList<>();

        String[][] queries = emptyEntity(CUSTOMER_TABLES.length);

        queries[0] = new String[]{
                QUERY_SELECT_CUSTOMERS
        };

        queries[1] = new String[]{
                QUERY_SELECT_ADDRESSES
        };

        ResultSet[] resultSets = getResultSets(queries);

        try {
            HashMap<Integer, Integer> hashMap = new HashMap<>(); //Will hold the index of an object in the arraylist
            int arrayListIndex = 0;

            ResultSet custRS = resultSets[0];

            while (custRS.next()) { //For each result
                int rsCustId = custRS.getInt(CustomerDAO.COLUMN_CUSTOMER_ID);
                String rsCustName = custRS.getString(CustomerDAO.COLUMN_CUSTOMER_NAME);

                hashMap.put(rsCustId, arrayListIndex++);

                customers.add(new Customer(rsCustId, rsCustName, "", ""));
            }


            ResultSet addrRS = resultSets[1];

            while (addrRS.next()) { //For each result
                int rsAddrId = addrRS.getInt(CustomerDAO.COLUMN_ADDRESS_ID);
                String rsAddrName = addrRS.getString(CustomerDAO.COLUMN_ADDRESS_NAME);
                String rsAddrPhone = addrRS.getString(CustomerDAO.COLUMN_ADDRESS_PHONE);

                Customer current = customers.get(hashMap.get(rsAddrId));
                current.setAddress(rsAddrName);
                current.setPhone(rsAddrPhone);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }
}
