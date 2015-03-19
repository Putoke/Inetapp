package com.training.superior.superiortraining.Models;

import android.os.AsyncTask;
import android.webkit.CookieManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by joakim on 15-03-18.
 *
 */
public class ScheduleTask extends AsyncTask<Void, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Void... params) {

        boolean success;
        HttpClient httpClient = new DefaultHttpClient();

        //Load saved cookies from cookie store.
        String[] keyValueSets = CookieManager.getInstance().getCookie("http://u-shell.csc.kth.se:8000").split(";");
        for(String cookie : keyValueSets)
        {
            System.out.println(cookie.toString());
            String[] keyValue = cookie.split("=");
            String key = keyValue[0];
            String value = "";
            if(keyValue.length>1) value = keyValue[1];
            ((DefaultHttpClient)httpClient).getCookieStore().addCookie(new BasicClientCookie(key, value));
        }

        try {
            HttpPost request = new HttpPost("http://u-shell.csc.kth.se:8000/login");

            request.addHeader("content-type", "application/x-www-form-urlencoded");

            HttpResponse response = httpClient.execute(request);

            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String readString, jSonString = "";
            while ( (readString = in.readLine()) != null ) {
                jSonString += readString;
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return false;
    }

    @Override
    protected void onPostExecute(final Boolean success) {

    }

    @Override
    protected void onCancelled() {

    }
}
