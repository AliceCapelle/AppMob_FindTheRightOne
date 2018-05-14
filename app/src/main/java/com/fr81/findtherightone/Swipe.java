package com.fr81.findtherightone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Swipe extends AppCompatActivity implements View.OnClickListener {

    Button bProfile;
    ImageButton bLike;
    ImageButton bDislike;
    FragmentProfile profileF = null;
    Bundle b;
    int cpt = 0;

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

        profileF = new FragmentProfile();

        b = new Bundle();
        b.putInt("cpt", cpt);
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
            case R.id.dislike:
                cpt++;
                b = new Bundle();
                b.putInt("cpt", cpt);
                profileF.setArguments(b);
                getSupportFragmentManager().beginTransaction().
                        remove(getSupportFragmentManager().findFragmentById(R.id.profileFragment)).commit();
                break;
            case R.id.like:
                cpt++;
                b = new Bundle();
                b.putInt("cpt", cpt);
                profileF = new FragmentProfile();
                profileF.setArguments(b);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.profileFragment, profileF)
                        .commit();
                break;
        }
    }
}
