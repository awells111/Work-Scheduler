package main.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.model.Appointment;
import main.model.Customer;

import java.util.ArrayList;
import java.util.LinkedList;

import static main.data.DAO.CODE_SUCCESS;

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

    public int login(String username, String password) {
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

    public void setCustomersFromDatabase() {
        /*Select all customers from the database*/
        ArrayList<Customer> customers = getCustomerDAO().getEntities();
        setCustomers(FXCollections.observableList(customers));
    }

    /*boolean represents an error -- (error = false) if a customer is added, else (error = true)*/
    public boolean addCustomer(Customer newCustomer) { //todo Fix error checking in all methods
        boolean error = true;
        int successCode = getCustomerDAO().insertEntity(newCustomer);

        if (successCode == CODE_SUCCESS) {
            getCustomers().add(newCustomer);

            error = false;
        }

        return error;
    }

    /*boolean represents an error -- (error = false) if a customer is updated, else (error = true)*/
    public boolean updateCustomer(Customer oldCustomer, Customer updatedCustomer) {
        boolean error = true;
        int successCode = getCustomerDAO().updateEntity(updatedCustomer);

        if (successCode == CODE_SUCCESS) {
            int oldCustomerIndex = getCustomers().indexOf(oldCustomer);
            getCustomers().set(oldCustomerIndex, updatedCustomer);

            error = false;
        }

        return error;
    }

    /*boolean represents an error -- (error = false) if a customer is deleted, else (error = true)*/
    public boolean deleteCustomer(Customer selectedCustomer) {
        boolean error = true;
        int successCode = getCustomerDAO().deleteEntity(selectedCustomer);

        if (successCode == CODE_SUCCESS) {
            getCustomers().remove(selectedCustomer);

            error = false;
        }

        return error;
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

    public void setAppointmentsFromDatabase() {
        /*Select all appointments from the database*/
        ArrayList<Appointment> appointments = getAppointmentDAO().getEntities();
        setAppointments(FXCollections.observableList(appointments));
    }

    /*boolean represents an error -- (error = false) if a appointment is added, else (error = true)*/
    public boolean addAppointment(Appointment newAppointment) {
        boolean error = true;
        int successCode = getAppointmentDAO().insertEntity(newAppointment);

        if (successCode == CODE_SUCCESS) {
            getAppointments().add(newAppointment);

            error = false;
        }

        return error;
    }

    /*boolean represents an error -- (error = false) if a appointment is updated, else (error = true)*/
    public boolean updateAppointment(Appointment oldAppointment, Appointment updatedAppointment) {
        boolean error = true;
        int successCode = getAppointmentDAO().updateEntity(updatedAppointment);

        if (successCode == CODE_SUCCESS) {
            int oldAppointmentIndex = getAppointments().indexOf(oldAppointment);
            getAppointments().set(oldAppointmentIndex, updatedAppointment);

            error = false;
        }

        return error;
    }

    /*boolean represents an error -- (error = false) if a appointment is deleted, else (error = true)*/
    public boolean deleteAppointment(Appointment selectedAppointment) {
        boolean error = true;
        int successCode = getAppointmentDAO().deleteEntity(selectedAppointment);

        if (successCode == CODE_SUCCESS) {
            getAppointments().remove(selectedAppointment);

            error = false;
        }

        return error;
    }

    public boolean appointmentOverlaps(Appointment appointment) {
        //If this appointment is not overlapping any other appointments, return false
        return getAppointmentDAO().selectOverlappedAppointments(appointment) != 0;
    }

    public boolean isOutsideBusinessHours(int hour) {
        return hour < GMT_OPEN_HOUR || hour > GMT_CLOSE_HOUR;
    }

    public LinkedList<Appointment> getCloseAppointments() { //todo this does not follow the same error checking conventions as above
        return getAppointmentDAO().getCloseAppointments();
    }
}
