package com.fr81.findtherightone;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;

public class RegistrationConfirmation extends AppCompatActivity {

    protected String mail;
    protected String passwd;
    protected String year;
    protected TextView txtResult;
    private BackendConnection backend = new BackendConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_confirmation);

        Intent i = getIntent();

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
                conn = backend.connect("https://tinder.student.elwinar.com/controller/register-confirmation.php", "POST");
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
            if(result.equals("OK"))
                txtResult.setText("Un mail a ete envoye a " + mail + " !");
            else
                txtResult.setText("Une erreur est survenue...");
        }
    }

}
