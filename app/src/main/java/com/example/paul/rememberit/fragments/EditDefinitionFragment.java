package com.example.paul.rememberit.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
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
public class EditDefinitionFragment extends Fragment {

    private DbHelper mDbHelper;
    private String command;
    private static int PADDING = 15;
    private SQLiteDatabase dbRead, dbWrite;
    private Cursor cursor;
    private static long defId, wordId;
    private String def, word, register;
    private int usability, partOfSpeech;
    private static CharSequence MESSAGE_SAVED = "Saved.";
    private static CharSequence MESSAGE_NO_SUCH_WORD = "Word doesn't exist.";
    private static CharSequence MESSAGE_UNEXP_ERROR = "Unexpected error in DB.";
    private static int TOAST_DURATION = Toast.LENGTH_SHORT;
    private EditText editTextDef;
    private Spinner spinnerPartOfSpeech;
    private EditText editTextRegister;
    private EditText editTextUsability;
    private EditText editTextWord;
    private Button mSaveButton;
    private Button mFindButton;
    private String[] projection = {
            DbContract.TableWords.COLUMN_NAME_WORD_ID,
            DbContract.TableWords.COLUMN_NAME_WORD
    };
    String sortOrder = DbContract.TableWords.COLUMN_NAME_WORD + " DESC";
    private Toast toast;
    private View view;

    public static EditDefinitionFragment newInstance(String word){
        Bundle args = new Bundle();
        args.putSerializable(DefinitionsFragment.INTENT_COMMAND_EDIT_DEFINITION, DefinitionsFragment.COMMAND_DEFINITION_NEW_WITH_WORD);
        args.putSerializable( DefinitionsFragment.INTENT_WORD_PASSED, word);

        EditDefinitionFragment fragment = new EditDefinitionFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_definition, container, false);

        editTextDef = (EditText)view.findViewById(R.id.add_definition);
        //editTextPoS = (EditText) findViewById(R.id.add_definition_part_of_speech);
        editTextRegister = (EditText) view.findViewById(R.id.add_definition_register);
        editTextUsability = (EditText) view.findViewById(R.id.add_definition_usability);
        editTextWord = (EditText) view.findViewById(R.id.add_definition_word);
        spinnerPartOfSpeech = (Spinner) view.findViewById(R.id.add_definition_part_of_speech);
        ArrayAdapter<CharSequence> adapterPoS = ArrayAdapter.createFromResource(getActivity(), R.array.parts_of_speech, R.layout.spinner_part_of_speech_dropdown_item);
        adapterPoS.setDropDownViewResource(R.layout.spinner_part_of_speech_dropdown_item);
        spinnerPartOfSpeech.setAdapter(adapterPoS);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            command = extras.getString(DefinitionsFragment.INTENT_COMMAND_EDIT_DEFINITION);

            mDbHelper = new DbHelper(getActivity());
            dbRead = mDbHelper.getReadableDatabase();
            dbWrite = mDbHelper.getWritableDatabase();

            if (command.compareTo(DefinitionsFragment.COMMAND_DEFINITION_NEW) == 0) {
                AutoCompleteTextView editTextWord = (AutoCompleteTextView) view.findViewById(R.id.add_definition_word);
                String[] cols = new String[]{DbContract.TableWords.COLUMN_NAME_WORD};
                int[] views = new int[]{R.id.auto_complete_edit_definition_word};

                SimpleCursorAdapter adapterWords = new SimpleCursorAdapter(getActivity(), R.layout.auto_complete_edit_definition, null, cols, views, 0);
                editTextWord.setAdapter(adapterWords);
                adapterWords.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
                    @Override
                    public CharSequence convertToString(Cursor cursor) {
                        return cursor.getString(cursor.getColumnIndexOrThrow(DbContract.TableWords.COLUMN_NAME_WORD));
                    }
                });

                adapterWords.setFilterQueryProvider(new FilterQueryProvider() {
                    @Override
                    public Cursor runQuery(CharSequence str) {
                        String whereClause = DbContract.TableWords.COLUMN_NAME_WORD + " LIKE ? ";
                        String[] whereArgs = {"%" + str + "%"};
                        String[] tableColumns = {
                                DbContract.TableWords.COLUMN_NAME_WORD_ID,
                                DbContract.TableWords.COLUMN_NAME_WORD
                        };

                        final Cursor cursorInFilter = dbRead.query(
                                DbContract.TableWords.TABLE_NAME,
                                tableColumns,                   // the columns to return
                                whereClause,                    // the columns for the WHERE clause
                                whereArgs,                      // the values for the WHERE clause
                                null,                           // group the rows
                                null,                           // filter by row groups
                                null                            // sort order
                        );

                        return cursorInFilter;
                    }
                });
            }

            switch (command) {
                case DefinitionsFragment.COMMAND_DEFINITION_NEW:
                    break;
                case DefinitionsFragment.COMMAND_DEFINITION_NEW_WITH_WORD:
                    word = extras.getString(DefinitionsFragment.INTENT_WORD_PASSED);
                    //wordId = extras.getLong(ViewDefinitionsActivity.INTENT_WORD_ID_PASSED);
                    editTextWord.setText(word);
                    break;
                case DefinitionsFragment.COMMAND_DEFINITION_EDIT:
                    defId = extras.getLong(DefinitionsFragment.INTENT_DEF_ID_PASSED);
                    def = extras.getString(DefinitionsFragment.INTENT_DEF_PASSED);
                    word = extras.getString(DefinitionsFragment.INTENT_WORD_PASSED);
                    register = extras.getString(DefinitionsFragment.INTENT_REGISTER_PASSED);
                    usability = extras.getInt(DefinitionsFragment.INTENT_USABILITY_PASSED);
                    partOfSpeech = extras.getInt(DefinitionsFragment.INTENT_PART_OF_SPEECH_PASSED);

                    editTextWord.setText(word);
                    EditText editTextDef = (EditText)view.findViewById(R.id.add_definition);
                    editTextDef.setText(def);
                    EditText editTextRegister = (EditText) view.findViewById(R.id.add_definition_register);
                    editTextRegister.setText(register);
                    EditText editTextUsability = (EditText) view.findViewById(R.id.add_definition_usability);
                    editTextUsability.setText(String.valueOf(usability));

                    spinnerPartOfSpeech.setSelection(partOfSpeech - 1);


                    String queryFull = DbContract.getSqlRequestDependentExamples(defId);
                    cursor = dbRead.rawQuery(queryFull, null);
                    String[] fromColumns = new String[]{
                            DbContract.TableExamples.COLUMN_NAME_EXAMPLE_ID,
                            DbContract.TableExamples.COLUMN_NAME_EXAMPLE
                    };
                    int[] toViews = new int[]{
                            R.id.edit_definition_dependent_examples_button_unlink,
                            R.id.edit_example_dependent_definitions_definition
                    };

                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                            R.layout.edit_definition_dependent_examples, cursor,
                            fromColumns, toViews, 0);

                    adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                            if (view.getId() == R.id.edit_definition_dependent_examples_button_unlink) {
                                long exampleId = Long.parseLong(cursor.getString(columnIndex));
                                view.setTag(R.string.key_example_id_passed_to_unlink, exampleId);
                                view.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                long exampleIdToUnlink = Long.parseLong(v.getTag(R.string.key_example_id_passed_to_unlink).toString());
                                                                DbHelper mDbHelper = new DbHelper(getActivity());
                                                                SQLiteDatabase dbWrite = mDbHelper.getWritableDatabase();

                                                                //deleting examples, linked only to one definition
                                                                //String queryToDeleteSingleExample = DbContract.getSqlRequestDependentExamples(exampleIdToUnlink);
                                                                String where = DbContract.TableExamples.COLUMN_NAME_EXAMPLE_ID + " = ? AND (SELECT COUNT(*) FROM " +
                                                                        DbContract.TableDefinitionExample.TABLE_NAME + " WHERE " + DbContract.TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID +
                                                                        " = ?) = 1";
                                                                String[] clause = {String.valueOf(exampleIdToUnlink), String.valueOf(exampleIdToUnlink)};
                                                                dbWrite.delete(DbContract.TableExamples.TABLE_NAME, where, clause);

                                                                //deleting example/definition entry
                                                                String where2 = DbContract.TableDefinitionExample.COLUMN_NAME_DEFINITION_ID + " = ? AND " +
                                                                        DbContract.TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID + " = ?";
                                                                String[] clause2 = {String.valueOf(EditDefinitionFragment.defId), String.valueOf(exampleIdToUnlink)};
                                                                dbWrite.delete(DbContract.TableExamples.TABLE_NAME, where2, clause2);
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

                    ListView lv = (ListView) view.findViewById(R.id.edit_definition_dependent_examples);
                    lv.setAdapter(adapter);
                    break;
            }

            mSaveButton = (Button)view.findViewById(R.id.button_edit_definition_save);
            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FindWordIdTask().execute();
                }
            });

            mFindButton = (Button)view.findViewById(R.id.button_edit_definition_find_new);
            mFindButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FindNotLinkedWordTask().execute(null, null, null);
                }
            });
        }

        return view;
    }

    private class FindNotLinkedWordTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            String whereClause = DbContract.TableWords.COLUMN_NAME_WORD_ID + " NOT IN (SELECT " + DbContract.TableDefinitions.COLUMN_NAME_WORD_ID +
                    " FROM " + DbContract.TableDefinitions.TABLE_NAME + ")";
            //String[] whereArgs = new String[] {DbContract.TableWords.COLUMN_NAME_WORD_ID};
            String groupBy = " RANDOM() ";
            SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
            qBuilder.setTables(DbContract.TableWords.TABLE_NAME);
//            qBuilder.appendWhere(DbContract.TableWords.COLUMN_NAME_WORD_ID + " NOT IN (SELECT " + DbContract.TableDefinitions.COLUMN_NAME_WORD_ID +
//                    " FROM " + DbContract.TableDefinitions.TABLE_NAME + ")");
            String[] projectionWord = {
                    DbContract.TableWords.COLUMN_NAME_WORD
            };
            String tempS = qBuilder.buildQuery(
                    projectionWord,                 // The columns to return
                    whereClause,                       // The columns for the WHERE clause
                    null,                       // The values for the WHERE clause
                    groupBy,                       // don't group the rows
                    null,                       // don't filter by row groups
                    null,                   // The sort order
                    "1"                       // limit
            );

            Cursor cursor = qBuilder.query(dbRead,
                    projectionWord,                 // The columns to return
                    whereClause,                       // The columns for the WHERE clause
                    null,                       // The values for the WHERE clause
                    groupBy,                       // don't group the rows
                    null,                       // don't filter by row groups
                    null,                   // The sort order
                    "1"                       // limit
            );

            if (cursor.moveToFirst()){
                word = cursor.getString(cursor.getColumnIndex(DbContract.TableWords.COLUMN_NAME_WORD));
            }
            return word;
        }

        @Override
        protected void onPostExecute(String word){
            editTextWord.setText(word);
        }
    }

    private class FindWordIdTask extends AsyncTask<String, Long, Long> {

        @Override
        protected Long doInBackground(String... params) {
            long wordId = -1;
            word = editTextWord.getText().toString();


            String whereClause = DbContract.TableWords.COLUMN_NAME_WORD + " = ? ";
            String[] whereArgs = new String[] {word};

            Cursor cursor = dbRead.query(
                    DbContract.TableWords.TABLE_NAME,
                    projection,                 // The columns to return
                    whereClause,                       // The columns for the WHERE clause
                    whereArgs,                       // The values for the WHERE clause
                    null,                       // don't group the rows
                    null,                       // don't filter by row groups
                    null                   // The sort order
            );

            if (cursor.moveToFirst()){
                wordId = cursor.getLong(cursor.getColumnIndex(DbContract.TableWords.COLUMN_NAME_WORD_ID));
            }
            return wordId;
        }

        @Override
        protected void onPostExecute(Long wordId){
            if(wordId > 0){
                new SaveDefinitionTask().execute(wordId, null, null);
            }
        }
    }

    private class SaveDefinitionTask extends AsyncTask<Long, Long, Long> {

        @Override
        protected Long doInBackground(Long... params) {
            long wordId = params[0];
            String definition = editTextDef.getText().toString();

            int partOfSpeech = spinnerPartOfSpeech.getSelectedItemPosition() + 1;
            //int partOfSpeech = Integer.parseInt(editTextPoS.getText().toString());
            String register = editTextRegister.getText().toString();


            ContentValues values = new ContentValues();
            values.put(DbContract.TableDefinitions.COLUMN_NAME_DEFINITION, definition);
            values.put(DbContract.TableDefinitions.COLUMN_NAME_WORD_ID, wordId);
            values.put(DbContract.TableDefinitions.COLUMN_NAME_PART_OF_SPEECH, partOfSpeech);
            values.put(DbContract.TableDefinitions.COLUMN_NAME_REGISTER, register);
            //work aroung until the feature is added
//            values.put(DbContract.TableDefinitions.COLUMN_NAME_USABILITY, usability);
            values.put(DbContract.TableDefinitions.COLUMN_NAME_USABILITY, 1);
            long newRowId = -1;

            switch (command) {
                case DefinitionsFragment.COMMAND_DEFINITION_NEW:
                    newRowId = dbWrite.insert(DbContract.TableDefinitions.TABLE_NAME, null, values);
                    break;
                case DefinitionsFragment.COMMAND_DEFINITION_NEW_WITH_WORD:
                    newRowId = dbWrite.insert(DbContract.TableDefinitions.TABLE_NAME, null, values);
                    break;
                case DefinitionsFragment.COMMAND_DEFINITION_EDIT:
                    String where = DbContract.TableDefinitions.COLUMN_NAME_DEFINITION_ID + " = ?";
                    String[] whereArgs = new String[]{String.valueOf(defId)};
                    newRowId = dbWrite.update(DbContract.TableDefinitions.TABLE_NAME, values, where, whereArgs);
                    break;
            }
            return newRowId;
        }

        @Override
        protected void onPostExecute(Long newRowId){
            if(newRowId > 0){
                //if(command == DefinitionsMenuActivity.COMMAND_DEFINITION_NEW_WITH_WORD){
                if(command.equals(DefinitionsFragment.COMMAND_DEFINITION_NEW_WITH_WORD)){
                    Intent intent = new Intent(getActivity(), DefinitionsActivity.class);
                    startActivity(intent);
                }

                if(newRowId <= 0){
                    toast = Toast.makeText(getActivity(), MESSAGE_UNEXP_ERROR, TOAST_DURATION);
                }else{
                    toast = Toast.makeText(getActivity(), MESSAGE_SAVED, TOAST_DURATION);
                }
                toast.show();
            } else {
                toast = Toast.makeText(getActivity(), MESSAGE_NO_SUCH_WORD, TOAST_DURATION);
                toast.show();
            }
        }
    }
}
