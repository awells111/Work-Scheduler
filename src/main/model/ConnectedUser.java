package main.model;

public class ConnectedUser {

    public static final int COLUMN_USER_ID = 1;
    public static final int COLUMN_USER_USERNAME = 2;
    public static final int COLUMN_USER_PASSWORD = 3;

    private int id;
    private String username;
    private String password;

    public ConnectedUser(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
