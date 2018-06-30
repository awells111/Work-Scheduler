package workscheduler.data;

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