package com.uwaterloo.smartpantry.user;

import com.uwaterloo.smartpantry.datalink.DataLinkREST;
import com.uwaterloo.smartpantry.inventory.ShoppingList;

import java.sql.Timestamp;

public class   User {
    private String username;
    private String userID;
    private String userEmail;

    private static User instance;

    public User(){}

    public static synchronized User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }


    // FIXME do we handle salt in local or cloud?
    private String salt;
    private Timestamp timestamp;

// TODO will be removed later
    final static String defaultUsername = "test";
    final static String defaultPassword = "test123";
//
    private static User currentUserInstance = null;




    public void setUserInfo(String userId, String userEmail, String displayedName) {
        this.userEmail = userEmail;
        this.userID = userId;
        this.username = displayedName;
    }
}
