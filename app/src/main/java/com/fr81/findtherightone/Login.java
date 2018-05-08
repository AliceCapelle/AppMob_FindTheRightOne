package com.fr81.findtherightone;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login {



    /**
     * https://developer.android.com/reference/java/net/HttpURLConnection
     * Method that send username and password to the API.
     * @param username string of the entered username
     * @param password string of the entered password
     * @return Server send us back a string "OK", "FIRST" or "FAIL"
     */
    public String postLogin(String username, String password){

        URL url = null;
        try {
            url = new URL("https://tinder.student.elwinar.com/controller/login.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject json = new JSONObject();

        return "BITE";
    }
}
