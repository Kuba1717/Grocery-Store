package com.example.sklep.view;

import com.example.sklep.HelloApplication;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ViewFactory {
    private BorderPane adminWindowView;
    private BorderPane adminUserWindowView;
    private BorderPane sellerWindowView;
    private BorderPane loginWindowView;
    private BorderPane registerWindowView;
    private BorderPane scheduleWindowView;
    private BorderPane addSellerUserWindowView;
    private AnchorPane orderUserView;
    private AnchorPane mainWindow;
    private final ObjectProperty<CurrentWindow> currentWindow;
    private final ObjectProperty<CurrentUserWindow> currentUserWindow;


    public ViewFactory() {
        this.currentWindow = new SimpleObjectProperty<>();
        this.currentUserWindow = new SimpleObjectProperty<>();
    }

    public void showAppWindow(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("app.fxml"));
        createStage(fxmlLoader);
    }

    public BorderPane getSellerWindow() {
        if (sellerWindowView == null) {
            try {
                sellerWindowView = new FXMLLoader(HelloApplication.class.getResource("seller.fxml")).load();

            } catch (Exception e) {
                return null;

            }
        }
        return sellerWindowView;
    }

    public BorderPane getLoginWindow() {
        if (loginWindowView == null) {
            try {
                loginWindowView = new FXMLLoader(HelloApplication.class.getResource("login.fxml")).load();

            } catch (Exception e) {
                return null;

            }
        }
        return loginWindowView;
    }

    public BorderPane getRegisterWindow() {
        if (registerWindowView == null) {
            try {
                registerWindowView = new FXMLLoader(HelloApplication.class.getResource("sign-up.fxml")).load();
            } catch (Exception e) {
                return null;
            }
        }
        return registerWindowView;
    }
    public BorderPane getAdminWindow() {
        if (adminWindowView == null) {
            try {
                adminWindowView = new FXMLLoader(HelloApplication.class.getResource("admin-page.fxml")).load();
            } catch (Exception e) {
                return null;
            }
        }
        return adminWindowView;
    }


    public BorderPane getScheduleWindow() {
        if (scheduleWindowView == null) {
            try {
                scheduleWindowView = new FXMLLoader(HelloApplication.class.getResource("modify-schedule.fxml")).load();

            } catch (Exception e) {
                return null;

            }
        }
        return scheduleWindowView;
    }

    public BorderPane getAddSellerUserWindow() {
        if (addSellerUserWindowView == null) {
            try {
                addSellerUserWindowView = new FXMLLoader(HelloApplication.class.getResource("add-seller.fxml")).load();

            } catch (Exception e) {
                return null;

            }
        }
        return addSellerUserWindowView;
    }

    public AnchorPane getOrderUserWindow() {
        if (orderUserView == null) {
            try {
                orderUserView = new FXMLLoader(HelloApplication.class.getResource("order.fxml")).load();

            } catch (Exception e) {
                return null;

            }
        }
        return orderUserView;
    }



    public void createStage(FXMLLoader fxmlLoader) {
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load());
        }catch(Exception e){
            e.printStackTrace();

        }


        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public ObjectProperty<CurrentWindow> getCurrentWindowProperty() {
        return currentWindow;
    }

    public ObjectProperty<CurrentUserWindow> getCurrentUserWindowProperty() {
        return currentUserWindow;
    }


    public BorderPane getAdminUserWindow() {
        if (adminUserWindowView == null) {
            try {
                adminUserWindowView = new FXMLLoader(HelloApplication.class.getResource("admin.fxml")).load();

            } catch (Exception e) {
                return null;

            }
        }
        return adminUserWindowView;
    }
}
