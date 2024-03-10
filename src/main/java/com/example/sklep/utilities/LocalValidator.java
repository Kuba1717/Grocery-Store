package com.example.sklep.utilities;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import net.synedra.validatorfx.Validator;

public class LocalValidator {
    Validator validator = new Validator();

    public void validatorCheck(TextField field, String regex, String description) {
        this.validator.createCheck()
                .dependsOn("field", field.textProperty())
                .withMethod(c -> {
                    String field1 = c.get("field");
                    if (!field1.matches(regex)) {
                        c.error(description);
                    }
                })
                .decorates(field)
                .immediate();


    }

    public void passwordValidator(PasswordField field, PasswordField field2, String regex, String description) {
        this.validator.createCheck()
                .dependsOn("field", field.textProperty()).dependsOn("field2", field2.textProperty())
                .withMethod(c -> {
                    String text1 = c.get("field");
                    String text2 = c.get("field2");
                    if (!(text1.matches(regex) && text1.equals(text2))) {
                        c.error(description);
                    }
                })
                .decorates(field2)
                .immediate();


    }

    public ObservableValue<Boolean> containsErrorsProperty() {
        return validator.containsErrorsProperty();
    }
}