package com.fr81.findtherightone;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;

public class Profile extends AppCompatActivity {

    BackendConnection b = new BackendConnection();
    TextView description;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        description = findViewById(R.id.tvDescription);
        sharedPreferences = getBaseContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        String mail = sharedPreferences.getString("PREFS_MAIL", null);
        new Profile.AsyncProfil().execute(mail);
    }


    private class AsyncProfil extends AsyncTask<String, String, String> {

        /**
         * Call server with post request to retrieve all info needed for the connected student
         * @param params take mail of student
         * @return
         */
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            String result = "fail";

            try {
                conn = b.connect("https://tinder.student.elwinar.com/controller/updateprofile.php", "POST");

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

        /**
         * Decode json and get element to feed UI
         * @param result string with all student info
         */
        @Override
        protected void onPostExecute(String result) {

            description.setText(result);

            //pour récupérer un atribut spécifique du student
            /*try {
                JSONObject jsonObject = new JSONObject(result.toString());
                des =  jsonObject.getString("description");
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

        }
    }
}
