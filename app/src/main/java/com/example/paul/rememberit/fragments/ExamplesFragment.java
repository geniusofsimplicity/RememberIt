package com.example.paul.rememberit.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.paul.rememberit.R;
import com.example.paul.rememberit.activities.EditDefinitionActivity;
import com.example.paul.rememberit.activities.EditExampleActivity;
import com.example.paul.rememberit.helpers.DbContract;
import com.example.paul.rememberit.helpers.DbHelper;

/**
 * Created by Paul on 23.06.2016.
 */
public class ExamplesFragment extends Fragment {

    private DbHelper mDbHelper;
    private Cursor cursor;
    //!!!!!!!!!!!
    private long defId;
    private String def;
    private String word;
    private int difficulty;
    private View view;
    public final static String INTENT_COMMAND = "com.vab.paul.vocup.command";
    public final static String COMMAND_NEW_EXAMPLE = "add definition";
    public final static String COMMAND_EDIT_EXAMPLE = "edit definition";
    public final static String INTENT_EXAMPLE_ID_PASSED ="example id";
    public final static String INTENT_EXAMPLE_PASSED ="example";
    public final static String INTENT_DIFFICULTY_PASSED ="example's difficulty";


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle extras = getActivity().getIntent().getExtras();
        if(defId == 0) {
            if (extras != null) {
                getDefWord(extras);
            } else {
                getDefWord(savedInstanceState);
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_examples, container, false);

//        updateUI();
        return view;
    }

    private void getDefWord(Bundle extras){
        if (extras != null)
        {
            defId = extras.getLong(DefinitionsFragment.INTENT_DEF_ID_PASSED);
            def = extras.getString(DefinitionsFragment.INTENT_DEF_PASSED);
            word = extras.getString(DefinitionsFragment.INTENT_WORD_PASSED);
        } else {
            defId = 0;
            def = "The definition is not available.";
            word = "None.";
        }
    }

    private void updateUI(){
        mDbHelper = new DbHelper(getActivity());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        TextView wordView = (TextView)view.findViewById(R.id.view_examples_word);
        wordView.setText(word);
        TextView defView = (TextView)view.findViewById(R.id.view_examples_definition);
        defView.setText(def);

        String queryFull = DbContract.getSqlRequestSelectExamples(defId);
        cursor = db.rawQuery(queryFull, null);
        String [] fromColumns = new String[]{
                DbContract.TableExamples.COLUMN_NAME_EXAMPLE,
                DbContract.TableExamples.COLUMN_NAME_EXAMPLE_ID,
                DbContract.TableExamples.COLUMN_NAME_DIFFICULTY,
                DbContract.TableExamples.COLUMN_NAME_EXAMPLE
        };
        int [] toViews = new int[]{
                R.id.view_examples_example,
                R.id.view_examples_button_edit,
                R.id.view_examples_button_edit,
                R.id.view_examples_button_edit
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.view_examples, cursor,
                fromColumns, toViews, 0);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.view_examples_button_edit) {
                    if (cursor.getColumnIndexOrThrow(DbContract.TableExamples.COLUMN_NAME_EXAMPLE_ID) == columnIndex) {
                        long exampleId = cursor.getLong(columnIndex);
                        view.setTag(R.string.key_example_id_passed, exampleId);
                        view.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(getActivity(), EditExampleActivity.class);
                                                        long exampleId = Long.parseLong(v.getTag(R.string.key_example_id_passed).toString());
                                                        String example = v.getTag(R.string.key_example_passed).toString();
                                                        int difficulty = Integer.parseInt(v.getTag(R.string.key_example_difficulty_passed).toString());
                                                        intent.putExtra(ExamplesFragment.INTENT_COMMAND, ExamplesFragment.COMMAND_EDIT_EXAMPLE);
                                                        intent.putExtra(ExamplesFragment.INTENT_EXAMPLE_ID_PASSED, exampleId);
                                                        intent.putExtra(ExamplesFragment.INTENT_EXAMPLE_PASSED, example);
                                                        intent.putExtra(ExamplesFragment.INTENT_DIFFICULTY_PASSED, difficulty);
                                                        intent.putExtra(DefinitionsFragment.INTENT_DEF_ID_PASSED, defId);
                                                        intent.putExtra(DefinitionsFragment.INTENT_DEF_PASSED, def);
                                                        intent.putExtra(DefinitionsFragment.INTENT_WORD_PASSED, word);
                                                        startActivity(intent);
                                                    }
                                                }
                        );
                    }


                    if (cursor.getColumnIndexOrThrow(DbContract.TableExamples.COLUMN_NAME_EXAMPLE) == columnIndex) {
                        String example = cursor.getString(columnIndex);
                        view.setTag(R.string.key_example_passed, example);
                    }

                    if (cursor.getColumnIndexOrThrow(DbContract.TableExamples.COLUMN_NAME_DIFFICULTY) == columnIndex) {
                        int difficulty = cursor.getInt(columnIndex);
                        view.setTag(R.string.key_example_difficulty_passed, difficulty);
                    }
                }

                else{
                    String text = cursor.getString(columnIndex);
                    TextView textView = (TextView) view;
                    textView.setText(text);
                }

                return true;
            }
        });

        ListView lv = (ListView)view.findViewById(R.id.tableExamples);
        lv.setAdapter(adapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_examples, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_example:
                Intent intent = new Intent(getActivity(), EditExampleActivity.class);
                intent.putExtra(ExamplesFragment.INTENT_COMMAND, ExamplesFragment.COMMAND_NEW_EXAMPLE);
                intent.putExtra(DefinitionsFragment.INTENT_DEF_ID_PASSED, defId);
                intent.putExtra(DefinitionsFragment.INTENT_DEF_PASSED, def);
                intent.putExtra(DefinitionsFragment.INTENT_WORD_PASSED, word);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong(DefinitionsFragment.INTENT_DEF_ID_PASSED, defId);
        savedInstanceState.putString(DefinitionsFragment.INTENT_DEF_PASSED, def);
        savedInstanceState.putString(DefinitionsFragment.INTENT_WORD_PASSED, word);
    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if(savedInstanceState != null){
//            getDefWord(savedInstanceState);
//            updateUI();
//        }
//    }



}
