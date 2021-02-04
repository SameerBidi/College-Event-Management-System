package com.cems.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cems.BaseParentActivity;
import com.cems.CEMSDataStore;
import com.cems.R;
import com.cems.adapter.MainPagerAdapter;
import com.cems.databinding.ActivityMainBinding;
import com.cems.model.UserType;

public class MainActivity extends BaseParentActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if(CEMSDataStore.getUserData().getUserType() == UserType.STUDENT) {
            binding.newEventFab.setVisibility(View.GONE);
        }

        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(),0);
        binding.mainViewPager.setAdapter(adapter);
        binding.mainTabLayout.setupWithViewPager(binding.mainViewPager);

        binding.newEventFab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(CEMSDataStore.getUserData().getUserType().equals(UserType.ADMIN)) {
            getMenuInflater().inflate(R.menu.admin_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logoutItem) {
            logout(MainActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout(Activity activity) {
        CEMSDataStore.setUserData(null);
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        switch (binding.mainTabLayout.getSelectedTabPosition()) {
            case 0:
                showLogoutAlert();
                break;
            case 1:
                binding.mainTabLayout.getTabAt(0).select();
                break;
        }
    }

    private void showLogoutAlert() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setMessage("Do you want to logout?");
        alert.setPositiveButton("Yes", (arg0, arg1) -> {
            logout(MainActivity.this);
        });
        alert.setNegativeButton("Cancel", (dialog, which) -> {
            alert.create().dismiss();
        });
        alert.create().show();
    }
}