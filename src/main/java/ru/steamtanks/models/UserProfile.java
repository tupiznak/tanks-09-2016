package ru.steamtanks.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserProfile {
    private Integer id;
    private String login;
    private String email;
    private String password;

    public UserProfile(
            String login,
            String password,
            String email) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.id = -1;
    }

    public UserProfile(
            String login,
            String password,
            String email,
            Integer id) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

}
