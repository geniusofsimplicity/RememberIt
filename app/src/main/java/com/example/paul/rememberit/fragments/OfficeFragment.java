package com.example.paul.rememberit.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.paul.rememberit.R;
import com.example.paul.rememberit.activities.UserActivity;
import com.example.paul.rememberit.helpers.MetaFileHelper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Paul on 24.06.2016.
 */
public class OfficeFragment extends Fragment{

    private Button mButtonSetupUser;
    private Spinner spinnerSubject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_office, container, false);

        mButtonSetupUser = (Button)view.findViewById(R.id.button_office_setup_user);
        mButtonSetupUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserActivity.class);
                startActivity(intent);
            }
        });

        spinnerSubject = (Spinner)view.findViewById(R.id.spinner_office_subject);
        ArrayAdapter<CharSequence> adapterSubject = ArrayAdapter.createFromResource(getActivity(), R.array.language, R.layout.spinner_language_dropdown_item);
        adapterSubject.setDropDownViewResource(R.layout.spinner_subject_dropdown_item);
        spinnerSubject.setAdapter(adapterSubject);
        int spinnerPos = adapterSubject.getPosition(MetaFileHelper.currentSubject);
        spinnerSubject.setSelection(spinnerPos);
        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MetaFileHelper.currentSubject = spinnerSubject.getSelectedItem().toString();
                try {
                    FileOutputStream fileOutputStream = getActivity().openFileOutput(MetaFileHelper.metafileName, Context.MODE_PRIVATE);
                    new MetaFileHelper().saveMetaToFile(fileOutputStream);
                    updateUserInfo();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    private void updateUserInfo(){
        UserInfoFragment userInfoFragment = (UserInfoFragment) getFragmentManager().findFragmentById(R.id.fragment_user_info);
        userInfoFragment.update();
    }


}
