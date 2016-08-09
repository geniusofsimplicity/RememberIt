package com.example.paul.rememberit.activities;

import android.support.v4.app.Fragment;

import com.example.paul.rememberit.fragments.UserFragment;

/**
 * Created by Paul on 24.06.2016.
 */
public class UserActivity extends FragmentWithInfoActivity {
    @Override
    protected Fragment createFragment() {
        return new UserFragment();
    }
}
