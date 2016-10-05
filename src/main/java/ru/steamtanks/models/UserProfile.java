package ru.steamtanks.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.concurrent.atomic.AtomicLong;

public class UserProfile {
    private static final AtomicLong ID_GENETATOR = new AtomicLong(0);
    private Long id;
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
        this.id = ID_GENETATOR.getAndIncrement();
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
    public Long getId() { return id; }

    public String getLogin() {
        return login;
    }

    public String getEmail() { return email; }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

}
