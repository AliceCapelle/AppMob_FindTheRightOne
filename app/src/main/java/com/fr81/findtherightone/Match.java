package com.fr81.findtherightone;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class Match  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        String mail = getIntent().getStringExtra("mail");
        String picPath = getIntent().getStringExtra("pic");

        picPath = picPath.replace("\\", "/");
        picPath = picPath.replace("..", "");


        ImageView imgMatch = findViewById(R.id.imgMatch);

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
}
