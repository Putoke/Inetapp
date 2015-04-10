package com.training.superior.superiortraining.Models;

import android.os.AsyncTask;
import android.webkit.CookieManager;

import com.training.superior.superiortraining.Controllers.LoginActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

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
        HttpClient httpClient = new DefaultHttpClient();
        try {
            JSONObject sendJson = new JSONObject();
            sendJson.put("email", mEmail);
            sendJson.put("password", mPassword);


            HttpPost request = new HttpPost("http://u-shell.csc.kth.se:8000/login");

            StringEntity paras = new StringEntity(sendJson.toString());
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(paras);
            HttpResponse response = httpClient.execute(request);

            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));




            String readString, jSonString = "";
            while ( (readString = in.readLine()) != null ) {
                jSonString += readString;
            }
            JSONObject jObject = new JSONObject(jSonString);
            String status = jObject.getString("status");
            int code = jObject.getInt("code");
            if (status.equals("OK") && code == 200) {
                success = true;
            }

            in.close();
        } catch (IOException|JSONException e) {
            e.printStackTrace();
        } finally {
            //Save cookies to cookie store.
            List<Cookie> cookies = ((DefaultHttpClient) httpClient).getCookieStore().getCookies();
            if(cookies != null) {
                for(Cookie cookie : cookies) {
                    String cookieString = cookie.getName() + "=" + cookie.getValue() + "; domain=" + cookie.getDomain();
                    CookieManager.getInstance().setCookie(cookie.getDomain(), cookieString);
                }
            }
            httpClient.getConnectionManager().shutdown();
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