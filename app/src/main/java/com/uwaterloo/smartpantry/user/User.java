package com.uwaterloo.smartpantry.user;

import com.uwaterloo.smartpantry.datalink.DataLinkREST;

import java.sql.Timestamp;

public class User {
    private String username;
    private String userID;
    private String userEmail;
    private String userpassword;

    // FIXME do we handle salt in local or cloud?
    private String salt;
    private Timestamp timestamp;

// TODO will be removed later
    final static String defaultUsername = "test";
    final static String defaultPassword = "test123";
//
    private static User currentUserInstance = null;

    protected User(String user_name, String user_ID, String user_email, String user_password) {
        username = user_name;
        userID = user_ID;
        userEmail = user_email;
        userpassword = user_password;
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public static boolean login(String username, String password) {
        if (username.equalsIgnoreCase(defaultUsername) && password.equals(defaultPassword)) {
            // last saved session
            currentUserInstance = new User(username, "123", "asd", password);
            System.out.println("login success");
            return true;
        } else {
            //query from cloud.
            return false;
        }
    }

    public static User getCurrentUser() {
        if (currentUserInstance != null) {
            return currentUserInstance;
        } else {
            //this should never happen
            return null;
        }
    }

    public static User createUser(String user_name, String user_ID, String user_email, String user_password) {
        if (isUserIDUnique(user_ID)) {
            currentUserInstance = new User(user_name, user_ID, user_email, user_password);

            // Register on Cloud side
            DataLinkREST.CreateUser(currentUserInstance);

            return currentUserInstance;
        } else {
            // User ID is not unique. need a different user ID for register
            return null;
        }
    }

    private static boolean isUserIDUnique(String userId) {
        return true;
    }


    public void saveLoginUser(User user) {
        //should save last used user in the local before close app

    }

    public String getUserId() {
        return userID;
    }

    public String getDisplayName() {
        return username;
    }
}
