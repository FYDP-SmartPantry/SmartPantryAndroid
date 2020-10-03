package com.uwaterloo.smartpantry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button registerButton = (Button) findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.user_name)).getText().toString();
                String password = ((EditText) findViewById(R.id.user_password)).getText().toString();
                String confirmPassword = ((EditText) findViewById(R.id.confirm_password)).getText().toString();
                String email = ((EditText) findViewById(R.id.user_email)).getText().toString();

                if (!"".equals(username) && !"".equals(password)) {
                    if (!password.equals(confirmPassword)) {
                        // Toast.makeText(this, "The password does not match. Please enter it again", Toast.LENGTH_LONG).show();
                        ((EditText) findViewById(R.id.user_name)).setText("");
                        ((EditText) findViewById(R.id.user_password)).setText("");
                        ((EditText) findViewById(R.id.confirm_password)).setText("");
                    } else {
                        Intent registerIntent = new Intent();
                        Bundle registerBundle = new Bundle();
                        registerBundle.putCharSequence("User", username);
                        registerBundle.putCharSequence("Password", password);
                        registerBundle.putCharSequence("Email", email);
                        registerIntent.putExtras(registerBundle);
                    }
                } else {
                    // Toast.makeText()
                }
            }
        });
    }
}