package com.example.sklep.model;

import com.example.sklep.utilities.LocalValidator;
import com.example.sklep.view.ViewFactory;

public class SessionManager {
    private static SessionManager instance;
    private User loggedInUser;
    private final LocalValidator localValidator = new LocalValidator();
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public LocalValidator getLocalValidator() {
        return localValidator;
    }

    private final ViewFactory viewFactory;
    private final PasswordHasher passwordHasher;


    private SessionManager(){
        this.viewFactory = new ViewFactory();
        this.passwordHasher = new PasswordHasher();

    }

    public PasswordHasher getPasswordHasher() {
        return passwordHasher;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
