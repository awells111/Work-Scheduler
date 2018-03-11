package main.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DAO {

    public static final int CODE_ERROR = -1;
    public static final int CODE_SUCCESS = 1;

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
     * @return {@value CODE_SUCCESS} if successful, else {@value CODE_ERROR}
     */
    int update(Connection conn, String[][] statements) {
        try {
            conn.setAutoCommit(false); //By setting AutoCommit to false, the we can cancel the first statement if the second one fails
            try {
                /*Build the prepared statements required to update the database*/
                PreparedStatement[] preparedStatements = buildPreparedStatements(conn, statements);

                /*Execute all of the PreparedStatements that were just built*/
                for (PreparedStatement p : preparedStatements) {
                    p.executeUpdate();
                }

                /*If no exceptions were thrown, commit the update*/
                conn.commit();
            } catch (SQLException e) {

                /*If an exception was thrown, cancel the update and rollback any changes that were made*/
                conn.rollback();
                throw e;
            }

            conn.close();
        } catch (
                SQLException e) {
            e.printStackTrace();
            return CODE_ERROR;
        }

        return CODE_SUCCESS;
    }

    ResultSet[] getResultSets(Connection conn, String[][] statements) {

        ResultSet[] resultSets = new ResultSet[statements.length];

        try {
            PreparedStatement[] preparedStatements = buildPreparedStatements(conn, statements);

            for (int i = 0; i < resultSets.length; i++) {
                resultSets[i] = preparedStatements[i].executeQuery();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSets;
    }

}
