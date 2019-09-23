package com.dewansoft.app.chatapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences sharedPreferences;
    Context context;
    private String phone,status;
    SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("sessionInfo",Context.MODE_PRIVATE);
    }

    public String getPhone() {
        phone = sharedPreferences.getString("userPhone","");
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        sharedPreferences.edit().putString("userPhone",phone).commit();
    }


    public void logout(){
        sharedPreferences.edit().remove("userPhone").commit();
    }
}
