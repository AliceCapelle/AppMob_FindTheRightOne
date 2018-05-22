package com.fr81.findtherightone;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class Match  extends AppCompatActivity implements View.OnClickListener {
    private Button btnContinuer, btnParler;
    private String mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        mail = getIntent().getStringExtra("mail");
        String picPath = getIntent().getStringExtra("pic");

        picPath = picPath.replace("\\", "/");
        picPath = picPath.replace("..", "");


        ImageView imgMatch = findViewById(R.id.imgMatch);
        btnContinuer = findViewById(R.id.btnContinuer);
        btnParler = findViewById(R.id.btnParler);

        btnContinuer.setOnClickListener(this);
        btnParler.setOnClickListener(this);
        imgMatch.setOnClickListener(this);

        Picasso.get().load("http://tinder.student.elwinar.com" + picPath).noFade().into(imgMatch, new Callback() {
            @Override
            public void onSuccess() {
                Log.i("MATCH", "onSuccess: MATCH PHOTO");
            }

            @Override
            public void onError(Exception e) {

            }
        });
        ToolBox.blackAndWhitePic(imgMatch);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnContinuer:
                Intent swipe = new Intent(this, Swipe.class);
                startActivity(swipe);
                break;
            case R.id.btnParler:
                Toast.makeText(Match.this, "développement en cours", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imgMatch:
               Intent profileother= new Intent(this, ProfileOtherUser.class);
               profileother.putExtra("mail", mail);
               startActivity(profileother);
               break;
        }
    }
}
