package com.example.bhaum.eduapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.bhaum.eduapp.app.AppController.TAG;
public class CreatePost extends AppCompatActivity {
    int REQUEST_CODE = 1234;
    EditText description;
    Button chooseFile, Post;
    String user_id;
    private Uri uri;
    private Service uploadService;
    String UPLOAD_URL = "http:192.168.0.103:8000/api/news/";
    //ProgressDialog progress;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        chooseFile = (Button)findViewById(R.id.chooseFileBtn);
        Post = (Button)findViewById(R.id.postBtn);
        description = (EditText)findViewById(R.id.description);
        user_id = getIntent().getStringExtra("USER_ID");
        Log.d(TAG, "USER ID: " + user_id);
        //progressBar = (ProgressBar)findViewById(R.id.prg);
        //get the storage permission
        getStoragePermission();


        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select file"),REQUEST_CODE);
            }
        });





        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressBar.setVisibility(View.VISIBLE);
                Log.d(TAG,"inside post click button");
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

                uploadService = new Retrofit.Builder()
                        .baseUrl(UPLOAD_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(Service.class);
                uploadFile();

            }
        });
    }


    private void getStoragePermission() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }

    }

    protected void uploadFile(){
        String descriptionText = description.getText().toString();
        Call<ResponseBody> fileUpload = null;
        if(uri!=null && !descriptionText.isEmpty()) {
            String filePath = getRealPathFromURIPath(uri, CreatePost.this);
            File file = new File(filePath);
            Log.d(TAG, "filePath=" + filePath);
            RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("myfile", file.getName(), mFile);
            RequestBody descrp = RequestBody.create(MediaType.parse("text/plain"), description.getText().toString());
            RequestBody usr_id = RequestBody.create(MediaType.parse("text/plain"), user_id);

            fileUpload = uploadService.createPost(usr_id, descrp, fileToUpload);

        }
        else if (uri == null  && !descriptionText.isEmpty()) {

            RequestBody descrp = RequestBody.create(MediaType.parse("text/plain"), descriptionText);
            RequestBody usr_id = RequestBody.create(MediaType.parse("text/plain"), user_id);

            fileUpload = uploadService.createPost(usr_id, descrp, null);

        }
        else if (uri!=null && descriptionText.isEmpty())
        {
            String filePath = getRealPathFromURIPath(uri, CreatePost.this);
            File file = new File(filePath);
            Log.d(TAG, "filePath=" + filePath);
            RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("myfile", file.getName(), mFile);
            RequestBody usr_id = RequestBody.create(MediaType.parse("text/plain"), user_id);


            fileUpload = uploadService.createPost(usr_id, null, fileToUpload);
        }
        else{
            Toast.makeText(CreatePost.this, "Post Cannot be empty", Toast.LENGTH_SHORT).show();
        }

        if(fileUpload !=null) {
            fileUpload.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(CreatePost.this, "Posted", Toast.LENGTH_LONG).show();
                    Intent back = new Intent(CreatePost.this, NavigationTab.class);
                    startActivity(back);

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, "Error " + t.getMessage());
                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            Log.e(TAG,"inside ActivityResult");
            uri = data.getData();
            chooseFile.setText(uri.toString());
            Log.e(TAG, uri.toString());
        }
        else {
            Log.d(TAG, "error");
        }
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1001:{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Permission Granted", Toast.LENGTH_SHORT ).show();
                }else{
                    Toast.makeText(this,"Permission not Granted", Toast.LENGTH_SHORT ).show();
                    finish();
                }
            }

        }
    }

}
