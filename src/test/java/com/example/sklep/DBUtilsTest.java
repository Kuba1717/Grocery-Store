package com.example.sklep;

import com.example.sklep.model.*;
import javafx.collections.ObservableList;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DBUtilsTest {
    private static final String JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

    @BeforeEach
    public void setUp() {
        DBUtils.setDatabaseConfiguration(JDBC_URL, "", "");

        try (Connection connection = DBUtils.getConnection();
             InputStreamReader reader = new InputStreamReader(DBUtilsTest.class.getResourceAsStream("/h2database.sql"))) {
            RunScript.execute(connection, reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        try (Connection connection = DBUtils.getConnection();
             InputStreamReader reader = new InputStreamReader(DBUtilsTest.class.getResourceAsStream("/cleanup.sql"))) {
            RunScript.execute(connection, reader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DBUtils.setDatabaseConfiguration("jdbc:mysql://localhost:3306/sklep", "root", "");


    }
    @Test
    public void testDatabaseConnection() {
        try {
            Connection connection = DBUtils.getConnection();

            assertNotNull(connection, "Connection is null");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error during database connection");
        }
    }

    @Test
    public void testSignUpUser() {
        DBUtils.setDatabaseConfiguration(JDBC_URL, "", "");

        String login = "testUser";
        String password = "testPassword";
        String name = "Test";
        String surname = "User";
        Boolean isAdmin = false;

        DBUtils.signUpUser(login, password, name, surname, isAdmin);

        User signedUpUser = SessionManager.getInstance().getLoggedInUser();
        //assertNotNull(signedUpUser, "Signed-up user is null");
        assertEquals(login, signedUpUser.getUsername());
        assertEquals(name, signedUpUser.getName());
        assertEquals(surname, signedUpUser.getSurname());
        assertEquals(isAdmin, signedUpUser.isAdmin());
    }
    @Test
    public void testGetProductListFromDatabaseExpired() {
        DBUtils.setDatabaseConfiguration(JDBC_URL, "", "");
        DBUtils.addProduct(new Product("ExpiredProduct", LocalDate.now().minusDays(1), "Food", 10));

        ObservableList<Product> expiredProducts = DBUtils.getProductListFromDatabase(true);

        assertNotNull(expiredProducts, "Product list is null");
        assertEquals(1, expiredProducts.size(), "Unexpected number of expired products");
        assertEquals("ExpiredProduct", expiredProducts.get(0).getProductName(), "Unexpected product name");
        assertEquals(LocalDate.now().minusDays(1), expiredProducts.get(0).getExpirationDate(), "Unexpected expiration date");
        assertEquals("Food", expiredProducts.get(0).getCategory(), "Unexpected category");
        assertEquals(10, expiredProducts.get(0).getQuantity(), "Unexpected quantity");
    }



    @Test
    public void testAddScheduleToDatabase() {
        DBUtils.setDatabaseConfiguration(JDBC_URL, "", "");

        String login = "testUser";
        String password = "testPassword";
        String name = "Test";
        String surname = "User";
        Boolean isAdmin = false;

        DBUtils.signUpUser(login, password, name, surname, isAdmin);
        int userId = SessionManager.getInstance().getLoggedInUser().getId();

        java.sql.Date dayOfWeek = new java.sql.Date(System.currentTimeMillis());

        LocalTime startTime = LocalTime.now();
        LocalTime endTime = startTime.plusHours(1);

        Schedule schedule = new Schedule(userId, dayOfWeek, startTime, endTime);

        DBUtils.addScheduleToDatabase(schedule);

        ObservableList<Schedule> schedules = DBUtils.getSchedulesFromDatabase();
        boolean scheduleFound = false;
        for (Schedule existingSchedule : schedules) {
            if (existingSchedule.getUserId() == userId &&
                    existingSchedule.getStartTime().isAfter(startTime.minusSeconds(1))  &&
                    existingSchedule.getStartTime().isBefore(startTime.plusSeconds(1)) &&
                    existingSchedule.getEndTime().isAfter(endTime.minusSeconds(1)) &&
                    existingSchedule.getEndTime().isBefore(endTime.plusSeconds(1))) {
                scheduleFound = true;
                break;
            }
        }

        assertTrue(scheduleFound);
    }
    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = new Random().nextInt(characters.length());
            randomString.append(characters.charAt(randomIndex));
        }

        return randomString.toString();
    }
}