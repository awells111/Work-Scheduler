package main.data;

import java.sql.SQLException;
import java.util.List;

public interface QueryBuilder<E> {

    /*Insert an entity into the database.*/
    void insertEntity(E e) throws SQLException, ClassNotFoundException;

    /*Update an entity in the database.*/
    void updateEntity(E e) throws SQLException, ClassNotFoundException;

    /*Delete an entity in the database.*/
    void deleteEntity(E e) throws SQLException, ClassNotFoundException;

    /*Returns a list of all entities from the database.*/
    List<E> getEntities() throws SQLException, ClassNotFoundException;
}
