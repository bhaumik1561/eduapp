package com.example.bhaum.eduapp;

import com.example.bhaum.eduapp.data.FeedItem;
import com.example.bhaum.eduapp.app.AppController;
import com.example.bhaum.eduapp.volley.LruBitmapCache;
import com.example.bhaum.eduapp.adapter.FeedListAdapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment  {


    private static final String TAG = "Nothing";
    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = "http://192.168.0.107:8000/api/news/";

    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(TAG, "inside fragment");
        final View view = inflater.inflate(R.layout.fragment_feed, container, false);
       /* final FragmentActivity c = getActivity();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);

        listItems = new ArrayList<>();

        for(int i=0; i<=10; i++)
        {
            Feed listItem = new Feed(
                    "prit","ppsp"
            );
        listItems.add(listItem);
        }

        adapter = new MyFeedAdapter(listItems,getContext() );
        recyclerView.setAdapter(adapter);
       */
        listView = (ListView) view.findViewById(R.id.list);
        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(getActivity(), feedItems);
        listView.setAdapter(listAdapter);

        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
                    URL_FEED, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }


        return view;

    }

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("news");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("news_id"));
                //item.setName(feedObj.getString("name"));

                // Image might be null sometimes
                String image = feedObj.isNull("file_name") ? null : feedObj
                        .getString("file_name");
                item.setAttachement(image);
                item.setAttachmentType(feedObj.getString("file_type"));
                item.setStatus(feedObj.getString("description"));
                //item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("date"));

                // url might be null sometimes
                feedItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}

