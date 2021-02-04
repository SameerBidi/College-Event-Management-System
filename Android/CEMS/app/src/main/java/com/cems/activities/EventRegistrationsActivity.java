package com.cems.activities;

import com.cems.CEMSDataStore;
import com.cems.R;
import com.cems.adapter.EventAdapter;
import com.cems.adapter.EventRegistrationsAdapter;
import com.cems.databinding.ActivityEventRegistrationsBinding;
import com.cems.model.Event;
import com.cems.model.EventRegistrations;
import com.cems.model.ServerResponse;
import com.cems.network.ApiInstance;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventRegistrationsActivity extends AppCompatActivity {

    private ActivityEventRegistrationsBinding binding;
    private ProgressDialog progress;
    String eventID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_registrations);

        progress = new ProgressDialog(this);

        eventID = getIntent().getStringExtra("eventID");

        progress.setMessage("Getting registrations...");
        progress.setCancelable(false);
        progress.show();

        Call<ServerResponse> data = ApiInstance.getClient().getEventRegistrations((String) CEMSDataStore.getUserData().getApiKey(), eventID);
        data.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    ServerResponse serverResponse = response.body();
                    if (serverResponse != null) {
                        if (serverResponse.getStatusCode() == 0) {
                            if (progress.isShowing()) {
                                progress.dismiss();
                            }

                            Type arrayList = new TypeToken<ArrayList<EventRegistrations>>(){}.getType();
                            ArrayList<EventRegistrations> eventRegistrationsList = new Gson().fromJson(serverResponse.getResponseJSON(), arrayList);

                            EventRegistrationsAdapter adapter = new EventRegistrationsAdapter(EventRegistrationsActivity.this, eventRegistrationsList);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EventRegistrationsActivity.this, RecyclerView.VERTICAL, false);
                            binding.eventRegistrationsRecyclerView.setLayoutManager(layoutManager);
                            binding.eventRegistrationsRecyclerView.setAdapter(adapter);
                        } else {
                            if (progress.isShowing()) {
                                progress.dismiss();
                            }
                            showAlert("Cannot get registrations", "Invalid Request\n" + serverResponse.getMessage());
                        }
                    }
                    else {
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                        showAlert("Cannot get registrations", "Invalid Request\nResponse null");
                    }
                }
                else {
                    if (progress.isShowing()) {
                        progress.dismiss();
                    }
                    showAlert("Cannot get registrations", "Invalid Request\nResponse failed");
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                showAlert("Cannot get registrations", "Server error occured");
            }
        });
    }

    private void showAlert(final String title, final String msg) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(EventRegistrationsActivity.this);
        if (title != null) alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton("Back", (arg0, arg1) -> alert.create().dismiss());
        alert.create().show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EventRegistrationsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}