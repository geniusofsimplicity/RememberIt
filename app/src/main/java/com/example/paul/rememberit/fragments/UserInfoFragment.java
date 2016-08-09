package com.example.paul.rememberit.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.paul.rememberit.R;
import com.example.paul.rememberit.helpers.MetaFileHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Paul on 22.06.2016.
 */
public class UserInfoFragment extends Fragment {


    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_user_info, container, false);
        update();

        return view;
    }

    private void readMetaFromFile() {
        InputStream inputStream = null;
        try {
            inputStream = getActivity().openFileInput(MetaFileHelper.metafileName);
            new MetaFileHelper().readMetaFromFile(inputStream);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction();
    }

    public void update(){
        readMetaFromFile();
        TextView userNameTextView = (TextView) view.findViewById(R.id.fragment_user_info_user_name);
        userNameTextView.setText(MetaFileHelper.userName);
        TextView subjectTextView = (TextView) view.findViewById(R.id.fragment_user_info_subject);
        subjectTextView.setText(MetaFileHelper.currentSubject);
    }
}
