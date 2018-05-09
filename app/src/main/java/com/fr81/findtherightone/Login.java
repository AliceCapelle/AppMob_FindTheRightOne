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

// TODO: 09/05/2018 Write log in external file
public class Login extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;


    /**
     * Main UI thread. We set the view, and get the text entered when the button
     * is clicked. We launch an asynctask (a thread), who run in the background and connect to
     * database
     * @param savedInstanceState
     */
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
            }
        });



    }


    /**
     * Inner class who extend AsyncTask. Start a thread when called.
     */
    private class AsyncLogin extends AsyncTask<String, String, String>
    {

        /**
         * First methode called when class is called. Performs task in the background.
         * Here, she send a post request to our server, in order to login.
         * She then retrieve string send with echo by php.
         * @param params username and password entered by user.
         * @return string of server answer.
         */
        @Override
        protected String doInBackground(String... params) {

            BackendConnection b = new BackendConnection();
            HttpURLConnection conn = null;

            try {
                conn = b.connect("https://tinder.student.elwinar.com/controller/login.php","POST");
                Log.i("doinbackground", "doInBackground: connect is over");
            } catch (IOException e) {
                e.printStackTrace();
            }

            try{
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mail", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();

                //de là
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                //à la, on oeut faire une méthode (envoyer les infos, prend conn en paramètre, void)

            } catch (MalformedURLException e) {
                Toast.makeText(Login.this, "Bad codding :  URL of server's page not " +
                        "working", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            } catch (IOException e) {
                Toast.makeText(Login.this, "failed or interrupted I/O operations",
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            //de là
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
            //à la, fonction (prend conn en paramètre, renvoit string

            Log.i("Login", "doInBackground: we're done");
            Log.i("Login", result.toString());
            return (result.toString());
        }

        /**
         * Method execute once doInBackground is done.
         * Does not require calling.
         * @param result Responce receive from the server, either "OK", "FIRST" or "FAIL"
         */
        // TODO: 09/05/2018  if "OK", we lauch swipe, if "FIRST", we lauch adj, if "FAIL", we display error
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(Login.this, result, Toast.LENGTH_LONG).show();
            Log.i("Login", "onPostExecute: over");
        }

    }



}



