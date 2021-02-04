package com.cems.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cems.R;
import com.cems.databinding.LayoutRegistrationsBinding;
import com.cems.model.EventRegistrations;

import java.util.ArrayList;

public class EventRegistrationsAdapter extends RecyclerView.Adapter<EventRegistrationsAdapter.NoticeViewHolder> {

    private ArrayList<EventRegistrations> eventRegistrationsList;
    private Activity mActivity;
    private ProgressDialog progress;

    public EventRegistrationsAdapter(Activity mActivity, ArrayList<EventRegistrations> eventRegistrationsList) {
        this.eventRegistrationsList = eventRegistrationsList;
        this.mActivity = mActivity;
        this.progress = new ProgressDialog(mActivity);
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutRegistrationsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.layout_registrations, parent, false);
        return new NoticeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        EventRegistrations eventRegistrations = eventRegistrationsList.get(position);

        holder.binding.nameTv.setText(eventRegistrations.getName());
        holder.binding.rollnoTv.setText("Roll No: " + eventRegistrations.getRollno());
        holder.binding.dateTv.setText("Date: " + eventRegistrations.getRegDate());
        holder.binding.timeTv.setText("Time: " + eventRegistrations.getRegTime());
    }

    @Override
    public int getItemCount() {
        return eventRegistrationsList.size();
    }

    public class NoticeViewHolder extends RecyclerView.ViewHolder {

        LayoutRegistrationsBinding binding;

        public NoticeViewHolder(LayoutRegistrationsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
