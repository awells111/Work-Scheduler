package main.log;

import main.model.ConnectedUser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserLog {

    /*File prints to log.txt at the project directory above the "src" folder*/
    private static final String USER_LOG_PATH = "log.txt";

    private static final String TIMESTAMP_FORMAT = "yyyy.MM.dd.HH.mm.ss";

    private ConnectedUser mConnectedUser;

    public UserLog(ConnectedUser connectedUser) {
        mConnectedUser = connectedUser;
    }

    public void logUser() {
        try {

            /*By setting this to true, our line will append to the log instead of creating a new one*/
            PrintStream fileStream = new PrintStream(new FileOutputStream(USER_LOG_PATH, true));

            /*Current timestamp*/
            String timeStamp = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());

            //Print the timestamp and username to the log
            fileStream.println(timeStamp + " User Logged In: " + mConnectedUser.getUsername());

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}
