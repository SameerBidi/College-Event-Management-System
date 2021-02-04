package com.cems.network;

import com.cems.model.Event;
import com.cems.model.ServerResponse;
import com.cems.model.User;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("login")
    Call<ServerResponse> login(@Body User user);

    @POST("register")
    Call<ServerResponse> register(@Body User user);

    @POST("postEvent")
    Call<ServerResponse> postEvent(@Header("apiKey") String apiKey, @Body Event event);

    @POST("getEvents")
    Call<ServerResponse> getEvents(@Header("apiKey") String apiKey);

    @POST("getMyEvents")
    Call<ServerResponse> getMyEvents(@Header("apiKey") String apiKey);

    @POST("getRegisteredEvents")
    Call<ServerResponse> getRegisteredEvents(@Header("apiKey") String apiKey);

    @POST("registerToEvent")
    Call<ServerResponse> registerToEvent(@Header("apiKey") String apiKey, @Body String eventID);

    @POST("checkRegistered")
    Call<ServerResponse> checkRegistered(@Header("apiKey") String apiKey, @Body String eventID);

    @POST("getEventRegistrations")
    Call<ServerResponse> getEventRegistrations(@Header("apiKey") String apiKey, @Body String eventID);

    @POST("deleteEvent")
    Call<ServerResponse> deleteEvent(@Header("apiKey") String apiKey, @Body String eventID);
}
