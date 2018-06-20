package com.fr81.findtherightone;

import android.content.Context;
import android.content.Intent;
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

import javax.net.ssl.HttpsURLConnection;

/**
 * Class to handle signup
 * Created by acapelle on 09/05/2018.
 */

public class Signup extends AppCompatActivity {

    public static final String LOGIN    = "fr81.signup.login";
    public static final String PASSWD   = "fr81.signup.passwd";
    public static final String YEAR     = "fr81.signup.year";

    private EditText etEmail;
    private EditText etPassword;
    private RadioGroup rgYear;
    private RadioButton rbYear;
    private BackendConnection b;
    private CheckForm c;

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
                String year;
                int selectedId = rgYear.getCheckedRadioButtonId();
                rbYear = findViewById(selectedId);
                if (rbYear.getText().equals("DUT1"))
                    year = "1";
                else
                    year = "2";

                if(c.checkEmail(mail) && c.checkPassword(password)){
                    if (b.isOnline((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
                        new Signup.AsyncSignup(mail, password, year).execute();
                    } else {
                        Toast.makeText(Signup.this, "No internet access", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Signup.this, "Mot de passe ou adresse mail non valide", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class AsyncSignup extends AsyncTask<String, String, String> {

        private String mail;
        private String passwd;
        private String year;

        public AsyncSignup(String mail, String passwd, String year){
            super();
            this.mail = mail;
            this.passwd = passwd;
            this.year = year;
        }

        /**
         * Request server to know if mail is already used or not
         * @param params mail
         * @return
         */
        @Override
        protected String doInBackground(String... params) {

            HttpsURLConnection conn = null;
            String result = null;

            try {
                conn = b.connect("https://skipti.fr/controller/student_exists.php", "POST");
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mail", mail);

                String query = builder.build().getEncodedQuery();
                b.sendData(conn, query);
                result = b.getData(conn);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServerException e) {
                Log.i("Signup", "Dev didn't do his job");
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            result.replace("\n", "");
            if(result.equals("OK")){
                Intent i = new Intent(Signup.this, RegistrationConfirmation.class);
                i.putExtra(Signup.LOGIN, mail);
                i.putExtra(Signup.PASSWD, passwd);
                i.putExtra(Signup.YEAR, year);
                startActivity(i);
            }
            else
                Toast.makeText(Signup.this, "Une erreur est survenue...", Toast.LENGTH_LONG).show();
        }
    }


}

