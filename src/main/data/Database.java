package main.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.model.Appointment;
import main.model.Customer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import static main.data.DAO.CODE_SUCCESS;

public class Database {

    public static final String ZONE_ID_DB = "UTC";
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm";
    static final String MYSQL_DATETIME_FORMAT = "'%Y-%m-%d %H:%i'"; //MYSQL Equivalent to "yyyy-MM-dd HH:mm"

    private static final int GMT_OPEN_HOUR = 8; //Opens at 8:00 GMT
    private static final int GMT_CLOSE_HOUR = 22; //Closes at 22:00 GMT

    public static final int CODE_NEW_ENTITY = -1;

    private DateTimeFormatter dateTimeFormatter;

    private DbConnection dbConnection;

    private ObservableList<Customer> customers;
    private ObservableList<Appointment> appointments;

    private UserDAO userDAO;
    private CustomerDAO customerDAO;
    private AppointmentDAO appointmentDAO;

    public Database() {
        dbConnection = new DbConnection();
        userDAO = new UserDAO(dbConnection);
        customerDAO = new CustomerDAO(dbConnection);
        appointmentDAO = new AppointmentDAO(this);

        dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_DATETIME).withLocale(Locale.getDefault());
    }

    public DbConnection getDbConnection() {
        return dbConnection;
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
    public boolean addCustomer(Customer newCustomer) {
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

    //todo Delete customer's appointments as well. Should be set as cascade in database.
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
    public int nextAppointmentId() {
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

    public String localDateTimeToString(LocalDateTime localDateTime) {
        return dateTimeFormatter.format(localDateTime);
    }

    public LocalDateTime localDateTimeFromString(String localDateTime) {
        return LocalDateTime.parse(localDateTime, dateTimeFormatter);
    }

    public String localDateTimeToDatabase(String localDateTimeString) {
        LocalDateTime localDateTime = localDateTimeFromString(localDateTimeString);
        ZonedDateTime ldtZoned = localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of(ZONE_ID_DB));
        return dateTimeFormatter.format(ldtZoned.toLocalDateTime());
    }

    public String databaseDateTimeToLocal(String databaseTimeString) {
        LocalDateTime localDateTime = localDateTimeFromString(databaseTimeString);
        ZonedDateTime localTime = localDateTime.atZone(ZoneId.of(ZONE_ID_DB)).withZoneSameInstant(ZoneId.systemDefault());
        return dateTimeFormatter.format(localTime);
    }

    public boolean isOutsideBusinessHours(int hour) {
        return hour < GMT_OPEN_HOUR || hour > GMT_CLOSE_HOUR;
    }

    public ArrayList<Appointment> getCloseAppointments() {
        return getAppointmentDAO().getCloseAppointments();
    }
}
