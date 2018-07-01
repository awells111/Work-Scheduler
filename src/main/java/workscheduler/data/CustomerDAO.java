package workscheduler.data;

import workscheduler.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends DAO implements QueryBuilder<Customer> {

    /*
    Customer Table
    */
    private static final String COLUMN_USER_ID = "userId";
    private static final String COLUMN_CUSTOMER_ID = "customerId";
    private static final String COLUMN_CUSTOMER_NAME = "customerName";

    /*
    Address Table
    */
    private static final String COLUMN_ADDRESS_NAME = "address";
    private static final String COLUMN_ADDRESS_PHONE = "phone";

    private static final String STATEMENT_INSERT_CUSTOMER = "CALL sp_customer_Insert_pk(?, ?, ?, ?)";

    /**
     * Insert a {@link Customer} into the database
     *
     * @param newCustomer The {@link Customer} that will be inserted into the database
     */
    @Override
    public int insert(Customer newCustomer) throws SQLException, ClassNotFoundException {
        try (Connection connection = createConnection()) {
            PreparedStatement stmt = connection.prepareStatement(STATEMENT_INSERT_CUSTOMER);

            int stmtIndex = 0;
            stmt.setInt(++stmtIndex, newCustomer.getUserId());
            stmt.setString(++stmtIndex, newCustomer.getName());
            stmt.setString(++stmtIndex, newCustomer.getAddress());
            stmt.setString(++stmtIndex, newCustomer.getPhone());

            ResultSet custRS = stmt.executeQuery();

            custRS.next();

            /*Return the id of the new Customer*/
            return custRS.getInt(1);
        }
    }

    private static final String STATEMENT_UPDATE_CUSTOMER = "CALL sp_customer_Update(?, ?, ?, ?)";

    /**
     * Update an existing {@link Customer} in the database
     *
     * @param updatedCustomer The {@link Customer} that will be updated in the database
     */
    @Override
    public void update(Customer updatedCustomer) throws SQLException, ClassNotFoundException {
        try (Connection connection = createConnection()) {
            PreparedStatement stmt = connection.prepareStatement(STATEMENT_UPDATE_CUSTOMER);

            int stmtIndex = 0;
            stmt.setInt(++stmtIndex, updatedCustomer.getId());
            stmt.setString(++stmtIndex, updatedCustomer.getName());
            stmt.setString(++stmtIndex, updatedCustomer.getAddress());
            stmt.setString(++stmtIndex, updatedCustomer.getPhone());

            stmt.executeUpdate();
        }
    }


    private static final String STATEMENT_DELETE_CUSTOMER = "CALL sp_customer_DeleteById(?)";

    /**
     * Delete an existing {@link Customer} in the database
     *
     * @param selectedCustomer The {@link Customer} that will be deleted in the database
     */
    @Override
    public void delete(Customer selectedCustomer) throws SQLException, ClassNotFoundException {
        try (Connection connection = createConnection()) {
            PreparedStatement stmt = connection.prepareStatement(STATEMENT_DELETE_CUSTOMER);

            int stmtIndex = 0;
            stmt.setInt(++stmtIndex, selectedCustomer.getId());

            stmt.executeUpdate();
        }
    }

    private static final String QUERY_SELECT_CUSTOMER_BY_ID = "CALL sp_customer_SelectByCustId(?)";

    @Override
    public Customer selectById(int customerId) throws SQLException, ClassNotFoundException {
        try (Connection connection = createConnection()) {
            PreparedStatement stmt = connection.prepareStatement(QUERY_SELECT_CUSTOMER_BY_ID);

            stmt.setInt(1, customerId);

            ResultSet custRS = stmt.executeQuery();

            custRS.next();

            return buildObject(custRS);
        }
    }

    private static final String QUERY_SELECT_CUSTOMERS = "CALL sp_customer_SelectByUserId(?)";

    @Override
    public List<Customer> getAll(int userId) throws SQLException, ClassNotFoundException {
        try (Connection connection = createConnection()) {
            PreparedStatement stmt = connection.prepareStatement(QUERY_SELECT_CUSTOMERS);

            stmt.setInt(1, userId);

            ResultSet custRS = stmt.executeQuery();

            List<Customer> customers = new ArrayList<>();

            while (custRS.next()) { //For each result
                customers.add(buildObject(custRS));
            }

            return customers;
        }
    }

    @Override
    public Customer buildObject(ResultSet custRS) throws SQLException {
        int rsUserId = custRS.getInt(CustomerDAO.COLUMN_USER_ID);
        int rsCustId = custRS.getInt(CustomerDAO.COLUMN_CUSTOMER_ID);
        String rsCustName = custRS.getString(CustomerDAO.COLUMN_CUSTOMER_NAME);
        String rsAddrName = custRS.getString(CustomerDAO.COLUMN_ADDRESS_NAME);
        String rsAddrPhone = custRS.getString(CustomerDAO.COLUMN_ADDRESS_PHONE);

        return new Customer(rsUserId, rsCustId, rsCustName, rsAddrName, rsAddrPhone);
    }
}
