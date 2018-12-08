package com.example.moazin.easylend.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moazin.easylend.PersonProfileActivity;
import com.example.moazin.easylend.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewTransactionDialogFragment extends DialogFragment {
    int to_id;
    String token;
    EditText num;
    int from_id;
    Double amount;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // TODO: Fix this transaction security bug at any cost, anyone can create a transaction with any one's name
        to_id = getArguments().getInt("to_id");
        from_id = getArguments().getInt("from_id");
        token = getArguments().getString("token");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_transaction_dialog, null);
        num = view.findViewById(R.id.new_transaction_amount);
        num.requestFocus();
        builder.setView(view)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        amount = Double.valueOf(num.getText().toString());
                        try {
                            JSONObject requestObj = new JSONObject();
                            requestObj.put("from_user", from_id);
                            requestObj.put("to_user", to_id);
                            requestObj.put("amount", amount);
                            String base_url = getString(R.string.base_url_emulator);
                            String url = "http://" + base_url + ":8000/transactions/";
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                    Request.Method.POST,
                                    url,
                                    requestObj,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Intent intent = new Intent();
                                            intent.setAction(getActivity().getString(R.string.exchange_dialog_closed));
                                            intent.putExtra("exchange_amount", amount);
                                            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }
                            ) {

                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("Authorization", "Token " + token);
                                    return params;
                                }

                            };
                            RequestQueue queue;
                            queue = Volley.newRequestQueue(getContext());
                            queue.add(jsonObjectRequest);
                        } catch (JSONException json){
                            json.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setTitle("Enter an amount");
        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }



}
