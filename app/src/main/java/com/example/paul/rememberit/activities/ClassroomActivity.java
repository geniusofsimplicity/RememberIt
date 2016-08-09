package com.example.paul.rememberit.activities;

import android.support.v4.app.Fragment;

import com.example.paul.rememberit.fragments.ClassroomFragment;

/**
 * Created by Paul on 24.06.2016.
 */
public class ClassroomActivity extends FragmentWithInfoActivity {
    @Override
    protected Fragment createFragment() {
        return new ClassroomFragment();
    }
}
