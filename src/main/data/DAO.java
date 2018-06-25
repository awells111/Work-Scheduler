package main.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DAO<T> implements DbObjectBuilder<T> {

    static final String VALUE_FROM_UNIXTIME = "FROM_UNIXTIME(?)";

    private DbConnection dbConnection;

    protected DbConnection getDbConnection() {
        return dbConnection;
    }

    protected void setDbConnection(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * @param statementsCount The number of statements required to execute an update
     * @return An empty String[][] that subclasses will fill to create an entity
     */
    String[][] emptyEntity(int statementsCount) {
        return new String[statementsCount][];
    }

    PreparedStatement[] buildPreparedStatements(Connection conn, String[][] statements) throws SQLException {

        /*Number of prepared statements to execute*/
        int preparedStatementsCount = statements.length;

        /*Will hold all of our statements*/
        PreparedStatement[] preparedStatements = new PreparedStatement[preparedStatementsCount];


        for (int i = 0; i < preparedStatementsCount; i++) {

            /*The statement String itself*/
            String statementString = statements[i][0];

            /*Create the PreparedStatement from the statement String*/
            PreparedStatement currentPS = conn.prepareStatement(statementString);

            /*Build our current PreparedStatement*/
            for (int j = 1; j < statements[i].length; j++) {

                /*
                 * CurrentPS is our current PreparedStatement
                 *
                 * j is the parameterIndex for currentPS. The parameterIndex starts at 1 for PreparedStatements.
                 *
                 * statements[i][0] is statement in String form
                 *
                 * statements[i][1] through statements[i][statements[i].length - 1] are the params
                 * */
                currentPS.setString(j, statements[i][j]);
            }

            preparedStatements[i] = currentPS; //Put the current PreparedStatement in the array
        }

        return preparedStatements;
    }

    /**
     * A method used for insert, update, and delete statements
     *
     * @param conn       A pre-existing instance of {@link java.sql.Connection} set by {@link DbConnection}
     * @param statements A String[][] representing the entity to be inserted, updated, or deleted
     */
    private void update(Connection conn, String[][] statements) throws SQLException {
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

    protected void update(String[][] statements) throws SQLException, ClassNotFoundException {
        try (Connection conn = getDbConnection().getConnection()) {
            update(conn, statements);
        }
    }

    private ResultSet[] getResultSets(Connection conn, String[][] statements) throws SQLException {

        ResultSet[] resultSets = new ResultSet[statements.length];

        PreparedStatement[] preparedStatements = buildPreparedStatements(conn, statements);

        for (int i = 0; i < resultSets.length; i++) {
            resultSets[i] = preparedStatements[i].executeQuery();
        }

        return resultSets;
    }

    protected ResultSet[] getResultSets(String[][] statements) throws SQLException, ClassNotFoundException {
        return getResultSets(getDbConnection().getConnection(), statements);
    }

}
