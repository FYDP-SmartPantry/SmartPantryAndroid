
package com.uwaterloo.smartpantry.data;

import com.uwaterloo.smartpantry.data.model.LoggedInUser;
import com.uwaterloo.smartpantry.user.User;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    // FIXME: Setup Test User for login and activity transition
    // TODO: This feature needs to pull the login info from application server or local database to complete the login.
    final String defaultUsername = "test";
    final String defaultPassword = "test123";

    public LoginDataSource() {
    }

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            // LoggedInUser fakeUser = new LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe");
            LoggedInUser defaultUser = new LoggedInUser(username, "test");

            if (User.login(username, password) == true) {
                System.out.println(username);
                return new Result.Success<>(defaultUser);
            }  else {
                return new Result.Failure<>(defaultUser);
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}