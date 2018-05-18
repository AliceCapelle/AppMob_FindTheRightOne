package com.fr81.findtherightone;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class Match  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        ImageView imgMatch= findViewById(R.id.imgMatch);

        ColorMatrix matrix;
        matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter;
        filter = new ColorMatrixColorFilter(matrix);
        imgMatch.setColorFilter(filter);


    }
}
