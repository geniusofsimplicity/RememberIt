package com.example.paul.rememberit.activities;

import android.support.v4.app.Fragment;

import com.example.paul.rememberit.fragments.StartFragment;


public class StartActivity extends FragmentWithInfoActivity {

    @Override
    protected Fragment createFragment() {
        return StartFragment.createFragment();
    }


}
