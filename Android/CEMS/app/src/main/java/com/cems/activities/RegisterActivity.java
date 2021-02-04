package com.cems.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.cems.BaseParentActivity;
import com.cems.CEMSDataStore;
import com.cems.R;
import com.cems.databinding.ActivityRegisterBinding;
import com.cems.model.ServerResponse;
import com.cems.model.User;
import com.cems.model.UserType;
import com.cems.network.ApiInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseParentActivity {

    private User user = new User();

    private ActivityRegisterBinding binding;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        progress = new ProgressDialog(this);

        String regType = getIntent().getStringExtra("regType");

        if (regType != null) {
            if (regType.equalsIgnoreCase("Student")) {
                binding.studentLayout.setVisibility(View.VISIBLE);

                ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CEMSDataStore.getYears());
                ArrayAdapter<String> branchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CEMSDataStore.getBranches());

                yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.regStudentYearDrop.setAdapter(yearAdapter);
                binding.regStudentYearDrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        user.setYear(CEMSDataStore.getYears().get(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.regStudentBranchDrop.setAdapter(branchAdapter);
                binding.regStudentBranchDrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        user.setBranch(CEMSDataStore.getBranches().get(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

            } else {
                binding.studentLayout.setVisibility(View.INVISIBLE);
            }
        }

        binding.registerBtn.setOnClickListener(v -> {
            String username = binding.regUsernameInput.getText().toString().trim();
            String password = binding.regPasswordInput.getText().toString().trim();
            String name = binding.regNameInput.getText().toString().trim();
            String email = binding.regEmailInput.getText().toString().trim();
            String cno = binding.regCnoInput.getText().toString().trim();
            String rollno = binding.regRollnoInput.getText().toString().trim();


            if(TextUtils.isEmpty(username)
                    || TextUtils.isEmpty(password)
                    || TextUtils.isEmpty(name)
                    || TextUtils.isEmpty(email)
                    || TextUtils.isEmpty(cno)) {

                showAlert("Alert", "All fields are mandatory");
                return;
            }

            if(regType != null && regType.equalsIgnoreCase("Student") && (user.getYear().isEmpty() || user.getBranch().isEmpty() || TextUtils.isEmpty(rollno))) {
                showAlert("Alert", "All fields are mandatory");
                return;
            }

            progress.setMessage("Registering...");
            progress.setCancelable(false);
            progress.show();

            user.setUserType(regType.equals("Student") ? UserType.STUDENT : UserType.STAFF);
            user.setUsername(username);
            user.setPassword(password);
            user.setName(name);
            user.setEmail(email);
            user.setCno(cno);
            user.setRollno(rollno);

            Call<ServerResponse> data = ApiInstance.getClient().register(user);
            data.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    if(response.isSuccessful())
                    {
                        ServerResponse serverResponse = response.body();
                        if(serverResponse != null)
                        {
                            if(serverResponse.getStatusCode() == 1)
                            {
                                if(progress.isShowing()) { progress.dismiss(); }
                                showAlert("Registration Error", serverResponse.getMessage());
                                return;
                            }
                            if(progress.isShowing()) { progress.dismiss(); }
                            showAlert("Registration Success","1: Confirm your email\n2: Activate your account by contacting admin");
                        }
                        else {
                            if(progress.isShowing()) { progress.dismiss(); }
                            showAlert("Registration Error", "Server Error Occured");
                        }
                    }
                    else {
                        if(progress.isShowing()) { progress.dismiss(); }
                        showAlert("Registration Error", "Server Error Occured");
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    if(progress.isShowing()) { progress.dismiss(); }
                    showAlert("Registration Error", "Server Error Occured");
                    t.printStackTrace();
                }
            });
        });
    }

    private void showAlert(final String title, final String msg)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
        if(title != null) alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton("Back", (arg0, arg1) -> alert.create().dismiss());
        alert.create().show();
    }
}