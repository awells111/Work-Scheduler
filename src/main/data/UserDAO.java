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

    private DbConnection dbConnection;

    UserDAO(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    private DbConnection getDbConnection() {
        return dbConnection;
    }

    private ResultSet[] getResultSets(String[][] statements) {
        try {
            return super.getResultSets(getDbConnection().getConnection(), statements);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new ResultSet[0];
    }

    int login(String username, String password) {

        String[][] queries = emptyEntity(USER_TABLES.length);

        queries[0] = new String[]{
                QUERY_SELECT_USER,
                username,
                password
        };

        ResultSet[] resultSets = getResultSets(queries);

        try {

            ResultSet userRs = resultSets[0];

            while (userRs.next()) { //For each result
                String dbUsername = userRs.getString(COLUMN_USER_USERNAME);
                String dbPassword = userRs.getString(COLUMN_USER_PASSWORD);

                return (dbUsername.equals(username) && dbPassword.equals(password)) ? CODE_SUCCESS : CODE_ERROR;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return CODE_ERROR;
    }
}
