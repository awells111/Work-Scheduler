package main.data;

import main.model.Appointment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AppointmentDAO extends DAO {

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

    private Database database;

    AppointmentDAO(Database database) {
        this.database = database;
        setDbConnection(database.getDbConnection());
    }

    public Database getDatabase() {
        return database;
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
     * @return {@value CODE_SUCCESS} if successful, else {@value CODE_ERROR}
     */
    int insertEntity(Appointment newAppointment) {
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
        return update(statements);
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
     * @return {@value CODE_SUCCESS} if successful, else {@value CODE_ERROR}
     */
    int updateEntity(Appointment updatedAppointment) {
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
        return update(statements);
    }

    private static final String STATEMENT_DELETE_APPOINTMENT = "DELETE FROM " + TABLE_APPOINTMENT + " WHERE " +
            COLUMN_APPOINTMENT_ID + " = ?";

    /**
     * Delete an existing {@link Appointment} in the database
     *
     * @param selectedAppointment The {@link Appointment} that will be deleted in the database
     * @return {@value CODE_SUCCESS} if successful, else {@value CODE_ERROR}
     */
    int deleteEntity(Appointment selectedAppointment) {
        /*Build the statements required to delete a appointment*/
        String[][] statements = emptyEntity(APPOINTMENT_TABLES.length);

        /*Delete Appointment Statement*/
        statements[0] = new String[]{
                STATEMENT_DELETE_APPOINTMENT,
                String.valueOf(selectedAppointment.getId())
        };

        /*Execute the required statements*/
        return update(statements);
    }

    private static final String QUERY_SELECT_APPOINTMENTS = "SELECT * FROM " + TABLE_APPOINTMENT;

    ArrayList getEntities() {
        ArrayList<Appointment> appointments = new ArrayList<>();

        String[][] queries = emptyEntity(APPOINTMENT_TABLES.length);

        queries[0] = new String[]{
                QUERY_SELECT_APPOINTMENTS
        };

        ResultSet[] resultSets = getResultSets(queries);

        try {
            ResultSet apptRS = resultSets[0];

            while (apptRS.next()) { //For each result
                int apptId = apptRS.getInt(COLUMN_APPOINTMENT_ID);
                int custId = apptRS.getInt(COLUMN_CUSTOMER_ID);
                String apptType = apptRS.getString(COLUMN_APPOINTMENT_TYPE);
                LocalDateTime apptStart = apptRS.getTimestamp(COLUMN_APPOINTMENT_START).toLocalDateTime();
                LocalDateTime apptEnd = apptRS.getTimestamp(COLUMN_APPOINTMENT_END).toLocalDateTime();

                appointments.add(new Appointment(apptId, custId, apptType, apptStart, apptEnd));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    /*Check if an appointment overlaps other appointments*/
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

    int selectOverlappedAppointments(Appointment appointment) {
        int count = 0;

        String[][] queries = emptyEntity(APPOINTMENT_TABLES.length);

        String apptStart = appointment.startEpochString();
        String apptEnd = appointment.endEpochString();

        queries[0] = new String[]{
                QUERY_SELECT_OVERLAPPED_APPOINTMENTS,
                Integer.toString(appointment.getCustomerId()),
                Integer.toString(appointment.getId()),
                apptStart,
                apptStart,
                apptEnd,
                apptEnd,
                apptStart,
                apptEnd
        };

        ResultSet[] resultSets = getResultSets(queries);

        try {
            ResultSet rs = resultSets[0];

            while (rs.next()) { //For each result
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
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

    ArrayList<Appointment> getCloseAppointments() {
        ArrayList<Appointment> appointments = new ArrayList<>(); //todo LinkedList

        String[][] queries = emptyEntity(APPOINTMENT_TABLES.length);

        queries[0] = new String[]{
                QUERY_SELECT_CLOSE_APPOINTMENTS
        };

        ResultSet[] resultSets = getResultSets(queries);

        try {
            ResultSet apptRS = resultSets[0];

            while (apptRS.next()) { //For each result
                int apptId = apptRS.getInt(COLUMN_APPOINTMENT_ID);
                int custId = apptRS.getInt(COLUMN_CUSTOMER_ID);
                String apptType = apptRS.getString(COLUMN_APPOINTMENT_TYPE);
                LocalDateTime apptStart = apptRS.getTimestamp(COLUMN_APPOINTMENT_START).toLocalDateTime();
                LocalDateTime apptEnd = apptRS.getTimestamp(COLUMN_APPOINTMENT_END).toLocalDateTime();

                appointments.add(new Appointment(apptId, custId, apptType, apptStart, apptEnd));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }
}
