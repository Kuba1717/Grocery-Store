package com.example.sklep.controller;

import com.example.sklep.model.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    @FXML
    private BorderPane mainWindow;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainWindow.setCenter(SessionManager.getInstance().getViewFactory().getLoginWindow());

        SessionManager.getInstance().getViewFactory().getCurrentWindowProperty().addListener((observableValue, oldVal, newVal)-> {
            switch (newVal){
                case LOGIN -> mainWindow.setCenter(SessionManager.getInstance().getViewFactory().getLoginWindow());
                case REGISTER -> mainWindow.setCenter(SessionManager.getInstance().getViewFactory().getRegisterWindow());
                case SELLER -> mainWindow.setCenter(SessionManager.getInstance().getViewFactory().getSellerWindow());
                case ADMIN -> mainWindow.setCenter(SessionManager.getInstance().getViewFactory().getAdminWindow());

            }
        });

    }

}
