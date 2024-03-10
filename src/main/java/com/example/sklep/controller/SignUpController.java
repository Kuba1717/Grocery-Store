package com.example.sklep.controller;
import com.example.sklep.model.DBUtils;
import com.example.sklep.model.PasswordHasher;
import com.example.sklep.model.SessionManager;
import com.example.sklep.view.CurrentWindow;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.input.MouseEvent;
import net.synedra.validatorfx.Validator;

public class SignUpController implements Initializable {


    @FXML
    private Button button_go_back;

    @FXML
    private Button button_sign_up;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField tf_login;

    @FXML
    private TextField tf_name;

    @FXML
    private PasswordField tf_password_first;

    @FXML
    private PasswordField tf_password_second;

    @FXML
    private TextField tf_surname;

    private SessionManager sessionManager = SessionManager.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sessionManager.getLocalValidator().validatorCheck(tf_name, "^[A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż]+$", "At least two alphabetic characters (no special characters)");
        sessionManager.getLocalValidator().validatorCheck(tf_surname, "^[A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż]+$", "At least two alphabetic characters (no special characters)");
        sessionManager.getLocalValidator().validatorCheck(tf_login, "^(.{2,})+$", "At least two characters");
        sessionManager.getLocalValidator().validatorCheck(tf_password_first, "^(.*[0-9].*).+$", "At least two digits");

        sessionManager.getLocalValidator().passwordValidator(tf_password_first,tf_password_second,"^(.*[0-9].*).+$", "At least two digits");

        button_sign_up.disableProperty().bind(sessionManager.getLocalValidator().containsErrorsProperty());

    }
    @FXML
    public void handleGoBack(MouseEvent mouseEvent) {
        SessionManager.getInstance().getViewFactory().getCurrentWindowProperty().set(CurrentWindow.LOGIN);

    }
    @FXML
    public void handleSignUp(MouseEvent mouseEvent) {

        String hashedPassword = sessionManager.getPasswordHasher().hash(tf_password_first.getText());
        DBUtils.signUpUser(
                tf_login.getText(),
                hashedPassword,
                tf_name.getText(),
                tf_surname.getText(),
                true
        );
    }






}



