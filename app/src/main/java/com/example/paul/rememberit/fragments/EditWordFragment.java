package com.example.paul.rememberit.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.rememberit.R;
import com.example.paul.rememberit.activities.DefinitionsActivity;
import com.example.paul.rememberit.activities.EditDefinitionActivity;
import com.example.paul.rememberit.helpers.DbContract;
import com.example.paul.rememberit.helpers.DbHelper;
import com.example.paul.rememberit.helpers.MetaFileHelper;

import java.util.Arrays;

/**
 * Created by Paul on 22.06.2016.
 */
public class EditWordFragment extends Fragment {

    private DbHelper mDbHelper;
    private String command;
    private long wordId;
    private float textSizeWords;
    private static int PADDING = 15;
    private static CharSequence SAVED_SUCCESSFULLY = "The word has been saved.";
    private static int TOAST_DURATION = Toast.LENGTH_SHORT;
    private EditText editTextWord;
    private Spinner editTextLanguage;
    private Toast toast;
    private Button mSaveButton;
    private Button mSaveAndWriteDefButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_word, container, false);

        editTextWord = (EditText)view.findViewById(R.id.edit_text_add_words_word);
        editTextLanguage = (Spinner)view.findViewById(R.id.spinner_add_words_language);

        ArrayAdapter<CharSequence> adapterLanguage = ArrayAdapter.createFromResource(getActivity(), R.array.language, R.layout.spinner_language_dropdown_item);
        adapterLanguage.setDropDownViewResource(R.layout.spinner_language_dropdown_item);
        editTextLanguage.setAdapter(adapterLanguage);

        mDbHelper = new DbHelper(getActivity());
        toast = Toast.makeText(getActivity(), SAVED_SUCCESSFULLY, TOAST_DURATION);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.BLUE);

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            command = extras.getString(WordsFragment.INTENT_COMMAND_EDIT_WORD);
            String word, language;
            String[] testArray = getResources().getStringArray(R.array.language);
            int pos;

            switch (command) {
                case WordsFragment.COMMAND_WORD_NEW:
                    language = MetaFileHelper.currentSubject;
                    pos = Arrays.asList(testArray).indexOf(language);
                    editTextLanguage.setSelection(pos);

                    break;
                case WordsFragment.COMMAND_WORD_EDIT:
                    wordId = extras.getLong(WordsFragment.INTENT_WORD_ID_PASSED);
                    word = extras.getString(WordsFragment.INTENT_WORD_PASSED);
                    language = extras.getString(WordsFragment.INTENT_LANGUAGE_PASSED);
                    pos = Arrays.asList(testArray).indexOf(language);

                    EditText editTextWord = (EditText)view.findViewById(R.id.edit_text_add_words_word);
                    editTextWord.setText(word);
                    //EditText editTextLanguage = (Spinner) findViewById(R.id.edit_text_add_words_language);
                    editTextLanguage.setSelection(pos);
                    break;
            }
        }

        mSaveButton = (Button)view.findViewById(R.id.button_edit_word_save);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = editTextWord.getText().toString();
                String language = editTextLanguage.getSelectedItem().toString();

                new SaveWordTask().execute(word, language, null, null);
            }
        });

        mSaveAndWriteDefButton = (Button)view.findViewById(R.id.button_edit_word_save_and_write_def);
        mSaveAndWriteDefButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWord();

                Intent intent = new Intent(getActivity(), EditDefinitionActivity.class);
                intent.putExtra(DefinitionsFragment.INTENT_COMMAND_EDIT_DEFINITION, DefinitionsFragment.COMMAND_DEFINITION_NEW_WITH_WORD);
                String word = editTextWord.getText().toString();
                intent.putExtra(DefinitionsFragment.INTENT_WORD_PASSED, word);
                startActivity(intent);
            }
        });

        return view;
    }

    public void saveWord() {
        String word = editTextWord.getText().toString();
        String language = editTextLanguage.getSelectedItem().toString();

        new SaveWordTask().execute(word, language, null, null);
    }


    private class SaveWordTask extends AsyncTask<String, Long, Long> {

        @Override
        protected Long doInBackground(String... params) {
            String word = params[0];
            String language = params[1];
            SQLiteDatabase dbWrite = mDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DbContract.TableWords.COLUMN_NAME_WORD, word);
            values.put(DbContract.TableWords.COLUMN_NAME_LANGUAGE, language);
            long newRowId = -1;
            dbWrite.beginTransaction();
            switch (command) {
                case WordsFragment.COMMAND_WORD_NEW:
                    newRowId = dbWrite.insert(DbContract.TableWords.TABLE_NAME, null, values);
                    break;
                case WordsFragment.COMMAND_WORD_EDIT:
                    String where = DbContract.TableWords.COLUMN_NAME_WORD_ID + " = ?";
                    String[] whereArgs = new String[] {String.valueOf(wordId)};
                    newRowId = dbWrite.update(DbContract.TableWords.TABLE_NAME, values, where, whereArgs);
                    break;
            }
            dbWrite.setTransactionSuccessful();
            dbWrite.endTransaction();
            dbWrite.close();
            return newRowId;
        }

        @Override
        protected void onPostExecute(Long newRowId){
            if(newRowId > 0){
                if(command == WordsFragment.COMMAND_WORD_NEW){
                    editTextWord.setText("");
                    //editTextLanguage.setText("");
                }
                toast.show();
            }
        }
    }

}
