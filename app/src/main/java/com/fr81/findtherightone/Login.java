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

        etEmail = (EditText) findViewById(R.id.etMailL);
        etPassword = (EditText) findViewById(R.id.etPasswordL);

        Button button = (Button) findViewById(R.id.okLoginButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = etEmail.getText().toString();
                final String password = etPassword.getText().toString();

                // TODO: 09/05/2018 check if connected to network 
                
                new AsyncLogin().execute(mail, password);
                
            }
        });
    }


    /**********************************************************************************************/
    /**
     * Inner class who extend AsyncTask. Start a thread when called.
     */
    private class AsyncLogin extends AsyncTask<String, String, String> {

        /**
         * First methode called when class is called. Performs task in the background.
         * Send a post request to our server, in order to login
         * @param params username and password entered by user
         * @return string of server answer.
         */
        @Override
        protected String doInBackground(String... params) {

            BackendConnection b = new BackendConnection();
            HttpURLConnection conn = null;
            String result = null;

            try {
                conn = b.connect("https://tinder.student.elwinar.com/controller/login.php", "POST");
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mail", params[0])
                        .appendQueryParameter("password", params[1]);

                String query = builder.build().getEncodedQuery();
                b.sendData(conn, query);
                result = b.getData(conn);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
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



