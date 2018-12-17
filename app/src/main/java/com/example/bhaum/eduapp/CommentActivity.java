package com.example.bhaum.eduapp;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.bhaum.eduapp.adapter.CommentListAdapter;
import com.example.bhaum.eduapp.data.Comments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.example.bhaum.eduapp.app.AppController;
import com.example.bhaum.eduapp.data.FeedItem;
import com.google.gson.JsonSerializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommentActivity extends AppCompatActivity {

    private static final int TAG = CommentActivity.MODE_PRIVATE;
    int like;
    int SELF_USER_ID = (SomeClass.Login_user_id);
    private CommentListAdapter listAdapter;
    String POST_COMMENT_URL = "http://192.168.0.103:8000/api/news/comments/create/";
    FeedItem item;

    List<Comments> commentsList;
    private String SELF_NAME = SomeClass.users.get(SELF_USER_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        item = (FeedItem) getIntent().getParcelableExtra("feedObj");

        NetworkImageView profilePic = (NetworkImageView) findViewById(R.id.profilePic);
        TextView name = (TextView)findViewById(R.id.name);
        TextView timestamp = (TextView)findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) findViewById(R.id.txtStatusMsg);
        ImageView feedImageView = (ImageView) findViewById(R.id.feedImage1);
        Button downloadFileBtn = (Button)findViewById(R.id.downloadFile);
        TextView totalLikes = (TextView)findViewById(R.id.totalLikes);
        final TextView totalComments = (TextView)findViewById(R.id.totalComments);
        final Button likeBtn = (Button) findViewById(R.id.likeBtn);
        Button commentBtn = (Button)findViewById(R.id.commentBtn);
        final ListView listView = (ListView)findViewById(R.id.commentsList);
        Button postComment = (Button) findViewById(R.id.postComment);
        final EditText commentText = (EditText)findViewById(R.id.commentText);



        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        LinearLayout root = (LinearLayout)findViewById(R.id.root);
        root.setTag("news_id:"+ item.getId() + " user_id:" + item.getUser_id());

        // user profile pic
        String Profile_Pic_URL = "http://192.168.0.103:8000/static/files/profile_pics/" + item.getProfilePic();
        profilePic.setImageUrl(Profile_Pic_URL, imageLoader);

        name.setText(item.getName());

        // Converting timestamp into x ago format
        final Date newDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            newDate = format.parse(item.getTimeStamp());

            format = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
            String date = format.format(newDate);
            timestamp.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Chcek for empty status message
        if (!TextUtils.isEmpty(item.getStatus())) {
            statusMsg.setText(item.getStatus());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }

        //set total likes
        totalLikes.setText(item.getTotalLikes() + " likes");
        Log.e(String.valueOf(TAG), item.getLiked_by().toString());
        //set total Comments
        totalComments.setText(item.getTotalComments() +" comments");

        // Feed image
        String url = "http://192.168.0.103:8000/static/files/feed_docs/"+ item.getAttachement();
        String attachementType = item.getAttachmentType();

        if(attachementType.toLowerCase().equals("jpg") || attachementType.toLowerCase().equals("jpeg") || attachementType.toLowerCase().equals("png") || attachementType.toLowerCase().equals("bmp"))
        {
            //Log.e(TAG, item.getAttachmentType()+"check");
            //Log.e(TAG, item.getAttachement());
            downloadFileBtn.setVisibility(View.GONE);
           feedImageView.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(url)
                    .into(feedImageView);
        }
        else if(attachementType.equals(""))
        {
            feedImageView.setVisibility(View.GONE);
            downloadFileBtn.setVisibility(View.GONE);
        }

        else
        {
            feedImageView.setVisibility(View.GONE);
            downloadFileBtn.setText("Download " + item.getAttachement());
        }


        //check if already liked
        HashMap<Integer, String> hm = (HashMap<Integer, String>) item.getLiked_by();
        Set<Integer> keys = hm.keySet();

        for(Integer key: keys){
            if(key==SELF_USER_ID){
                likeBtn.setTag("1");
                likeBtn.setBackgroundResource(R.color.colorPrimary);
            }
        }

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentText.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(commentText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout vParent = (LinearLayout)v.getParent();
                LinearLayout vvParent = (LinearLayout)vParent.getParent();

                LinearLayout vvchild = (LinearLayout)vvParent.getChildAt(5);
                TextView likesView = (TextView)vvchild.getChildAt(0);

                int spaceIndex = likesView.getText().toString().indexOf(" ");
                int like = Integer.parseInt(likesView.getText().toString().substring(0, spaceIndex));

                String tags[] = vvParent.getTag().toString().split(" ");
                final int news_id = Integer.parseInt(tags[0].split(":")[1]);
                //final int user_id = Integer.parseInt(tags[1].split(":")[1]);
                //Log.e(TAG, String.valueOf(news_id) + String.valueOf(user_id));

                if((likeBtn.getTag().toString().equals("0")))
                {
                    like+=1;
                    likesView.setText(like + " likes");
                    likeBtn.setTag("1");
                    likeBtn.setBackgroundResource(R.color.colorPrimary);
                    item.addLiked_by(SELF_USER_ID, SELF_NAME);
                    // send request to add like
                    String URL_ADDLIKE = "http://192.168.0.103:8000/api/news/likes/create/";
                    StringRequest postRequest = new StringRequest(Request.Method.POST, URL_ADDLIKE,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("RESPONSE:", response);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("ERROR RESPONSE:", error.toString());
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams()
                        {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("news_id", String.valueOf(news_id));
                            params.put("user_id", String.valueOf(SELF_USER_ID));

                            return params;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(postRequest);
                }
                else{
                    like-=1;
                    likesView.setText(like +" likes");
                    likeBtn.setTag("0");
                    likeBtn.setBackgroundResource(android.R.drawable.btn_default);
                    item.removeLiked_by(SELF_USER_ID);

                    String URL_REMOVELIKE = "http://192.168.0.103:8000/api/news/likes/remove/";
                    StringRequest postRequest = new StringRequest(Request.Method.POST, URL_REMOVELIKE,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("RESPONSE:", response);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("ERROR RESPONSE:", error.toString());
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams()
                        {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("news_id", String.valueOf(news_id));
                            params.put("user_id", String.valueOf(SELF_USER_ID));

                            return params;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(postRequest);
                }
            }
        });


        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(String.valueOf(TAG), String.valueOf(SELF_USER_ID));


                StringRequest addCommentRequest = new StringRequest(Request.Method.POST, POST_COMMENT_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject responseObj = new JSONObject(response);
                                    if(responseObj.has("error")){
                                        try {

                                            Toast.makeText(CommentActivity.this, responseObj.getString("error"),Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else{

                                        Comments newComment = new Comments();
                                        try {
                                            newComment.setComment_id(Integer.parseInt(responseObj.getString("comment_id")));
                                            newComment.setNews_id(item.getId());
                                            newComment.setUser_id(SELF_USER_ID);
                                            newComment.setDescription(String.valueOf(commentText.getText()));
                                            newComment.setTimestamp(responseObj.getString("date"));
                                            newComment.setCommented_by(SELF_NAME);

                                            commentText.setText("");
                                            InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        item.addComment(newComment);

                                        //change  total comments
                                        totalComments.setText(item.getTotalComments()+ " comments");

                                        listAdapter.notifyDataSetChanged();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                try {
                                    String errorData = new String(error.networkResponse.data,"UTF-8");
                                    JSONObject errorObj = new JSONObject(errorData);
                                    Toast.makeText(CommentActivity.this, errorObj.getString("error"), Toast.LENGTH_SHORT).show();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }){
                    protected Map<String , String> getParams(){

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user_id", String.valueOf(SELF_USER_ID));
                        params.put("news_id", String.valueOf(item.getId()));
                        params.put("description", String.valueOf(commentText.getText()));
                        Log.d(String.valueOf(TAG), params.toString());
                        return  params;
                    }
                };
                AppController.getInstance().addToRequestQueue(addCommentRequest);
            }

        });

        //adding comments
        commentsList = new ArrayList<Comments>();

        commentsList = item.getList_comments();

        listAdapter = new CommentListAdapter(this, commentsList);

        listView.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            v.getParent().requestDisallowInterceptTouchEvent(true);
                                            return false;
                                        }
                                    });
                SomeClass.setListViewHeightBasedOnChildren(listView);
        listView.setAdapter(listAdapter);

    }

/*
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("MyData", item);
        setResult(1, intent);
        super.onBackPressed();
    }

    */
}
