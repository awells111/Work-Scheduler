package workscheduler.data;

import java.sql.PreparedStatement;
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
        try (PreparedStatement stmt = getDbConnection().getConnection().prepareStatement(QUERY_SELECT_USER))
        {
        int stmtIndex = 0;
        stmt.setString(++stmtIndex, username);
        stmt.setString(++stmtIndex, password);

        ResultSet userRS = stmt.executeQuery();

        userRS.next();

        String dbUsername = userRS.getString(COLUMN_USER_USERNAME);
        String dbPassword = userRS.getString(COLUMN_USER_PASSWORD);

        return dbUsername.equals(username) && dbPassword.equals(password);
        }
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