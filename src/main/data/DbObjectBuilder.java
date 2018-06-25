package main.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DbObjectBuilder<E> {

    /*Build the object from the ResultSet returned by the database.*/
    public E buildObject(ResultSet rs) throws SQLException;

}