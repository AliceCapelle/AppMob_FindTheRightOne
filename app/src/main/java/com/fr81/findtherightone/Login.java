package com.fr81.findtherightone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;

// TODO: 09/05/2018 Write log in external file
public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText etEmail;
    private EditText etPassword;
    private String userMail;
    private Button buttonLogin;
    private Button buttonHome;
    private Button buttonSignUp;
    private Button buttonFP;
    private ImageView loginLogo;
    private BackendConnection b;
    private static final String PREFS_MAIL = "PREFS_MAIL";
    private static final String PREFS_CO = "PREFS_CO";
    SharedPreferences sharedPreferences;
    private static final String PREFS = "PREFS";
    private CheckBox cbStayConnected;
    private Boolean stayConnected = false;

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

        etEmail = findViewById(R.id.etMailL);
        etPassword = findViewById(R.id.etPasswordL);

        cbStayConnected = findViewById(R.id.cbStayConnected);

        buttonLogin = findViewById(R.id.okLoginButton);
        buttonHome = findViewById(R.id.bHomeL);
        buttonSignUp = findViewById(R.id.bSignupL);
        buttonFP = findViewById(R.id.bPwdForgot);

        buttonLogin.setOnClickListener(this);
        buttonHome.setOnClickListener(this);
        buttonSignUp.setOnClickListener(this);
        buttonFP.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okLoginButton:
                final String mail = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                userMail = mail;
                if (b.isOnline((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
                    new AsyncLogin().execute(mail, password);
                } else {
                    Toast.makeText(Login.this, "No internet access", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bHomeL:
                Intent home = new Intent(this, Home.class);
                startActivity(home);
                break;
            case R.id.bSignupL:
                Intent signup = new Intent(this, Signup.class);
                startActivity(signup);
                break;
            case R.id.bPwdForgot :
                Intent forgotPwd = new Intent(this, PasswordForgoten.class);
                startActivity(forgotPwd);
                break;
        }
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


            HttpURLConnection conn = null;
            String result = null;

            try {
                conn = b.connect("http://skipti.fr/controller/login.php", "POST");
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
        // TODO: 09/05/2018  if "OK", we launch swipe, if "FIRST", we lauch adj, if "FAIL", we display error
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("OK") || result.equals("FIRST")){
                sharedPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);
                if(cbStayConnected.isChecked())
                    stayConnected = true;
                if (!sharedPreferences.contains(PREFS_MAIL)) {
                    sharedPreferences
                            .edit()
                            .putString(PREFS_MAIL, userMail)
                            .putBoolean(PREFS_CO, stayConnected)
                            .apply();

                }
                else{
                    getSharedPreferences(PREFS, 0).edit().clear().commit();
                    sharedPreferences
                            .edit()
                            .putString(PREFS_MAIL, userMail)
                            .putBoolean(PREFS_CO, stayConnected)
                            .apply();
                }
                Intent i = new Intent(Login.this, Swipe.class);
                startActivity(i);
            }
            else if(result.equals("FAIL")){
                Toast.makeText(Login.this, "Adresse mail ou mot de passe incorrect" , Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(Login.this, "Oops, le serveur à un probleme" , Toast.LENGTH_LONG).show();
            }

        }

    }


}



