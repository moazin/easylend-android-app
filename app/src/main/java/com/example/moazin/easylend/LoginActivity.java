package com.example.moazin.easylend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // get all text boxes loaded
        mUsernameEditText = findViewById(R.id.login_username_box);
        mPasswordEditText = findViewById(R.id.login_password_box);

        // grabbing the progress bar
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setIndeterminate(true);

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
                String base_url = getString(R.string.base_url_emulator);
                String port = getString(R.string.port);
                String protocol = getString(R.string.protocol);
                String url = protocol + "://" + base_url + ":" + port + "/api-token-auth/";

                // Let's set the request
                JSONObject request_data = new JSONObject();
                try {
                    request_data.put("username", mUsernameEditText.getText().toString());
                    request_data.put("password", mPasswordEditText.getText().toString());
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, request_data, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response){
                                    try {
                                        mProgressBar.setVisibility(View.INVISIBLE);
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
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                requestQueue.add(jsonObjectRequest);
                    mProgressBar.setVisibility(View.VISIBLE);
                } catch(JSONException jsonException){
                    Toast.makeText(LoginActivity.this, "JSON Error in Request", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
