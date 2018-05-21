package com.example.bhaum.eduapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;


public class NavigationTab extends AppCompatActivity /*implements FeedFragment.OnListFragmentInteractionListener*/{

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
                    android.support.v4.app.FragmentManager ff=getSupportFragmentManager();
                   ff.beginTransaction().replace(R.id.myframe, new FeedFragment()).commit();

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
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


}
