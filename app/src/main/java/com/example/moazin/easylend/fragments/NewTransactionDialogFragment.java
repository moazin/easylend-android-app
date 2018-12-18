package com.example.moazin.easylend.fragments;

import android.app.Activity;
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
import android.view.Window;
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
import com.example.moazin.easylend.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewTransactionDialogFragment extends DialogFragment {

    int ToId;
    String mAuthToken;
    EditText mAmountEditText;
    int FromId;
    Double mAmount;
    String action;
    Context mContext;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        action = getString(R.string.exchange_dialog_closed);
        mContext = getContext();
        Bundle args = getArguments();
        if(args != null){
            ToId = args.getInt("to_id");
            FromId = args.getInt("from_id");
            mAuthToken = args.getString("token");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater;
        View view;
        Activity activity = getActivity();
        if(activity != null){
            inflater = activity.getLayoutInflater();
            view = inflater.inflate(R.layout.new_transaction_dialog, null);
            mAmountEditText = view.findViewById(R.id.new_transaction_amount);
            mAmountEditText.requestFocus();
            builder.setView(view)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        mAmount = Double.valueOf(mAmountEditText.getText().toString());
                        try {
                            JSONObject requestObj = new JSONObject();
                            requestObj.put("from_user", FromId);
                            requestObj.put("to_user", ToId);
                            requestObj.put("amount", mAmount);
                            String base_url = getString(R.string.base_url_emulator);
                            String url = "https://" + base_url + "/transactions/";
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                    Request.Method.POST,
                                    url,
                                    requestObj,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Intent intent = new Intent();
                                            intent.setAction(action);
                                            intent.putExtra("exchange_amount", mAmount);
                                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
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
                                    params.put("Authorization", "Token " + mAuthToken);
                                    return params;
                                }

                            };
                            RequestQueue queue;
                            queue = Volley.newRequestQueue(mContext);
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
        }
        Dialog dialog = builder.create();
        Window dialogWindow = dialog.getWindow();
        if(dialogWindow != null){
            dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        return dialog;
    }



}
