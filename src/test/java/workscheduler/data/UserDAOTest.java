package workscheduler.data;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import workscheduler.model.Appointment;
import workscheduler.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDAOTest {

    private int testUserId = Integer.MIN_VALUE;
    private static final String TEST_USERNAME = "Tester";
    private static final String TEST_PASSWORD = "Password";

    private Customer customer;
    private static final String EXPECTED_NAME = "Larry";
    private static final String EXPECTED_ADDRESS = "123 New York Ave";
    private static final String EXPECTED_PHONE = "3198311238";

    private Appointment appointment;
    private static final String EXPECTED_TYPE = "Videoconference";
    private static final LocalDateTime EXPECTED_START = LocalDateTime.of(2003, 8, 1, 11, 16, 0, 0);
    private static final LocalDateTime EXPECTED_END = EXPECTED_START.plusMonths(8).plusDays(6).plusHours(9).plusMinutes(0).plusSeconds(0).plusNanos(0);

    @BeforeEach
    void setUp() {
        LocalDateTime tempDate = LocalDateTime.of(2000, 1, 1, 1, 1, 1, 1);

        customer = new Customer(Integer.MIN_VALUE, Integer.MIN_VALUE, "test", "test", "test");
        appointment = new Appointment(Integer.MIN_VALUE, Integer.MIN_VALUE, "test", tempDate, tempDate.plusMinutes(1));
    }

    @Test
    void DAOTests() {
        /*Delete the user if it exists*/
        try {
            deleteUser();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        /*Begin User insert*/
        try {
            testUserId = insertUser();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        assertTrue(testUserId > 0);
        /*-- End User insert --*/


        /*Begin User login*/
        try {
            testUserId = Integer.MIN_VALUE;
            testUserId = login();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        assertTrue(testUserId > 0);
        /*-- End User login --*/


        /*Begin Customer insert*/
        try {
            customer.setUserId(testUserId);
            customer.setId(insertCustomer(customer));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        assertTrue(customer.getId() > 0);
        /*-- End Customer insert --*/


        /*Begin Customer update*/
        try {
            updateCustomer(new Customer(testUserId, customer.getId(), EXPECTED_NAME, EXPECTED_ADDRESS, EXPECTED_PHONE));
            customer = selectCustomerById(customer.getId());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        assertTrue(customer.getName().equals(EXPECTED_NAME)
                && customer.getAddress().equals(EXPECTED_ADDRESS)
                && customer.getPhone().equals(EXPECTED_PHONE));
        /*-- End Customer update --*/


        /*Begin Appointment insert*/
        try {
            appointment.setCustomerId(customer.getId());
            appointment.setId(insertAppointment(appointment));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        assertTrue(appointment.getId() > 0);
        /*-- End Appointment insert --*/


        /*Begin Appointment update*/
        try {
            updateAppointment(new Appointment(appointment.getId(), customer.getId(), EXPECTED_TYPE, EXPECTED_START, EXPECTED_END));
            appointment = selectAppointmentById(appointment.getId());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        assertTrue(appointment.getType().equals(EXPECTED_TYPE) &&
                appointment.getStart().equals(EXPECTED_START) &&
                appointment.getEnd().equals(EXPECTED_END));
        /*-- End Appointment update --*/


        /*Begin Appointment delete*/
        {
            List<Appointment> appointmentList = new ArrayList<>();
            try {
                deleteAppointment(appointment);
                appointmentList = getAppointments(testUserId);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            assertTrue(appointmentList.isEmpty());
        }
        /*-- End Appointment delete --*/


        /*Begin Customer delete*/
        {
            List<Customer> customerList = new ArrayList<>();
            try {
                deleteCustomer(customer);
                customerList = getCustomers(testUserId);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            assertTrue(customerList.isEmpty());
        }
        /*-- End Customer delete --*/


        /*Begin User delete*/
        try {
            deleteUser();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        /*-- End User delete --*/
    }

    int insertUser() throws SQLException, ClassNotFoundException {
        DAO userDAO = new UserDAO();

        Connection userConnection = userDAO.createConnection();

        PreparedStatement userStatement = userConnection.prepareStatement("CALL sp_user_Insert_pk(?, ?)");

        int stmtIndex = 0;
        userStatement.setString(++stmtIndex, TEST_USERNAME);
        userStatement.setString(++stmtIndex, TEST_PASSWORD);

        ResultSet userRS = userStatement.executeQuery();

        userRS.next();

        return userRS.getInt(1);
    }

    void deleteUser() throws SQLException, ClassNotFoundException {
        DAO userDAO = new UserDAO();

        Connection userConnection = userDAO.createConnection();

        PreparedStatement userStatement = userConnection.prepareStatement("CALL sp_user_DeleteByUserName(?)");

        int stmtIndex = 0;
        userStatement.setString(++stmtIndex, TEST_USERNAME);

        userStatement.executeUpdate();
    }

    int login() throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();

        return userDAO.login(TEST_USERNAME, TEST_PASSWORD);
    }

    int insertCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        CustomerDAO customerDAO = new CustomerDAO();

        return customerDAO.insert(customer);
    }

    void updateCustomer(Customer updatedCustomer) throws SQLException, ClassNotFoundException {
        CustomerDAO customerDAO = new CustomerDAO();

        customerDAO.update(updatedCustomer);
    }

    Customer selectCustomerById(int customerId) throws SQLException, ClassNotFoundException {
        CustomerDAO customerDAO = new CustomerDAO();
        return customerDAO.selectById(customerId);
    }

    void deleteCustomer(Customer selectedCustomer) throws SQLException, ClassNotFoundException {
        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.delete(selectedCustomer);
    }

    List<Customer> getCustomers(int userId) throws SQLException, ClassNotFoundException {
        CustomerDAO customerDAO = new CustomerDAO();
        return customerDAO.getAll(userId);
    }

    int insertAppointment(Appointment appointment) throws SQLException, ClassNotFoundException {
        AppointmentDAO appointmentDAO = new AppointmentDAO();

        return appointmentDAO.insert(appointment);
    }

    void updateAppointment(Appointment updatedAppointment) throws SQLException, ClassNotFoundException {
        AppointmentDAO appointmentDAO = new AppointmentDAO();

        appointmentDAO.update(updatedAppointment);
    }

    Appointment selectAppointmentById(int appointmentId) throws SQLException, ClassNotFoundException {
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        return appointmentDAO.selectById(appointmentId);
    }

    void deleteAppointment(Appointment selectedAppointment) throws SQLException, ClassNotFoundException {
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        appointmentDAO.delete(selectedAppointment);
    }

    List<Appointment> getAppointments(int userId) throws SQLException, ClassNotFoundException {
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        return appointmentDAO.getAll(userId);
    }
}