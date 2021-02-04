package com.cems.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cems.CEMSDataStore;
import com.cems.R;
import com.cems.activities.AddEventActivity;
import com.cems.activities.EventRegistrationsActivity;
import com.cems.activities.LoginActivity;
import com.cems.activities.MainActivity;
import com.cems.activities.RegisterActivity;
import com.cems.databinding.ActivityAddEventBinding;
import com.cems.databinding.LayoutEventBinding;
import com.cems.model.Event;
import com.cems.model.EventRegistrations;
import com.cems.model.ServerResponse;
import com.cems.model.User;
import com.cems.model.UserType;
import com.cems.network.ApiInstance;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.NoticeViewHolder> {

    private ArrayList<Event> events;
    private Activity mActivity;
    private ProgressDialog progress;

    public EventAdapter(Activity mActivity, ArrayList<Event> events) {
        this.events = events;
        this.mActivity = mActivity;
        this.progress = new ProgressDialog(mActivity);
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutEventBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.layout_event, parent, false);
        return new NoticeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        Event event = events.get(position);

        holder.binding.eventNameTv.setText(event.getEventName());
        holder.binding.eventDateTv.setText(event.getEventDate());
        holder.binding.eventStartTimeTv.setText(event.getEventStartTime());
        holder.binding.eventEndTimeTv.setText(event.getEventEndTime());
        holder.binding.eventTypeTv.setText("Type: " + event.getEventType().toString());
        holder.binding.eventVenueTv.setText("Venue: " + event.getEventVenue());
        holder.binding.eventPriceTv.setText("Cost: " + event.getEventCost() + " Rs");

        if(CEMSDataStore.getUserData().getUserType() == UserType.STUDENT) {
            checkRegistered(event.getEventID(), holder.binding.eventRegBtn);
        }
        else {
            holder.binding.eventCv.setOnLongClickListener(v -> {

                if(CEMSDataStore.getUserData().getUserType().equals(UserType.ADMIN)) {
                    showEventOptionsAlert(event.getEventID());
                }
                else {
                    if(CEMSDataStore.getUserData().getUserID().equals(event.getEventRegistrarID())) {
                        showEventOptionsAlert(event.getEventID());
                    }
                    else {
                        viewEventRegistrations(event.getEventID());
                    }
                }

                return true;
            });

            holder.binding.eventRegBtn.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class NoticeViewHolder extends RecyclerView.ViewHolder {

        LayoutEventBinding binding;

        public NoticeViewHolder(LayoutEventBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    private void checkRegistered(String eventID, Button button) {

        Call<ServerResponse> data = ApiInstance.getClient().checkRegistered((String) CEMSDataStore.getUserData().getApiKey(), eventID);
        data.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    ServerResponse serverResponse = response.body();
                    if (serverResponse != null) {
                        if (serverResponse.getStatusCode() == 0) {
                            boolean registered = new Gson().fromJson(serverResponse.getResponseJSON(), boolean.class);
                            if(registered) {
                                button.setText("REGISTERED");
                                button.setEnabled(false);
                            }
                            else {
                                button.setOnClickListener(v -> {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                    builder.setTitle("Do you really want to register?");

                                    // add the buttons
                                    builder.setPositiveButton("Yes", (dialog, which) -> {
                                        registerToEvent(eventID);
                                    });
                                    builder.setNegativeButton("Cancel", null);

                                    // create and show the alert dialog
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void registerToEvent(String eventID) {
        progress.setMessage("Registering event...");
        progress.setCancelable(false);
        progress.show();

        Call<ServerResponse> data = ApiInstance.getClient().registerToEvent((String) CEMSDataStore.getUserData().getApiKey(), eventID);
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

                            Toast.makeText(mActivity, "Success", Toast.LENGTH_SHORT).show();

                            mActivity.finish();
                            mActivity.overridePendingTransition(0, 0);
                            mActivity.startActivity(mActivity.getIntent());
                            mActivity.overridePendingTransition(0, 0);

                        } else {
                            if (progress.isShowing()) {
                                progress.dismiss();
                            }
                            showAlert("Cannot register to event", serverResponse.getMessage());
                        }
                    } else {
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                        showAlert("Cannot register to event", "Server error occured\nResponse null");
                    }
                } else {
                    if (progress.isShowing()) {
                        progress.dismiss();
                    }
                    showAlert("Cannot register to event", "Server error occured\nResponse failed");
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                t.printStackTrace();
                showAlert("Cannot register to event", "Server error occured");
            }
        });
    }

    private void viewEventRegistrations(String eventID) {
        Intent intent = new Intent(mActivity, EventRegistrationsActivity.class);
        intent.putExtra("eventID", eventID);
        mActivity.startActivity(intent);
    }

    private void deleteEvent(String eventID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Are you sure you want to delete?");

        // add the buttons
        builder.setPositiveButton("Yes", (dialog, which) -> {
            progress.setMessage("Deleting event...");
            progress.setCancelable(false);
            progress.show();

            Call<ServerResponse> data = ApiInstance.getClient().deleteEvent((String) CEMSDataStore.getUserData().getApiKey(), eventID);
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
                                showAlert("Event", "Event Deleted");
                                mActivity.finish();
                                mActivity.overridePendingTransition(0, 0);
                                mActivity.startActivity(mActivity.getIntent());
                                mActivity.overridePendingTransition(0, 0);
                            } else {
                                if (progress.isShowing()) {
                                    progress.dismiss();
                                }
                                showAlert("Cannot delete event", serverResponse.getMessage());
                            }
                        }
                        else {
                            if (progress.isShowing()) {
                                progress.dismiss();
                            }
                            showAlert("Cannot delete event", "Invalid Request\nResponse null");
                        }
                    }
                    else {
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                        showAlert("Cannot delete event", "Invalid Request\nResponse failed");
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    if (progress.isShowing()) {
                        progress.dismiss();
                    }
                    showAlert("Cannot delete event", "Server error occured");
                }
            });
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showEventOptionsAlert(String eventID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Choose an option");

        String[] options = {"View Registrations", "Delete Event", "Cancel"};
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    viewEventRegistrations(eventID);
                    break;
                case 1:
                    deleteEvent(eventID);
                    break;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlert(final String title, final String msg) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
        if (title != null) alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton("Back", (arg0, arg1) -> alert.create().dismiss());
        alert.create().show();
    }
}
