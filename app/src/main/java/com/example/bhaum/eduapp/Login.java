package com.example.bhaum.eduapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {

    private static final String TAG = Login.class.getName();
    EditText et1;
    EditText et2;
    Button lgn;
    RequestQueue mRequestQueue;
    StringRequest mStringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        et1 = (EditText) findViewById(R.id.username);
        et2 = (EditText) findViewById(R.id.password);
        lgn = (Button) findViewById(R.id.LoginButton);
        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    sendRequestAndPrintResponse();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        });


    }

    private void sendRequestAndPrintResponse() throws JSONException {
        final String e1 = et1.getText().toString();
        final String e2 = et2.getText().toString();
        final TextView mTextView = (TextView) findViewById(R.id.mTextView);

        //wb1.addParam(e1,"Username");
        //wb1.addParam(e2, "Password");
        String url = "http://192.168.1.104:8000/api/user/login/";


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Toast.makeText(getApplicationContext(), "Login Succesfull", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username", e1);
                params.put("password", e2);


                return params;
            }
        };

        MySingleton.getInstance(this).getRequestQueue().add(postRequest);
    }
}