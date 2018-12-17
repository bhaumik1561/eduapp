package com.example.bhaum.eduapp;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

interface Service{

    @Multipart
    @POST("create/")
    Call<ResponseBody> createPost(
            @Part("user_id") RequestBody user_id,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part myfile
            );
}