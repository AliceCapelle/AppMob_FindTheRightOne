package com.fr81.findtherightone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by acapelle on 09/05/2018.
 */

public class Signup extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private RadioGroup rgYear;
    private RadioButton rbYear;
    private BackendConnection b = new BackendConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etEmail = findViewById(R.id.etMailSU);
        etPassword = findViewById(R.id.etPasswordSU);
        rgYear = findViewById(R.id.radioGroupYear);


        Button button = findViewById(R.id.okSignupButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                final String year;
                int selectedId = rgYear.getCheckedRadioButtonId();
                rbYear = findViewById(selectedId);
                if (rbYear.getText().equals("DUT1"))
                    year = "1";
                else
                    year = "2";
                if (b.isOnline((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
                    new Signup.AsyncSignup().execute(mail, password, year);
                } else {
                    Toast.makeText(Signup.this, "No internet access", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private class AsyncSignup extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection conn = null;
            String result = null;

            try {
                conn = b.connect("https://tinder.student.elwinar.com/controller/student_exists.php", "POST");
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mail", params[0]);

                String query = builder.build().getEncodedQuery();
                b.sendData(conn, query);
                result = b.getData(conn);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(Signup.this, result, Toast.LENGTH_LONG).show();
        }
    }


}

