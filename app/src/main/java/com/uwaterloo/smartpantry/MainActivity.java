package com.uwaterloo.smartpantry;

import com.uwaterloo.smartpantry.ui.login.LoginActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*registerButton.setOnClickListener(new View.OnClickListener() {
            // OnClick -> the login_register activity
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginRegActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(LoginRegActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });*/
    }

}