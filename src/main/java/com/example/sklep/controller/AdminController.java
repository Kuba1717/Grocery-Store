package com.example.sklep.controller;

import com.example.sklep.HelloApplication;
import com.example.sklep.model.DBUtils;
import com.example.sklep.model.Product;
import com.example.sklep.model.SessionManager;
import com.example.sklep.model.User;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.example.sklep.view.CurrentUserWindow.ADDSELLER;
import static com.example.sklep.view.CurrentUserWindow.SCHEDULE;
import static com.example.sklep.view.CurrentWindow.LOGIN;

public class AdminController implements Initializable {

    @FXML
    private Button button_logout;
    @FXML
    private Label label_name;

    @FXML
    private Button button_refresh;

    @FXML
    private Button button_addSeller;

    @FXML
    private Button button_addProduct;

    @FXML
    private TableView<Product> productsTableView;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, LocalDate> expirationDateColumn;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    @FXML
    private TableColumn<Product, Void> deleteColumn;

    private ObservableList<Product> products;
    @FXML
    private BorderPane mainAdminWindow;

    @FXML
    private Button button_remanent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        products = DBUtils.getProductListFromDatabase(false);
        products.addAll(DBUtils.getProductListFromDatabase(true));
        updateLabelNameAndSurname();

        productsTableView.setItems(products);

        productNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));
        expirationDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().expirationDateProperty()));
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        quantityColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getQuantity()));

        setDeleteColumnFactory();
    }

    private void updateLabelNameAndSurname() {
        try {
            User loggedInUser = SessionManager.getInstance().getLoggedInUser();
            if (label_name != null) {
                if (loggedInUser != null) {
                    String loggedInUserName = loggedInUser.getName();
                    String loggedInUserSurname = loggedInUser.getSurname();

                    label_name.setText("Logged in to the manager's account: " + loggedInUserName +" "+loggedInUserSurname);
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
    private void setDeleteColumnFactory() {
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());

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
    private void handleModifySchedule() {
        System.out.println("Modify Schedule clicked");
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("modify-schedule.fxml"));
            Scene scene = new Scene(loader.load());

            Stage addProductStage = new Stage();
            addProductStage.setTitle("Add Product");
            addProductStage.initModality(Modality.APPLICATION_MODAL);
            addProductStage.setScene(scene);
            addProductStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void handleLogout() {
        System.out.println("Admin logout clicked");
        SessionManager.getInstance().setLoggedInUser(null);
        SessionManager.getInstance().getViewFactory().getCurrentWindowProperty().set(LOGIN);

    }

    @FXML
    private void handleAddSeller() {
        System.out.println("Add Seller clicked");
        SessionManager.getInstance().getViewFactory().getCurrentUserWindowProperty().set(ADDSELLER);

    }



    @FXML
    private void handleAddProduct() {
        System.out.println("Add Product clicked");
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("order.fxml"));
            Scene scene = new Scene(loader.load());

            Stage addProductStage = new Stage();
            addProductStage.setTitle("Add Product");
            addProductStage.initModality(Modality.APPLICATION_MODAL);
            addProductStage.setScene(scene);
            addProductStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @FXML
    private void handleRemanent() {
        System.out.println("Remnant clicked");
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("remanent.fxml"));
            Stage remnantStage = new Stage();
            remnantStage.setTitle("Remanent");
            remnantStage.initModality(Modality.APPLICATION_MODAL);
            remnantStage.setScene(new Scene(loader.load()));

            RemanentController remnantController = loader.getController();
            remnantController.initialize();

            remnantStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleRefreshButton(MouseEvent mouseEvent) {
        try {
            ObservableList<Product> updatedProducts = DBUtils.getProductListFromDatabase(false);
            updatedProducts.addAll(DBUtils.getProductListFromDatabase(true));

            products.clear();
            products.addAll(updatedProducts);
            productsTableView.setItems(products);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
