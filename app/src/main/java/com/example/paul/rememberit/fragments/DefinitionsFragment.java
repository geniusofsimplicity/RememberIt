package com.example.paul.rememberit.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.rememberit.R;
import com.example.paul.rememberit.activities.DefinitionsActivity;
import com.example.paul.rememberit.activities.EditDefinitionActivity;
import com.example.paul.rememberit.activities.EditWordActivity;
import com.example.paul.rememberit.activities.ExamplesActivity;
import com.example.paul.rememberit.helpers.DbContract;
import com.example.paul.rememberit.helpers.DbHelper;

/**
 * Created by Paul on 23.06.2016.
 */
public class DefinitionsFragment extends Fragment {

    public final static String INTENT_DEF_ID_PASSED = "DEF_ID_PASSED";
    public final static String INTENT_DEF_PASSED = "DEF_PASSED";
    public final static String INTENT_WORD_PASSED = "WORD_PASSED";
    public final static String INTENT_WORD_ID_PASSED = "WORD_ID_PASSED";
    public final static String INTENT_REGISTER_PASSED = "register_PASSED";
    public final static String INTENT_USABILITY_PASSED = "usability_PASSED";
    public final static String INTENT_PART_OF_SPEECH_PASSED = "pOfSp_PASSED";
    public final static int DEF_ID_TO_PASS = 1;
    public final static int DEF_TO_PASS = 2;
    public final static int WORD_TO_PASS = 3;
    private static int TOAST_DURATION = Toast.LENGTH_SHORT;
    private DbHelper mDbHelper;
    private SQLiteDatabase dbRead;
    private MenuItem subtitleItem;
    private boolean mDefinitionsAll = true;
    private CheckBox checkboxExamplesOnOff;
    private float textSizeWords;
    private ListView lv;
    private Cursor cursor;
    private String[] fromColumns;
    private int[] toViews;
    private enum DisplayModes{
        SHOW_ALL, SHOW_WITHOUT_EXAMPLES
    };

    public final static String INTENT_COMMAND_EDIT_DEFINITION = "DefinitionsMenuActivity.command";
    public final static String COMMAND_DEFINITION_NEW = "new definition";
    public final static String COMMAND_DEFINITION_NEW_WITH_WORD = "new definition with word";
    public final static String COMMAND_DEFINITION_EDIT = "edit definition";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listview_fragment, container, false);

        lv = (ListView)view.findViewById(R.id.listview_fragment_listview);

        mDbHelper = new DbHelper(getActivity());
        dbRead = mDbHelper.getReadableDatabase();

        fromColumns = new String[]{
                DbContract.TableWords.COLUMN_NAME_WORD,
                DbContract.TableDefinitions.COLUMN_NAME_PART_OF_SPEECH,
                DbContract.TableDefinitions.COLUMN_NAME_REGISTER,
                DbContract.TableWords.COLUMN_NAME_LANGUAGE,
                DbContract.TableDefinitions.COLUMN_NAME_DEFINITION,
                DbContract.TableDefinitions.COLUMN_NAME_DEFINITION_ID,
                DbContract.TableDefinitions.COLUMN_NAME_DEFINITION,
                DbContract.TableWords.COLUMN_NAME_WORD,
                DbContract.TableDefinitions.COLUMN_NAME_DEFINITION_ID,
                DbContract.TableDefinitions.COLUMN_NAME_DEFINITION,
                DbContract.TableWords.COLUMN_NAME_WORD,
                DbContract.TableDefinitions.COLUMN_NAME_PART_OF_SPEECH,
                DbContract.TableDefinitions.COLUMN_NAME_REGISTER,
                DbContract.TableDefinitions.COLUMN_NAME_USABILITY
        };
        toViews = new int[]{
                R.id.view_definitions_word,
                R.id.view_definitions_part_of_speech,
                R.id.view_definitions_register,
                R.id.view_definitions_language,
                R.id.view_definitions_definition,
                R.id.button_view_definitions_view_examples,
                R.id.button_view_definitions_view_examples,
                R.id.button_view_definitions_view_examples,
                R.id.button_view_definitions_edit_definition,
                R.id.button_view_definitions_edit_definition,
                R.id.button_view_definitions_edit_definition,
                R.id.button_view_definitions_edit_definition,
                R.id.button_view_definitions_edit_definition,
                R.id.button_view_definitions_edit_definition
        };

//        checkboxExamplesOnOff = (CheckBox)view.findViewById(R.id.checkbox_definitions_display);
//        DisplayModes displayMode;
//        if(checkboxExamplesOnOff.isEnabled()){
//            displayMode = DisplayModes.SHOW_WITHOUT_EXAMPLES;
//        }else{
//            displayMode = DisplayModes.SHOW_ALL;
//        }

//        checkboxExamplesOnOff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkboxExamplesOnOff.isChecked()) {
//                    new ShowDefinitionsTask().execute(DisplayModes.SHOW_WITHOUT_EXAMPLES);
//                } else {
//                    new ShowDefinitionsTask().execute(DisplayModes.SHOW_ALL);
//                }
//            }
//        });

//        new ShowDefinitionsTask().execute(displayMode);
        updateUI();

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_definitions, menu);

        subtitleItem = menu.findItem(R.id.menu_item_show_all_definitions);
        if(mDefinitionsAll){
            subtitleItem.setTitle(R.string.show_all);
        }else{
            subtitleItem.setTitle(R.string.show_with_no_examples);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_definition:
                Intent intent = new Intent(getActivity(), EditDefinitionActivity.class);
                intent.putExtra(DefinitionsFragment.INTENT_COMMAND_EDIT_DEFINITION, DefinitionsFragment.COMMAND_DEFINITION_NEW);
                startActivity(intent);
                return true;
            case R.id.menu_item_show_all_definitions:
                mDefinitionsAll = !mDefinitionsAll;
                if(mDefinitionsAll){
                    subtitleItem.setTitle(R.string.show_all);
                }else{
                    subtitleItem.setTitle(R.string.show_with_no_examples);
                }
                updateUI();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI(){
        if(mDefinitionsAll){
            new ShowDefinitionsTask().execute(DisplayModes.SHOW_WITHOUT_EXAMPLES);
        }else {
            new ShowDefinitionsTask().execute(DisplayModes.SHOW_ALL);
        }
    }


    private class ShowDefinitionsTask extends AsyncTask<DisplayModes, Void, Cursor> {

        @Override
        protected Cursor doInBackground(DisplayModes... params) {
            DisplayModes displayMode = params[0];
            String sql = "";
            switch (displayMode){
                case SHOW_ALL:
                    sql = DbContract.SQL_SELECT_ALL_DEFINITIONS;
                    break;
                case SHOW_WITHOUT_EXAMPLES:
                    sql = DbContract.SQL_SELECT_DEFINITIONS_WITHOUT_EXAMPLES;
                    break;
            }
            cursor = dbRead.rawQuery(sql, null);

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor){
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                    R.layout.view_definitions, cursor,
                    fromColumns, toViews, 0);
            adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                    if (view.getId() == R.id.button_view_definitions_view_examples ||
                            view.getId() == R.id.button_view_definitions_edit_definition) {
                        if (cursor.getColumnIndexOrThrow(DbContract.TableDefinitions.COLUMN_NAME_DEFINITION_ID) == columnIndex) {
                            long defId = Long.parseLong(cursor.getString(columnIndex));
                            view.setTag(R.string.key_def_id_passed, defId);
                        }
                        if (cursor.getColumnIndexOrThrow(DbContract.TableDefinitions.COLUMN_NAME_DEFINITION) == columnIndex) {
                            String def = cursor.getString(columnIndex);
                            view.setTag(R.string.key_def_passed, def);
                        }
                        if (cursor.getColumnIndexOrThrow(DbContract.TableWords.COLUMN_NAME_WORD) == columnIndex) {
                            String word = cursor.getString(columnIndex);
                            view.setTag(R.string.key_word_passed, word);
                        }
                        if(view.getId() == R.id.button_view_definitions_view_examples){
                            view.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(getActivity(), ExamplesActivity.class);
                                                            long defId = Long.parseLong(v.getTag(R.string.key_def_id_passed).toString());
                                                            String def = v.getTag(R.string.key_def_passed).toString();
                                                            String word = v.getTag(R.string.key_word_passed).toString();
                                                            intent.putExtra(DefinitionsFragment.INTENT_DEF_ID_PASSED, defId);
                                                            intent.putExtra(DefinitionsFragment.INTENT_DEF_PASSED, def);
                                                            intent.putExtra(DefinitionsFragment.INTENT_WORD_PASSED, word);
                                                            startActivity(intent);
                                                        }
                                                    }
                            );
                        }else{
                            if (cursor.getColumnIndexOrThrow(DbContract.TableDefinitions.COLUMN_NAME_REGISTER) == columnIndex) {
                                String register = cursor.getString(columnIndex);
                                view.setTag(R.string.key_register_passed_to_edit_definition, register);
                            }
                            if (cursor.getColumnIndexOrThrow(DbContract.TableDefinitions.COLUMN_NAME_USABILITY) == columnIndex) {
                                int usability = cursor.getInt(columnIndex);
                                view.setTag(R.string.key_usability_passed_to_edit_definition, usability);
                            }
                            if (cursor.getColumnIndexOrThrow(DbContract.TableDefinitions.COLUMN_NAME_PART_OF_SPEECH) == columnIndex) {
                                int pOfSp = cursor.getInt(columnIndex);
                                view.setTag(R.string.key_pOfSp_passed_to_edit_definition, pOfSp);
                            }
                            view.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(getActivity(), EditDefinitionActivity.class);
                                                            long defId = Long.parseLong(v.getTag(R.string.key_def_id_passed).toString());
                                                            String def = v.getTag(R.string.key_def_passed).toString();
                                                            String word = v.getTag(R.string.key_word_passed).toString();
                                                            String register = v.getTag(R.string.key_register_passed_to_edit_definition).toString();
                                                            int usability = Integer.parseInt(v.getTag(R.string.key_usability_passed_to_edit_definition).toString());
                                                            int pOfSp = Integer.parseInt(v.getTag(R.string.key_pOfSp_passed_to_edit_definition).toString());
                                                            intent.putExtra(DefinitionsFragment.INTENT_COMMAND_EDIT_DEFINITION, DefinitionsFragment.COMMAND_DEFINITION_EDIT);
                                                            intent.putExtra(DefinitionsFragment.INTENT_DEF_ID_PASSED, defId);
                                                            intent.putExtra(DefinitionsFragment.INTENT_DEF_PASSED, def);
                                                            intent.putExtra(DefinitionsFragment.INTENT_WORD_PASSED, word);
                                                            intent.putExtra(DefinitionsFragment.INTENT_REGISTER_PASSED, register);
                                                            intent.putExtra(DefinitionsFragment.INTENT_USABILITY_PASSED, usability);
                                                            intent.putExtra(DefinitionsFragment.INTENT_PART_OF_SPEECH_PASSED, pOfSp);
                                                            startActivity(intent);
                                                        }
                                                    }
                            );
                        }
                    } else {
                        String text = cursor.getString(columnIndex);
                        TextView textView = (TextView) view;
                        textView.setText(text);
                    }
                    return true;
                }
            });


            lv.setAdapter(adapter);
        }
    }

}
