package com.example.sklep.controller;

import com.example.sklep.controller.command.AddProductCommand;
import com.example.sklep.controller.command.CommandQueue;
import com.example.sklep.model.DBUtils;
import com.example.sklep.model.Product;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderController {

    @FXML
    private TextField productNameField;

    @FXML
    private DatePicker expirationDatePicker;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField quantityField;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<Product> orderedProductsTable;

    private final AdminController adminController;
    private final CommandQueue commandQueue;


    public OrderController() {
        this.adminController = new AdminController();
        this.commandQueue = new CommandQueue();
    }

    @FXML
    public void initialize() {
        initializeTableView();
    }

    private void initializeTableView() {
        TableColumn<Product, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getProductName()));

        TableColumn<Product, LocalDate> expirationDateColumn = new TableColumn<>("Expiration Date");
        expirationDateColumn.setCellValueFactory(param ->  new SimpleObjectProperty<>(param.getValue().getExpirationDate()));

        TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getCategory()));

        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(param ->  new SimpleObjectProperty<>(param.getValue().getQuantity()));

        orderedProductsTable.getColumns().addAll(productNameColumn, expirationDateColumn, categoryColumn, quantityColumn);


        TableColumn<Product, Integer>orderSecondsLeftColumn = new TableColumn<>("Seconds Left");
        orderSecondsLeftColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().orderSecondsLeftProperty()));

        orderSecondsLeftColumn.setCellFactory(column -> new TableCell<Product, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });

        TableColumn<Product, Void> deleteOrderedColumn = new TableColumn<>("Delete");
        deleteOrderedColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    System.out.println("Delete clicked for: " + product.getProductName());

                    orderedProductsTable.getItems().remove(product);
                    commandQueue.cancelCommand(new AddProductCommand(product, 30));
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



        orderedProductsTable.getColumns().addAll(orderSecondsLeftColumn);
        orderedProductsTable.getColumns().addAll(deleteOrderedColumn);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            orderedProductsTable.getItems().forEach(product -> product.orderSecondsLeftProperty());
            orderedProductsTable.refresh();
            orderedProductsTable.getItems().removeIf(product -> product.orderSecondsLeftProperty() <= 0);


        }));

        timeline.setCycleCount(Animation.INDEFINITE);

        timeline.play();

        clearFields();
    }
    @FXML
    public void handleAddProduct() {
        String productName = productNameField.getText();
        LocalDate expirationDate = expirationDatePicker.getValue();
        String category = categoryField.getText();
        int quantity = Integer.parseInt(quantityField.getText());

        Product product = new Product(productName, expirationDate, category, quantity);


        commandQueue.addCommand(new AddProductCommand(product, 30));

        orderedProductsTable.getItems().add(product);

        clearFields();
    }

    @FXML
    public void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    private void clearFields() {
        productNameField.clear();
        expirationDatePicker.setValue(null);
        categoryField.clear();
        quantityField.clear();
    }
}