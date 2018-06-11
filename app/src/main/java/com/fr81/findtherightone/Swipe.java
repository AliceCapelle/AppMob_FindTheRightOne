package com.fr81.findtherightone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.net.HttpURLConnection;

// TODO: 17/05/18 on affiche fragment plus de profile si numberOfStundent = 0 

/**
 * Class to handle swipe, like and dislike
 */

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
    private Animation shake;
    private Button bDeco;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        sadStudent = findViewById(R.id.ivSadStudent);
        tvNoProfile = findViewById(R.id.tvNoProfile);


        bProfile = findViewById(R.id.bProfileS);
        bLike = findViewById(R.id.like);
        bDislike = findViewById(R.id.dislike);
        bDeco = findViewById(R.id.bDecoS);
        bMoreProfile = findViewById(R.id.bMoreProfile);

        bProfile.setOnClickListener(this);
        bLike.setOnClickListener(this);
        bDislike.setOnClickListener(this);
        bMoreProfile.setOnClickListener(this);
        bDeco.setOnClickListener(this);

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        mail = sharedPreferences.getString("PREFS_MAIL", null);


        if (BackendConnection.isOnline((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
            new Swipe.AsyncSwipe().execute(mail);
        }
        else{
            Toast.makeText(Swipe.this, "Pas de connection internet !", Toast.LENGTH_SHORT);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bDecoS:
                getSharedPreferences("PREFS", 0).edit().clear().commit();
                Intent home = new Intent(this, Home.class);
                startActivity(home);
                break;
            case R.id.bProfileS:
                Intent profile = new Intent(this, Profile.class);
                startActivity(profile);
                break;
            case R.id.dislike:
                shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                bDislike.startAnimation(shake);
                if (!fin) {
                    try {
                        new AsyncDislike().execute(student.getString("email"), mail);
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
            case R.id.like:
                shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                bLike.startAnimation(shake);
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
                break;
        }
    }

    private class AsyncSwipe extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            String result = "fail";

            try {
                conn = back.connect("http://skipti.fr/controller/swipe.php", "POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mail", params[0]);

                String query = builder.build().getEncodedQuery();
                back.sendData(conn, query);

                result = back.getData(conn);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServerException e) {
                Toast.makeText(Swipe.this, "Dev didn't do his job", Toast.LENGTH_SHORT).show();
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
                else{
                    sadStudent.setVisibility(View.VISIBLE);
                    tvNoProfile.setVisibility(View.VISIBLE);
                    fin=true;
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
                conn = back.connect("http://skipti.fr/controller/like_student.php", "POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mail", params[0])
                        .appendQueryParameter("mail_co", params[1]);


                String query = builder.build().getEncodedQuery();
                back.sendData(conn, query);

                result = back.getData(conn);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServerException e) {
                Toast.makeText(Swipe.this, "Dev didn't do his job", Toast.LENGTH_SHORT).show();
            }
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            result.replace("\n","");
            if (result.equals("MATCH")) {
                Intent i = new Intent(Swipe.this, Match.class);
                i.putExtra("mail", mail);
                i.putExtra("mailCo", mailCo);

                i.putExtra("pic", picStudent);

                startActivity(i);
            }

        }
    }

    private class AsyncDislike extends AsyncTask<String, String, String> {
        String mail, mailCo;

        @Override
        protected String doInBackground(String... params) {
            mail = params[0];
            mailCo = params[1];
            HttpURLConnection conn;
            String result = "fail";
            try {
                conn = back.connect("http://skipti.fr/controller/dislike_student.php", "POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mail", mail)
                        .appendQueryParameter("mail_co", mailCo);


                String query = builder.build().getEncodedQuery();
                back.sendData(conn, query);

                result = back.getData(conn);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServerException e) {
                Toast.makeText(Swipe.this, "Dev didn't do his job", Toast.LENGTH_SHORT).show();
            }
            return result;

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
            bundle.putString("mail", student.getString("email"));

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
