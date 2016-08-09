package com.example.paul.rememberit.activities;

import android.support.v4.app.Fragment;

import com.example.paul.rememberit.fragments.EditExampleFragment;

/**
 * Created by Paul on 23.06.2016.
 */
public class EditExampleActivity extends FragmentWithInfoActivity {
    @Override
    protected Fragment createFragment() {
        return new EditExampleFragment();
    }
}
