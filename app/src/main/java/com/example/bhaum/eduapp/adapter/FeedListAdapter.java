package com.example.bhaum.eduapp.adapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bhaum.eduapp.FeedImageView;
import com.example.bhaum.eduapp.volley.LruBitmapCache;
import com.example.bhaum.eduapp.app.AppController;
import com.example.bhaum.eduapp.data.FeedItem;
import com.example.bhaum.eduapp.R;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import static com.example.bhaum.eduapp.app.AppController.TAG;

public class FeedListAdapter extends BaseAdapter {
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
        FeedImageView feedImageView = (FeedImageView) convertView
                .findViewById(R.id.feedImage1);
        final Button downloadFileBtn = (Button)convertView
                .findViewById(R.id.downloadFile);
        final TextView totalLikes = (TextView)convertView
                .findViewById(R.id.totalLikes);
        final Button likeButton = (Button)convertView
                .findViewById(R.id.likeBtn);

        final FeedItem item = feedItems.get(position);

        name.setText(item.getName());

        //setting news_id and user_id on root tag
        root.setTag("news_id:"+ item.getId() + " user_id:" + item.getUser_id());
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

        // user profile pic
        //profilePic.setImageUrl(item.getProfilePic(), imageLoader);
        profilePic.setDefaultImageResId(R.drawable.ic_account_circle_black_24dp);

        // Feed image
        String url = "http://192.168.0.103:8000/static/files/"+ item.getAttachement();
        String attachementType = item.getAttachmentType();

        if(attachementType.toLowerCase().equals("jpg") || attachementType.toLowerCase().equals("jpeg") || attachementType.toLowerCase().equals("png") || attachementType.toLowerCase().equals("bmp"))
        {
            //Log.e(TAG, item.getAttachmentType()+"check");
            //Log.e(TAG, item.getAttachement());
            downloadFileBtn.setVisibility(View.GONE);
            feedImageView.setImageUrl(url, imageLoader);
            feedImageView.setVisibility(View.VISIBLE);
            feedImageView
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
        }
        else
        {
            feedImageView.setVisibility(View.GONE);
            downloadFileBtn.setText("Download " + item.getAttachement());
        }

        downloadFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code for checking sd card but in emulator it is not available
               /* if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
                    Log.d(TAG, "available");
                }
                else
                {
                    Log.d(TAG, "not available");
                }

                */
                //String str = (String) downloadFileBtn.getText();
                //filename = str.substring(str.indexOf(" ")+1);
                //URL_FILE += filename;
                //new DownloadFileFromURL().execute(URL_FILE);
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
                final int user_id = Integer.parseInt(tags[1].split(":")[1]);
                //Log.e(TAG, String.valueOf(news_id) + String.valueOf(user_id));

                if((likeButton.getTag().toString().equals("0")))
                {
                    like+=1;
                    likesView.setText(like + " likes");
                    likeButton.setTag("1");
                    likeButton.setBackgroundResource(R.color.colorPrimary);

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
                            params.put("user_id", String.valueOf(user_id));

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
                            params.put("user_id", String.valueOf(user_id));

                            return params;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(postRequest);
                }
            }
        });
        return convertView;
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getDataDirectory().toString()
                        + "/" + filename);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            //pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            //dismissDialog(progress_bar_type);

        }
    }

    //checking for external storage available
    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}