package com.example.tfgapp.API;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = "http://tfgapi-env.eba-xqpimhpg.us-east-2.elasticbeanstalk.com/";


    public static Retrofit retrofit = null;

    public static Retrofit getApiClient() {
        if(retrofit == null) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .build();

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}