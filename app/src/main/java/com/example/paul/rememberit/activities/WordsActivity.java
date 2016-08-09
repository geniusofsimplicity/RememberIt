package com.example.paul.rememberit.activities;

import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.paul.rememberit.R;
import com.example.paul.rememberit.fragments.WordsFragment;

public class WordsActivity extends FragmentWithInfoActivity {

    @Override
    protected Fragment createFragment() {
        return new WordsFragment();
    }



}
