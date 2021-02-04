package com.cems.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cems.BaseParentActivity;
import com.cems.CEMSDataStore;
import com.cems.R;
import com.cems.databinding.ActivityLoginBinding;
import com.cems.model.ServerResponse;
import com.cems.model.User;
import com.cems.model.UserData;
import com.cems.network.ApiInstance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseParentActivity {

    ActivityLoginBinding binding;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        progress = new ProgressDialog(this);

        setupListeners();
    }

    private void setupListeners() {
        binding.loginBtn.setOnClickListener(v -> {
            String username = binding.loginUsernameInput.getText().toString().trim();
            String password = binding.loginPasswordInput.getText().toString().trim();

            if (username.trim().equals("")) {
                showAlert(null, "Username cannot be empty");
                return;
            }
            if (password.trim().equals("")) {
                showAlert(null, "Password cannot be empty");
                return;
            }

            progress.setMessage("Logging in...");
            progress.setCancelable(false);
            progress.show();

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);

            Call<ServerResponse> data = ApiInstance.getClient().login(user);
            data.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    if (response.isSuccessful()) {
                        ServerResponse serverResponse = response.body();
                        if (serverResponse != null) {
                            if (serverResponse.getStatusCode() == 1) {
                                if (progress.isShowing()) {
                                    progress.dismiss();
                                }
                                showAlert(null, serverResponse.getMessage());
                                return;
                            }
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            if (progress.isShowing()) {
                                progress.dismiss();
                            }

                            CEMSDataStore.setUserData(gson.fromJson(serverResponse.getResponseJSON(), UserData.class));

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if (progress.isShowing()) {
                                progress.dismiss();
                            }
                            showAlert("Login Error", "Server Error Occured");
                        }
                    } else {
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                        showAlert("Login Error", "Server Error Occured");
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    if (progress.isShowing()) {
                        progress.dismiss();
                    }
                    showAlert("Login Error", "Server Error Occured");
                    t.printStackTrace();
                }
            });
        });

        binding.signupBtn.setOnClickListener(v -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Sign up as?");
            final View customLayout = getLayoutInflater().inflate(R.layout.layout_signup_alert, null);
            builder.setView(customLayout);
            final AlertDialog dialog = builder.create();
            customLayout.findViewById(R.id.alertStudBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.putExtra("regType", "Student");
                    dialog.dismiss();
                    startActivity(intent);
                }
            });
            customLayout.findViewById(R.id.alertStaffBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.putExtra("regType", "Staff");
                    dialog.dismiss();
                    startActivity(intent);
                }
            });
            dialog.show();
        });
    }

    private void showAlert(final String title, final String msg) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
        if (title != null) alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton("Back", (arg0, arg1) -> alert.create().dismiss());
        alert.create().show();
    }
}