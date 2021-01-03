package com.example.registrationactivity;

import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class APICLIENT {
    private static String baseUrl = "https://5ff17db8db1158001748b061.mockapi.io/api/userDetails/Registration/";
     private  static  Retrofit retrofit = null;
    static  Retrofit getClient()
    {
        return new Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
    }
}
