package com.example.sklep.model;

import com.example.sklep.utilities.AlertHelper;
import com.example.sklep.view.CurrentWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class DBUtils {

    public static String jdbcUrl= "jdbc:mysql://localhost:3306/sklep";
    public static String dbUser = "root";
    public static String dbPassword = "";

    public DBUtils() {
        jdbcUrl = "jdbc:mysql://localhost:3306/sklep";
        dbUser = "root";
        dbPassword = "";
    }
    public static void setDatabaseConfiguration(String jdbcUrl, String dbUser, String dbPassword) {
        DBUtils.jdbcUrl = jdbcUrl;
        DBUtils.dbUser = dbUser;
        DBUtils.dbPassword = dbPassword;
    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
    }


    public static int getNextId(String tableName, String idColumnName) {
        int nextId = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = getConnection();
            System.out.println(jdbcUrl);
            statement = connection.createStatement();
            String query = "SELECT MAX(" + idColumnName + ") FROM " + tableName;
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                int maxId = resultSet.getInt(1);
                nextId = maxId + 1;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return nextId;
    }

    public static void signUpUser( String login, String password, String name, String surname, Boolean isAdmin) {

        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psIsTaken = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = getConnection();


            int lastId = getNextId("uzytkownik", "id");

            psIsTaken = connection.prepareStatement("SELECT * FROM uzytkownik WHERE login = ?");
            psIsTaken.setString(1, login);
            resultSet = psIsTaken.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("ERROR USER EXISTS");
                AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "User already exists", "");


            } else {
                int newId = lastId;

                psInsert = connection.prepareStatement("INSERT INTO uzytkownik (id, login, haslo, imie, nazwisko, admin) VALUES (?, ?, ?, ?, ?, ?)");
                psInsert.setInt(1, newId);
                psInsert.setString(2, login);
                psInsert.setString(3, password);
                psInsert.setString(4, name);
                psInsert.setString(5, surname);
                psInsert.setBoolean(6, isAdmin);
                psInsert.executeUpdate();
                SessionManager.getInstance().setLoggedInUser(new User(newId, login, name,surname,password,isAdmin));
                if(isAdmin) {
                    SessionManager.getInstance().getViewFactory().getCurrentWindowProperty().set(CurrentWindow.ADMIN);

                }
                else {
                    SessionManager.getInstance().getViewFactory().getCurrentWindowProperty().set(CurrentWindow.SELLER);

                }

            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (psInsert != null) psInsert.close();
                if (psIsTaken != null) psIsTaken.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void logInUser(String login, String password) {
        PasswordHasher passwordHasher = new PasswordHasher();
        Connection connection = null;
        PreparedStatement psSelect = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = getConnection();

            psSelect = connection.prepareStatement("select id,haslo,imie,nazwisko, admin from uzytkownik where login =?");
            psSelect.setString(1, login);
            resultSet = psSelect.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "User doesn't exist", "");

            } else {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String retrievedPassword = resultSet.getString("haslo");
                    String name = resultSet.getString("imie");
                    String surname = resultSet.getString("nazwisko");
                    boolean isAdmin = resultSet.getBoolean("admin");

                    if (passwordHasher.matches(password, retrievedPassword)) {

                        SessionManager.getInstance().setLoggedInUser(new User(id, login, name,surname,password,isAdmin));
                        if(isAdmin) {
                            SessionManager.getInstance().getViewFactory().getCurrentWindowProperty().set(CurrentWindow.ADMIN);
                        }
                        else {
                            SessionManager.getInstance().getViewFactory().getCurrentWindowProperty().set(CurrentWindow.SELLER);
                        }

                    }
                    else{
                        AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Wrong password", "");
                    }

                }
            }
        } catch (ClassNotFoundException e ) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "ClassNotFound", "");
        } catch ( SQLException e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "SQL Exception", "");
        }
        finally {
            try {
                if (resultSet != null) resultSet.close();
                if (psSelect != null) psSelect.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ObservableList<Product> getProductListFromDatabase(boolean expired) {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = getConnection();


            statement = connection.createStatement();
            String query;
            if (expired) {
                query = "SELECT * FROM produkty WHERE data_waznosci <= CURDATE()";
            } else {
                query = "SELECT * FROM produkty WHERE data_waznosci > CURDATE()";
            }
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String productName = resultSet.getString("nazwa_produktu");
                LocalDate expirationDate = resultSet.getDate("data_waznosci").toLocalDate();
                String category = resultSet.getString("kategoria");
                int quantity = resultSet.getInt("ilosc");

                Product product = new Product(productName, expirationDate, category, quantity);
                productList.add(product);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {

        }

        return productList;
    }

    public static void deleteProduct(Product product) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = getConnection();


            String query = "DELETE FROM produkty WHERE nazwa_produktu = ? AND data_waznosci = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setDate(2, java.sql.Date.valueOf(product.getExpirationDate()));
            preparedStatement.executeUpdate();
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Delete", "Product deleted", "");


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Deletion failed", "");

        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addProduct(Product product) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = getConnection();


            psUpdate = connection.prepareStatement("UPDATE produkty SET ilosc = ilosc + ? WHERE nazwa_produktu = ? AND data_waznosci = ?");
            psUpdate.setInt(1, product.getQuantity());
            psUpdate.setString(2, product.getProductName());
            psUpdate.setDate(3, Date.valueOf(product.getExpirationDate()));

            int updatedRows = psUpdate.executeUpdate();

            if (updatedRows == 0) {

                psInsert = connection.prepareStatement("INSERT INTO produkty (nazwa_produktu, data_waznosci, kategoria, ilosc) VALUES ( ?, ?, ?, ?)");

                psInsert.setString(1, product.getProductName());
                psInsert.setDate(2, Date.valueOf(product.getExpirationDate()));
                psInsert.setString(3, product.getCategory());
                psInsert.setInt(4, product.getQuantity());
                psInsert.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (psInsert != null) psInsert.close();
                if (psUpdate != null) psUpdate.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static ObservableList<Schedule> getScheduleListForUserFromDatabase(int userId) {
        ObservableList<Schedule> scheduleList = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = getConnection();


            String query = "SELECT * FROM grafik_pracy WHERE id_uzytkownika = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Date dayOfWeek = resultSet.getDate("dzien_tygodnia");
                LocalTime startTime = resultSet.getTime("godzina_rozpoczecia").toLocalTime();
                LocalTime endTime = resultSet.getTime("godzina_zakonczenia").toLocalTime();

                Schedule schedule = new Schedule(userId, dayOfWeek, startTime, endTime);
                scheduleList.add(schedule);


                System.out.println("User ID: " + userId +
                        ", Day of Week: " + dayOfWeek +
                        ", Start Time: " + startTime +
                        ", End Time: " + endTime);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return scheduleList;
    }

    public static ObservableList<User> getUsersFromDatabase() {
        ObservableList<User> users = FXCollections.observableArrayList();

        try {
            Connection connection = getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM uzytkownik");

            while (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String userName = resultSet.getString("imie");
                String userSurname = resultSet.getString("nazwisko");


                users.add(new User(userId, userName, userSurname, "exampleUsername", "examplePassword", false));
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static ObservableList<Integer> getUserIdsFromDatabase() throws SQLException {
        ObservableList<Integer> userIds = FXCollections.observableArrayList();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT id FROM uzytkownik");

            while (resultSet.next()) {
                int userId = resultSet.getInt("id");
                userIds.add(userId);
            }

        } finally {

            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return userIds;
    }

    public static ObservableList<Schedule> getSchedulesFromDatabase() {
        ObservableList<Schedule> schedules = FXCollections.observableArrayList();

        try {
            Connection connection = getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM grafik_pracy");

            while (resultSet.next()) {
                int userId = resultSet.getInt("id_uzytkownika");
                Date dayOfWeek = resultSet.getDate("dzien_tygodnia");
                LocalTime startTime = resultSet.getTime("godzina_rozpoczecia").toLocalTime();
                LocalTime endTime = resultSet.getTime("godzina_zakonczenia").toLocalTime();

                schedules.add(new Schedule(userId, dayOfWeek, startTime, endTime));
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return schedules;
    }

    public static void addScheduleToDatabase(Schedule schedule) {
        try {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO grafik_pracy (id_uzytkownika, dzien_tygodnia, godzina_rozpoczecia, godzina_zakonczenia) VALUES (?, ?, ?, ?)");
            preparedStatement.setInt(1, schedule.getUserId());
            preparedStatement.setDate(2, new java.sql.Date(schedule.getDayOfWeek().getTime()));
            preparedStatement.setTime(3, Time.valueOf(schedule.getStartTime()));
            preparedStatement.setTime(4, Time.valueOf(schedule.getEndTime()));

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void deleteScheduleFromDatabase(Schedule schedule) {
        try {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM grafik_pracy WHERE id_uzytkownika = ?");
            preparedStatement.setInt(1, schedule.getUserId());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Product> getRemanent(boolean expired) {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = getConnection();


            statement = connection.createStatement();
            String query;
            if (expired) {
                query = "SELECT * FROM produkty WHERE data_waznosci <= CURDATE()";
            } else {
                query = "SELECT * FROM produkty WHERE data_waznosci > CURDATE()";
            }
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String productName = resultSet.getString("nazwa_produktu");
                LocalDate expirationDate = resultSet.getDate("data_waznosci").toLocalDate();
                String category = resultSet.getString("kategoria");
                int quantity = resultSet.getInt("ilosc");

                Product product = new Product(productName, expirationDate, category, quantity);
                productList.add(product);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {

        }

        return productList;
    }

}
