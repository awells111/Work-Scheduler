package main.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DbObjectBuilder<T> {

    /*Build the object from the ResultSet returned by the database.*/
    public T buildObject(ResultSet rs) throws SQLException;

}