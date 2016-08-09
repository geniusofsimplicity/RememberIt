package com.example.paul.rememberit.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.paul.rememberit.R;
import com.example.paul.rememberit.activities.EditWordActivity;
import com.example.paul.rememberit.helpers.DbContract;
import com.example.paul.rememberit.helpers.DbHelper;

/**
 * Created by Paul on 22.06.2016.
 */
public class WordsFragment extends Fragment {

    private ListView lv;
    private DbHelper mDbHelper;
    private float textSizeWords;
    private Cursor cursor;
    private static String DIALOG_TITLE_DELETE_WORD = "Following word will be deleted:";
    SimpleCursorAdapter adapter;
    static Long wordIdToDelete;
    final Context context = getActivity();
    private static int PADDING = 15;
    public final static String INTENT_WORD_ID_PASSED = "ViewWordsActivity_word_id_passed";
    public final static String INTENT_WORD_PASSED = "ViewWordsActivity_word_passed";
    public final static String INTENT_LANGUAGE_PASSED = "ViewWordsActivity_language_passed";
    public final static String INTENT_COMMAND_EDIT_WORD = "WordsMenuActivity.command";
    public final static String COMMAND_WORD_NEW = "new word";
    public final static String COMMAND_WORD_EDIT = "edit word";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listview_fragment, container, false);

        lv = (ListView)view.findViewById(R.id.listview_fragment_listview);
        new DisplayWordsTask().execute(null, null, null);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_words, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_word:
                Intent intent = new Intent(getActivity(), EditWordActivity.class);
                intent.putExtra(WordsFragment.INTENT_COMMAND_EDIT_WORD, WordsFragment.COMMAND_WORD_NEW);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDetach() {
        cursor.close();
        super.onDetach();
    }

    private class DisplayWordsTask extends AsyncTask<String, Integer, Cursor> {

        @Override
        protected Cursor doInBackground(String... params) {
            mDbHelper = new DbHelper(getActivity());
            textSizeWords = getResources().getDimension(R.dimen.table_words_textsize);
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            String[] projection = {
                    DbContract.TableWords.COLUMN_NAME_WORD_ID,
                    DbContract.TableWords.COLUMN_NAME_WORD,
                    DbContract.TableWords.COLUMN_NAME_LANGUAGE
            };

            String sortOrder =
                    DbContract.TableWords.COLUMN_NAME_WORD + " ASC";

            cursor = db.query(
                    DbContract.TableWords.TABLE_NAME,
                    projection,                 // The columns to return
                    null,                       // The columns for the WHERE clause
                    null,                       // The values for the WHERE clause
                    null,                       // don't group the rows
                    null,                       // don't filter by row groups
                    sortOrder                   // The sort order
            );
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor){
            super.onPostExecute(cursor);
            String [] from = new String[]{DbContract.TableWords.COLUMN_NAME_WORD_ID,
                    DbContract.TableWords.COLUMN_NAME_WORD_ID,
                    DbContract.TableWords.COLUMN_NAME_WORD,
                    DbContract.TableWords.COLUMN_NAME_WORD,
                    DbContract.TableWords.COLUMN_NAME_LANGUAGE,
                    DbContract.TableWords.COLUMN_NAME_WORD_ID,
                    DbContract.TableWords.COLUMN_NAME_WORD,
                    DbContract.TableWords.COLUMN_NAME_LANGUAGE};

            int [] to = new int[]{
                    R.id.view_words_word_id,
                    R.id.button_view_words_delete_word,
                    R.id.button_view_words_delete_word,
                    R.id.view_words_word,
                    R.id.view_words_language,
                    R.id.button_view_words_edit_word,
                    R.id.button_view_words_edit_word,
                    R.id.button_view_words_edit_word};

            adapter = new SimpleCursorAdapter(getActivity(),
                    R.layout.view_words, cursor,
                    from, to, 0);

            adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                    if (view.getId() == R.id.button_view_words_edit_word) {
                        if (cursor.getColumnIndexOrThrow(DbContract.TableWords.COLUMN_NAME_WORD_ID) == columnIndex) {
                            long wordId = cursor.getLong(columnIndex);
                            view.setTag(R.string.key_word_id_passed, wordId);
                        }
                        if (cursor.getColumnIndexOrThrow(DbContract.TableWords.COLUMN_NAME_WORD) == columnIndex) {
                            String word = cursor.getString(columnIndex);
                            view.setTag(R.string.key_word_passed, word);
                        }
                        if (cursor.getColumnIndexOrThrow(DbContract.TableWords.COLUMN_NAME_LANGUAGE) == columnIndex) {
                            String language = cursor.getString(columnIndex);
                            view.setTag(R.string.key_language_passed, language);
                        }
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), EditWordActivity.class);
                                long wordId = Long.parseLong(v.getTag(R.string.key_word_id_passed).toString());
                                String word = v.getTag(R.string.key_word_passed).toString();
                                String language = v.getTag(R.string.key_language_passed).toString();
                                intent.putExtra(WordsFragment.INTENT_WORD_ID_PASSED, wordId);
                                intent.putExtra(WordsFragment.INTENT_WORD_PASSED, word);
                                intent.putExtra(WordsFragment.INTENT_LANGUAGE_PASSED, language);
                                intent.putExtra(WordsFragment.INTENT_COMMAND_EDIT_WORD, WordsFragment.COMMAND_WORD_EDIT);
                                startActivity(intent);
                            }
                        });
                    } else {
                        if (view.getId() == R.id.button_view_words_delete_word){
                            if (cursor.getColumnIndexOrThrow(DbContract.TableWords.COLUMN_NAME_WORD_ID) == columnIndex) {
                                long wordId = cursor.getLong(columnIndex);
                                view.setTag(R.string.key_word_id_passed, wordId);
                            }
                            if (cursor.getColumnIndexOrThrow(DbContract.TableWords.COLUMN_NAME_WORD) == columnIndex) {
                                String word = cursor.getString(columnIndex);
                                view.setTag(R.string.key_word_passed, word);
                            }
                            view.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(final View v){
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                    alertDialogBuilder.setTitle(DIALOG_TITLE_DELETE_WORD);
                                    String word = v.getTag(R.string.key_word_passed).toString();
                                    WordsFragment.wordIdToDelete = (long) v.getTag(R.string.key_word_id_passed);
                                    alertDialogBuilder.setMessage(word)
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    new DeleteWordTask().execute(WordsFragment.wordIdToDelete, null, null);
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            });
                        }else {
                            String text = cursor.getString(columnIndex);
                            TextView textView = (TextView) view;
                            textView.setText(text);
                        }
                    }
                    return true;
                }
            });
            lv.setAdapter(adapter);
        }

        private class DeleteWordTask extends AsyncTask<Long, Void, Void>{

            @Override
            protected Void doInBackground(Long[] params) {
                Long wordId = params[0];

                SQLiteDatabase dbWrite = mDbHelper.getWritableDatabase();
                dbWrite.beginTransaction();
                String sqlDelteFromUP = DbContract.getSqlDeleteWordInUP(wordId);
                String sqlDelteFromExamples = DbContract.getSqlDeleteWordInExamples(wordId);
                String sqlDelteFromDefEx = DbContract.getSqlDeleteWordInDefEx(wordId);
                String whereFromWordsT = DbContract.TableWords.COLUMN_NAME_WORD_ID + " = " + wordId;
                //ContentValues values = new ContentValues();
//            SQLiteStatement sqlStmtDeleteFromUP = dbWrite.compileStatement(sqlDelteFromUP);
//            SQLiteStatement sqlStmtDeleteFromExamples = dbWrite.compileStatement(sqlDelteFromExamples);
//            SQLiteStatement sqlStmtDeleteFromDefEx = dbWrite.compileStatement(sqlDelteFromDefEx);
                dbWrite.delete(DbContract.TableUserProgress.TABLE_NAME, sqlDelteFromUP, null);
                dbWrite.delete(DbContract.TableUserProgress.TABLE_NAME, sqlDelteFromExamples, null);
                dbWrite.delete(DbContract.TableUserProgress.TABLE_NAME, sqlDelteFromDefEx, null);
                dbWrite.delete(DbContract.TableWords.TABLE_NAME, whereFromWordsT, null);
                dbWrite.setTransactionSuccessful();
                dbWrite.endTransaction();

                return null;
            }

            @Override
            protected void onPostExecute(Void param){
                new DisplayWordsTask().execute(null, null, null);
            }
        }
    }

}
