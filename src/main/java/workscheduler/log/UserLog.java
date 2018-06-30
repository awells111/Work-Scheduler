package workscheduler.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class UserLog {

    private String username;
    private String logPath;
    private LocalDateTime localDateTime;

    public UserLog(String username, String logPath, LocalDateTime localDateTime) {
        this.username = username;
        this.logPath = logPath;
        this.localDateTime = localDateTime;
    }

    public void logUser() throws SecurityException, IOException {

        /*By setting this to true, our line will append to the log instead of creating a new one*/
        PrintStream fileStream = new PrintStream(new FileOutputStream(logPath, true));

        //Print the timestamp and username to the log
        fileStream.println(localDateTime.toString() + " User Logged In: " + username);

        fileStream.close();
    }

    void delete() throws IOException {
        Files.deleteIfExists(Paths.get(logPath));
    }
}
