package com.example.bhaum.eduapp;

import com.example.bhaum.eduapp.data.FeedItem;
import com.example.bhaum.eduapp.app.AppController;
import com.example.bhaum.eduapp.volley.LruBitmapCache;
import com.example.bhaum.eduapp.adapter.FeedListAdapter;
import com.example.bhaum.eduapp.data.Comments;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class FeedFragment extends Fragment {




    //String BASE_URL = getResources().getString(R.string.baseUrl);
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    private static final String TAG = "Nothing";
    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = "http://192.168.0.103:8000/api/news/";
    private String URL_FILE = "http://192.168.0.103:8000/static/files/";
    private Context context;
    public HashMap<Integer, String> map;
    private String URL_USERS = "http://192.168.0.103:8000/api/users/";
    FloatingActionButton createPost;
    private DownloadManager downloadManager;

    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        Log.e(TAG, "inside fragment");
        final View view = inflater.inflate(R.layout.fragment_feed, container, false);

        context = view.getContext();
        listView = (ListView) view.findViewById(R.id.list);
        final Button downloadFileBtn = (Button)view.findViewById(R.id.downloadFile);
        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(getActivity(), feedItems);
        listView.setAdapter(listAdapter);

        //We first find all users and store into hashmap

        // We first check for cached request
    /*    Cache cache = AppController.getInstance().getRequestQueue().getCache();
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
        */
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
                    URL_FEED, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    //VolleyLog.d(TAG, "Response: " + response.toString());
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
        //}

        createPost = (FloatingActionButton) view.findViewById(R.id.createPost);
        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreatePost.class);
                //intent.putExtra("USER_ID", "1");
                startActivity(intent);
            }
        });
        return view;

    }


    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("news");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("news_id"));
                item.setUser_id(feedObj.getInt("user_id"));
                item.setName(SomeClass.users.get(feedObj.getInt("user_id")));


                // Image might be null sometimes
                String image = feedObj.isNull("file_name") ? null : feedObj
                        .getString("file_name");
                item.setAttachement(image);
                item.setAttachmentType(feedObj.getString("file_type"));
                item.setStatus(feedObj.getString("description"));
                item.setProfilePic(feedObj.getString("n_profile_pic"));
                item.setTimeStamp(feedObj.getString("date"));
                //item.setTotalLikes(feedObj.getInt("total_likes"));


                //set liked by
                JSONArray likedByArray = feedObj.getJSONArray("liked_by");
                for(int k = 0; k < likedByArray.length(); k++){
                    JSONObject likebyObj = (JSONObject) likedByArray.get(k);

                    for (Iterator<String> it = likebyObj.keys(); it.hasNext(); ) {
                        String key = it.next();
                        item.addLiked_by(Integer.parseInt(key), (String) likebyObj.get(key));
                    }
                }


                //add comments to the feed object
                JSONArray commentsArray = feedObj.getJSONArray("comments");
                if(commentsArray!=null) {
                    for (int j = 0; j < commentsArray.length(); j++) {
                        JSONObject commentObj = (JSONObject) commentsArray.get(j);

                        Comments c = new Comments();
                        c.setComment_id(commentObj.getInt("comment_id"));
                        c.setNews_id(feedObj.getInt("news_id"));
                        c.setUser_id(feedObj.getInt("user_id"));
                        c.setDescription(commentObj.getString("description"));
                        c.setTimestamp(commentObj.getString("date"));

                        c.setC_profile_pic(commentObj.getString("c_profile_pic"));

                        //set the name who hase commented
                        Pattern p = Pattern.compile("\"([^\"]*)\"");
                        Matcher m = p.matcher(commentObj.getString("commented_by").split(":")[1]);
                        while (m.find()) {
                            c.setCommented_by(m.group(1));
                        }

                        item.addComment(c);

                    }
                }
                feedItems.add(item);

            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "inside onActivity Result");
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == 1){
                FeedItem item = (FeedItem) data.getSerializableExtra("MyData");
                Log.e(TAG," onActivityResult" +String.valueOf(item.getId()));
            }

    }
}

