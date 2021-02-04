package com.cems.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cems.CEMSDataStore;
import com.cems.fragment.EventFragment;
import com.cems.fragment.MyEventFragment;
import com.cems.fragment.RegisteredEventsFragment;
import com.cems.model.UserType;

public class MainPagerAdapter extends FragmentPagerAdapter {
    public MainPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                EventFragment eventFragment = new EventFragment();
                return eventFragment;
            }
            case 1: {
                if(CEMSDataStore.getUserData().getUserType().equals(UserType.STUDENT)) {
                    RegisteredEventsFragment registeredEventsFragment = new RegisteredEventsFragment();
                    return registeredEventsFragment;
                }
                else {
                    MyEventFragment myEventFragment = new MyEventFragment();
                    return myEventFragment;
                }
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: {
                return "Events";
            }
            case 1: {
                if(CEMSDataStore.getUserData().getUserType().equals(UserType.STUDENT)) {
                    return "Registered Events";
                }
                else {
                    return "My Events";
                }
            }
        }
        return null;
    }
}
