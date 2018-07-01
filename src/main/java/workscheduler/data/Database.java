package workscheduler.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import workscheduler.model.Appointment;
import workscheduler.model.Customer;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

public class Database {

    public static final String ZONE_ID_DB = "UTC";
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm";

    private static final int UTC_OPEN_HOUR = 8; //The business opens at 8:00 UTC
    private static final int UTC_CLOSE_HOUR = 22; //The business closes at 22:00 UTC

    private ObservableList<Customer> customers;
    private ObservableList<Appointment> appointments;

    private int userId = Integer.MIN_VALUE;
    private UserDAO userDAO;
    private CustomerDAO customerDAO;
    private AppointmentDAO appointmentDAO;

    public Database() {
        userDAO = new UserDAO();
        customerDAO = new CustomerDAO();
        appointmentDAO = new AppointmentDAO();
    }

    public int getUserId() {
        return userId;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void login(String username, String password) throws SQLException, ClassNotFoundException {
        userId = getUserDAO().login(username, password);
    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    private void setCustomers(ObservableList<Customer> customers) {
        this.customers = customers;
    }

    public ObservableList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomersFromDatabase() throws SQLException, ClassNotFoundException {
        /*Select all customers from the database*/
        List<Customer> customers = getCustomerDAO().getAll(userId);
        setCustomers(FXCollections.observableList(customers));
    }

    public void addCustomer(Customer newCustomer) throws SQLException, ClassNotFoundException {
        newCustomer.setId(getCustomerDAO().insert(newCustomer));
        getCustomers().add(newCustomer);
    }

    public void updateCustomer(Customer oldCustomer, Customer updatedCustomer) throws SQLException, ClassNotFoundException {
        getCustomerDAO().update(updatedCustomer);

        int oldCustomerIndex = getCustomers().indexOf(oldCustomer);
        getCustomers().set(oldCustomerIndex, updatedCustomer);
    }

    public void deleteCustomer(Customer selectedCustomer) throws SQLException, ClassNotFoundException {
        getCustomerDAO().delete(selectedCustomer);
        getCustomers().remove(selectedCustomer);
    }

    public AppointmentDAO getAppointmentDAO() {
        return appointmentDAO;
    }

    private void setAppointments(ObservableList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public ObservableList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointmentsFromDatabase() throws SQLException, ClassNotFoundException {
        /*Select all appointments from the database*/
        List<Appointment> appointments = getAppointmentDAO().getAll(userId);
        setAppointments(FXCollections.observableList(appointments));
    }

    public void addAppointment(Appointment newAppointment) throws SQLException, ClassNotFoundException {
        newAppointment.setId(getAppointmentDAO().insert(newAppointment));
        getAppointments().add(newAppointment);
    }

    public void updateAppointment(Appointment oldAppointment, Appointment updatedAppointment) throws SQLException, ClassNotFoundException {
        getAppointmentDAO().update(updatedAppointment);

        int oldAppointmentIndex = getAppointments().indexOf(oldAppointment);
        getAppointments().set(oldAppointmentIndex, updatedAppointment);
    }

    public void deleteAppointment(Appointment selectedAppointment) throws SQLException, ClassNotFoundException {
        getAppointmentDAO().delete(selectedAppointment);
        getAppointments().remove(selectedAppointment);
    }

    public boolean appointmentOverlaps(Appointment appointment) throws SQLException, ClassNotFoundException {
        //If this appointment is not overlapping any other appointments, return false
        return getAppointmentDAO().selectOverlappedAppointments(appointment) != 0;
    }

    public boolean isOutsideBusinessHours(LocalDateTime ldt) {
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of(ZONE_ID_DB));

        int hour = zdt.getHour();
        int minutes = zdt.getMinute();

        return (hour < UTC_OPEN_HOUR || hour > UTC_CLOSE_HOUR) || (hour == UTC_CLOSE_HOUR && minutes > 0);
    }

    public LinkedList<Appointment> getCloseAppointments() throws SQLException, ClassNotFoundException {
        return getAppointmentDAO().getCloseAppointments(userId);
    }
}
