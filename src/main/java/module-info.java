module com.example.sklep {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires de.mkammerer.argon2.nolibs;
    requires net.synedra.validatorfx;


    opens com.example.sklep to javafx.fxml;
    opens com.example.sklep.model to javafx.fxml;
    opens com.example.sklep.controller to javafx.fxml;
    opens com.example.sklep.view to javafx.fxml;
    opens  com.example.sklep.utilities to javafx.fxml;

    exports com.example.sklep;
    exports com.example.sklep.view;
    exports com.example.sklep.controller;
    exports com.example.sklep.model;
    exports com.example.sklep.utilities;
    exports com.example.sklep.controller.command;
    opens com.example.sklep.controller.command to javafx.fxml;

}