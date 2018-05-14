package com.example.bhaum.eduapp;



import android.app.VoiceInteractor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.*;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectionFragment extends Fragment {


    public SelectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selection, container, false);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
               super.onActivityCreated(savedInstanceState);

               RequestQueue queue = Volley.newRequestQueue(getActivity());
               String url = "http://192.168.0.104:8000/api/branch";

               JsonObjectRequest objectRequest = new JsonObjectRequest(
                       Request.Method.GET,
                       url,
                       null,
                       new Response.Listener<JSONObject>() {
                           @Override
                           public void onResponse(JSONObject response) {
                               Log.i("Info", response.toString());
                           }
                       },
                       new Response.ErrorListener() {
                           @Override
                           public void onErrorResponse(VolleyError error) {

                           }
                       }
               );
    }



}
