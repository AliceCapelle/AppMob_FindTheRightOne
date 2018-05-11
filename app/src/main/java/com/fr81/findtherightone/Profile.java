package com.fr81.findtherightone;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;


public class Profile extends AppCompatActivity {

    private BackendConnection b = new BackendConnection();
    private TextView tvDescription;
    private TextView tvName;
    private TextView tvAdjectives;
    private TextView tvMatch;
    private ImageView imgProfile;
    private SharedPreferences sharedPreferences;
    ProgressBar p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        p = findViewById(R.id.pBProfile);
        p.setVisibility(View.VISIBLE);

        tvDescription = findViewById(R.id.tvDescription);
        tvName = findViewById(R.id.tvName);
        tvAdjectives = findViewById(R.id.tvAdjectives);
        tvMatch = findViewById(R.id.tvMatch);
        imgProfile = findViewById(R.id.imgProfile);

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

            try {
                JSONObject json = new JSONObject(result.toString());
                JSONObject student = json.getJSONObject("student");

                String description = student.getString("description");
                tvDescription.setText(description.equals("null") ? "Pas de description" : description);
                tvName.setText(student.getString("surname"));
                String adjectives = student.getString("adj1") + " - " +
                                    student.getString("adj2") + " - " +
                                    student.getString("adj3");
                tvAdjectives.setText(adjectives);
                String match = json.getString("match") + " match(s)";
                tvMatch.setText(match);

                String picPath = student.getString("pic");
                picPath = picPath.replace("\\", "/");
                picPath = picPath.replace("..", "");
                Log.i("PICTURE", "http://tinder.student.elwinar.com" + picPath);

                Picasso.get().load("http://tinder.student.elwinar.com" + picPath).into(imgProfile, new Callback() {
                    @Override
                    public void onSuccess() {
                        p.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
