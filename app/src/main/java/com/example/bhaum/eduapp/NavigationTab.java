package com.example.bhaum.eduapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bhaum.eduapp.adapter.FeedListAdapter;
import com.example.bhaum.eduapp.app.AppController;
import com.example.bhaum.eduapp.data.FeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NavigationTab extends AppCompatActivity {


    private static final String TAG = "tag";
    private TextView mTextMessage;
    static Context context;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        Fragment fragment = null;

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_feed:
                    android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                   ft.replace(R.id.myframe, new FeedFragment())
                    .addToBackStack(null)
                    .commit();

                return true;

                case R.id.navigation_attendance:
                    //fragment = new AttendanceFragment();
                    //loadFragment(fragment);
                    return true;

                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    private boolean loadFragment(android.support.v4.app.Fragment fragment)
    {
        if(fragment!=null)
        {
            Log.e(TAG, "inside load fragment");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.myframe, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_tab);
        context = getApplicationContext();
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        findAllUsers();

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mOnNavigationItemSelectedListener.onNavigationItemSelected(navigation.getMenu().findItem(R.id.navigation_feed));

        }

    private void findAllUsers() {


        final String USERS_URL = "http://192.168.0.103:8000/api/users/";

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, USERS_URL, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response


                        try {
                            JSONArray usersArray = response.getJSONArray("users");

                            for(int i = 0; i< usersArray.length() ; i++){

                                JSONObject user = (JSONObject) usersArray.get(i);
                                SomeClass.users.put(user.getInt("id"), user.getString("first_name") +" " + user.getString("last_name") ) ;
                                Log.e(TAG, "putted");
                            }

                            //Log.e(TAG, String.valueOf(Arrays.asList(SomeClass.users)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "error "+ e.toString());
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

// add it to the RequestQueue
        AppController.getInstance().addToRequestQueue(getRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==1){
            Log.e(TAG, "back from comment activity");
        }
        else {
            Log.e(TAG, "error in fetching");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
