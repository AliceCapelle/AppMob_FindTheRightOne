package com.fr81.findtherightone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class WelcomeScreen extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        if(isConnected()){
            Intent swipe = new Intent(WelcomeScreen.this, Swipe.class);
            startActivity(swipe);
            finish();
        }
        else {
            Intent home = new Intent(WelcomeScreen.this, Home.class);
            startActivity(home);
            finish();
        }

    }

    /**
     * Method to know if user was previously connected and choosed to
     * stay connected
     * @return true if already connected, false otherwise
     */
    public Boolean isConnected(){
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        Boolean co = sharedPreferences.getBoolean("PREFS_CO", false);
        Log.i("welcome", co.toString());
        return co;
    }

}

