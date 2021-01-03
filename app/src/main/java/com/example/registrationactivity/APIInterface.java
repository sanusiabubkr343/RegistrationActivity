package com.example.registrationactivity;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {

    @POST("userProfile") // This is your endPoint
    Call<Model> getResources(@Body Model model);

    // For Image
    @Multipart
    @POST("storageReference")
    Call<ServerResponse> upload(
            @Part MultipartBody.Part file,
            @Part ("file") RequestBody name
    );

}
