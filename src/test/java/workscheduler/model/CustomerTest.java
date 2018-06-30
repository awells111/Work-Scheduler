package workscheduler.model;

import javafx.beans.property.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;

    private static final int EXPECTED_CUSTOMER_ID = 3;
    private static final String EXPECTED_NAME = "Jake Peralta";
    private static final String EXPECTED_ADDRESS = "100 Charming Avenue";
    private static final String EXPECTED_PHONE = "6789998212";

    private static final IntegerProperty EXPECTED_CUSTOMER_ID_PROPERTY = new SimpleIntegerProperty(EXPECTED_CUSTOMER_ID);
    private static final StringProperty EXPECTED_NAME_PROPERTY = new SimpleStringProperty(EXPECTED_NAME);
    private static final StringProperty EXPECTED_ADDRESS_PROPERTY = new SimpleStringProperty(EXPECTED_ADDRESS);
    private static final StringProperty EXPECTED_PHONE_PROPERTY = new SimpleStringProperty(EXPECTED_PHONE);
    
    @BeforeEach
    void setUp() {
        customer = new Customer(3, "Jake Peralta", "100 Charming Avenue", "6789998212");
    }

    @BeforeEach
    void testDefaultCustomerId() {
        /*Test the default value of customers that are not assigned an id*/
        Customer customer = new Customer(EXPECTED_NAME, EXPECTED_ADDRESS, EXPECTED_PHONE);
        assertEquals(Integer.MIN_VALUE, customer.getId());
    }

    @Test
    void getId() {
        assertEquals(EXPECTED_CUSTOMER_ID, customer.getId());
    }

    @Test
    void idProperty() {
        assertEquals(EXPECTED_CUSTOMER_ID_PROPERTY.get(), customer.idProperty().get());
    }

    @Test
    void setId() {
        customer.setId(-1);
        assertEquals(customer.getId(), -1);
    }

    @Test
    void getName() {
        assertEquals(EXPECTED_NAME, customer.getName());
    }

    @Test
    void nameProperty() {
        assertEquals(EXPECTED_NAME_PROPERTY.get(), customer.nameProperty().get());
    }

    @Test
    void getAddress() {
        assertEquals(EXPECTED_ADDRESS, customer.getAddress());
    }

    @Test
    void addressProperty() {
        assertEquals(EXPECTED_ADDRESS_PROPERTY.get(), customer.addressProperty().get());
    }

    @Test
    void getPhone() {
        assertEquals(EXPECTED_PHONE, customer.getPhone());
    }

    @Test
    void phoneProperty() {
        assertEquals(EXPECTED_PHONE_PROPERTY.get(), customer.phoneProperty().get());
    }

    @Test
    void toStringTest() {
        String customerString = customer.toString();
        boolean contains = customerString.contains("id") &&
                customerString.contains(Integer.toString(customer.getId())) &&
                customerString.contains("name") &&
                customerString.contains(customer.getName()) &&
                customerString.contains("address") &&
                customerString.contains(customer.getAddress()) &&
                customerString.contains("phone") &&
                customerString.contains(customer.getPhone());

        assertTrue(contains);
    }

    @Test
    void equalsTest() {
        assertEquals(new Customer(EXPECTED_CUSTOMER_ID, EXPECTED_NAME, EXPECTED_ADDRESS, EXPECTED_PHONE), customer);
    }

    @Test
    void hashCodeTest() {
        assertEquals(new Customer(EXPECTED_CUSTOMER_ID, EXPECTED_NAME, EXPECTED_ADDRESS, EXPECTED_PHONE).hashCode(), customer.hashCode());
    }
}