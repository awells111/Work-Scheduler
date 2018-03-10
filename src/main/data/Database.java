package main.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.model.Customer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import static main.data.DAO.CODE_SUCCESS;

public class Database {

    private static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    public static final int CODE_NEW_CUSTOMER = -1;

    private DateTimeFormatter dateTimeFormatter;

    private DbConnection dbConnection;

    private ObservableList<Customer> customers;

    private UserDAO userDAO;
    private CustomerDAO customerDAO;

    public Database() {
        dbConnection = new DbConnection();
        userDAO = new UserDAO(dbConnection);
        customerDAO = new CustomerDAO(dbConnection);

        dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_DATETIME).withLocale(Locale.getDefault());
    }

    private void setCustomers(ObservableList<Customer> customers) {
        this.customers = customers;
    }

    public ObservableList<Customer> getCustomers() {
        return customers;
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

    public String dateToString(LocalDate localDate) {
        return dateTimeFormatter.format(localDate);
    }

    public LocalDate dateFromString(String dateTime) {
        return LocalDate.parse(dateTime, dateTimeFormatter);
    }
}
