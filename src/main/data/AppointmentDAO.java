package main.data;

import main.model.Appointment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AppointmentDAO extends DAO<Appointment> {

    /*
    Appointment Table
    */
    private static final String TABLE_APPOINTMENT = "appointment";
    private static final String COLUMN_APPOINTMENT_ID = "appointmentId";
    private static final String COLUMN_CUSTOMER_ID = "customerId";
    private static final String COLUMN_APPOINTMENT_TYPE = "title";
    private static final String COLUMN_APPOINTMENT_START = "start";
    private static final String COLUMN_APPOINTMENT_END = "end";

    /**
     * The tables required to modify an appointment entity
     */
    private static final String[] APPOINTMENT_TABLES = {TABLE_APPOINTMENT};

    AppointmentDAO(DbConnection dbConnection) {
        setDbConnection(dbConnection);
    }

    private static final String STATEMENT_INSERT_APPOINTMENT = "INSERT INTO " + TABLE_APPOINTMENT + "(" +
            COLUMN_APPOINTMENT_ID + ", " +
            COLUMN_CUSTOMER_ID + ", " +
            COLUMN_APPOINTMENT_TYPE + ", " +
            COLUMN_APPOINTMENT_START + ", " +
            COLUMN_APPOINTMENT_END + ") VALUES (?, ?, ?, " + VALUE_FROM_UNIXTIME + ", " + VALUE_FROM_UNIXTIME + ")";

    /**
     * Insert an {@link Appointment} into the database
     *
     * @param newAppointment The {@link Appointment} that will be inserted into the database
     */
    @Override
    public void insertEntity(Appointment newAppointment) throws SQLException, ClassNotFoundException {
        /*Build the statements required to insert a appointment*/
        String[][] statements = emptyEntity(APPOINTMENT_TABLES.length);

        /*Insert Appointment Statement*/
        statements[0] = new String[]{
                STATEMENT_INSERT_APPOINTMENT,
                Integer.toString(newAppointment.getId()),
                Integer.toString(newAppointment.getCustomerId()),
                newAppointment.getType(),
                newAppointment.startEpochString(),
                newAppointment.endEpochString()
        };

        /*Execute the required statements*/
        update(statements);
    }

    private static final String STATEMENT_UPDATE_APPOINTMENT = "UPDATE " + TABLE_APPOINTMENT +
            " SET " + COLUMN_APPOINTMENT_TYPE + " = ?, " +
            COLUMN_APPOINTMENT_START + " = " + VALUE_FROM_UNIXTIME + ", " +
            COLUMN_APPOINTMENT_END + " = " + VALUE_FROM_UNIXTIME +
            " WHERE " + COLUMN_APPOINTMENT_ID + " = ?";

    /**
     * Update an existing {@link Appointment} in the database
     *
     * @param updatedAppointment The {@link Appointment} that will be updated in the database
     */
    @Override
    public void updateEntity(Appointment updatedAppointment) throws SQLException, ClassNotFoundException {
        /*Build the statements required to delete a appointment*/
        String[][] statements = emptyEntity(APPOINTMENT_TABLES.length);

        /*Update Appointment Statement*/
        statements[0] = new String[]{
                STATEMENT_UPDATE_APPOINTMENT,
                updatedAppointment.getType(),
                updatedAppointment.startEpochString(),
                updatedAppointment.endEpochString(),
                Integer.toString(updatedAppointment.getId())
        };

        /*Execute the required statements*/
        update(statements);
    }

    private static final String STATEMENT_DELETE_APPOINTMENT = "DELETE FROM " + TABLE_APPOINTMENT + " WHERE " +
            COLUMN_APPOINTMENT_ID + " = ?";

    /**
     * Delete an existing {@link Appointment} in the database
     *
     * @param selectedAppointment The {@link Appointment} that will be deleted in the database
     */
    @Override
    public void deleteEntity(Appointment selectedAppointment) throws SQLException, ClassNotFoundException {
        /*Build the statements required to delete a appointment*/
        String[][] statements = emptyEntity(APPOINTMENT_TABLES.length);

        /*Delete Appointment Statement*/
        statements[0] = new String[]{
                STATEMENT_DELETE_APPOINTMENT,
                String.valueOf(selectedAppointment.getId())
        };

        /*Execute the required statements*/
        update(statements);
    }

    private static final String QUERY_SELECT_APPOINTMENTS = "SELECT * FROM " + TABLE_APPOINTMENT;
    @Override
    public List<Appointment> getEntities() throws SQLException, ClassNotFoundException {
        List<Appointment> appointments = new ArrayList<>();

        ResultSet apptRS = getResultSet(new String[]{
                QUERY_SELECT_APPOINTMENTS
        });

        while (apptRS.next()) {
            appointments.add(buildObject(apptRS));
        }

        return appointments;
    }

    /*Check if an appointment overlaps any other appointments*/
    private static final String QUERY_SELECT_OVERLAPPED_APPOINTMENTS = "SELECT count(*) FROM " + TABLE_APPOINTMENT + //SELECT all appointments
            " WHERE " + COLUMN_CUSTOMER_ID + " = ? " + //WHERE customerId is the same
            "AND " + COLUMN_APPOINTMENT_ID + " != ? " + //AND only look at other appointments

            "AND ((" +//AND (one of three things)

            /*appt start time is between the start date and end date*/
            VALUE_FROM_UNIXTIME + " > " + COLUMN_APPOINTMENT_START + " AND " +
            VALUE_FROM_UNIXTIME + " < " + COLUMN_APPOINTMENT_END +

            /*OR appt end time is between the start date and end date*/
            ") OR (" +
            VALUE_FROM_UNIXTIME + " > " + COLUMN_APPOINTMENT_START + " AND "
            + VALUE_FROM_UNIXTIME + " < " + COLUMN_APPOINTMENT_END +

            /*OR the appt start time is before/equal to the start date, and the appt end time is equal to/after the end date*/
            ") OR (" +
            VALUE_FROM_UNIXTIME + " <= " + COLUMN_APPOINTMENT_START + " AND "
            + VALUE_FROM_UNIXTIME + " >= " + COLUMN_APPOINTMENT_END + "))";
    int selectOverlappedAppointments(Appointment appointment) throws SQLException, ClassNotFoundException {
        int count = 0;

        String apptStart = appointment.startEpochString();
        String apptEnd = appointment.endEpochString();

        ResultSet rs = getResultSet(new String[]{
                QUERY_SELECT_OVERLAPPED_APPOINTMENTS,
                Integer.toString(appointment.getCustomerId()),
                Integer.toString(appointment.getId()),
                apptStart,
                apptStart,
                apptEnd,
                apptEnd,
                apptStart,
                apptEnd
        });

        while (rs.next()) { //For each result
            count = rs.getInt(1);
        }

        return count;
    }

    /*Select appointments within 15 minutes of now*/
    private static final String QUERY_SELECT_CLOSE_APPOINTMENTS = "SELECT * FROM " + TABLE_APPOINTMENT +
            " WHERE (now() < " +
            COLUMN_APPOINTMENT_START +
            ") AND (" +
            COLUMN_APPOINTMENT_START +
            " < (now() + INTERVAL 15 MINUTE))";
    LinkedList<Appointment> getCloseAppointments() throws SQLException, ClassNotFoundException {
        LinkedList<Appointment> appointments = new LinkedList<>();

        ResultSet apptRS = getResultSet(new String[]{
                QUERY_SELECT_CLOSE_APPOINTMENTS
        });

        while (apptRS.next()) {
            appointments.add(buildObject(apptRS));
        }

        return appointments;
    }

    @Override
    public Appointment buildObject(ResultSet apptRS) throws SQLException {
        int apptId = apptRS.getInt(COLUMN_APPOINTMENT_ID);
        int custId = apptRS.getInt(COLUMN_CUSTOMER_ID);
        String apptType = apptRS.getString(COLUMN_APPOINTMENT_TYPE);
        LocalDateTime apptStart = apptRS.getTimestamp(COLUMN_APPOINTMENT_START).toLocalDateTime();
        LocalDateTime apptEnd = apptRS.getTimestamp(COLUMN_APPOINTMENT_END).toLocalDateTime();

        return new Appointment(apptId, custId, apptType, apptStart, apptEnd);
    }
}
