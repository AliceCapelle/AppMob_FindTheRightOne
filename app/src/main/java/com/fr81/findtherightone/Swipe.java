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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;

public class Swipe extends AppCompatActivity implements View.OnClickListener {

    Button bProfile;
    ImageButton bLike;
    ImageButton bDislike;
    FragmentProfile profileF = null;
    Bundle b;
    BackendConnection back;
    Array student;
    int cpt;


    //Mettre json dans tableau, garder le numéro du student affiché dans cpt.
    //On execute les methode buildbunder et setnewprofile une fois dans onpostexcute
    //puis à chaque fois qu'on clique sur un button

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

        /*
        setNewProfile(buildBundle());
         */

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
                //setNewProfile(buildBundle());
                profileF.setArguments(b);
                getSupportFragmentManager().beginTransaction().
                        remove(getSupportFragmentManager().findFragmentById(R.id.profileFragment)).commit();
                break;
            case R.id.like:
                //setNewProfile(buildBundle());
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
                JSONArray json =new JSONArray(result.toString());
                Log.i("RESULTAT REQUETE SWIPE", json.toString());
                json.get(1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


        public Bundle buildBundle(String name, String adjs, String description){
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("ajds", adjs);
            bundle.putString("description", description);
            return bundle;
        }

        public void setNewProfile(Bundle b){
            profileF = new FragmentProfile();
            profileF.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.profileFragment, profileF)
                    .commit();
    }
}
