package com.example.sklep.controller;

import com.example.sklep.model.DBUtils;
import com.example.sklep.model.SessionManager;
import com.example.sklep.view.CurrentWindow;
import com.example.sklep.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController {

    @FXML
    private Button button_login;

    @FXML
    private Button button_sign_up;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField tf_login;

    @FXML
    private PasswordField tf_password;

    @FXML
    void handleLogin(MouseEvent event) {
        DBUtils.logInUser(
                tf_login.getText(),
                tf_password.getText()
        );
    }

    @FXML
    void handleRegister(MouseEvent event) {
        SessionManager.getInstance().getViewFactory().getCurrentWindowProperty().set(CurrentWindow.REGISTER);
    }

}

