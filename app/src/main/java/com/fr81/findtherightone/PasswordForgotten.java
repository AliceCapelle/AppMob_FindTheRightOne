package com.fr81.findtherightone;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Class to handle forgotten password
 * Created by acapelle on 04/06/2018.
 */

public class PasswordForgotten extends AppCompatActivity implements View.OnClickListener {

    private EditText mail;
    private Button passwordForgotButton;
    private Button homeButton;
    private Button loginButton;
    private BackendConnection b;
    private TextView hintEmailFP;
    private TextView tvSuccesFP;
    private TextView descartesMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgoten_password);

        mail = findViewById(R.id.etMailFP);
        passwordForgotButton = findViewById(R.id.okForgotButton);
        homeButton = findViewById(R.id.bHomeFP);
        loginButton = findViewById(R.id.bLoginFP);
        hintEmailFP = findViewById(R.id.hintEmailFP);
        tvSuccesFP = findViewById(R.id.tvSuccesFP);
        descartesMail = findViewById(R.id.descartesMail);

        passwordForgotButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.okForgotButton:
                new AsyncForgotPassword().execute();
                break;
            case R.id.bHomeFP:
                Intent home = new Intent(this, Home.class);
                startActivity(home);
                break;
            case R.id.bLoginFP:
                Intent signup = new Intent(this, Signup.class);
                startActivity(signup);
                break;
        }

    }

    private class AsyncForgotPassword extends AsyncTask<String, String, String> {

        /**
         * Request server to know if mail is already used or not
         * @param params mail
         * @return
         */
        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection conn = null;
            String result = null;

            try {
                conn = b.connect("http://skipti.fr/controller/forgot_passwd.php", "POST");
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mail", mail.getText().toString());

                String query = builder.build().getEncodedQuery();
                b.sendData(conn, query);
                result = b.getData(conn);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServerException e) {
                Log.i("Password forgotten", "Dev didn't do his job");
            }
            return result;
        }

        /**
         * Display in front that email have been sent
         * @param result echo from server
         */
        @Override
        protected void onPostExecute(String result) {
            mail.setVisibility(View.INVISIBLE);
            passwordForgotButton.setVisibility(View.INVISIBLE);
            hintEmailFP.setVisibility(View.INVISIBLE);
            descartesMail.setVisibility(View.INVISIBLE);
            tvSuccesFP.setVisibility(View.VISIBLE);

            Toast.makeText(PasswordForgotten.this, "Email envoyé !", Toast.LENGTH_LONG).show();
        }
    }
}
