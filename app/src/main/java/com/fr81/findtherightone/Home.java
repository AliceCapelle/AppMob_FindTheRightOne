package com.fr81.findtherightone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Activity for home page who is launch when app is open.
 * From there we can got to login and sign up.
 */

public class Home extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Button login = (Button) findViewById(R.id.loginButton);
        Button signup = (Button) findViewById(R.id.signupButton);

        login.setOnClickListener(this);
        signup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                Intent login = new Intent(this, Login.class);
                startActivity(login);
                break;
            case R.id.signupButton:
                Intent signup = new Intent(this, Signup.class);
                startActivity(signup);
                break;
        }
    }
}
