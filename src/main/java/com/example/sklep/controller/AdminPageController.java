package com.example.sklep.controller;

import com.example.sklep.model.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

import com.example.sklep.view.CurrentUserWindow;


public class AdminPageController implements Initializable {

    @FXML
    private BorderPane mainAdminWindow;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainAdminWindow.setCenter(SessionManager.getInstance().getViewFactory().getAdminUserWindow());

        SessionManager.getInstance().getViewFactory().getCurrentUserWindowProperty().addListener((observableValue, oldVal, newVal)-> {
            switch (newVal){
                case ADMIN ->  mainAdminWindow.setCenter(SessionManager.getInstance().getViewFactory().getAdminUserWindow());
                case ADDSELLER ->  mainAdminWindow.setCenter(SessionManager.getInstance().getViewFactory().getAddSellerUserWindow());

            }
        });

    }

}
