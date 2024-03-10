package com.example.sklep;

import com.example.sklep.model.SessionManager;
import javafx.application.Application;
import javafx.stage.Stage;


import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SessionManager.getInstance().getViewFactory().showAppWindow();

    }

    public static void main(String[] args) {
        launch();

    }
}