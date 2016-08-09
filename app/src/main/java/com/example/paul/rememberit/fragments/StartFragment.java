package com.example.paul.rememberit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.paul.rememberit.R;
import com.example.paul.rememberit.activities.DefinitionsActivity;
import com.example.paul.rememberit.activities.SchoolActivity;
import com.example.paul.rememberit.activities.WordsActivity;

/**
 * Created by Paul on 22.06.2016.
 */
public class StartFragment extends Fragment {

    private Button mWordsButton;
    private Button mDefinitionsButton;
    private Button mSchoolButton;

    public static StartFragment createFragment(){
        return new StartFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        mWordsButton = (Button)view.findViewById(R.id.fragment_start_words_button);
        mWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), WordsActivity.class);
                startActivity(intent);
            }
        });

        mDefinitionsButton = (Button)view.findViewById(R.id.fragment_start_definitions_button);
        mDefinitionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DefinitionsActivity.class);
                startActivity(intent);
            }
        });
        mSchoolButton = (Button)view.findViewById(R.id.fragment_start_school_button);
        mSchoolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SchoolActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void openWordsMenu(View view){
//        Intent intent = new Intent(this,WordsMenuActivity.class);
//        startActivity(intent);
    }

    public void openDefinitionsMenu(View view){
//        Intent intent = new Intent(this,DefinitionsMenuActivity.class);
//        startActivity(intent);
    }

    public void enterSchool(View view){
//        Intent intent = new Intent(this, SchoolActivity.class);
//        startActivity(intent);
    }

}
