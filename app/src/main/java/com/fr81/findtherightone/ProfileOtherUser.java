package com.fr81.findtherightone;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Class to display activity of profile of other student
 */
public class ProfileOtherUser extends AppCompatActivity implements View.OnClickListener {
    private BackendConnection b = new BackendConnection();
    private TextView tvDescription_Other;
    private TextView tv_Other_Name;
    private TextView tvAdjectives_Other;
    private TextView tvMatch_Other;
    private ImageView imgProfile_Other;
    private Button bSwipe_Other;
    ProgressBar p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_other_user);

        p = findViewById(R.id.pBProfile_Other);
        p.setVisibility(View.VISIBLE);
        bSwipe_Other = findViewById(R.id.bSwipe_Other);
        bSwipe_Other.setOnClickListener(this);


        tv_Other_Name = findViewById(R.id.tv_Other_Name);
        tvAdjectives_Other = findViewById(R.id.tvAdjectives_Other);
        tvDescription_Other = findViewById(R.id.tvDescription_Other);
        tvMatch_Other = findViewById(R.id.tvMatch_Other);
        imgProfile_Other = findViewById(R.id.imgProfile_Other);

        Intent intent = getIntent();
        String mail = getIntent().getStringExtra("mail");
        Log.i("profileotheruser",mail);
        new ProfileOtherUser.AsyncProfileOther().execute(mail);

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imgProfile_Other.setColorFilter(filter);



    }
    // Top menu to navigate on click
    @Override
    public void onClick(View v){
        if (v.getId() == R.id.bSwipe_Other){
            Intent Swipe =  new Intent(this, Swipe.class);
            startActivity(Swipe);
        }
    }



    private class AsyncProfileOther extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn = null;
            String result = null;
            try {
                conn = b.connect("http://skipti.fr/controller/profil_other_user.php", "POST");
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mail", strings[0]);
                String query = builder.build().getEncodedQuery();
                b.sendData(conn, query);
                result = b.getData(conn);


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServerException e) {
                Toast.makeText(ProfileOtherUser.this, "Dev didn't do his job", Toast.LENGTH_SHORT).show();
            }
            return result;
        }

        /**
         * Decode json and get element to feed UI
         * @param result string with all student info
         */
        @Override
        protected void onPostExecute(String result) {
            try{
                Log.i("profilotheruser", result);
                JSONObject json = new JSONObject(result.toString());
                JSONObject info_other_student = json.getJSONObject("student");
                Log.i("profilotheruser", json.toString());
                Log.i("profilotheruser", info_other_student.toString());
                tv_Other_Name.setText(info_other_student.getString("surname"));
                String adjectives_other_student = info_other_student.getString("adj1") + " - " +
                        info_other_student.getString("adj2") + " - " +
                        info_other_student.getString("adj3");
                tvAdjectives_Other.setText(adjectives_other_student);
                tvDescription_Other.setText(info_other_student.getString("description"));
                tvMatch_Other.setText(info_other_student.getString("match"));

                String picPath = info_other_student.getString("pic");
                picPath = picPath.replace("\\", "/");
                picPath = picPath.replace("..", "");
                Log.i("PICTURE", "http://skipti.fr" + picPath);

                Picasso.get().load("http://skipti.fr" + picPath).noFade().into(imgProfile_Other);


            }catch (Exception exception) {
                exception.printStackTrace();
            }
        }


    }


}


