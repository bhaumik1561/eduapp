package com.example.bhaum.eduapp.adapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.esafirm.rxdownloader.RxDownloader;
import com.example.bhaum.eduapp.CommentActivity;
import com.example.bhaum.eduapp.SomeClass;
import com.example.bhaum.eduapp.app.AppController;
import com.example.bhaum.eduapp.data.FeedItem;
import com.example.bhaum.eduapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import rx.Observer;

import static com.example.bhaum.eduapp.app.AppController.TAG;

public class FeedListAdapter extends BaseAdapter {

    private DownloadManager downloadManager;
    private long refid;
    private Uri Download_Uri;
    ArrayList<Long> list = new ArrayList<>();


    private int SELF_USER_ID = SomeClass.Login_user_id;
    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private String URL_FILE = "http://192.168.0.103:8000/static/files/";
    private String filename;
    public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.feed_item, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        LinearLayout root = (LinearLayout)convertView.findViewById(R.id.root);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) convertView
                .findViewById(R.id.txtStatusMsg);

        NetworkImageView profilePic = (NetworkImageView) convertView
                .findViewById(R.id.profilePic);
        //FeedImageView feedImageView = (FeedImageView) convertView
        //        .findViewById(R.id.feedImage1);

        ImageView feedImageView = (ImageView)convertView
                    .findViewById(R.id.feedImage1);

        final Button downloadFileBtn = (Button)convertView
                .findViewById(R.id.downloadFile);
        final TextView totalLikes = (TextView)convertView
                .findViewById(R.id.totalLikes);
        final TextView totalComments = (TextView)convertView
                .findViewById(R.id.totalComments);
        final Button likeButton = (Button)convertView
                .findViewById(R.id.likeBtn);
        Button commentBtn = (Button)convertView
                .findViewById(R.id.commentBtn);

        final FeedItem item = feedItems.get(position);

        //setting news_id and user_id on root tag
        root.setTag("news_id:"+ item.getId() + " user_id:" + item.getUser_id() + " position:" + position);


        // user profile pic
        String Profile_Pic_URL = "http://192.168.0.103:8000/static/files/profile_pics/" + item.getProfilePic();
        profilePic.setImageUrl(Profile_Pic_URL, imageLoader);
        //profilePic.setDefaultImageResId(R.drawable.ic_account_circle_black_24dp);

        name.setText(item.getName());

        // Converting timestamp into x ago format
        Date newDate;
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

        //set total Comments
        //Log.e(TAG, String.valueOf(item.getTotalComments()));
        totalComments.setText(item.getTotalComments() +" comments");

        // Feed image
        String url = "http://192.168.0.103:8000/static/files/feed_docs/"+ item.getAttachement();
        String attachementType = item.getAttachmentType();

        if(attachementType.toLowerCase().equals("jpg") || attachementType.toLowerCase().equals("jpeg") || attachementType.toLowerCase().equals("png") || attachementType.toLowerCase().equals("bmp"))
        {

            downloadFileBtn.setVisibility(View.GONE);
            feedImageView.setVisibility(View.VISIBLE);
            Glide.with(activity)
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

        downloadFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String url = URL_FILE + "feed_docs/" + item.getAttachement();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RxDownloader.getInstance(activity)
                                .download(url, Environment.getDataDirectory() + "/eduapp/" + item.getAttachement(), getMimeType(url))
                                .subscribe(new Observer<String>() {
                                    @Override
                                    public void onCompleted() {
                                        Log.d(TAG, "completed");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e(TAG,"error" + e.toString());
                                    }

                                    @Override
                                    public void onNext(String s) {

                                    }
                                });
                    }
                });

                thread.start();

            }
        });

        //check if already liked
        HashMap<Integer, String> hm = (HashMap<Integer, String>) item.getLiked_by();
        Set<Integer> keys = hm.keySet();

        for(Integer key: keys){
            if(key==SELF_USER_ID){
                likeButton.setTag("1");
                likeButton.setBackgroundResource(R.color.colorPrimary);
            }
        }



        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout vParent = (LinearLayout)v.getParent();
                LinearLayout vvParent = (LinearLayout)vParent.getParent();

                LinearLayout vvchild = (LinearLayout)vvParent.getChildAt(5);
                TextView likesView = (TextView)vvchild.getChildAt(0);

                int spaceIndex = likesView.getText().toString().indexOf(" ");
                int like = Integer.parseInt(likesView.getText().toString().substring(0, spaceIndex));

                String tags[] = vvParent.getTag().toString().split(" ");
                final int position = Integer.parseInt(tags[2].split(":")[1]);

                FeedItem f = feedItems.get(position);
                Log.e(TAG, f.getLiked_by().toString());
                Intent intent = new Intent(activity.getApplicationContext(), CommentActivity.class);
                intent.putExtra("feedObj",  f);
                activity.startActivity(intent);

            }
        });


        likeButton.setOnClickListener(new View.OnClickListener() {
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

                if((likeButton.getTag().toString().equals("0")))
                {
                    like+=1;
                    likesView.setText(like + " likes");
                    likeButton.setTag("1");
                    likeButton.setBackgroundResource(R.color.colorPrimary);
                    item.addLiked_by(SELF_USER_ID, SomeClass.users.get(SELF_USER_ID));
                    //Log.d(TAG, String.valueOf(item.getLiked_by()));
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
                    likeButton.setTag("0");
                    likeButton.setBackgroundResource(android.R.drawable.btn_default);
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
        return convertView;
    }


    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}