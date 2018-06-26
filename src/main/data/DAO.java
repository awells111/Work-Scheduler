package main.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

abstract class DAO<E> implements DbObjectBuilder<E>, QueryBuilder<E> {

    static final String VALUE_FROM_UNIXTIME = "FROM_UNIXTIME(?)";

    private DbConnection dbConnection;

    private DbConnection getDbConnection() {
        return dbConnection;
    }

    void setDbConnection(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * @param statementsCount The number of statements required to execute an update
     * @return An empty String[][] that subclasses will fill to create an entity
     */
    String[][] emptyEntity(int statementsCount) {
        return new String[statementsCount][];
    }

    /*Used to for queries that require one PreparedStatement.*/
    private PreparedStatement buildPreparedStatement(Connection conn, String[] statement) throws SQLException {
        String statementString = statement[0];

        PreparedStatement currentPS = conn.prepareStatement(statementString);

        for (int j = 1; j < statement.length; j++) {
            currentPS.setString(j, statement[j]);
        }

        return currentPS;
    }

    /*Used to for queries that require more than one PreparedStatement.*/
    private PreparedStatement[] buildPreparedStatements(Connection conn, String[][] statements) throws SQLException {

        /*Number of prepared statements to execute*/
        int preparedStatementsCount = statements.length;

        /*Will hold all of our statements*/
        PreparedStatement[] preparedStatements = new PreparedStatement[preparedStatementsCount];


        for (int i = 0; i < preparedStatementsCount; i++) {
            preparedStatements[i] = buildPreparedStatement(conn, statements[i]); //Put the current PreparedStatement in the array
        }

        return preparedStatements;
    }

    /**
     * A method used for insert, update, and delete statements
     *
     * @param statements A String[][] representing the entity to be inserted, updated, or deleted
     */
    void update(String[][] statements) throws SQLException, ClassNotFoundException {
        try (Connection conn = getDbConnection().getConnection()) {
            conn.setAutoCommit(false); //By setting AutoCommit to false, the we can cancel the first statement if the second one fails

            /*Build the prepared statements required to update the database*/
            PreparedStatement[] preparedStatements = buildPreparedStatements(conn, statements);

            try {
                /*Execute all of the PreparedStatements that were just built*/
                for (PreparedStatement p : preparedStatements) {
                    p.executeUpdate();
                }

                /*If no exceptions were thrown, commit the update*/
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    ResultSet getResultSet(String[] statement) throws SQLException, ClassNotFoundException {
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
    void insertEntity(E e) throws SQLException, ClassNotFoundException;

    /*Update an entity in the database.*/
    void updateEntity(E e) throws SQLException, ClassNotFoundException;

    /*Delete an entity in the database.*/
    void deleteEntity(E e) throws SQLException, ClassNotFoundException;

    /*Returns a list of all entities from the database.*/
    List<E> getEntities() throws SQLException, ClassNotFoundException;
}