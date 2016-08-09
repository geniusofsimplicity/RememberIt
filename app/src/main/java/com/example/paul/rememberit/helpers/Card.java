package com.example.paul.rememberit.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Paul on 26.06.2016.
 */
public class Card {

    public Long userId;
    public Long wordId;
    public String word;
    public String definition;
    public String example;
    public Long exampleId;
    public String lastview;
    public String nextreview;
    public int progress = 0;
    public int favourite;

    public void setLastview() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        lastview = dateFormat.format(cal.getTime());
        //lastview = cal.getTime();
    }

    public void setNextReview(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        int numberOfDays;
        cal.getTime();
        if (progress == 0){
            numberOfDays = 1;
        } else {
            numberOfDays = getNumberOfDays();
        }
        cal.add(Calendar.DATE, numberOfDays);
        nextreview = dateFormat.format(cal.getTime());
        //nextreview = cal.getTime();
    }

    private int getNumberOfDays(){
        int n = 1, m = 1;
        while (m < progress){
            n+=m;
            m++;
        }
        return n;
    }

}
