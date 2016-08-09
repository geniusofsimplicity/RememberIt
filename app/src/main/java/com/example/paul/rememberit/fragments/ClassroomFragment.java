package com.example.paul.rememberit.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.paul.rememberit.R;
import com.example.paul.rememberit.activities.SchoolActivity;
import com.example.paul.rememberit.helpers.Card;
import com.example.paul.rememberit.helpers.DbContract;
import com.example.paul.rememberit.helpers.DbHelper;
import com.example.paul.rememberit.helpers.MetaFileHelper;
import com.example.paul.rememberit.helpers.ProgressRow;
import com.example.paul.rememberit.helpers.Teacher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 24.06.2016.
 */
public class ClassroomFragment extends Fragment {

    private DbHelper mDbHelper;
    private Cursor cursor;
    private Teacher teacher;
    private Card currentCard;
    private Button buttonEasy;
    private Button buttonMeduim;
    private Button buttonHard;
    private View view;

    public static byte numberOfWordsForLesson = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_classroom, container, false);

        buttonEasy = (Button)view.findViewById(R.id.button_classroom_easy);
        buttonEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacher.saveCard(0);
                if (teacher.hasNextCard()){
                    displayCurrentCard();
                }else{
                    saveAndExitLesson();
                }
            }
        });

        buttonMeduim = (Button)view.findViewById(R.id.button_classroom_medium);
        buttonMeduim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacher.saveCard(1);
                if (teacher.hasNextCard()){
                    displayCurrentCard();
                }else{
                    saveAndExitLesson();
                }
            }
        });
        buttonHard = (Button)view.findViewById(R.id.button_classroom_hard);
        buttonHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacher.saveCard(2);
                if (teacher.hasNextCard()){
                    displayCurrentCard();
                }else{
                    saveAndExitLesson();
                }
            }
        });

        mDbHelper = new DbHelper(getActivity());
        new QueryWaitingRowsTask().execute(null, null, null);

        return view;
    }

    private void displayCurrentCard(){
        cleanViews();
        currentCard = teacher.nextCard();
        Button buttonShowAnswer = (Button)view.findViewById(R.id.button_classroom_show_answer);


        int progress = currentCard.progress;

        if (progress < 1){
            String word = currentCard.word;
            TextView textViewWord = (TextView)view.findViewById(R.id.text_classroom_word);
            textViewWord.setText(word);

            String def = currentCard.definition;
            TextView textViewDef = (TextView)view.findViewById(R.id.text_classroom_definition);
            textViewDef.setText(def);

            String example = currentCard.example;
            TextView textViewEx = (TextView)view.findViewById(R.id.text_classroom_example);
            textViewEx.setText(example);

            buttonShowAnswer.setEnabled(false);
        }
        if (1 <= progress && progress < 4){
            String word = currentCard.word;
            TextView textViewWord = (TextView)view.findViewById(R.id.text_classroom_word);
            textViewWord.setText(word);

            String example = currentCard.example;
            TextView textViewEx = (TextView)view.findViewById(R.id.text_classroom_example);
            textViewEx.setText(example);

            buttonShowAnswer.setEnabled(true);
            saveButtonsActivate(false);
        }
        if (4 <= progress && progress < 7){
            String word = currentCard.word;

            String def = currentCard.definition;
            TextView textViewDef = (TextView)view.findViewById(R.id.text_classroom_definition);
            textViewDef.setText(def);

            String example = currentCard.example;
            String exampleWithoutWord = example.replace(word, "____");
            TextView textViewEx = (TextView)view.findViewById(R.id.text_classroom_example);
            textViewEx.setText(exampleWithoutWord);

            buttonShowAnswer.setEnabled(true);
            saveButtonsActivate(false);
        }
        if (7 <= progress){
            String word = currentCard.word;

            String def = currentCard.definition;
            String defWithoutWord = def.replace(word, "____");
            TextView textViewDef = (TextView)view.findViewById(R.id.text_classroom_definition);
            textViewDef.setText(defWithoutWord);

            buttonShowAnswer.setEnabled(true);
            saveButtonsActivate(false);
        }
    }

    private void cleanViews(){
        TextView textViewWord = (TextView) view.findViewById(R.id.text_classroom_word);
        textViewWord.setText("");

        TextView textViewDef = (TextView) view.findViewById(R.id.text_classroom_definition);
        textViewDef.setText("");

        TextView textViewEx = (TextView) view.findViewById(R.id.text_classroom_example);
        textViewEx.setText("");
    }

    public void showAnswer(View view){
        int progress = currentCard.progress;
        if (progress < 1){
        }
        if (1 <= progress && progress < 4){
            String def = currentCard.definition;
            TextView textViewDef = (TextView) view.findViewById(R.id.text_classroom_definition);
            textViewDef.setText(def);
        }
        if (4 <= progress && progress < 7){
            String word = currentCard.word;
            TextView textViewWord = (TextView) view.findViewById(R.id.text_classroom_word);
            textViewWord.setText(word);
        }
        if (7 <= progress){
            String word = currentCard.word;
            TextView textViewWord = (TextView) view.findViewById(R.id.text_classroom_word);
            textViewWord.setText(word);
        }

        saveButtonsActivate(true);
    }

    private void saveButtonsActivate(boolean isEnabled){
        buttonEasy.setEnabled(isEnabled);
        buttonMeduim.setEnabled(isEnabled);
        buttonHard.setEnabled(isEnabled);
    }

    private void saveAndExitLesson(){
        new SaveOneLessonTask().execute(null, null, null);
        Intent intent = new Intent(getActivity(), SchoolActivity.class);
        startActivity(intent);
    }


    private class QueryWaitingRowsTask extends AsyncTask<Void, String, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {
            SQLiteDatabase dbRead = mDbHelper.getReadableDatabase();
            String sqlExamplesForRevision = DbContract.getSqlExamplesForRevision();
            cursor = dbRead.rawQuery(sqlExamplesForRevision, null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor){
            ArrayList<Long> progressList = new ArrayList<Long>();;
            ArrayList<ProgressRow> poolOfExamples = new ArrayList<ProgressRow>();;

            if ( cursor.moveToFirst() ) {
                int wordsIdColInd = cursor.getColumnIndexOrThrow(DbContract.TableUserProgress.COLUMN_NAME_WORD_ID);
                int progressColInd = cursor.getColumnIndexOrThrow(DbContract.TableUserProgress.COLUMN_NAME_PROGRESS);
                int exampleIdColInd = cursor.getColumnIndexOrThrow(DbContract.TableUserProgress.COLUMN_NAME_EXAMPLE_ID);
                long temp = -1;
                do {
                    ProgressRow row = new ProgressRow();
                    row.wordId = cursor.getLong(wordsIdColInd);
                    row.exampleId = cursor.getLong(exampleIdColInd);
                    poolOfExamples.add(row);
                    temp = cursor.getLong(progressColInd);
                    progressList.add(temp);
                } while (cursor.moveToNext());
            }
            ArrayList<ProgressRow> resultExamplesIds = new ArrayList<ProgressRow>();
            resultExamplesIds = Teacher.prepareWordsForLesson(poolOfExamples, progressList, numberOfWordsForLesson);

            List<Long> wordsOneLessonIds = new ArrayList<Long>();
            List<Long> exsOneLessonIds = new ArrayList<Long>();
            Long tempId;

            for(ProgressRow row: resultExamplesIds){
                tempId = row.wordId;
                wordsOneLessonIds.add(tempId);
                tempId = row.exampleId;
                exsOneLessonIds.add(tempId);
            }
            String sqlOneLessonCards = DbContract.getSqlOneLessonCards(wordsOneLessonIds, exsOneLessonIds, MetaFileHelper.userId, numberOfWordsForLesson);
            new QueryOneLessonCardsTask().execute(sqlOneLessonCards, null, null);
        }
    }

    private class QueryOneLessonCardsTask extends AsyncTask<String, Void, Cursor>{

        @Override
        protected Cursor doInBackground(String... input) {
            String sqlOneLessonCards = input[0];
            SQLiteDatabase dbRead = mDbHelper.getReadableDatabase();
            cursor = dbRead.rawQuery(sqlOneLessonCards, null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor){
            List<Card> cardsForOneLesson = new ArrayList<Card>();
            String columnNameTemp;

            if ( cursor.moveToFirst() ) {
                int wordIdColInd = cursor.getColumnIndexOrThrow(DbContract.TableUserProgress.COLUMN_NAME_WORD_ID);
                //int wordColInd = cursor.getColumnIndexOrThrow("W." + DbContract.TableWords.COLUMN_NAME_WORD);
                columnNameTemp = DbContract.TableWords.COLUMN_NAME_WORD;
                int wordColInd = cursor.getColumnIndexOrThrow(columnNameTemp);

                int defColInd = cursor.getColumnIndexOrThrow(DbContract.TableDefinitions.COLUMN_NAME_DEFINITION);
                int exampleColInd = cursor.getColumnIndexOrThrow(DbContract.TableExamples.COLUMN_NAME_EXAMPLE);
                int defIdColInd = cursor.getColumnIndexOrThrow(DbContract.TableWordDefinition.COLUMN_NAME_DEFINITION_ID);
                int exampleIdColInd = cursor.getColumnIndexOrThrow(DbContract.TableUserProgress.COLUMN_NAME_EXAMPLE_ID);
                int lastviewColInd = cursor.getColumnIndexOrThrow(DbContract.TableUserProgress.COLUMN_NAME_LASTVIEW);
                int nextReviewColInd = cursor.getColumnIndexOrThrow(DbContract.TableUserProgress.COLUMN_NAME_NEXTREVIEW);
                int progressColInd = cursor.getColumnIndexOrThrow(DbContract.TableUserProgress.COLUMN_NAME_PROGRESS);
                int favouriteColInd = cursor.getColumnIndexOrThrow(DbContract.TableUserProgress.COLUMN_NAME_FAVOURITE);
                String lastViewStr, nextReviewStr;
                long temp = -1;
                do {
                    Card card = new Card();
                    card.wordId = cursor.getLong(wordIdColInd);
                    card.word = cursor.getString(wordColInd);
                    card.definition = cursor.getString(defColInd);
                    card.example = cursor.getString(exampleColInd);
                    card.exampleId = cursor.getLong(exampleIdColInd);
                    lastViewStr = cursor.getString(lastviewColInd);
                    if (lastViewStr != null) {
                        //card.lastview = Date.valueOf(lastViewStr);
                        card.lastview = lastViewStr;
                        nextReviewStr = cursor.getString(nextReviewColInd);
                        //card.nextreview = Date.valueOf(nextReviewStr);
                        card.nextreview = nextReviewStr;
                        card.progress = cursor.getInt(progressColInd);
                        card.favourite = cursor.getInt(favouriteColInd);
                    }
                    cardsForOneLesson.add(card);
                } while (cursor.moveToNext());
                teacher = new Teacher(cardsForOneLesson);
                displayCurrentCard();
            }else{
                Intent intent = new Intent(getActivity(), SchoolActivity.class);
                startActivity(intent);
            }
        }
    }

    private class SaveOneLessonTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            SQLiteDatabase dbWrite = mDbHelper.getWritableDatabase();
            dbWrite.beginTransaction();
            String sqlInsert = DbContract.getSqlLessonCardsInsert();
            //ContentValues values = new ContentValues();
            SQLiteStatement sqlStmtInsert = dbWrite.compileStatement(sqlInsert);
            List<Card> cards = teacher.getCardsForOneLesson();
            long newRowId;
            String whereClause = "";

            for(Card c: cards){
            /*values.put(DbContract.TableUserProgress.COLUMN_NAME_USER_ID, String.valueOf(StartActivity.userId));
            values.put(DbContract.TableUserProgress.COLUMN_NAME_WORD_ID, String.valueOf(c.wordId));
            values.put(DbContract.TableUserProgress.COLUMN_NAME_EXAMPLE_ID, String.valueOf(c.exampleId));
            values.put(DbContract.TableUserProgress.COLUMN_NAME_LASTVIEW, c.lastview);
            values.put(DbContract.TableUserProgress.COLUMN_NAME_NEXTREVIEW, c.nextreview);
            values.put(DbContract.TableUserProgress.COLUMN_NAME_PROGRESS, String.valueOf(c.progress));
            values.put(DbContract.TableUserProgress.COLUMN_NAME_FAVOURITE, String.valueOf(c.favourite));
            newRowId = dbWrite.insertWithOnConflict(DbContract.TableUserProgress.TABLE_NAME, BaseColumns._ID, values, SQLiteDatabase.CONFLICT_REPLACE);
            */
                whereClause = DbContract.TableUserProgress.COLUMN_NAME_USER_ID + " = " + MetaFileHelper.userId + " AND " +
                        DbContract.TableUserProgress.COLUMN_NAME_WORD_ID + " = " + String.valueOf(c.wordId) + " AND " +
                        DbContract.TableUserProgress.COLUMN_NAME_EXAMPLE_ID + " = " + String.valueOf(c.exampleId);
                dbWrite.delete(DbContract.TableUserProgress.TABLE_NAME, whereClause, null);

                sqlStmtInsert.bindString(1, String.valueOf(MetaFileHelper.userId));
                sqlStmtInsert.bindString(2, String.valueOf(c.wordId));
                sqlStmtInsert.bindString(3, String.valueOf(c.exampleId));
                sqlStmtInsert.bindString(4, c.lastview);
                sqlStmtInsert.bindString(5, c.nextreview);
                sqlStmtInsert.bindString(6, String.valueOf(c.progress));
                sqlStmtInsert.bindString(7, String.valueOf(c.favourite));
                sqlStmtInsert.execute();
            }
            dbWrite.setTransactionSuccessful();
            dbWrite.endTransaction();
            return null;
        }
    }

}
