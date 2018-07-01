package workscheduler.model;

import javafx.beans.property.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {

    private Appointment appointment;

    private static final int EXPECTED_APPOINTMENT_ID = 5;
    private static final int EXPECTED_CUSTOMER_ID = 3;
    private static final String EXPECTED_TYPE = "Phone";
    private static final LocalDateTime EXPECTED_START = LocalDateTime.of(2010, 2, 27, 5, 10, 8, 90);
    private static final LocalDateTime EXPECTED_END = EXPECTED_START.plusMonths(1).plusDays(3).plusHours(2).plusMinutes(5).plusSeconds(3).plusNanos(5);

    private static final IntegerProperty EXPECTED_APPOINTMENT_ID_PROPERTY = new SimpleIntegerProperty(EXPECTED_APPOINTMENT_ID);
    private static final IntegerProperty EXPECTED_CUSTOMER_ID_PROPERTY = new SimpleIntegerProperty(EXPECTED_CUSTOMER_ID);
    private static final StringProperty EXPECTED_TYPE_PROPERTY = new SimpleStringProperty(EXPECTED_TYPE);
    private static final ObjectProperty<LocalDateTime> EXPECTED_START_PROPERTY = new SimpleObjectProperty<>(EXPECTED_START);
    private static final ObjectProperty<LocalDateTime> EXPECTED_END_PROPERTY = new SimpleObjectProperty<>(EXPECTED_END);

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.of(2010, 2, 27, 5, 10, 8, 90);
        LocalDateTime end = start.plusMonths(1).plusDays(3).plusHours(2).plusMinutes(5).plusSeconds(3).plusNanos(5);

        appointment = new Appointment(5, 3, "Phone", start, end);
    }

    @Test
    void testDefaultAppointmentId() {
        /*Test the default value of appointments that are not assigned an id*/
        Appointment appointment = new Appointment(EXPECTED_CUSTOMER_ID, EXPECTED_TYPE, EXPECTED_START, EXPECTED_END);
        assertEquals(Integer.MIN_VALUE, appointment.getId());
    }

    @Test
    void getId() {
        assertEquals(EXPECTED_APPOINTMENT_ID, appointment.getId());
    }

    @Test
    void idProperty() {
        assertEquals(EXPECTED_APPOINTMENT_ID_PROPERTY.get(), appointment.idProperty().get());
    }

    @Test
    void setId() {
        appointment.setId(-1);
        assertEquals(appointment.getId(), -1);
    }

    @Test
    void getCustomerId() {
        assertEquals(EXPECTED_CUSTOMER_ID, appointment.getCustomerId());
    }

    @Test
    void setCustomerId() {
        appointment.setCustomerId(-1);
        assertEquals(appointment.getCustomerId(), -1);
    }

    @Test
    void customerIdProperty() {
        assertEquals(EXPECTED_CUSTOMER_ID_PROPERTY.get(), appointment.customerIdProperty().get());
    }

    @Test
    void getType() {
        assertEquals(EXPECTED_TYPE, appointment.getType());
    }

    @Test
    void typeProperty() {
        assertEquals(EXPECTED_TYPE_PROPERTY.get(), appointment.typeProperty().get());
    }

    @Test
    void getStart() {
        assertEquals(EXPECTED_START, appointment.getStart());
    }

    @Test
    void startProperty() {
        assertEquals(EXPECTED_START_PROPERTY.get(), appointment.startProperty().get());
    }

    @Test
    void getEnd() {
        assertEquals(EXPECTED_END, appointment.getEnd());
    }

    @Test
    void endProperty() {
        assertEquals(EXPECTED_END_PROPERTY.get(), appointment.endProperty().get());
    }

    @Test
    void toStringTest() {
        String appointmentString = appointment.toString();
        boolean contains = appointmentString.contains("Appointment Id") &&
                appointmentString.contains(Integer.toString(appointment.getId())) &&
                appointmentString.contains("Customer Id") &&
                appointmentString.contains(Integer.toString(appointment.getCustomerId())) &&
                appointmentString.contains("Type") &&
                appointmentString.contains(appointment.getType()) &&
                appointmentString.contains("Start") &&
                appointmentString.contains(appointment.getStart().toString()) &&
                appointmentString.contains("End") &&
                appointmentString.contains(appointment.getEnd().toString());

        assertTrue(contains);
    }

    @Test
    void equalsTest() {
        assertEquals(new Appointment(EXPECTED_APPOINTMENT_ID, EXPECTED_CUSTOMER_ID, EXPECTED_TYPE, EXPECTED_START, EXPECTED_END), appointment);
    }

    @Test
    void hashCodeTest() {
        assertEquals(
                new Appointment(EXPECTED_APPOINTMENT_ID, EXPECTED_CUSTOMER_ID, EXPECTED_TYPE, EXPECTED_START, EXPECTED_END).hashCode(),
                appointment.hashCode());
    }
}