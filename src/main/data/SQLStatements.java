package main.data;

class SQLStatements {

    /*
    User Table
    */
    static final String COLUMN_USER_ID = "userId";
    static final String COLUMN_USER_USERNAME = "userName";
    static final String COLUMN_USER_PASSWORD = "password";

    /*User Table Statements*/
    static final String SELECT_USER = "SELECT * FROM user WHERE userName=? AND password=?";
}
