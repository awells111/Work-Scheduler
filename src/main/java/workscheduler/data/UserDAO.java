package workscheduler.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class UserDAO extends DAO {

    /*
    User Table
    */
    private static final String COLUMN_USER_ID = "userId";
    private static final String COLUMN_USER_USERNAME = "userName";
    private static final String COLUMN_USER_PASSWORD = "password";

    private static final String QUERY_SELECT_USER = "CALL sp_user_SelectByLogin(?, ?)";

    int login(String username, String password) throws SQLException, ClassNotFoundException {

        try (Connection connection = createConnection()) {
            PreparedStatement stmt = connection.prepareStatement(QUERY_SELECT_USER);

            int stmtIndex = 0;
            stmt.setString(++stmtIndex, username);
            stmt.setString(++stmtIndex, password);

            ResultSet userRS = stmt.executeQuery();

            userRS.next();

            String dbUsername = userRS.getString(COLUMN_USER_USERNAME);
            String dbPassword = userRS.getString(COLUMN_USER_PASSWORD);

            if (dbUsername.equals(username) && dbPassword.equals(password)) {
                return userRS.getInt(COLUMN_USER_ID);
            }

            return Integer.MIN_VALUE;
        }
    }
}