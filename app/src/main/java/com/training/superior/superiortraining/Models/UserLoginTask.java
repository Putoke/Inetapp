package com.training.superior.superiortraining.Models;

import android.os.AsyncTask;

import com.training.superior.superiortraining.Controllers.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

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
        boolean success = false;
        try {

            URL url = new URL("http://u-shell.csc.kth.se:8000/login/" + mEmail + "/" + mPassword);
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String readString, jSonString = "";
            while ( (readString = br.readLine()) != null ) {
                jSonString += readString;
            }
            JSONObject jObject = new JSONObject(jSonString);
            String status = jObject.getString("status");
            int code = jObject.getInt("code");
            if (status.equals("OK") && code == 200) {
                success = true;
            }

        } catch (IOException|JSONException e) {
            e.printStackTrace();
        }

        return success;
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