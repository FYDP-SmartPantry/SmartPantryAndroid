package com.uwaterloo.smartpantry.data;

public class UserInfo {
    public final static String usernameString = "username";
    public final static String hashString = "hash";

    String username = null;
    String hash = null;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public UserInfo(){};
}
