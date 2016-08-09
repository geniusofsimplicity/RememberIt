package com.example.paul.rememberit.activities;

import android.support.v4.app.Fragment;

import com.example.paul.rememberit.fragments.EditWordFragment;

/**
 * Created by Paul on 22.06.2016.
 */
public class EditWordActivity extends FragmentWithInfoActivity {
    @Override
    protected Fragment createFragment() {
        return new EditWordFragment();
    }
}
