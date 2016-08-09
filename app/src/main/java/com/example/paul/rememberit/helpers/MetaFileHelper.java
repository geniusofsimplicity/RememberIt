package com.example.paul.rememberit.helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Paul on 22.06.2016.
 */
public class MetaFileHelper {
    public static long userId;
    public static String userName;
    public static String currentSubject;
    public static String SEPARATOR = ",";
    public static String metafileName = "metadata";

    public void saveMetaToFile(FileOutputStream fileOutputStream){
        OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        if(userId > 0) {
            try {
                bufferedWriter.write(String.valueOf(userId));
                bufferedWriter.newLine();
                bufferedWriter.write(userName);
                bufferedWriter.newLine();
                bufferedWriter.write(currentSubject);
                bufferedWriter.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void readMetaFromFile(InputStream inputStream){
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        try {
            if (inputStream != null){

                String userIdString = bufferedReader.readLine();
                if (userIdString != null) {
                    userId = Long.parseLong(userIdString);
                    userName = bufferedReader.readLine();
                    currentSubject = bufferedReader.readLine();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (bufferedReader != null){
                    bufferedReader.close();
                }
                if (inputStreamReader != null){
                    inputStreamReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
