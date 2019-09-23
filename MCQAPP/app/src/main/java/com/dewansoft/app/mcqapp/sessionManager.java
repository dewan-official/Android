package com.dewansoft.app.mcqapp;

import android.content.Context;
import android.content.SharedPreferences;

public class sessionManager {
    SharedPreferences sharedPreferences;
    Context context;
    private String tb_name,status;
    sessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("sessionInfo",Context.MODE_PRIVATE);
    }

    public String gettb_name() {
        tb_name = sharedPreferences.getString("tb_name","");
        return tb_name;
    }

    public void settb_name(String tb_name) {
        this.tb_name = tb_name;
        sharedPreferences.edit().putString("tb_name",tb_name).commit();
    }

    public String getStatus() {
        status = sharedPreferences.getString("userStatus","");
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        sharedPreferences.edit().putString("userStatus",status).commit();
    }
}
