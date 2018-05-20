package com.fr81.findtherightone;

import android.app.Notification;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;

// TODO: 17/05/18 on affiche fragment plus de profile si numberOfStundent = 0 

public class Swipe extends AppCompatActivity implements View.OnClickListener {

    private Button bProfile;
    private ImageButton bLike;
    private ImageButton bDislike;
    private FragmentProfile profileF = null;
    private Bundle b;
    private BackendConnection back;
    private int cpt = 0;
    private int numberOfStundent = 0;
    private JSONArray arrayStudnent;
    private JSONObject student;
    private String mail;
    private boolean fin = false;
    private String picStudent;
    private ImageView sadStudent;
    private TextView tvNoProfile;
    private Button bMoreProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        sadStudent = findViewById(R.id.ivSadStudent);
        tvNoProfile = findViewById(R.id.tvNoProfile);
        bMoreProfile = findViewById(R.id.bMoreProfile);

        bProfile = findViewById(R.id.bProfileS);
        bLike = findViewById(R.id.like);
        bDislike = findViewById(R.id.dislike);

        bProfile.setOnClickListener(this);
        bLike.setOnClickListener(this);
        bDislike.setOnClickListener(this);
        bMoreProfile.setOnClickListener(this);

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        mail = sharedPreferences.getString("PREFS_MAIL", null);


        new Swipe.AsyncSwipe().execute(mail);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bProfileS:
                Intent profile = new Intent(this, Profile.class);
                startActivity(profile);
                break;
            case R.id.dislike:
                arrayStudnent.remove(arrayStudnent.length() - 1);
                if (arrayStudnent.length() > 0) {
                    b = buildBundle();
                    setNewProfile(b);
                } else {
                    fin = true;
                    getSupportFragmentManager().beginTransaction().remove(profileF).commit();
                    sadStudent.setVisibility(View.VISIBLE);
                    tvNoProfile.setVisibility(View.VISIBLE);
                    bMoreProfile.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.like:
                if (!fin) {
                    try {
                        picStudent = student.getString("pic");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        new AsyncLike().execute(student.getString("email"), mail);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                arrayStudnent.remove(arrayStudnent.length() - 1);
                if (arrayStudnent.length() > 0) {
                    b = buildBundle();
                    setNewProfile(b);
                } else {
                    fin = true;
                    getSupportFragmentManager().beginTransaction().remove(profileF).commit();
                    sadStudent.setVisibility(View.VISIBLE);
                    tvNoProfile.setVisibility(View.VISIBLE);
                    bMoreProfile.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.bMoreProfile:
                sadStudent.setVisibility(View.INVISIBLE);
                tvNoProfile.setVisibility(View.INVISIBLE);
                bMoreProfile.setVisibility(View.INVISIBLE);
                new Swipe.AsyncSwipe().execute(mail);
                fin = false;
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
            try {
                arrayStudnent = new JSONArray(result.toString());

                if (arrayStudnent.length() > 0) {
                    b = buildBundle();
                    setNewProfile(b);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class AsyncLike extends AsyncTask<String, String, String> {
        String mail, mailCo;

        @Override
        protected String doInBackground(String... params) {
            mail = params[0];
            mailCo = params[1];
            HttpURLConnection conn;
            String result = "fail";
            try {
                conn = back.connect("https://tinder.student.elwinar.com/controller/like_student.php", "POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mail", params[0])
                        .appendQueryParameter("mail_co", params[1]);


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
            if (result.equals("MATCH")) {
                Intent i = new Intent(Swipe.this, Match.class);
                i.putExtra("mail", mail);
                i.putExtra("mailCo", mailCo);

                i.putExtra("pic", picStudent);

                startActivity(i);
            }

        }


    }

    public Bundle buildBundle() {
        Bundle bundle = new Bundle();
        try {
            student = arrayStudnent.getJSONObject(arrayStudnent.length() - 1);

            String adjs = student.getString("adj1") + " - " +
                    student.getString("adj2") + " - " +
                    student.getString("adj3");

            bundle.putString("name", student.getString("surname"));
            bundle.putString("adjs", adjs);
            bundle.putString("description", student.getString("description"));
            try {
                Log.i("SWIPE PIC", student.getString("pic"));
                bundle.putString("pic", student.getString("pic"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bundle;
    }

    public void setNewProfile(Bundle b) {
        profileF = new FragmentProfile();
        profileF.setArguments(b);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.profileFragment, profileF)
                .commit();
    }
}
