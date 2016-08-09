package com.example.paul.rememberit.activities;

import android.support.v4.app.Fragment;
import com.example.paul.rememberit.fragments.EditDefinitionFragment;

/**
 * Created by Paul on 23.06.2016.
 */
public class EditDefinitionActivity extends FragmentWithInfoActivity {
    @Override
    protected Fragment createFragment() {
        EditDefinitionFragment mFragment = new EditDefinitionFragment();
        return new EditDefinitionFragment();
    }
}
