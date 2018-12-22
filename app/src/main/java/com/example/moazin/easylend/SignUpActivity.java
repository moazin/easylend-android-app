package com.example.moazin.easylend;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    private EditText mFirstNameEditText, mLastNameEditText, mUsernameEditText, mEmailEditText, mPasswordEditText, mRepeatPasswordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sign Up");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // get all of our fields and buttons
        mFirstNameEditText = findViewById(R.id.signup_firstname);
        mLastNameEditText = findViewById(R.id.signup_lastname);
        mUsernameEditText= findViewById(R.id.signup_username);
        mEmailEditText = findViewById(R.id.signup_email);
        mPasswordEditText = findViewById(R.id.signup_password);
        mRepeatPasswordEditText = findViewById(R.id.signup_repeat_password);
        Button sign_up_button = findViewById(R.id.signup_signup_button);


        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Maybe add proper validation checks here on registration methods
                if(!(mPasswordEditText.getText().toString().equals(mRepeatPasswordEditText.getText().toString()))){
                    Toast.makeText(SignUpActivity.this, "Passwords must match!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        // prepare the request object
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("first_name", mFirstNameEditText.getText());
                        jsonObject.put("last_name", mLastNameEditText.getText());
                        jsonObject.put("email", mEmailEditText.getText());
                        jsonObject.put("username", mUsernameEditText.getText());
                        jsonObject.put("password", mPasswordEditText.getText());
                        // create a request object
                        String base_url = getString(R.string.base_url_emulator);
                        String port  = getString(R.string.port);
                        String protocol = getString(R.string.protocol);
                        String url = protocol + "://" + base_url + ":" + port + "/users/newuser";
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(SignUpActivity.this, "Successfully Created", Toast.LENGTH_LONG).show();
                                    Intent redirect_login = new Intent(SignUpActivity.this, LoginActivity.class);
                                    startActivity(redirect_login);
                                    finish();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(SignUpActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        );
                        RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
                        requestQueue.add(jsonObjectRequest);
                    } catch(JSONException jsonException){
                        Toast.makeText(SignUpActivity.this, "JSON error while creating request object", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
