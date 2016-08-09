package com.example.paul.rememberit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.paul.rememberit.R;
import com.example.paul.rememberit.activities.ClassroomActivity;
import com.example.paul.rememberit.activities.OfficeActivity;

/**
 * Created by Paul on 24.06.2016.
 */
public class SchoolFragment extends Fragment {

    private Button mButtonClassroom;
    private Button mButtonOffice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school, container, false);

        mButtonClassroom = (Button)view.findViewById(R.id.button_school_enter_classroom);
        mButtonClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClassroomActivity.class);
                startActivity(intent);
            }
        });

        mButtonOffice = (Button)view.findViewById(R.id.button_school_enter_office);
        mButtonOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OfficeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
