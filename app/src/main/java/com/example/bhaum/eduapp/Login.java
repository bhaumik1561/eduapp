package com.example.bhaum.eduapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
    private int STORAGE_PERMISSION_CODE = 23;
    private int STORAGE_PERMISSION_CODE1 = 24;
    private int STORAGE_PERMISSION_CODE2 = 25;
    private int STORAGE_PERMISSION_CODE3 = 26;
    SQLiteDatabase mydb = null;
    static String uid, uname, passwd;

    private static final String TAG = Login.class.getName();
    EditText et1;
    EditText et2;
    Button lgn;
    RequestQueue mRequestQueue;
    StringRequest mStringRequest;
    static String dbname = "eduapp.db";
    String dbpath = "/data/data/com.example.bhaum.eduapp/databases/";
    int image_req = 111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        mysql m = new mysql(this);

        m.checkMydb();
        checkLogin();
        requestPermission(0);


        et1 = (EditText) findViewById(R.id.username);
        et2 = (EditText) findViewById(R.id.password);
        lgn = (Button) findViewById(R.id.LoginButton);
        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    /*
                    final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
                    progressDialog.setTitle("Logging in");

                    progressDialog.setCancelable(false);
                    progressDialog.show();*/
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
        String url = "http://192.168.0.103:8000/api/user/login/";


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // response


                        try {
                            JSONObject responseObj = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), "logged in", Toast.LENGTH_LONG).show();
                            addDatatoTable(String.valueOf(responseObj.getString("id")), e1, e2);
                            SomeClass.Login_user_id = responseObj.getInt("id");
                            Log.e(TAG, "yeey" +SomeClass.Login_user_id);
                        } catch (JSONException e) {
                            Log.e(TAG, "Error login" + e.toString());
                            e.printStackTrace();
                        }


                        Intent intent = new Intent(getApplicationContext(), NavigationTab.class);
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        try {
                            String ed=new String(error.networkResponse.data,"UTF-8");
                            JSONObject errorObj = new JSONObject(ed);
                            Toast.makeText(Login.this, errorObj.getString("non_field_errors"), Toast.LENGTH_SHORT).show();

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", e1);
                params.put("password", e2);


                return params;
            }
        };

        MySingleton.getInstance(this).getRequestQueue().add(postRequest);
    }

    void addDatatoTable(String uid, String uname, String passwd) {

        SQLiteDatabase mydb = null;

        String mypath = dbpath + dbname;

        try {

            mydb = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);

            // mydb.execSQL("delete from device_info");


            mydb.execSQL("insert into eduapp values('" + uid + "','" + uname + "','" + passwd + "')");
            mydb.close();
            Intent i = new Intent(getApplicationContext(), NavigationTab.class);
            startActivity(i);
            finish();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Error.Response", e.toString());
        }


    }

    void checkLogin() {
        String mypath = dbpath + dbname;
        try {

            mydb = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);


            Cursor cur = mydb.rawQuery("select * from eduapp", null);
            int len = cur.getCount();


            while (cur.moveToNext()) {
                uid = cur.getString(cur.getColumnIndex("uid"));
                uname = cur.getString(cur.getColumnIndex("unname"));
                passwd = cur.getString(cur.getColumnIndex("passwd"));


            }


            if (!uname.equals("")) {

                Intent i = new Intent(getApplicationContext(), NavigationTab.class);
                SomeClass.Login_user_id = Integer.parseInt(uid);
               // Log.e(TAG, String.valueOf(SomeClass.Login_user_id));
                Toast.makeText(getApplicationContext(), "Logging in", Toast.LENGTH_LONG).show();
                startActivity(i);
                finish();

            }


            mydb.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();


        }
    }

    private void requestPermission(int x) {

        if (x == 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
            }

            //And finally ask for the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else if (x == 1) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
            }

            //And finally ask for the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, STORAGE_PERMISSION_CODE1);

        } else if (x == 2) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_WIFI_STATE)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
            }

            //And finally ask for the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, STORAGE_PERMISSION_CODE2);

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
            }

            //And finally ask for the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE3);

        }


    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                // Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE1) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can use internet", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE2) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can use wifi", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE3) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can call", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}