package com.fr81.findtherightone;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class Profile extends AppCompatActivity {

    private BackendConnection b = new BackendConnection();
    private TextView tvDescription;
    private TextView tvName;
    private TextView tvAdjectives;
    private TextView tvMatch;
    private ImageView imgProfile;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

            //pour récupérer un atribut spécifique du student
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

                Picasso.get().load("http://tinder.student.elwinar.com" + picPath).into(imgProfile);

                /*new DownloadImageTask((ImageView) findViewById(R.id.imgProfile))
                        .execute("http://tinder.student.elwinar.com" + picPath);*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {

            Bitmap img = null;


            try {
                URL url = new URL(urls[0]);
                img = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }

            if (img == null)
                Log.i("IMG", "Est null..");
            return img;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }*/

}
