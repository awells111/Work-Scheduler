package main.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
     * The tables required to modify a user entity
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


    @Override
    public Object buildObject(ResultSet rs) {
        throw new RuntimeException("We are not implementing buildObject for userDAO.");
    }

    @Override
    public void insertEntity(Object o) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("We are not implementing insertEntity for userDAO.");
    }

    @Override
    public void updateEntity(Object o) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("We are not implementing updateEntity for userDAO.");
    }

    @Override
    public void deleteEntity(Object o) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("We are not implementing deleteEntity for userDAO.");
    }

    @Override
    public List getEntities() throws SQLException, ClassNotFoundException {
        throw new RuntimeException("We are not implementing getEntities for userDAO.");
    }
}