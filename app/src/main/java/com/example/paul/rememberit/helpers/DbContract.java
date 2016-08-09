package com.example.paul.rememberit.helpers;

import android.provider.BaseColumns;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Paul on 22.06.2016.
 */
public class DbContract {

    public DbContract(){}

    public static abstract class TableWords implements BaseColumns {
        public static final String TABLE_NAME = "words";
        //public static final String COLUMN_NAME_WORD_ID = "wordid";
        public static final String COLUMN_NAME_WORD_ID = BaseColumns._ID;
        public static final String COLUMN_NAME_WORD = "word";
        public static final String COLUMN_NAME_LANGUAGE = "language";
    }

    public static abstract class TableDefinitions implements BaseColumns {
        public static final String TABLE_NAME = "definitions";
        //public static final String COLUMN_NAME_DEFINITION_ID = "defid";
        public static final String COLUMN_NAME_DEFINITION_ID = BaseColumns._ID;
        public static final String COLUMN_NAME_WORD_ID = "wordid";
        public static final String COLUMN_NAME_PART_OF_SPEECH = "partofspeech";
        public static final String COLUMN_NAME_DEFINITION = "definition";
        public static final String COLUMN_NAME_REGISTER = "register";
        public static final String COLUMN_NAME_USABILITY = "usability";
    }

    public static abstract class TableExamples implements BaseColumns {
        public static final String TABLE_NAME = "examples";
        //public static final String COLUMN_NAME_EXAMPLE_ID = "exampleid";
        public static final String COLUMN_NAME_EXAMPLE_ID = BaseColumns._ID;
        //public static final String COLUMN_NAME_DEFINITION_ID = "defid";
        public static final String COLUMN_NAME_EXAMPLE = "example";
        public static final String COLUMN_NAME_DIFFICULTY = "difficulty";
    }

    public static abstract class TableUserProgress implements BaseColumns {
        public static final String TABLE_NAME = "userprogress";
        //public static final String COLUMN_NAME_USER_ID = "userid";
        public static final String COLUMN_NAME_USER_ID = BaseColumns._ID;
        //public static final String COLUMN_NAME_USER_ID = "userid";
        public static final String COLUMN_NAME_WORD_ID = "wordid";
        public static final String COLUMN_NAME_EXAMPLE_ID = "exampleid";
        public static final String COLUMN_NAME_LASTVIEW = "lastview";
        public static final String COLUMN_NAME_NEXTREVIEW = "nextreview";
        public static final String COLUMN_NAME_PROGRESS = "progress";
        public static final String COLUMN_NAME_FAVOURITE = "favourite";
    }

    public static abstract class TableUsers implements BaseColumns {
        public static final String TABLE_NAME = "users";
        //public static final String COLUMN_NAME_USER_ID = "userid";
        public static final String COLUMN_NAME_USER_ID = BaseColumns._ID;
        public static final String COLUMN_NAME_USER_NAME = "username";
    }

    public static abstract class TableWordDefinition implements BaseColumns {
        public static final String TABLE_NAME = "word_definition";
        //public static final String COLUMN_NAME_WORD_ID = "wordid";
        public static final String COLUMN_NAME_WORD_ID = BaseColumns._ID;
        public static final String COLUMN_NAME_DEFINITION_ID = "defid";
    }

    public static abstract class TableDefinitionExample implements BaseColumns {
        public static final String TABLE_NAME = "definition_example";
        //public static final String COLUMN_NAME_DEFINITION_ID = "defid";
        public static final String COLUMN_NAME_DEFINITION_ID = BaseColumns._ID;
        public static final String COLUMN_NAME_EXAMPLE_ID = "exampleid";
    }

    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String TEXT_TYPE = " TEXT";
    private static final String DATETIME_TYPE = " DATETIME";
    private static final String PRIMARY_KEY = " PRIMARY KEY ";

    public static final String COLUMN_NAME__ID = "_id";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_TABLE_WORDS =
            "CREATE TABLE IF NOT EXISTS " + TableWords.TABLE_NAME +" (" +
                    //TableWords._ID + " INTEGER PRIMARY KEY autoincrement" + COMMA_SEP +
                    //TableWords.COLUMN_NAME_WORD_ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                    TableWords._ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                    TableWords.COLUMN_NAME_WORD + TEXT_TYPE + COMMA_SEP +
                    TableWords.COLUMN_NAME_LANGUAGE + TEXT_TYPE +
                    ");";

    public static final String SQL_DELETE_TABLE_WORDS =
            "DROP TABLE IF EXISTS " + TableWords.TABLE_NAME;

    public static final String SQL_CREATE_TABLE_DEFINITIONS =
            "CREATE TABLE IF NOT EXISTS " + TableDefinitions.TABLE_NAME + " (" +
                    TableDefinitions.COLUMN_NAME_DEFINITION_ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                    TableDefinitions.COLUMN_NAME_WORD_ID + INTEGER_TYPE + COMMA_SEP +
                    TableDefinitions.COLUMN_NAME_PART_OF_SPEECH + INTEGER_TYPE + COMMA_SEP +
                    TableDefinitions.COLUMN_NAME_DEFINITION + TEXT_TYPE + COMMA_SEP +
                    TableDefinitions.COLUMN_NAME_REGISTER + TEXT_TYPE + COMMA_SEP +
                    TableDefinitions.COLUMN_NAME_USABILITY + INTEGER_TYPE +
                    " );";

    public static final String SQL_DELETE_TABLE_DEFINITIONS =
            "DROP TABLE IF EXISTS " + TableDefinitions.TABLE_NAME;

    public static final String SQL_CREATE_TABLE_EXAMPLES =
            "CREATE TABLE IF NOT EXISTS " + TableExamples.TABLE_NAME + " (" +
                    TableExamples.COLUMN_NAME_EXAMPLE_ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                    //TableExamples.COLUMN_NAME_DEFINITION_ID + TEXT_TYPE + COMMA_SEP +
                    TableExamples.COLUMN_NAME_EXAMPLE + TEXT_TYPE + COMMA_SEP +
                    TableExamples.COLUMN_NAME_DIFFICULTY + INTEGER_TYPE +
                    " );";

    public static final String SQL_DELETE_TABLE_EXAMPLES =
            "DROP TABLE IF EXISTS " + TableExamples.TABLE_NAME;

    public static final String SQL_CREATE_TABLE_USER_PROGRESS =
            "CREATE TABLE IF NOT EXISTS " + TableUserProgress.TABLE_NAME + " (" +
                    //TableUserProgress._ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                    //TableUserProgress.COLUMN_NAME_ID + INTEGER_TYPE + COMMA_SEP +
                    TableUserProgress.COLUMN_NAME_USER_ID + INTEGER_TYPE + COMMA_SEP +
                    TableUserProgress.COLUMN_NAME_WORD_ID + INTEGER_TYPE + COMMA_SEP +
                    TableUserProgress.COLUMN_NAME_EXAMPLE_ID + INTEGER_TYPE + COMMA_SEP +
                    TableUserProgress.COLUMN_NAME_LASTVIEW + DATETIME_TYPE + COMMA_SEP +
                    TableUserProgress.COLUMN_NAME_NEXTREVIEW + DATETIME_TYPE + COMMA_SEP +
                    TableUserProgress.COLUMN_NAME_PROGRESS + INTEGER_TYPE + COMMA_SEP +
                    TableUserProgress.COLUMN_NAME_FAVOURITE + INTEGER_TYPE +
                    " );";

    public static final String SQL_DELETE_TABLE_USER_PROGRESS =
            "DROP TABLE IF EXISTS " + TableUserProgress.TABLE_NAME;

    public static final String SQL_CREATE_TABLE_USERS =
            "CREATE TABLE IF NOT EXISTS " + TableUsers.TABLE_NAME + " (" +
                    TableUsers.COLUMN_NAME_USER_ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                    TableUsers.COLUMN_NAME_USER_NAME + TEXT_TYPE +
                    " );";

    public static final String SQL_DELETE_TABLE_USERS =
            "DROP TABLE IF EXISTS " + TableUsers.TABLE_NAME;

    public static final String SQL_CREATE_TABLE_WORD_DEFINITION =
            "CREATE TABLE IF NOT EXISTS " + TableDefinitionExample.TABLE_NAME + " (" +
                    TableWordDefinition.COLUMN_NAME_WORD_ID + INTEGER_TYPE + COMMA_SEP +
                    TableWordDefinition.COLUMN_NAME_DEFINITION_ID + INTEGER_TYPE +
                    " );";

    public static final String SQL_DELETE_TABLE_WORD_DEFINITION =
            "DROP TABLE IF EXISTS " + TableWordDefinition.TABLE_NAME;

    public static final String SQL_CREATE_TABLE_DEFINITION_EXAMPLE =
            "CREATE TABLE IF NOT EXISTS " + TableDefinitionExample.TABLE_NAME + " (" +
                    TableDefinitionExample.COLUMN_NAME_DEFINITION_ID + INTEGER_TYPE + COMMA_SEP +
                    TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID + INTEGER_TYPE +
                    " );";

    public static final String SQL_DELETE_TABLE_DEFINITION_EXAMPLE =
            "DROP TABLE IF EXISTS " + TableDefinitionExample.TABLE_NAME;

    public static final String SQL_SELECT_ALL_DEFINITIONS =
            "SELECT D." + TableDefinitions.COLUMN_NAME_DEFINITION_ID + COMMA_SEP +
                    " W." + TableWords.COLUMN_NAME_WORD + COMMA_SEP +
                    " D." + TableDefinitions.COLUMN_NAME_PART_OF_SPEECH + COMMA_SEP +
                    " D." + TableDefinitions.COLUMN_NAME_REGISTER + COMMA_SEP +
                    " W." + TableWords.COLUMN_NAME_LANGUAGE + COMMA_SEP +
                    " D." + TableDefinitions.COLUMN_NAME_DEFINITION + COMMA_SEP +
                    " D." + TableDefinitions.COLUMN_NAME_USABILITY +
                    " FROM " + TableDefinitions.TABLE_NAME + " AS D JOIN " + TableWords.TABLE_NAME +
                    " AS W ON W." + TableWords.COLUMN_NAME_WORD_ID +
                    " = " + TableDefinitions.COLUMN_NAME_WORD_ID + ";"
            ;

    public static final String SQL_SELECT_DEFINITIONS_WITHOUT_EXAMPLES =
            "SELECT SUB." + TableDefinitions.COLUMN_NAME_DEFINITION_ID + " AS '_id', " +
                    TableWords.COLUMN_NAME_WORD + ", " + TableWords.COLUMN_NAME_LANGUAGE + ", " +
                    TableDefinitions.COLUMN_NAME_PART_OF_SPEECH + COMMA_SEP + TableDefinitions.COLUMN_NAME_REGISTER +
                    COMMA_SEP + TableDefinitions.COLUMN_NAME_DEFINITION + COMMA_SEP +
                    TableDefinitions.COLUMN_NAME_USABILITY + " FROM (SELECT * FROM " +
                    TableDefinitions.TABLE_NAME + " AS D LEFT JOIN " + TableDefinitionExample.TABLE_NAME +
                    " AS DE ON D." + TableDefinitions.COLUMN_NAME_DEFINITION_ID + " = DE." +
                    TableDefinitionExample.COLUMN_NAME_DEFINITION_ID + " WHERE DE." + TableDefinitionExample.COLUMN_NAME_DEFINITION_ID +
                    " IS NULL) AS SUB JOIN " + TableWords.TABLE_NAME + " AS W ON W." +
                    TableWords.COLUMN_NAME_WORD_ID + " = SUB." + TableDefinitions.COLUMN_NAME_WORD_ID + ";";

    public static final String SQL_SELECT_EXAMPLES =
            "SELECT E." + TableExamples.COLUMN_NAME_EXAMPLE_ID + COMMA_SEP +
                    TableExamples.COLUMN_NAME_EXAMPLE +
                    " FROM " + TableExamples.TABLE_NAME + " AS E JOIN " +
                    TableDefinitionExample.TABLE_NAME + " AS DE ON DE." +
                    TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID + " = E." + TableExamples.COLUMN_NAME_EXAMPLE_ID +
                    " AND DE." + TableDefinitionExample.COLUMN_NAME_DEFINITION_ID + " = ";
    ;

    public static String getSqlRequestSelectExamples(long defId){
        String sql = "SELECT E." + TableExamples.COLUMN_NAME_EXAMPLE_ID + COMMA_SEP +
                " E." + TableExamples.COLUMN_NAME_EXAMPLE + COMMA_SEP +
                " E." + TableExamples.COLUMN_NAME_DIFFICULTY +
                " FROM " + TableExamples.TABLE_NAME + " AS E JOIN " +
                TableDefinitionExample.TABLE_NAME + " AS DE ON DE." +
                TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID + " = E." + TableExamples.COLUMN_NAME_EXAMPLE_ID +
                " AND DE." + TableDefinitionExample.COLUMN_NAME_DEFINITION_ID + " = " + defId + ";";
        ;
        return sql;
    }

    public static String getSqlRequestDependentDefinitions(long exampleId, long defId){
        String sql = "SELECT D." + TableDefinitions.COLUMN_NAME_DEFINITION_ID + COMMA_SEP +
                " D." + TableDefinitions.COLUMN_NAME_DEFINITION + COMMA_SEP +
                " W." + TableWords.COLUMN_NAME_WORD +
                " FROM " + TableDefinitions.TABLE_NAME + " AS D" +
                " JOIN " + TableDefinitionExample.TABLE_NAME + " AS DE ON DE." +
                TableDefinitionExample.COLUMN_NAME_DEFINITION_ID + " = D." +
                TableDefinitions.COLUMN_NAME_DEFINITION_ID + " AND DE." +
                TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID + " = " + exampleId +
                " AND D." + TableDefinitions.COLUMN_NAME_DEFINITION_ID + " <> " + defId +
                " JOIN " + TableWords.TABLE_NAME + " AS W ON W." +
                TableWords.COLUMN_NAME_WORD_ID + " = D." + TableDefinitions.COLUMN_NAME_WORD_ID +
                " AND DE." + TableDefinitionExample.COLUMN_NAME_DEFINITION_ID + " <> " + exampleId +  ";";
        return sql;
    }

    public static String getSqlRequestDependentExamples(long defId){
        String sql = "SELECT E." + TableExamples.COLUMN_NAME_EXAMPLE_ID + COMMA_SEP +
                " E." + TableExamples.COLUMN_NAME_EXAMPLE +
                " FROM " + TableExamples.TABLE_NAME + " AS E JOIN " +
                TableDefinitionExample.TABLE_NAME + " AS DE ON DE." +
                TableDefinitionExample.COLUMN_NAME_DEFINITION_ID + " = " + defId +
                " AND DE." + TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID +
                " = E." + TableExamples.COLUMN_NAME_EXAMPLE_ID + ";";
        return sql;
    }

    public static String getSqlDeleteSingleExample(long exampleId) {
        String sql = "DELETE FROM " + TableExamples.TABLE_NAME +
                " WHERE " + TableExamples.COLUMN_NAME_EXAMPLE_ID +
                " = " + exampleId +
                " AND (SELECT COUNT(*) FROM " + TableDefinitionExample.TABLE_NAME +
                " WHERE " + TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID +
                " = " + exampleId + ") = 1;";
        return sql;
    }

    public static String getSqlDeleteWordInUP(long wordId){
        String sql = /*"DELETE FROM " + TableUserProgress.TABLE_NAME +
                " WHERE " + */TableUserProgress.COLUMN_NAME_EXAMPLE_ID + " IN" +
                " (SELECT UP." + TableUserProgress.COLUMN_NAME_EXAMPLE_ID + " FROM " +
                TableDefinitions.TABLE_NAME + " AS D" +
                " JOIN " + TableDefinitionExample.TABLE_NAME + " AS DE" +
                " ON D." + TableDefinitions.COLUMN_NAME_WORD_ID + " = " + String.valueOf(wordId) +
                " AND D." + TableDefinitions.COLUMN_NAME_DEFINITION_ID + " = DE." + TableDefinitionExample.COLUMN_NAME_DEFINITION_ID +
                " JOIN " + TableExamples.TABLE_NAME + " AS E" +
                " ON DE." + TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID + " = E." + TableExamples.COLUMN_NAME_EXAMPLE_ID +
                " JOIN " + TableUserProgress.TABLE_NAME + " AS UP" +
                " ON UP." + TableUserProgress.COLUMN_NAME_EXAMPLE_ID + " = E." + TableExamples.COLUMN_NAME_EXAMPLE_ID +
                " AND UP." + TableUserProgress.COLUMN_NAME_USER_ID + " = " + String.valueOf(MetaFileHelper.userId) + ")";
        return sql;
    }

    public static String getSqlDeleteWordInExamples(long wordId){
        String sql = /*"DELETE FROM " + TableExamples.TABLE_NAME +
                " WHERE " +*/ TableExamples.COLUMN_NAME_EXAMPLE_ID + " IN" +
                " (SELECT E." + TableExamples.COLUMN_NAME_EXAMPLE_ID +
                " FROM " + TableDefinitions.TABLE_NAME + " AS D" +
                " JOIN " + TableDefinitionExample.TABLE_NAME + " AS DE" +
                " ON D." + TableDefinitions.COLUMN_NAME_WORD_ID + " = " + String.valueOf(wordId) +
                " AND D." + TableDefinitions.COLUMN_NAME_DEFINITION_ID + " = DE." + TableDefinitionExample.COLUMN_NAME_DEFINITION_ID +
                " JOIN " + TableExamples.TABLE_NAME + " AS E" +
                " ON DE." + TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID + " = E." + TableExamples.COLUMN_NAME_EXAMPLE_ID + ")";
        return sql;
    }

    public static String getSqlDeleteWordInDefEx(long wordId){
        String sql = /*"DELETE FROM " + TableDefinitionExample.TABLE_NAME +
                " WHERE " +*/ TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID + " IN" +
                " (SELECT DE." + TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID +
                " FROM " + TableDefinitions.TABLE_NAME + " AS D" +
                " JOIN " + TableDefinitionExample.TABLE_NAME + " AS DE" +
                " ON D." + TableDefinitions.COLUMN_NAME_WORD_ID + " = " + String.valueOf(wordId) +
                " AND D." + TableDefinitions.COLUMN_NAME_DEFINITION_ID + " = DE." + TableDefinitionExample.COLUMN_NAME_DEFINITION_ID + ")";
        return sql;
    }

    public static String getSqlOneLessonCards(List<Long> wordsIds, List<Long> examplesIds, long userId, int limit){
        Set<Long> hs = new HashSet<>();
        hs.addAll(wordsIds);
        wordsIds.clear();
        wordsIds.addAll(hs);
        hs.clear();
        hs.addAll(examplesIds);
        examplesIds.clear();
        examplesIds.addAll(hs);
        String wordsIdsStr = wordsIds.toString();
        wordsIdsStr = wordsIdsStr.replace("[", "(");
        wordsIdsStr = wordsIdsStr.replace("]", ")");
        String exsIdsStr = examplesIds.toString();
        exsIdsStr = exsIdsStr.replace("[", "(");
        exsIdsStr = exsIdsStr.replace("]", ")");
        int numberOfGivenExamples = examplesIds.size();
        String sql = "SELECT W." + TableWords.COLUMN_NAME_WORD_ID + " AS 'wordid', W." +
                TableWords.COLUMN_NAME_WORD + " AS 'word', D." + TableDefinitions.COLUMN_NAME_DEFINITION +
                " AS 'definition', E." + TableExamples.COLUMN_NAME_EXAMPLE + " AS 'example', D." + TableDefinitions.COLUMN_NAME_DEFINITION_ID +
                " AS 'defid', DE." + TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID +
                " AS 'exampleid', " + TableUserProgress.COLUMN_NAME_LASTVIEW + ", " +
                TableUserProgress.COLUMN_NAME_NEXTREVIEW + ", " + TableUserProgress.COLUMN_NAME_PROGRESS + ", " +
                TableUserProgress.COLUMN_NAME_FAVOURITE + " FROM " + TableUserProgress.TABLE_NAME +
                " AS UP JOIN " + TableWords.TABLE_NAME + " AS W ON W." + TableWords.COLUMN_NAME_WORD_ID +
                " = UP." + TableUserProgress.COLUMN_NAME_WORD_ID + " AND UP." + TableUserProgress.COLUMN_NAME_EXAMPLE_ID +
                " IN " + exsIdsStr + " AND UP." + TableUserProgress.COLUMN_NAME_WORD_ID +
                " IN " + wordsIdsStr + " AND UP." + TableUserProgress.COLUMN_NAME_USER_ID + " = " + String.valueOf(userId) +
                " AND UP." + TableUserProgress.COLUMN_NAME_NEXTREVIEW + " < Datetime('now') " +
                " JOIN " + TableDefinitionExample.TABLE_NAME + " AS DE ON DE." + TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID +
                " = UP." + TableUserProgress.COLUMN_NAME_EXAMPLE_ID +
                " JOIN " + TableDefinitions.TABLE_NAME + " AS D ON D." + TableDefinitions.COLUMN_NAME_DEFINITION_ID +
                " = DE." + TableDefinitionExample.COLUMN_NAME_DEFINITION_ID +
                " JOIN " + TableExamples.TABLE_NAME + " AS E ON E." + TableExamples.COLUMN_NAME_EXAMPLE_ID +
                " = UP." + TableUserProgress.COLUMN_NAME_EXAMPLE_ID +
                " UNION " +
                "SELECT SUBSELECT.'wordid', SUBSELECT." + TableWords.COLUMN_NAME_WORD +
                ", SUBSELECT." + TableDefinitions.COLUMN_NAME_DEFINITION + ", SUBSELECT." + TableExamples.COLUMN_NAME_EXAMPLE +
                ", SUBSELECT._Id" +
                ", SUBSELECT." + TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID +"" +
                ", NULL AS '" + TableUserProgress.COLUMN_NAME_LASTVIEW +
                "', NULL AS '" + TableUserProgress.COLUMN_NAME_NEXTREVIEW +
                "', NULL AS '" + TableUserProgress.COLUMN_NAME_PROGRESS +
                "', NULL AS '" + TableUserProgress.COLUMN_NAME_FAVOURITE +
                "' FROM (SELECT W." + TableWords.COLUMN_NAME_WORD_ID + " AS 'wordid', W." + TableWords.COLUMN_NAME_WORD +
                " AS 'word', D." + TableDefinitions.COLUMN_NAME_DEFINITION + ", E." + TableExamples.COLUMN_NAME_EXAMPLE +
                ", D." + TableDefinitions.COLUMN_NAME_DEFINITION_ID + ", DE." + TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID +
                " FROM " + TableDefinitionExample.TABLE_NAME + " AS DE JOIN " + TableExamples.TABLE_NAME +
                " AS E ON E." + TableExamples.COLUMN_NAME_EXAMPLE_ID + " = DE." + TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID +
                " AND NOT EXISTS ( SELECT * FROM " + TableUserProgress.TABLE_NAME + " AS up WHERE up." + TableUserProgress.COLUMN_NAME_WORD_ID +
                " = W." + TableWords.COLUMN_NAME_WORD_ID + " AND up." + TableUserProgress.COLUMN_NAME_EXAMPLE_ID + " = DE." + TableDefinitionExample.COLUMN_NAME_EXAMPLE_ID + " AND UP."+
                TableUserProgress.COLUMN_NAME_USER_ID + " = " + String.valueOf(userId) +")" +
                " JOIN " + TableDefinitions.TABLE_NAME + " AS D ON D." + TableDefinitions.COLUMN_NAME_DEFINITION_ID +
                " = DE." + TableDefinitionExample.COLUMN_NAME_DEFINITION_ID +
                " JOIN " + TableWords.TABLE_NAME + " AS W ON W." + TableWords.COLUMN_NAME_WORD_ID + " = D." + TableDefinitions.COLUMN_NAME_WORD_ID +
                " AND W." + TableWords.COLUMN_NAME_LANGUAGE + " = '" + MetaFileHelper.currentSubject + "'" +
                " GROUP BY wordId ORDER BY RANDOM() LIMIT " + String.valueOf(limit - numberOfGivenExamples) + ") AS SUBSELECT;";
        return sql;
    }

    public static String getSqlLessonCardsInsert(){
        String sql = "INSERT OR REPLACE INTO " + DbContract.TableUserProgress.TABLE_NAME + " (" + TableUserProgress.COLUMN_NAME_USER_ID +", " +
                TableUserProgress.COLUMN_NAME_WORD_ID +", " + TableUserProgress.COLUMN_NAME_EXAMPLE_ID +", " + TableUserProgress.COLUMN_NAME_LASTVIEW +
                ", " + TableUserProgress.COLUMN_NAME_NEXTREVIEW + ", " + TableUserProgress.COLUMN_NAME_PROGRESS +
                ", " + TableUserProgress.COLUMN_NAME_FAVOURITE + ") values(?, ?, ?, ?, ?, ?, ?)";
        return sql;
    }

    public static String getSqlLessonCardsDelete(){
        String sql = "INSERT OR REPLACE INTO " + DbContract.TableUserProgress.TABLE_NAME + " (" + TableUserProgress.COLUMN_NAME_USER_ID +", " +
                TableUserProgress.COLUMN_NAME_WORD_ID +", " + TableUserProgress.COLUMN_NAME_EXAMPLE_ID +", " + TableUserProgress.COLUMN_NAME_LASTVIEW +
                ", " + TableUserProgress.COLUMN_NAME_NEXTREVIEW + ", " + TableUserProgress.COLUMN_NAME_PROGRESS +
                ", " + TableUserProgress.COLUMN_NAME_FAVOURITE + ") values(?, ?, ?, ?, ?, ?, ?)";
        return sql;
    }

    public static String getSqlExamplesForRevision(){
        String sql = "SELECT UP.* FROM " + TableUserProgress.TABLE_NAME + " AS UP" +
                " JOIN " + TableWords.TABLE_NAME + " AS W" +
                " ON W." + TableWords.COLUMN_NAME_WORD_ID + " = UP." + TableUserProgress.COLUMN_NAME_WORD_ID +
                " AND UP." + TableUserProgress.COLUMN_NAME_USER_ID + " = " + String.valueOf(MetaFileHelper.userId) +
                " AND W." + TableWords.COLUMN_NAME_LANGUAGE + " = '" + MetaFileHelper.currentSubject + "'" +
                " AND UP." + TableUserProgress.COLUMN_NAME_NEXTREVIEW + " <= Datetime('now')";
        return sql;
    }

}
