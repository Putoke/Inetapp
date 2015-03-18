package com.training.superior.superiortraining.Models;

import android.os.AsyncTask;

import com.training.superior.superiortraining.Controllers.LoginActivity;
import com.training.superior.superiortraining.R;

/**
 * Created by joakim on 15-03-18.
 *
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mEmail;
    private final String mPassword;
    private final LoginActivity activity;

    public UserLoginTask(String email, String password, LoginActivity activity) {
        mEmail = email;
        mPassword = password;
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.


        // TODO: register the new account here.
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        activity.loginResponse(success);
    }

    @Override
    protected void onCancelled() {
        activity.loginCancelled();
    }
}