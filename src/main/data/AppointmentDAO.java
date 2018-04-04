package main.data;

import main.model.Appointment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static main.data.Database.MYSQL_DATETIME_FORMAT;

public class AppointmentDAO extends DAO{

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
            COLUMN_APPOINTMENT_END + ") VALUES (?, ?, ?, ?, ?)";
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
                getDatabase().localDateTimeToDatabase(newAppointment.getStart()),
                getDatabase().localDateTimeToDatabase(newAppointment.getEnd())
        };

        /*Execute the required statements*/
        return update(statements);
    }

    private static final String STATEMENT_UPDATE_APPOINTMENT = "UPDATE " + TABLE_APPOINTMENT +
            " SET " + COLUMN_APPOINTMENT_TYPE + " = ?, " +
            COLUMN_APPOINTMENT_START + " = ?, " +
            COLUMN_APPOINTMENT_END + " = ? WHERE " +
            COLUMN_APPOINTMENT_ID + " = ?";
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
                getDatabase().localDateTimeToDatabase(updatedAppointment.getStart()),
                getDatabase().localDateTimeToDatabase(updatedAppointment.getEnd()),
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


    private static final String QUERY_SELECT_APPOINTMENTS = "SELECT " +
            COLUMN_APPOINTMENT_ID +
            ", " +
            COLUMN_CUSTOMER_ID +
            ", " +
            COLUMN_APPOINTMENT_TYPE +
            ", " +
            "DATE_FORMAT(" + COLUMN_APPOINTMENT_START + ", " + MYSQL_DATETIME_FORMAT +
            ") AS " + COLUMN_APPOINTMENT_START +
            ", " +
            "DATE_FORMAT(" + COLUMN_APPOINTMENT_END + ", " + MYSQL_DATETIME_FORMAT +
            ") AS " + COLUMN_APPOINTMENT_END +
            " FROM " +
            TABLE_APPOINTMENT;

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
                String apptStart = getDatabase().databaseDateTimeToLocal(apptRS.getString(COLUMN_APPOINTMENT_START));
                String apptEnd = getDatabase().databaseDateTimeToLocal(apptRS.getString(COLUMN_APPOINTMENT_END));

                appointments.add(new Appointment(apptId, custId, apptType, apptStart, apptEnd));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    /*Check if an appointment overlaps other appointments*/
    private static final String QUERY_SELECT_OVERLAPPED_APPOINTMENTS = "SELECT count(*) FROM " +
            TABLE_APPOINTMENT +
            " WHERE " + COLUMN_CUSTOMER_ID + " = ? AND ((? > " + //WHERE customerId is the same
            COLUMN_APPOINTMENT_START +
            " AND ? < " +//AND (one of three things)
            COLUMN_APPOINTMENT_END + //appt start time is between the start date and end date
            ") OR (? > " + // OR appt end time is between the start date and end date
            COLUMN_APPOINTMENT_START +
            " AND ? < " +
            COLUMN_APPOINTMENT_END +
            ") OR (? <= " + //OR the appt start time is before/equal to the start date, and the appt end time is equal to/after the end date
            COLUMN_APPOINTMENT_START +
            " AND ? >= " +
            COLUMN_APPOINTMENT_END + "))";

    int selectOverlappedAppointments(Appointment appointment) {
        int count = 0;

        String[][] queries = emptyEntity(APPOINTMENT_TABLES.length);

        String apptStart = getDatabase().localDateTimeToDatabase(appointment.getStart());
        String apptEnd = getDatabase().localDateTimeToDatabase(appointment.getEnd());

        queries[0] = new String[]{
                QUERY_SELECT_OVERLAPPED_APPOINTMENTS,
                Integer.toString(appointment.getCustomerId()),
                apptStart,
                apptStart,
                apptEnd,
                apptEnd,
                apptStart,
                apptEnd
        };

        ResultSet[] resultSets = getResultSets(queries);

        try {
            ResultSet rs = resultSets[0]; //todo Can we just put resultSets[0] in the while loop

            while (rs.next()) { //For each result
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    /*Select appointments within 15 minutes of now*/
    private static final String QUERY_SELECT_CLOSE_APPOINTMENTS = "SELECT " +
            COLUMN_APPOINTMENT_ID +
            ", " +
            COLUMN_CUSTOMER_ID +
            ", " +
            COLUMN_APPOINTMENT_TYPE +
            ", " +
            "DATE_FORMAT(" + COLUMN_APPOINTMENT_START + ", " + MYSQL_DATETIME_FORMAT +
            ") AS " + COLUMN_APPOINTMENT_START +
            ", " +
            "DATE_FORMAT(" + COLUMN_APPOINTMENT_END + ", " + MYSQL_DATETIME_FORMAT +
            ") AS " + COLUMN_APPOINTMENT_END +
            " FROM appointment WHERE (now() < " +
            COLUMN_APPOINTMENT_START +
            ") AND (" +
            COLUMN_APPOINTMENT_START +
            " < (now() + INTERVAL 15 MINUTE))";

    ArrayList<Appointment> getCloseAppointments() {
        ArrayList<Appointment> appointments = new ArrayList<>();

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
                String apptStart = getDatabase().databaseDateTimeToLocal(apptRS.getString(COLUMN_APPOINTMENT_START));
                String apptEnd = getDatabase().databaseDateTimeToLocal(apptRS.getString(COLUMN_APPOINTMENT_END));

                appointments.add(new Appointment(apptId, custId, apptType, apptStart, apptEnd));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }
}
