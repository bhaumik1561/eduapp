package com.example.bhaum.eduapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.bhaum.eduapp.adapter.AttendanceRecyclerViewAdapter;
import com.example.bhaum.eduapp.data.Student;
import com.example.bhaum.eduapp.app.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.security.auth.Subject;

public class AttendanceList extends AppCompatActivity {
    private static final String TAG = "AttendanceList";
    String BRANCH = "Devraj";
    String STANDARD = "8";
    String SUBJECT = "Science";
    AttendanceRecyclerViewAdapter recyclerViewAdapter;
    RecyclerView recyclerView;
    Button saveAttendance;

    String STUDENT_LIST_URL = "http://192.168.0.103:8000/api/studentlist/"+ BRANCH +"/" + STANDARD + "/" + SUBJECT + "/";

    private ArrayList<Student> studentLists = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        saveAttendance = (Button)findViewById(R.id.saveAttendance);
        StringRequest getStudentList = new StringRequest(Request.Method.GET, STUDENT_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            parseJsonObject(responseObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        AppController.getInstance().addToRequestQueue(getStudentList);


        saveAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recyclerViewAdapter!=null){
                    //construct the json to send request
                    studentLists = recyclerViewAdapter.getStudentList();
                    for(int i = 0 ; i< studentLists.size(); i++){
                        Student s = studentLists.get(i);
                        Log.d(TAG, String.valueOf(s.isAttend_status()));
                    }
                }
                else {
                    Log.d(TAG, "recyclerView empty");
                }
            }
        });
    }

    private void parseJsonObject(JSONObject responseObj) {

        try {
            JSONArray studentsArray = responseObj.getJSONArray("studentlist");
            for(int i = 0 ; i< studentsArray.length(); i++){
                JSONObject student = (JSONObject) studentsArray.get(i);

                Student student1 = new Student();
                student1.setUser_id(student.getInt("user_id"));
                student1.setName(student.getString("name"));
                student1.setRoll_no(student.getInt("roll_no"));
                student1.setUser_type(student.getString("user_type"));
                student1.setAttend_status(true);

                studentLists.add(student1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerViewAdapter = new AttendanceRecyclerViewAdapter(this, studentLists);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
