package com.example.paul.rememberit.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paul.rememberit.R;
import com.example.paul.rememberit.helpers.DbContract;
import com.example.paul.rememberit.helpers.DbHelper;
import com.example.paul.rememberit.helpers.MetaFileHelper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Paul on 24.06.2016.
 */
public class UserFragment extends Fragment {

    private Toast toast;
    private static CharSequence MESSAGE_SAVED = "Added";
    private static CharSequence MESSAGE_UNEXP_ERROR = "Error in DB.";
    private static int TOAST_DURATION = Toast.LENGTH_SHORT;
    private Button mButtonAddNew;
    private Button mButtonUpdate;
    private EditText editTextName;
    private DbHelper mDbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);

        editTextName = (EditText)view.findViewById(R.id.edit_text_create_user_name);

        mButtonAddNew = (Button) view.findViewById(R.id.button_edit_user_new);
        mButtonAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = editTextName.getText().toString();

                if(mDbHelper == null){
                    mDbHelper = new DbHelper(getActivity());
                }

                SQLiteDatabase dbWrite = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DbContract.TableUsers.COLUMN_NAME_USER_NAME, userName);
                long newRowId = -1;


                newRowId = dbWrite.insert(DbContract.TableUsers.TABLE_NAME, null, values);

                if(newRowId > 0){
                    toast = new Toast(getActivity());
                    toast = Toast.makeText(getActivity(), MESSAGE_SAVED, TOAST_DURATION);
                    toast.show();
                }else{
                    toast = Toast.makeText(getActivity(), MESSAGE_UNEXP_ERROR, TOAST_DURATION);
                    toast.show();
                }
                MetaFileHelper.userId = newRowId;
                MetaFileHelper.userName = userName;

                try {
                    FileOutputStream fileOutputStream = getActivity().openFileOutput(MetaFileHelper.metafileName, Context.MODE_PRIVATE);
                    new MetaFileHelper().saveMetaToFile(fileOutputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateUserInfo();

            }
        });

        mButtonUpdate = (Button)view.findViewById(R.id.button_edit_user_update);
        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextName.getText().toString();

                if(mDbHelper == null){
                    mDbHelper = new DbHelper(getActivity());
                }

                SQLiteDatabase dbWrite = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DbContract.TableUsers.COLUMN_NAME_USER_NAME, userName);
                long newRowId = -1;


                String where = DbContract.TableUsers.COLUMN_NAME_USER_ID + " = ?";
                String[] whereArgs = new String[]{String.valueOf(MetaFileHelper.userId)};
                newRowId = dbWrite.update(DbContract.TableUsers.TABLE_NAME, values, where, whereArgs);

                if(newRowId > 0){
                    toast = new Toast(getActivity());
                    toast = Toast.makeText(getActivity(), MESSAGE_SAVED, TOAST_DURATION);
                    toast.show();
                }else{
                    toast = Toast.makeText(getActivity(), MESSAGE_UNEXP_ERROR, TOAST_DURATION);
                    toast.show();
                }
                MetaFileHelper.userId = newRowId;
                MetaFileHelper.userName = userName;

                try {
                    FileOutputStream fileOutputStream = getActivity().openFileOutput(MetaFileHelper.metafileName, Context.MODE_PRIVATE);
                    new MetaFileHelper().saveMetaToFile(fileOutputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateUserInfo();

            }
        });


        return view;
    }

    private void updateUserInfo(){
        UserInfoFragment userInfoFragment = (UserInfoFragment) getFragmentManager().findFragmentById(R.id.fragment_user_info);
        userInfoFragment.update();
    }

}
