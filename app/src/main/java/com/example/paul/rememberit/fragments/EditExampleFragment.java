package com.example.paul.rememberit.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.rememberit.R;
import com.example.paul.rememberit.activities.DefinitionsActivity;
import com.example.paul.rememberit.helpers.DbContract;
import com.example.paul.rememberit.helpers.DbHelper;

/**
 * Created by Paul on 23.06.2016.
 */
public class EditExampleFragment extends Fragment {

    private long defId;
    private static long exampleId;
    private String def;
    private String word;
    private String command;
    private int difficulty;
    private EditText exampleEditText;
    private Spinner difficultySpinner, difficultyView;
    private DbHelper mDbHelper;
    private Cursor cursor;
    private View view;
    private Button mSaveButton;
    private static CharSequence MESSAGE_SAVED = "Saved.";
    private static CharSequence MESSAGE_NO_SUCH_WORD = "Word doesn't exist.";
    private static CharSequence MESSAGE_UNEXP_ERROR = "Unexpected error in DB.";
    private static int TOAST_DURATION = Toast.LENGTH_SHORT;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_example, container, false);


        mDbHelper = new DbHelper(getActivity());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Bundle extras =getActivity().getIntent().getExtras();
        if (extras != null)
        {
            defId = extras.getLong(DefinitionsFragment.INTENT_DEF_ID_PASSED);
            def = extras.getString(DefinitionsFragment.INTENT_DEF_PASSED);
            word = extras.getString(DefinitionsFragment.INTENT_WORD_PASSED);
            command = extras.getString(ExamplesFragment.INTENT_COMMAND);
            difficulty = extras.getInt(ExamplesFragment.INTENT_DIFFICULTY_PASSED);
            TextView wordView, defView;
            wordView = (TextView)view.findViewById(R.id.edit_example_word);
            wordView.setText(word);
            defView = (TextView)view.findViewById(R.id.edit_example_definition);
            defView.setText(def);
            difficultyView = (Spinner)view.findViewById(R.id.edit_example_difficulty);
            difficultyView.setSelection(difficulty);
            switch (command){
                case ExamplesFragment.COMMAND_NEW_EXAMPLE:
                    break;

                case ExamplesFragment.COMMAND_EDIT_EXAMPLE:
                    exampleId = extras.getLong(ExamplesFragment.INTENT_EXAMPLE_ID_PASSED);
                    String example = extras.getString(ExamplesFragment.INTENT_EXAMPLE_PASSED);
                    EditText exampleEditView = (EditText)view.findViewById(R.id.edit_example_example);
                    exampleEditView.setText(example);
                    String queryFull = DbContract.getSqlRequestDependentDefinitions(exampleId, defId);
                    cursor = db.rawQuery(queryFull, null);
                    String [] fromColumns = new String[]{
                            DbContract.TableDefinitions.COLUMN_NAME_DEFINITION_ID,
                            DbContract.TableDefinitions.COLUMN_NAME_DEFINITION,
                            DbContract.TableWords.COLUMN_NAME_WORD
                    };
                    int [] toViews = new int[]{
                            R.id.edit_example_dependent_definitions_button_unlink,
                            R.id.edit_example_dependent_definitions_definition,
                            R.id.edit_example_dependent_definitions_word
                    };

                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                            R.layout.edit_example_dependent_definitions, cursor,
                            fromColumns, toViews, 0);

                    adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                            if (view.getId() == R.id.edit_example_dependent_definitions_button_unlink) {
                                long defId = Long.parseLong(cursor.getString(columnIndex));
                                view.setTag(R.string.key_def_id_passed_to_unlink, defId);
                                view.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                long defIdToUnlink = Long.parseLong(v.getTag(R.string.key_def_id_passed_to_unlink).toString());
                                                                DbHelper mDbHelper = new DbHelper(getActivity());
                                                                ;
                                                                SQLiteDatabase dbWrite = mDbHelper.getWritableDatabase();
                                                                String where = DbContract.TableDefinitionExample.COLUMN_NAME_DEFINITION_ID + " = ? AND " +
                                                                        DbContract.TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID + " = ?";
                                                                String[] clause = {String.valueOf(defIdToUnlink), String.valueOf(EditExampleFragment.exampleId)};
                                                                dbWrite.delete(DbContract.TableDefinitionExample.TABLE_NAME, where, clause);
                                                            }
                                                        }
                                );
                            } else {
                                String text = cursor.getString(columnIndex);
                                TextView textView = (TextView) view;
                                textView.setText(text);
                            }

                            return true;
                        }
                    });

                    ListView lv = (ListView)view.findViewById(R.id.edit_example_definitions);
                    lv.setAdapter(adapter);

                    break;

                default:
                    break;
            }
        } else {
            defId = 0;
            def = "The definition is not available.";
            word = "None.";
        }

        mSaveButton = (Button)view.findViewById(R.id.button_edit_example_save);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exampleEditText = (EditText)view.findViewById(R.id.edit_example_example);
                new SaveExampleTask().execute(null, null, null);
//                String example = exampleEditText.getText().toString();
//
//                Spinner difficultySpinner = (Spinner)view.findViewById(R.id.edit_example_difficulty);
//                int difficulty = difficultySpinner.getSelectedItemPosition();
//                ContentValues values = new ContentValues();
//                values.put(DbContract.TableExamples.COLUMN_NAME_EXAMPLE, example);
//                values.put(DbContract.TableExamples.COLUMN_NAME_DIFFICULTY, difficulty);
//                SQLiteDatabase dbWrite = mDbHelper.getWritableDatabase();
//                SQLiteDatabase dbRead = mDbHelper.getReadableDatabase();
//                String where;
//                String[] whereArgs;
//                long newRowId = 0;
//
//                switch (command) {
//                    case ExamplesFragment.COMMAND_NEW_EXAMPLE:
//                        newRowId = dbWrite.insert(
//                                DbContract.TableExamples.TABLE_NAME, null, values);
//                        String [] tableColumns = new String[]{DbContract.TableExamples.COLUMN_NAME_EXAMPLE_ID};
//                        where = DbContract.TableExamples.COLUMN_NAME_EXAMPLE + " = ?";
//                        whereArgs = new String[]{example};
//                        Cursor cursor = dbRead.query(DbContract.TableExamples.TABLE_NAME, tableColumns, where, whereArgs, null, null, null);
//                        if (cursor.moveToFirst()){
//                            exampleId = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.TableExamples.COLUMN_NAME_EXAMPLE_ID));
//                        }
//                        cursor.close();
//                        if(exampleId > 0) {
//                            values.clear();
//                            values.put(DbContract.TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID, exampleId);
//                            values.put(DbContract.TableDefinitionExample.COLUMN_NAME_DEFINITION_ID, defId);
//
//                            newRowId = dbWrite.insert(
//                                    DbContract.TableDefinitionExample.TABLE_NAME, null, values);
//                        }
//                        break;
//                    case ExamplesFragment.COMMAND_EDIT_EXAMPLE:
//                        where = DbContract.TableExamples.COLUMN_NAME_EXAMPLE_ID + " = ?";
//                        whereArgs = new String[]{String.valueOf(exampleId)};
//                        newRowId = dbWrite.update(DbContract.TableExamples.TABLE_NAME, values, where, whereArgs);
//                        break;
//                }
//
//                if(newRowId > 0) {
//                    Toast toast;
//                    toast = Toast.makeText(getActivity(), MESSAGE_SAVED, TOAST_DURATION);
//                    TextView v1 = (TextView) toast.getView().findViewById(android.R.id.message);
//                    v1.setTextColor(Color.BLUE);
//                    exampleEditText.setText("");
//                    difficultySpinner.setSelection(0);
//                    toast.show();
//                }
            }
        });

        return view;
    }

    private class SaveExampleTask extends AsyncTask<Void, Void, Long> {

        @Override
        protected Long doInBackground(Void... params) {
            exampleEditText = (EditText)view.findViewById(R.id.edit_example_example);
            String example = exampleEditText.getText().toString();

            difficultySpinner = (Spinner)view.findViewById(R.id.edit_example_difficulty);
            int difficulty = difficultySpinner.getSelectedItemPosition();
            ContentValues values = new ContentValues();
            values.put(DbContract.TableExamples.COLUMN_NAME_EXAMPLE, example);
            values.put(DbContract.TableExamples.COLUMN_NAME_DIFFICULTY, difficulty);
            SQLiteDatabase dbWrite = mDbHelper.getWritableDatabase();
            SQLiteDatabase dbRead = mDbHelper.getReadableDatabase();
            String where;
            String[] whereArgs;
            long newRowId = 0;

            switch (command) {
                case ExamplesFragment.COMMAND_NEW_EXAMPLE:
                    newRowId = dbWrite.insert(
                            DbContract.TableExamples.TABLE_NAME, null, values);
                    String [] tableColumns = new String[]{DbContract.TableExamples.COLUMN_NAME_EXAMPLE_ID};
                    where = DbContract.TableExamples.COLUMN_NAME_EXAMPLE + " = ?";
                    whereArgs = new String[]{example};
                    Cursor cursor = dbRead.query(DbContract.TableExamples.TABLE_NAME, tableColumns, where, whereArgs, null, null, null);
                    if (cursor.moveToFirst()){
                        exampleId = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.TableExamples.COLUMN_NAME_EXAMPLE_ID));
                    }
                    cursor.close();
                    if(exampleId > 0) {
                        values.clear();
                        values.put(DbContract.TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID, exampleId);
                        values.put(DbContract.TableDefinitionExample.COLUMN_NAME_DEFINITION_ID, defId);

                        newRowId = dbWrite.insert(
                                DbContract.TableDefinitionExample.TABLE_NAME, null, values);
                    }
                    break;
                case ExamplesFragment.COMMAND_EDIT_EXAMPLE:
                    where = DbContract.TableExamples.COLUMN_NAME_EXAMPLE_ID + " = ?";
                    whereArgs = new String[]{String.valueOf(exampleId)};
                    newRowId = dbWrite.update(DbContract.TableExamples.TABLE_NAME, values, where, whereArgs);
                    break;
            }
            return newRowId;
        }

        @Override
        protected void onPostExecute(Long newRowId){
            if(newRowId > 0) {
                Toast toast;
                toast = Toast.makeText(getActivity(), MESSAGE_SAVED, TOAST_DURATION);
                TextView v1 = (TextView) toast.getView().findViewById(android.R.id.message);
                v1.setTextColor(Color.BLUE);
                exampleEditText.setText("");
                difficultySpinner.setSelection(0);
                toast.show();
            }
        }
    }
}
