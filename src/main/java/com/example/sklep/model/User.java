package com.example.sklep.model;

public class User {

    private int id;
    private String username;
    private String name;
    private String surname;
    private String password;
    private boolean isAdmin;

    public User(int id, String username, String name, String surname, String password, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }


    public int getId() {return id;}

}