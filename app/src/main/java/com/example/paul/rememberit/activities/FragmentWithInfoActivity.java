package com.example.paul.rememberit.activities;

import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.paul.rememberit.R;
import com.example.paul.rememberit.fragments.UserInfoFragment;


public abstract class FragmentWithInfoActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager manager = getSupportFragmentManager();

        Fragment userInfoFragment = manager.findFragmentById(R.id.fragment_user_info);
        if (userInfoFragment == null) {
            userInfoFragment = new UserInfoFragment();
            manager.beginTransaction()
                    .add(R.id.fragment_user_info, userInfoFragment)
                    .commit();
        }

        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

    }


}
