package com.cems.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cems.CEMSDataStore;
import com.cems.R;
import com.cems.activities.AddEventActivity;
import com.cems.adapter.EventAdapter;
import com.cems.databinding.FragmentEventBinding;
import com.cems.databinding.FragmentRegisteredEventsBinding;
import com.cems.model.Event;
import com.cems.model.ServerResponse;
import com.cems.model.UserType;
import com.cems.network.ApiInstance;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisteredEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisteredEventsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentRegisteredEventsBinding binding;
    private ProgressDialog progress;

    public RegisteredEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisteredEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisteredEventsFragment newInstance(String param1, String param2) {
        RegisteredEventsFragment fragment = new RegisteredEventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registered_events, container, false);

        progress = new ProgressDialog(getActivity());

        progress.setMessage("Getting events...");
        progress.setCancelable(false);
        progress.show();

        Call<ServerResponse> data = ApiInstance.getClient().getRegisteredEvents((String) CEMSDataStore.getUserData().getApiKey());
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

                            Type arrayList = new TypeToken<ArrayList<Event>>(){}.getType();
                            ArrayList<Event> events = new Gson().fromJson(serverResponse.getResponseJSON(), arrayList);

                            EventAdapter adapter = new EventAdapter(getActivity(), events);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                            binding.registeredEventsRecyclerView.setLayoutManager(layoutManager);
                            binding.registeredEventsRecyclerView.setAdapter(adapter);
                        } else {
                            if (progress.isShowing()) {
                                progress.dismiss();
                            }
                            showAlert("Cannot get events", "Invalid Request\n" + serverResponse.getMessage());
                        }
                    }
                    else {
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                        showAlert("Cannot get events", "Invalid Request\nResponse null");
                    }
                }
                else {
                    if (progress.isShowing()) {
                        progress.dismiss();
                    }
                    showAlert("Cannot get events", "Invalid Request\nResponse failed");
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                showAlert("Cannot get events", "Server error occured");
            }
        });

        return binding.getRoot();
    }

    private void showAlert(final String title, final String msg) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        if (title != null) alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton("Back", (arg0, arg1) -> alert.create().dismiss());
        alert.create().show();
    }
}