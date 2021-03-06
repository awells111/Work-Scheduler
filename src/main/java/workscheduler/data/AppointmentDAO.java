package workscheduler.data;

import workscheduler.model.Appointment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AppointmentDAO extends DAO implements QueryBuilder<Appointment> {

    /*
    Appointment Table
    */
    private static final String COLUMN_APPOINTMENT_ID = "appointmentId";
    private static final String COLUMN_CUSTOMER_ID = "customerId";
    private static final String COLUMN_APPOINTMENT_TYPE = "title";
    private static final String COLUMN_APPOINTMENT_START = "start";
    private static final String COLUMN_APPOINTMENT_END = "end";

    private static final String STATEMENT_INSERT_APPOINTMENT = "CALL sp_appointment_Insert_pk(?, ?, ?, ?)";

    /**
     * Insert an {@link Appointment} into the database
     *
     * @param newAppointment The {@link Appointment} that will be inserted into the database
     */
    @Override
    public int insert(Appointment newAppointment) throws SQLException, ClassNotFoundException {
        try (Connection connection = createConnection()) {
            PreparedStatement stmt = connection.prepareStatement(STATEMENT_INSERT_APPOINTMENT);

            int stmtIndex = 0;
            stmt.setInt(++stmtIndex, newAppointment.getCustomerId());
            stmt.setString(++stmtIndex, newAppointment.getType());
            stmt.setTimestamp(++stmtIndex, Timestamp.valueOf(newAppointment.getStart()));
            stmt.setTimestamp(++stmtIndex, Timestamp.valueOf(newAppointment.getEnd()));

            ResultSet apptRS = stmt.executeQuery();

            apptRS.next();

            /*Return the id of the new Appointment*/
            return apptRS.getInt(1);
        }
    }

    private static final String STATEMENT_UPDATE_APPOINTMENT = "CALL sp_appointment_UpdateById(?, ?, ?, ?)";

    /**
     * Update an existing {@link Appointment} in the database
     *
     * @param updatedAppointment The {@link Appointment} that will be updated in the database
     */
    @Override
    public void update(Appointment updatedAppointment) throws SQLException, ClassNotFoundException {
        try (Connection connection = createConnection()) {
            PreparedStatement stmt = connection.prepareStatement(STATEMENT_UPDATE_APPOINTMENT);

            int stmtIndex = 0;
            stmt.setString(++stmtIndex, updatedAppointment.getType());
            stmt.setTimestamp(++stmtIndex, Timestamp.valueOf(updatedAppointment.getStart()));
            stmt.setTimestamp(++stmtIndex, Timestamp.valueOf(updatedAppointment.getEnd()));
            stmt.setInt(++stmtIndex, updatedAppointment.getId());

            stmt.executeUpdate();
        }
    }

    private static final String STATEMENT_DELETE_APPOINTMENT = "CALL sp_appointment_DeleteById(?)";

    /**
     * Delete an existing {@link Appointment} in the database
     *
     * @param selectedAppointment The {@link Appointment} that will be deleted in the database
     */
    @Override
    public void delete(Appointment selectedAppointment) throws SQLException, ClassNotFoundException {
        try (Connection connection = createConnection()) {
            PreparedStatement stmt = connection.prepareStatement(STATEMENT_DELETE_APPOINTMENT);

            int stmtIndex = 0;
            stmt.setInt(++stmtIndex, selectedAppointment.getId());

            stmt.executeUpdate();
        }
    }

    private static final String QUERY_SELECT_APPOINTMENT_BY_ID = "CALL sp_appointment_SelectByApptId(?)";

    @Override
    public Appointment selectById(int appointmentId) throws SQLException, ClassNotFoundException {
        try (Connection connection = createConnection()) {
            PreparedStatement stmt = connection.prepareStatement(QUERY_SELECT_APPOINTMENT_BY_ID);

            stmt.setInt(1, appointmentId);

            ResultSet custRS = stmt.executeQuery();

            custRS.next();

            return buildObject(custRS);
        }
    }

    private static final String QUERY_SELECT_APPOINTMENTS = "CALL sp_appointment_SelectByCustomerId(?)";

    @Override
    public List<Appointment> getAll(int userId) throws SQLException, ClassNotFoundException {
        try (Connection connection = createConnection()) {
            PreparedStatement stmt = connection.prepareStatement(QUERY_SELECT_APPOINTMENTS);

            stmt.setInt(1, userId);

            ResultSet apptRS = stmt.executeQuery();

            List<Appointment> appointments = new ArrayList<>();

            while (apptRS.next()) {
                appointments.add(buildObject(apptRS));
            }

            return appointments;
        }
    }

    /*Check if an appointment overlaps any other appointments*/
    private static final String QUERY_SELECT_OVERLAPPED_APPOINTMENTS = "CALL sp_appointment_SelectOverlapped(?, ?, ?, ?)";

    int selectOverlappedAppointments(Appointment appointment) throws SQLException, ClassNotFoundException {
        try (Connection connection = createConnection()) {
            PreparedStatement stmt = connection.prepareStatement(QUERY_SELECT_OVERLAPPED_APPOINTMENTS);

            int stmtIndex = 0;
            stmt.setInt(++stmtIndex, appointment.getCustomerId());
            stmt.setInt(++stmtIndex, appointment.getId());
            stmt.setTimestamp(++stmtIndex, Timestamp.valueOf(appointment.getStart()));
            stmt.setTimestamp(++stmtIndex, Timestamp.valueOf(appointment.getEnd()));

            ResultSet apptRS = stmt.executeQuery();


            int count = 0;

            while (apptRS.next()) { //For each result
                count = apptRS.getInt(1);
            }

            return count;
        }
    }

    /*Select appointments within 15 minutes of now*/
    private static final String QUERY_SELECT_CLOSE_APPOINTMENTS = "CALL sp_appointment_SelectClose(?)";

    LinkedList<Appointment> getCloseAppointments(int userId) throws SQLException, ClassNotFoundException {
        LinkedList<Appointment> appointments = new LinkedList<>();

        try (Connection connection = createConnection()) {
            PreparedStatement stmt = connection.prepareStatement(QUERY_SELECT_CLOSE_APPOINTMENTS);

            stmt.setInt(1, userId);

            ResultSet apptRS = stmt.executeQuery();

            while (apptRS.next()) {
                appointments.add(buildObject(apptRS));
            }

            return appointments;
        }
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
