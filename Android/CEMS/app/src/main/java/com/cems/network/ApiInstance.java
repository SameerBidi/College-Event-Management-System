package com.cems.network;

import com.cems.CEMSDataStore;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiInstance {


    public static ApiInterface getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CEMSDataStore.getBaseRequestURL())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiInterface.class);
    }
}
