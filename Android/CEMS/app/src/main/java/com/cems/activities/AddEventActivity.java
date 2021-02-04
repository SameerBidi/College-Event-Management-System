package com.cems.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.cems.BaseParentActivity;

import com.cems.CEMSDataStore;
import com.cems.R;
import com.cems.databinding.ActivityAddEventBinding;
import com.cems.model.Event;
import com.cems.model.EventType;
import com.cems.model.ServerResponse;
import com.cems.network.ApiInstance;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEventActivity extends BaseParentActivity {

    ActivityAddEventBinding binding;

    Event event = new Event();

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_event);

        progress = new ProgressDialog(this);

        binding.eventTypeDrop.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EventType.values()));
        binding.eventTypeDrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                event.setEventType(EventType.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.eventDateBtn.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                Calendar selDate = Calendar.getInstance();
                selDate.set(year, month, dayOfMonth);

                String date = new SimpleDateFormat("dd MMM yyyy").format(selDate.getTime());
                event.setEventDate(date);
                binding.eventDateBtn.setText(date);

            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        binding.eventStartTimeBtn.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                Calendar selTime = Calendar.getInstance();
                selTime.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

                String time = new SimpleDateFormat("hh:mm a").format(selTime.getTime());
                event.setEventStartTime(time);
                binding.eventStartTimeBtn.setText(time);

            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
            timePickerDialog.show();
        });

        binding.eventEndTimeBtn.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                Calendar selTime = Calendar.getInstance();
                selTime.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

                String time = new SimpleDateFormat("hh:mm a").format(selTime.getTime());
                event.setEventEndTime(time);
                binding.eventEndTimeBtn.setText(time);

            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
            timePickerDialog.show();
        });

        binding.eventSelYearsBtn.setOnClickListener(v -> {
            showArrayListSelectionAlert("Select Applicable Years", new YearAdapter(AddEventActivity.this, CEMSDataStore.getYears()));
        });

        binding.eventSelBranchesBtn.setOnClickListener(v -> {
            showArrayListSelectionAlert("Select Applicable Branches", new BranchAdapter(AddEventActivity.this, CEMSDataStore.getBranches()));
        });

        binding.addEventBtn.setOnClickListener(v -> {
            String name = binding.eventNameInput.getText().toString().trim();
            String venue = binding.eventVenueInput.getText().toString().trim();
            String cost = binding.eventCostInput.getText().toString().trim();

            if(TextUtils.isEmpty(name)
                    || TextUtils.isEmpty(venue)
                    || TextUtils.isEmpty(cost)
                    || event.getEventType() == null
                    || event.getEventDate() == null
                    || event.getEventStartTime() == null
                    || event.getEventEndTime() == null
                    || !event.getEventDate().matches(".*\\d.*")
                    || !event.getEventStartTime().matches(".*\\d.*")
                    || !event.getEventEndTime().matches(".*\\d.*")
                    || event.getYears().isEmpty()
                    || event.getBranches().isEmpty()) {

                showAlert(null, "All fields are necessary");

                return;
            }

            event.setEventName(name);
            event.setEventVenue(venue);
            event.setEventCost(cost);

            progress.setMessage("Adding event...");
            progress.setCancelable(false);
            progress.show();

            Call<ServerResponse> data = ApiInstance.getClient().postEvent((String) CEMSDataStore.getUserData().getApiKey(), event);
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

                                Toast.makeText(AddEventActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                if (progress.isShowing()) {
                                    progress.dismiss();
                                }
                                showAlert("Cannot add event", "Invalid Request\n" + serverResponse.getMessage());
                            }
                        } else {
                            if (progress.isShowing()) {
                                progress.dismiss();
                            }
                            showAlert("Cannot add event", "Server error occured\nResponse null");
                        }
                    } else {
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                        showAlert("Cannot add event", "Server error occured\nResponse failed");
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    if (progress.isShowing()) {
                        progress.dismiss();
                    }
                    t.printStackTrace();
                    showAlert("Cannot add event", "Server error occured");
                }
            });
        });
    }

    private void showArrayListSelectionAlert(String title, ArrayAdapter<String> adapter) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
        builder.setTitle(title);
        final View customLayout = getLayoutInflater().inflate(R.layout.layout_listview_alert, null);
        ListView listView = customLayout.findViewById(R.id.alertListView);
        listView.setAdapter(adapter);
        builder.setPositiveButton("OK", (dialog, which) -> {
            if(!event.getYears().isEmpty()) {
                binding.eventSelYearsBtn.setText(new Gson().toJson(event.getYears()).replaceAll("[\\[\\]\"]", "").replaceAll("[,]", ", "));
            }
            else {
                binding.eventSelYearsBtn.setText("SELECT YEARS");
            }

            if(!event.getBranches().isEmpty()) {
                binding.eventSelBranchesBtn.setText(new Gson().toJson(event.getBranches()).replaceAll("[\\[\\]\"]", "").replaceAll("[,]", ", "));
            }
            else {
                binding.eventSelBranchesBtn.setText("SELECT BRANCHES");
            }
            builder.create().dismiss();
        });
        builder.setView(customLayout);
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class YearAdapter extends ArrayAdapter<String> {

        public YearAdapter(@NonNull Context context, ArrayList<String> data) {
            super(context, 0, data);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String value = getItem(position);

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_checkbox_item, parent, false);
            }

            CheckBox checkBox = convertView.findViewById(R.id.lvChkItem);
            checkBox.setText(value);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (!event.getYears().contains(value)) {
                        event.getYears().add(value);
                    }
                }
                else {
                    event.getYears().remove(value);
                }
            });

            if (event.getYears().contains(value)) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

            return convertView;
        }
    }

    private class BranchAdapter extends ArrayAdapter<String> {

        public BranchAdapter(@NonNull Context context, ArrayList<String> data) {
            super(context, 0, data);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String value = getItem(position);

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_checkbox_item, parent, false);
            }

            CheckBox checkBox = convertView.findViewById(R.id.lvChkItem);
            checkBox.setText(value);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (!event.getBranches().contains(value)) {
                        event.getBranches().add(value);
                    }
                }
                else {
                    event.getBranches().remove(value);
                }
            });

            if (event.getBranches().contains(value)) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

            return convertView;
        }
    }

    private void showAlert(final String title, final String msg) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(AddEventActivity.this);
        if (title != null) alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton("Back", (arg0, arg1) -> alert.create().dismiss());
        alert.create().show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}