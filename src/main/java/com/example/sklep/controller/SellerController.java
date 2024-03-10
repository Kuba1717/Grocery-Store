package com.example.sklep.controller;

import com.example.sklep.model.*;
import com.example.sklep.view.CurrentWindow;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.LocalDateStringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class SellerController implements Initializable {

    @FXML
    private Button button_logout;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TableColumn<Product, String> categoryColumnExpired;

    @FXML
    private TableColumn<Product, Void> deleteColumnExpired;

    @FXML
    private TableColumn<Product, LocalDate> expirationDateColumn;

    @FXML
    private TableView<Product> expiredProductsTableView;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, String> productNameColumnExpired;

    @FXML
    private TableView<Product> productsTableView;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    @FXML
    private TableColumn<Product, Integer> quantityColumnExpired;

    @FXML
    private Label sellerStatusLabel;

    @FXML
    private Label label_name;

    @FXML
    private TableColumn<Product, Void> updateColumn;

    @FXML
    private TableView<Schedule> scheduleTableView;

    @FXML
    private TableColumn<Schedule, Date> dayScheduleColumn;
    @FXML
    private TableColumn<Schedule, LocalTime> startScheduleColumn;

    @FXML
    private TableColumn<Schedule, LocalTime> endScheduleColumn;

    private SessionManager sessionManager = SessionManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        sellerStatusLabel.setText("Welcome, Seller!");

        updateLabelNameAndSurname();

        ObservableList<Product> products = DBUtils.getProductListFromDatabase(false);
        ObservableList<Product> expiredProducts = DBUtils.getProductListFromDatabase(true);




        productsTableView.setItems(products);
        expiredProductsTableView.setItems(expiredProducts);

        productNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));
        expirationDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().expirationDateProperty()));
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        quantityColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getQuantity()));


        productNameColumnExpired.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));
        categoryColumnExpired.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        quantityColumnExpired.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getQuantity()));

        loadScheduleDataForCurrentUser();



        setUpdateColumnFactory();
        setDeleteColumnFactory();
    }

    private void setUpdateColumnFactory() {
        updateColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");

            {
                updateButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    System.out.println("Update clicked for: " + product.getProductName());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });
    }

    @FXML
    private void setDeleteColumnFactory() {
        deleteColumnExpired.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    System.out.println("Delete clicked for: " + product.getProductName());

                    DBUtils.deleteProduct(product);

                    getTableView().getItems().remove(product);

                    getTableView().refresh();

                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }
    @FXML
    void handleLogout(MouseEvent event) {
        sessionManager.setLoggedInUser(null);
        sessionManager.getViewFactory().getCurrentWindowProperty().set(CurrentWindow.LOGIN);


    }
    private void updateLabelNameAndSurname() {
        try {
            User loggedInUser = SessionManager.getInstance().getLoggedInUser();
            if (label_name != null) {
                if (loggedInUser != null) {
                    String loggedInUserName = loggedInUser.getName();
                    String loggedInUserSurname = loggedInUser.getSurname();

                    label_name.setText("Logged in to the seller's account: " + loggedInUserName +" "+loggedInUserSurname);
                } else {
                    label_name.setText("Not logged in");
                }
            } else {
                System.err.println("label_name is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void loadScheduleDataForCurrentUser() {
        User loggedInUser = SessionManager.getInstance().getLoggedInUser();

        if (loggedInUser != null) {
            ObservableList<Schedule> scheduleData = DBUtils.getScheduleListForUserFromDatabase(loggedInUser.getId());

            if (scheduleTableView != null) {

                scheduleTableView.setItems(null);

                scheduleTableView.setItems(scheduleData);

                dayScheduleColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDayOfWeek()));
                startScheduleColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStartTime()));
                endScheduleColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEndTime()));
            }
        }
    }















}

