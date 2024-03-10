package com.example.sklep.controller;

import com.example.sklep.model.DBUtils;
import com.example.sklep.model.SessionManager;
import com.example.sklep.view.CurrentUserWindow;
import com.example.sklep.view.CurrentWindow;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class AddSellerController implements Initializable {

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
        sessionManager.getLocalValidator().validatorCheck(tf_name, "^[A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż]+$", "Minimum dwa znaki alfabetu");
        sessionManager.getLocalValidator().validatorCheck(tf_surname, "^[A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż]+$", "Minimum dwa znaki alfabetu");
        sessionManager.getLocalValidator().validatorCheck(tf_login, "^(.{2,})+$", "xd nwm");
        sessionManager.getLocalValidator().validatorCheck(tf_password_first, "^(.*[0-9].*).+$", "minimum jedna cyfra");

        sessionManager.getLocalValidator().passwordValidator(tf_password_first,tf_password_second,"^(.*[0-9].*).+$", "minimum jedna cyfra");

        button_sign_up.disableProperty().bind(sessionManager.getLocalValidator().containsErrorsProperty());

    }
    @FXML
    public void handleGoBack(MouseEvent mouseEvent) {
        SessionManager.getInstance().getViewFactory().getCurrentUserWindowProperty().set(CurrentUserWindow.ADMIN);

    }
    @FXML
    public void handleSignUp(MouseEvent mouseEvent) {

        String hashedPassword = sessionManager.getPasswordHasher().hash(tf_password_first.getText());
        DBUtils.signUpUser(
                tf_login.getText(),
                hashedPassword,
                tf_name.getText(),
                tf_surname.getText(),
                false
        );
        SessionManager.getInstance().getViewFactory().getCurrentWindowProperty().set(CurrentWindow.ADMIN);
    }

}
