package com.pedromihael.desmascarandoapp.data;

import android.app.Activity;
import android.content.Context;

import com.pedromihael.desmascarandoapp.DatabaseHelper;
import com.pedromihael.desmascarandoapp.User;
import com.pedromihael.desmascarandoapp.data.model.LoggedInUser;
import com.pedromihael.desmascarandoapp.ui.login.LoginActivity;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password, Context context) {

        DatabaseHelper dbHelper = new DatabaseHelper(context);

        try {
            // TODO: handle loggedInUser authentication

            User user = new User(username, password);
            if (dbHelper.getUser(user)) {
                Integer userId = dbHelper.getUserID(user);
                String userName = dbHelper.getUserName(user);
                LoggedInUser authUser = new LoggedInUser(userId.toString(), userName);
                return new Result.Success<>(authUser);
            } else {
                // nao tem user ou senha errada
                return new Result.Error(new IOException("Error logging in"));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}