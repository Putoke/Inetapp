package com.training.superior.superiortraining.Models;

import android.os.AsyncTask;
import android.webkit.CookieManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by joakim on 4/12/15.
 */
public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

    String name, lastname, email, password;

    public UserRegisterTask(String name, String lastname, String email, String password) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean success = false;
        HttpClient httpClient = new DefaultHttpClient();

        //Load saved cookies from cookie store.
        String[] keyValueSets = CookieManager.getInstance().getCookie(Misc.ServerAddress).split(";");


        String sessionCookie = "";
        for(String cookie : keyValueSets)
        {
            if(cookie.contains("session"))
                sessionCookie = cookie;
            System.out.println(cookie.toString());
        }

        JSONArray jsonArray = new JSONArray();
        JSONObject sendJson = new JSONObject();
        try {
            sendJson.put("Name", name);
            sendJson.put("Lastname", lastname);
            sendJson.put("Email", email);
            sendJson.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            HttpPost request = new HttpPost(Misc.ServerAddress + "/register");

            request.addHeader("Cookie", sessionCookie);
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return success;
    }
}
