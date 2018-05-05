package com.ase.eu.travel_buddy;

public class UserPreferences {

    private static UserPreferences instance = null;
    private boolean rememberMe = false;
    private String username;
    private String password;

    public static UserPreferences getInstance() {
        if(instance == null) {
            instance = new UserPreferences();
        }
        return instance;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
