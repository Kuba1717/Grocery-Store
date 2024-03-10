package com.example.sklep.controller;

import com.example.sklep.model.DBUtils;
import com.example.sklep.model.Product;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class RemanentController {

    @FXML
    private TableView<Product> remnantTableView;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    @FXML
    private Button refreshButton;

    private ObservableList<Product> remnantData = FXCollections.observableArrayList();

    public void initialize() {
        productNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));
        quantityColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getQuantity()));

        loadRemnantData();

        remnantTableView.setItems(remnantData);
    }

    @FXML
    private void handleRefreshRemnant() {
        loadRemnantData();
    }

    private void loadRemnantData() {
        remnantData.clear();
        remnantData.addAll(DBUtils.getProductListFromDatabase(true));
        remnantData.addAll(DBUtils.getProductListFromDatabase(false));
    }
}
