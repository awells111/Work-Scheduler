package main.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.model.Appointment;
import main.model.Customer;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class Database {

    public static final String ZONE_ID_DB = "UTC";
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm";

    private static final int GMT_OPEN_HOUR = 8; //The business opens at 8:00 GMT
    private static final int GMT_CLOSE_HOUR = 22; //The business closes at 22:00 GMT

    private ObservableList<Customer> customers;
    private ObservableList<Appointment> appointments;

    private UserDAO userDAO;
    private CustomerDAO customerDAO;
    private AppointmentDAO appointmentDAO;

    public Database() {
        DbConnection dbConnection = new DbConnection();
        userDAO = new UserDAO(dbConnection);
        customerDAO = new CustomerDAO(dbConnection);
        appointmentDAO = new AppointmentDAO(dbConnection);
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    /*Returns true if logged in, else false*/
    public boolean login(String username, String password) throws SQLException, ClassNotFoundException {
        return getUserDAO().login(username, password);
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
        List<Customer> customers = getCustomerDAO().getAll();
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
        List<Appointment> appointments = getAppointmentDAO().getAll();
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

    public boolean isOutsideBusinessHours(int hour) {
        return hour < GMT_OPEN_HOUR || hour > GMT_CLOSE_HOUR;
    }

    public LinkedList<Appointment> getCloseAppointments() throws SQLException, ClassNotFoundException {
        return getAppointmentDAO().getCloseAppointments();
    }
}
