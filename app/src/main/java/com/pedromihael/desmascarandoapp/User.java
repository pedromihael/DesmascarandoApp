package com.pedromihael.desmascarandoapp;

public class User {

    private String name;
    private String email;
    private String password;
    private String user_id;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String id) {
        this.user_id = id;
    }

    public User() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getUser_id() { return user_id; }

    public void setUser_id(String user_id) { this.user_id = user_id; }


}
