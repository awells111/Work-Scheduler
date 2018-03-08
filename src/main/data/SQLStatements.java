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

    /*
    Customer Table
    */
    static final String COLUMN_CUSTOMER_ID = "customerId";
    static final String COLUMN_CUSTOMER_NAME = "customerName";

    /*Customer Table Statements*/
    static final String SELECT_CUSTOMERS = "SELECT * FROM customer";
    static final String INSERT_CUSTOMER = "INSERT INTO `customer`(`customerId`, `customerName`, `addressId`) VALUES (?, ?, ?)";

    static final String DELETE_CUSTOMER = "DELETE FROM customer WHERE customerId = ?";

    /*
    Address Table
    */
    static final String COLUMN_ADDRESS_ID = "addressId";
    static final String COLUMN_ADDRESS_NAME = "address";
    static final String COLUMN_ADDRESS_PHONE = "phone";

    /*Address Table Statements*/
    static final String SELECT_ADDRESSES = "SELECT * FROM address";
    static final String INSERT_ADDRESS = "INSERT INTO `address`(`addressId`, `address`, `phone`) VALUES (?, ?, ?)";

    static final String DELETE_ADDRESS = "DELETE FROM address WHERE addressId = ?";

}
