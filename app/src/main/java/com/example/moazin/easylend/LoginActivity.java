package com.example.moazin.easylend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText username_edittext;
    private EditText password_edittext;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar  = getSupportActionBar();
        actionBar.setTitle("Log In");

        // get all text boxes loaded
        username_edittext = findViewById(R.id.login_username_box);
        password_edittext = findViewById(R.id.login_password_box);

        // grabbing the progress bar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        Button signup_redirect = findViewById(R.id.login_signup_button);
        signup_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redirect_to_signup = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(redirect_to_signup);
            }
        });

        Button login_button = findViewById(R.id.login_login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                String url = "http://10.0.2.2:8000/api-token-auth/";

                // Let's set the request
                JSONObject request_data = new JSONObject();
                try {
                    request_data.put("username", username_edittext.getText().toString());
                    request_data.put("password", password_edittext.getText().toString());
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, request_data, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response){
                                    try {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        String token = response.getString("token");
                                        int id = response.getInt("id");
                                        String first_name = response.getString("first_name");
                                        String last_name = response.getString("last_name");
                                        String email = response.getString("email");
                                        String username = response.getString("username");
//                                        Toast.makeText(LoginActivity.this, token, Toast.LENGTH_LONG).show();
                                        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
                                        preferenceEditor.putString("token", token);
                                        preferenceEditor.putInt("id", id);
                                        preferenceEditor.putString("first_name", first_name);
                                        preferenceEditor.putString("last_name", last_name);
                                        preferenceEditor.putString("email", email);
                                        preferenceEditor.putString("username", username);
                                        preferenceEditor.apply();
                                        Intent redirect_home = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(redirect_home);
                                        finish();
                                    } catch (JSONException jsonException){
                                        Toast.makeText(LoginActivity.this, "JSON Error in Response", Toast.LENGTH_LONG).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                requestQueue.add(jsonObjectRequest);
                    progressBar.setVisibility(View.VISIBLE);
                } catch(JSONException jsonException){
                    Toast.makeText(LoginActivity.this, "JSON Error in Request", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
