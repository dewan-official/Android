package com.dewansoft.app.chatapp;

import android.content.Context;

import java.text.DateFormat;
import java.util.Date;

public class DateClass {
    private Context context;
    public DateClass(Context context) {
        this.context = context;
    }
    long date = new Date().getTime();
    String time = (String) android.text.format.DateFormat.format("EEE, d MMM yyyy",date);
    public String getDate(){
        return time;
    }
}
