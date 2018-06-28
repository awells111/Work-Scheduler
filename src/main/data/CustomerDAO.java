package main.data;

import main.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends DAO<Customer> {

    /*
    View combining the relevant fields from the Customer and Address tables
    */
    private static final String VIEW_CUSTOMERS = "vw_customer";

    /*
    Customer Table
    */
    private static final String COLUMN_CUSTOMER_ID = "customerId";
    private static final String COLUMN_CUSTOMER_NAME = "customerName";

    /*
    Address Table
    */
    private static final String COLUMN_ADDRESS_NAME = "address";
    private static final String COLUMN_ADDRESS_PHONE = "phone";

    CustomerDAO(DbConnection dbConnection) {
        setDbConnection(dbConnection);
    }

    private static final String STATEMENT_INSERT_CUSTOMER = "CALL sp_customer_Insert_pk(?, ?, ?)";

    /**
     * Insert a {@link Customer} into the database
     *
     * @param newCustomer The {@link Customer} that will be inserted into the database
     */
    @Override
    public int insert(Customer newCustomer) throws SQLException, ClassNotFoundException {
        String[] statement = new String[]{
                STATEMENT_INSERT_CUSTOMER,
                newCustomer.getName(), //customerName
                newCustomer.getAddress(), //address
                newCustomer.getPhone() //phone
        };

        ResultSet custRS = executeQuery(statement);

        custRS.next();

        /*Return the id of the new Customer*/
        return custRS.getInt(1);
    }

    private static final String STATEMENT_UPDATE_CUSTOMER = "CALL sp_customer_Update(?, ?, ?, ?)";

    /**
     * Update an existing {@link Customer} in the database
     *
     * @param updatedCustomer The {@link Customer} that will be updated in the database
     */
    @Override
    public void update(Customer updatedCustomer) throws SQLException, ClassNotFoundException {
        String[] statement = new String[]{
                STATEMENT_UPDATE_CUSTOMER,
                Integer.toString(updatedCustomer.getId()), //customerId
                updatedCustomer.getName(), //customerName
                updatedCustomer.getAddress(), //address
                updatedCustomer.getPhone() //phone
        };

        executeUpdate(statement);
    }


    private static final String STATEMENT_DELETE_CUSTOMER = "CALL sp_customer_DeleteById(?)";

    /**
     * Delete an existing {@link Customer} in the database
     *
     * @param selectedCustomer The {@link Customer} that will be deleted in the database
     */
    @Override
    public void delete(Customer selectedCustomer) throws SQLException, ClassNotFoundException {
        String[] statement = new String[]{
                STATEMENT_DELETE_CUSTOMER,
                Integer.toString(selectedCustomer.getId()) //customerId
        }; //Only deleting the customer because the address will cascade delete.

        executeUpdate(statement);
    }

    private static final String QUERY_SELECT_CUSTOMERS = "SELECT * FROM " + VIEW_CUSTOMERS;
    @Override
    public List<Customer> getAll() throws SQLException, ClassNotFoundException {
        List<Customer> customers = new ArrayList<>();

        ResultSet custRS = executeQuery(new String[]{
                QUERY_SELECT_CUSTOMERS
        });

        while (custRS.next()) { //For each result
            customers.add(buildObject(custRS));
        }

        return customers;
    }

    @Override
    public Customer buildObject(ResultSet custRS) throws SQLException {
        int rsCustId = custRS.getInt(CustomerDAO.COLUMN_CUSTOMER_ID);
        String rsCustName = custRS.getString(CustomerDAO.COLUMN_CUSTOMER_NAME);
        String rsAddrName = custRS.getString(CustomerDAO.COLUMN_ADDRESS_NAME);
        String rsAddrPhone = custRS.getString(CustomerDAO.COLUMN_ADDRESS_PHONE);

        return new Customer(rsCustId, rsCustName, rsAddrName, rsAddrPhone);
    }
}
