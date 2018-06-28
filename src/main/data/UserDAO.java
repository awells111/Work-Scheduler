package main.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO extends DAO {

    /*
    User Table
    */
    private static final String COLUMN_USER_USERNAME = "userName";
    private static final String COLUMN_USER_PASSWORD = "password";

    private static final String QUERY_SELECT_USER = "CALL sp_user_SelectByLogin(?, ?)";

    UserDAO(DbConnection dbConnection) {
        setDbConnection(dbConnection);
    }

    boolean login(String username, String password) throws SQLException, ClassNotFoundException {
        ResultSet userRs = executeQuery(new String[]{
                QUERY_SELECT_USER,
                username,
                password
        });

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
    public int insert(Object o) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("We are not implementing insert for userDAO.");
    }

    @Override
    public void update(Object o) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("We are not implementing update for userDAO.");
    }

    @Override
    public void delete(Object o) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("We are not implementing delete for userDAO.");
    }

    @Override
    public List getAll() throws SQLException, ClassNotFoundException {
        throw new RuntimeException("We are not implementing getAll for userDAO.");
    }
}