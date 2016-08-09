package com.example.paul.rememberit.activities;

import android.support.v4.app.Fragment;

import com.example.paul.rememberit.fragments.DefinitionsFragment;

/**
 * Created by Paul on 23.06.2016.
 */
public class DefinitionsActivity extends FragmentWithInfoActivity {
    @Override
    protected Fragment createFragment() {
        return new DefinitionsFragment();
    }
}
