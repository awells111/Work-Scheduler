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

    public static final int CODE_NEW_ENTITY = -1;

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

    /*Since the database does not use autoincrement, we will take the last customer and increment the id*/
    public int nextCustomerId() {
        if (getCustomers().size() == 0) { //If no customers exist
            return 1;
        }

        return getCustomers().get(getCustomers().size() - 1).getId() + 1; //return 1 + the last customerId in the list
    }

    public void setCustomersFromDatabase() throws SQLException, ClassNotFoundException {
        /*Select all customers from the database*/
        List<Customer> customers = getCustomerDAO().getEntities();
        setCustomers(FXCollections.observableList(customers));
    }

    public void addCustomer(Customer newCustomer) throws SQLException, ClassNotFoundException {
        getCustomerDAO().insertEntity(newCustomer);
        getCustomers().add(newCustomer);
    }

    public void updateCustomer(Customer oldCustomer, Customer updatedCustomer) throws SQLException, ClassNotFoundException {
        getCustomerDAO().updateEntity(updatedCustomer);

        int oldCustomerIndex = getCustomers().indexOf(oldCustomer);
        getCustomers().set(oldCustomerIndex, updatedCustomer);
    }

    public void deleteCustomer(Customer selectedCustomer) throws SQLException, ClassNotFoundException {
        getCustomerDAO().deleteEntity(selectedCustomer);
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

    /*Since the database does not use autoincrement, we will take the last appointment and increment the id*/
    public int nextAppointmentId() { //todo Use primary key autoincrement and remove all nextId() methods
        if (getAppointments().size() == 0) { //If no appointments exist
            return 1;
        }

        return getAppointments().get(getAppointments().size() - 1).getId() + 1; //return 1 + the last appointmentId in the list
    }

    public void setAppointmentsFromDatabase() throws SQLException, ClassNotFoundException {
        /*Select all appointments from the database*/
        List<Appointment> appointments = getAppointmentDAO().getEntities();
        setAppointments(FXCollections.observableList(appointments));
    }

    /*boolean represents an error -- (error = false) if a appointment is added, else (error = true)*/
    public void addAppointment(Appointment newAppointment) throws SQLException, ClassNotFoundException {
        getAppointmentDAO().insertEntity(newAppointment);
        getAppointments().add(newAppointment);
    }

    public void updateAppointment(Appointment oldAppointment, Appointment updatedAppointment) throws SQLException, ClassNotFoundException {
        getAppointmentDAO().updateEntity(updatedAppointment);

        int oldAppointmentIndex = getAppointments().indexOf(oldAppointment);
        getAppointments().set(oldAppointmentIndex, updatedAppointment);
    }

    public void deleteAppointment(Appointment selectedAppointment) throws SQLException, ClassNotFoundException {
        getAppointmentDAO().deleteEntity(selectedAppointment);
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
