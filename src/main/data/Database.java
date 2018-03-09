package main.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.model.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public class Database {

    public static final int CODE_NEW_CUSTOMER = -1;

    private DbConnection dbConnection;

    private ObservableList<Customer> customers;

    public Database() {
        dbConnection = new DbConnection();
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

    /*Since the database does not use autoincrement, we will take the last customer and increment the id*/
    public int nextCustomerId() {
        if (getCustomers().size() == 0) { //If no customers exist
            return 1;
        }

        return getCustomers().get(getCustomers().size() - 1).getId() + 1; //return 1 + the last customerId in the list
    }

    public void setCustomersFromDatabase() {
        /*Select all customers from the database*/
        try {
            ArrayList<Customer> customers = getDbConnection().getCustomers();
            setCustomers(FXCollections.observableList(customers));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*boolean represents an error -- (error = false) if a customer is added, else (error = true)*/
    public boolean addCustomer(Customer newCustomer) {
        boolean error = true;
        int numUpdated = getDbConnection().insertCustomer(newCustomer);

        if (numUpdated > 0) {
            getCustomers().add(newCustomer);

            error = false;
        }

        return error;
    }

    /*boolean represents an error -- (error = false) if a customer is updated, else (error = true)*/
    public boolean updateCustomer(Customer oldCustomer, Customer updatedCustomer) {
        boolean error = true;
        int numUpdated = getDbConnection().updateCustomer(updatedCustomer);

        if (numUpdated > 0) {
            int oldCustomerIndex = getCustomers().indexOf(oldCustomer);
            getCustomers().set(oldCustomerIndex, updatedCustomer);

            error = false;
        }

        return error;
    }

    /*boolean represents an error -- (error = false) if a customer is deleted, else (error = true)*/
    public boolean deleteCustomer(Customer selectedCustomer) {
        boolean error = true;
        int numUpdated = getDbConnection().deleteCustomer(selectedCustomer);

        if (numUpdated > 0) {
            getCustomers().remove(selectedCustomer);

            error = false;
        }

        return error;
    }

}
