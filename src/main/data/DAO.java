package main.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

abstract class DAO<E> implements DbObjectBuilder<E>, QueryBuilder<E> {

    private DbConnection dbConnection;

    DbConnection getDbConnection() {
        return dbConnection;
    }

    void setDbConnection(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /*Build a prepared statement from a String[]. The first String of the array is the database query. The other Strings
     * of the array are parameters for the query.*/
    private PreparedStatement buildPreparedStatement(Connection conn, String[] statement) throws SQLException {
        String statementString = statement[0];

        PreparedStatement currentPS = conn.prepareStatement(statementString);

        for (int j = 1; j < statement.length; j++) {
            currentPS.setString(j, statement[j]);
        }

        return currentPS;
    }

    void executeUpdate(String[] statement) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = buildPreparedStatement(getDbConnection().getConnection(), statement);

        preparedStatement.executeUpdate(); //Throws a SQL exception if the update is unsuccessful
    }

    ResultSet executeQuery(String[] statement) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = buildPreparedStatement(getDbConnection().getConnection(), statement);

        return preparedStatement.executeQuery();
    }
}

interface DbObjectBuilder<E> {

    /*Build the object from the ResultSet returned by the database.*/
    E buildObject(ResultSet rs) throws SQLException;

}

interface QueryBuilder<E> {

    /*Insert an entity into the database.*/
    int insert(E e) throws SQLException, ClassNotFoundException;

    /*Update an entity in the database.*/
    void update(E e) throws SQLException, ClassNotFoundException;

    /*Delete an entity in the database.*/
    void delete(E e) throws SQLException, ClassNotFoundException;

    /*Returns a list of all entities from the database.*/
    List<E> getAll() throws SQLException, ClassNotFoundException;
}