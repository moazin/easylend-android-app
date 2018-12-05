package com.example.moazin.easylend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        Button signup_redirect = findViewById(R.id.login_signup_button);
        signup_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redirect_to_signup = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(redirect_to_signup);
            }
        });
    }
}
