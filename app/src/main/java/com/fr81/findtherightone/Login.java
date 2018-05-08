package com.fr81.findtherightone;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.mail);
        etPassword = (EditText) findViewById(R.id.password);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                final String mail = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                new AsyncLogin().execute(mail,password);;
                Log.i("mail", mail);
                Log.i("password", password);
            }
        });



    }



    private class AsyncLogin extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... params) {
            final int CONNECTION_TIMEOUT = 10000;
            final int READ_TIMEOUT = 15000;
            HttpURLConnection conn = null;

            URL url = null;
            try {
                url = new URL("https://tinder.student.elwinar.com/controller/login.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mail", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            int response_code = 0;
            try {
                response_code = conn.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuilder result = null;
            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                InputStream input = null;
                try {
                    input = conn.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                result = new StringBuilder();
                String line;

                try {
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                } catch (IOException e) {
                    e.printStackTrace();


                }

            }

            Log.i("Login", "doInBackground: we're done");
            Log.i("Login", result.toString());
            return (result.toString());
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(Login.this, result, Toast.LENGTH_LONG);
            Log.i("Login", "onPostExecute: over");
        }

    }



    }
            /**
             * https://developer.android.com/reference/java/net/HttpURLConnection
             * Method that send username and password to the API.
             * @param username string of the entered username
             * @param password string of the entered password
             * @return Server send us back a string "OK", "FIRST" or "FAIL"
             */
    /*public String postLogin(String username, String password) throws IOException {
        final int CONNECTION_TIMEOUT = 10000;
        final int READ_TIMEOUT = 15000;
        HttpURLConnection conn = null;

        URL url = null;
        try {
            url = new URL("https://tinder.student.elwinar.com/controller/login.php");
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("mail", username)
                    .appendQueryParameter("password", password);
            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        int response_code = conn.getResponseCode();
        StringBuilder result = null;
        // Check if successful connection made
        if (response_code == HttpURLConnection.HTTP_OK) {
            InputStream input = null;
            try {
                input = conn.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }


        }
        return (result.toString());

    }*/

