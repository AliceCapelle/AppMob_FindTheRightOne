package com.fr81.findtherightone;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by acapelle on 04/06/2018.
 */

public class PasswordForgoten extends AppCompatActivity implements View.OnClickListener {

    private EditText mail;
    private Button passwordForgotButton;
    private Button homeButton;
    private Button loginButton;
    private BackendConnection b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgoten_password);

        mail = findViewById(R.id.etMailFP);
        passwordForgotButton = findViewById(R.id.okForgotButton);
        homeButton = findViewById(R.id.bHomeFP);
        loginButton = findViewById(R.id.bLoginFP);

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
         *
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
            }
            return result;
        }

        //todo : ajouter echo dans le php, afficher page si succes ou toast si fail
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(PasswordForgoten.this, "Email envoyé !", Toast.LENGTH_LONG).show();
        }
    }
}
