package main.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends DAO {

    /*
    User Table
    */
    private static final String TABLE_USER = "user";
    private static final String COLUMN_USER_USERNAME = "userName";
    private static final String COLUMN_USER_PASSWORD = "password";
    /*User Table Statements*/
    private static final String QUERY_SELECT_USER = "SELECT * FROM user WHERE userName=? AND password=?";

    /**
     * The tables required to modify a customer entity
     */
    private static final String[] USER_TABLES = {TABLE_USER};

    UserDAO(DbConnection dbConnection) {
        setDbConnection(dbConnection);
    }

    boolean login(String username, String password) throws SQLException, ClassNotFoundException {

        String[][] queries = emptyEntity(USER_TABLES.length);

        queries[0] = new String[]{
                QUERY_SELECT_USER,
                username,
                password
        };

        ResultSet[] resultSets = getResultSets(queries);

        ResultSet userRs = resultSets[0];

        while (userRs.next()) { //For each result
            String dbUsername = userRs.getString(COLUMN_USER_USERNAME);
            String dbPassword = userRs.getString(COLUMN_USER_PASSWORD);

            return dbUsername.equals(username) && dbPassword.equals(password);
        }

        return false;
    }
}
