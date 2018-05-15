package com.fr81.findtherightone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

public class Swipe extends AppCompatActivity implements View.OnClickListener {

    Button bProfile;
    ImageButton bLike;
    ImageButton bDislike;
    FragmentProfile profileF = null;
    Bundle b;
    BackendConnection back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        bProfile = findViewById(R.id.bProfileS);
        bLike = findViewById(R.id.like);
        bDislike = findViewById(R.id.dislike);

        bProfile.setOnClickListener(this);
        bLike.setOnClickListener(this);
        bDislike.setOnClickListener(this);

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        String mail = sharedPreferences.getString("PREFS_MAIL", null);

        new Swipe.AsyncSwipe().execute(mail);

        profileF = new FragmentProfile();

        profileF.setArguments(b);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.profileFragment, profileF)
                .commit();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bProfileS:
                Intent profile = new Intent(this, Profile.class);
                startActivity(profile);
                break;
            /*case R.id.dislike:
                profileF.setArguments(b);
                getSupportFragmentManager().beginTransaction().
                        remove(getSupportFragmentManager().findFragmentById(R.id.profileFragment)).commit();
                break;
            case R.id.like:
                profileF = new FragmentProfile();
                profileF.setArguments(b);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.profileFragment, profileF)
                        .commit();
                break;*/
        }
    }

    private class AsyncSwipe extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            String result = "fail";

            try {
                conn = back.connect("https://tinder.student.elwinar.com/controller/swipe.php", "POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mail", params[0]);

                String query = builder.build().getEncodedQuery();
                back.sendData(conn, query);

                result = back.getData(conn);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("RESULTAT REQUETE SWIPE", result);
            try {
                JSONObject json = new JSONObject(result.toString());
                Log.i("RESULTAT REQUETE SWIPE", json.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }



        public void setNewProfile(Bundle b){
            profileF = new FragmentProfile();
            profileF.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.profileFragment, profileF)
                    .commit();

    }
}
