package workscheduler.log;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class UserLogTest {

    private UserLog userLog;

    private static final String TEST_PATH = "src/main/java/workscheduler/log/TestLog.txt";
    private static final String EXPECTED_USERNAME = "Tester";
    private static final LocalDateTime EXPECTED_TIME = LocalDateTime.of(1989, 4, 13, 2, 28, 6, 9);

    @BeforeEach
    void setUp() {
        userLog = new UserLog("Tester", TEST_PATH, EXPECTED_TIME);

        try {
            userLog.logUser();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        try {
            userLog.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void logUser() {
        String line = "";
        try {
            Scanner scanner = new Scanner(new File(TEST_PATH));

            line = scanner.nextLine();

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertTrue(line.contains(EXPECTED_TIME.toString()) &&
                line.contains("User Logged In") &&
                line.contains(EXPECTED_USERNAME));
    }

    @Test
    void delete() {
        try {
            userLog.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(TEST_PATH);

        assertFalse(file.exists());
    }
}