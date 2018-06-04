package com.fr81.findtherightone;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;

public class RegistrationConfirmation extends AppCompatActivity implements  View.OnClickListener{

    protected String mail;
    protected String passwd;
    protected String year;
    protected TextView txtResult;
    //ADD Tibo
    protected TextView tv_Confirmation;
    protected ImageView imgSign_up;
    protected Button bHome;
    protected  Button bLogin;
    protected ProgressBar pBarRegistration;
    //END
    private BackendConnection backend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_confirmation);

        Intent i = getIntent();
        //ADD Tibo
        tv_Confirmation=findViewById(R.id.tv_Confirmation);
        imgSign_up = findViewById(R.id.imgSign_Up);
        pBarRegistration = findViewById(R.id.pBarRegistration);
        pBarRegistration.setVisibility(View.VISIBLE);
        tv_Confirmation.setVisibility(View.INVISIBLE);
        imgSign_up.setVisibility(View.INVISIBLE);
        bHome = findViewById(R.id.bHomeC);
        bLogin = findViewById(R.id.bLoginC);
        bHome.setOnClickListener(this);
        bLogin.setOnClickListener(this);
        //END
        txtResult = (TextView)findViewById(R.id.register_result);
        mail    = i.getStringExtra(Signup.LOGIN);
        passwd  = i.getStringExtra(Signup.PASSWD);
        year    = i.getStringExtra(Signup.YEAR);

        if (backend.isOnline((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
            new RegistrationConfirmation.AsyncRegister().execute();
        } else {
            Toast.makeText(RegistrationConfirmation.this, "No internet access", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bHomeC:
                Intent intentH = new Intent(this, Home.class);
                startActivity(intentH);
                break;
            case R.id.bLoginC:
                Intent intentL = new Intent(this, Login.class);
                startActivity(intentL);
                break;

        }
    }


    private class AsyncRegister extends AsyncTask<String, String, String> {

        /**
         * Request server to add student to db and send email
         * @param params mail, password and year
         * @return
         */
        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection conn = null;
            String result = null;

            try {
                conn = backend.connect("http://skipti.fr/controller/register-confirmation.php", "POST");
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mail", mail)
                        .appendQueryParameter("password", passwd)
                        .appendQueryParameter("year", year);

                String query = builder.build().getEncodedQuery();
                backend.sendData(conn, query);
                result = backend.getData(conn);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("OK")) {
                Intent i = getIntent();
                pBarRegistration.setVisibility(View.INVISIBLE);
                txtResult.setVisibility(View.INVISIBLE);
                String confirmation = "Un email de confirmation a été envoyé à "+
                        i.getStringExtra(Signup.LOGIN)+"@etu.parisdescartes.fr";
                tv_Confirmation.setText(confirmation);
                tv_Confirmation.setVisibility(View.VISIBLE);
                imgSign_up.setVisibility(View.VISIBLE);
            }
            else
                tv_Confirmation.setText("Une erreur est survenue...");
        }
    }

}
