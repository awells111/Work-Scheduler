package main.data;

import main.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAO extends DAO {

    /*
    Customer Table
    */
    private static final String TABLE_CUSTOMER = "customer";
    private static final String COLUMN_CUSTOMER_ID = "customerId";
    private static final String COLUMN_CUSTOMER_NAME = "customerName";

    /*
    Address Table
    */
    private static final String TABLE_ADDRESS = "address";
    private static final String COLUMN_ADDRESS_ID = "customerId";
    private static final String COLUMN_ADDRESS_NAME = "address";
    private static final String COLUMN_ADDRESS_PHONE = "phone";

    /**
     * The tables required to modify a customer entity
     */
    private static final String[] CUSTOMER_TABLES = {TABLE_CUSTOMER, TABLE_ADDRESS};

    CustomerDAO(DbConnection dbConnection) {
        setDbConnection(dbConnection);
    }

    private static final String STATEMENT_INSERT_CUSTOMER = "INSERT INTO `" + TABLE_CUSTOMER + "`(`" +
            COLUMN_CUSTOMER_ID + "`, `" +
            COLUMN_CUSTOMER_NAME +
            "`) VALUES (?, ?)";
    private static final String STATEMENT_INSERT_ADDRESS = "INSERT INTO `" + TABLE_ADDRESS + "`(`" +
            COLUMN_ADDRESS_ID + "`, `" +
            COLUMN_ADDRESS_NAME + "`, `" +
            COLUMN_ADDRESS_PHONE + "`) VALUES (?, ?, ?)";

    /*todo A join should be used here*/
    /**
     * Insert a {@link Customer} into the database
     *
     * @param newCustomer The {@link Customer} that will be inserted into the database
     */
    void insertEntity(Customer newCustomer) throws SQLException, ClassNotFoundException {
        String id = Integer.toString(newCustomer.getId()); //The same ID is used for both Address and Customer

        /*Build the statements required to insert a customer*/
        String[][] statements = emptyEntity(CUSTOMER_TABLES.length);

        /*Insert Customer Statement*/
        statements[0] = new String[]{
                STATEMENT_INSERT_CUSTOMER,
                id, //customerId
                newCustomer.getName(), //customerName
        };

        /*Insert Address Statement*/
        statements[1] = new String[]{
                STATEMENT_INSERT_ADDRESS,
                id, //customerId
                newCustomer.getAddress(), //address
                newCustomer.getPhone() //phone
        };

        /*Execute the required statements*/
        update(statements);
    }

    private static final String STATEMENT_UPDATE_CUSTOMER = "UPDATE " + TABLE_CUSTOMER + " SET " +
            COLUMN_CUSTOMER_NAME + " = ? WHERE " +
            COLUMN_CUSTOMER_ID + " = ?";
    private static final String STATEMENT_UPDATE_ADDRESS = "UPDATE " + TABLE_ADDRESS + " SET " +
            COLUMN_ADDRESS_NAME + " = ?, " +
            COLUMN_ADDRESS_PHONE + " = ? WHERE " +
            COLUMN_ADDRESS_ID + " = ?";

    /**
     * Update an existing {@link Customer} in the database
     *
     * @param updatedCustomer The {@link Customer} that will be updated in the database
     */
    void updateEntity(Customer updatedCustomer) throws SQLException, ClassNotFoundException {
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
        update(statements);
    }


    private static final String STATEMENT_DELETE_CUSTOMER = "DELETE FROM " + TABLE_CUSTOMER +
            " WHERE " + COLUMN_CUSTOMER_ID + " = ?";

    /**
     * Delete an existing {@link Customer} in the database
     *
     * @param selectedCustomer The {@link Customer} that will be deleted in the database
     */
    void deleteEntity(Customer selectedCustomer) throws SQLException, ClassNotFoundException {
        String id = Integer.toString(selectedCustomer.getId()); //The same ID is used for both Address and Customer

        /*Build the statements required to delete a customer*/
        String[][] statements = emptyEntity(1); //Only deleting the customer because the address will cascade delete.

        /*Delete Customer Statement*/
        statements[0] = new String[]{
                STATEMENT_DELETE_CUSTOMER,
                id //customerId
        };

        /*Execute the required statements*/
        update(statements);
    }

    private static final String QUERY_SELECT_CUSTOMERS = "SELECT " +
            TABLE_CUSTOMER + "." + COLUMN_CUSTOMER_ID + ", " +
            COLUMN_CUSTOMER_NAME + ", " +
            COLUMN_ADDRESS_NAME + ", " +
            COLUMN_ADDRESS_PHONE + " FROM " +
            TABLE_CUSTOMER + ", " +
            TABLE_ADDRESS + " WHERE " +
            TABLE_CUSTOMER + "." + COLUMN_CUSTOMER_ID + " = " +
            TABLE_ADDRESS + "." + COLUMN_ADDRESS_ID;

    ArrayList<Customer> getEntities() throws SQLException, ClassNotFoundException {
        ArrayList<Customer> customers = new ArrayList<>();

        String[][] queries = emptyEntity(1); //We only need one query

        queries[0] = new String[]{
                QUERY_SELECT_CUSTOMERS
        };

        ResultSet[] resultSets = getResultSets(queries);

        ResultSet custRS = resultSets[0];

        while (custRS.next()) { //For each result
            int rsCustId = custRS.getInt(CustomerDAO.COLUMN_CUSTOMER_ID);
            String rsCustName = custRS.getString(CustomerDAO.COLUMN_CUSTOMER_NAME);
            String rsAddrName = custRS.getString(CustomerDAO.COLUMN_ADDRESS_NAME);
            String rsAddrPhone = custRS.getString(CustomerDAO.COLUMN_ADDRESS_PHONE);

            customers.add(new Customer(rsCustId, rsCustName, rsAddrName, rsAddrPhone));
        }

        return customers;
    }
}
