package com.dewansoft.app.chatapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NewUserRegister{
    private static NewUserRegister NUR;
    private RequestQueue RQ;
    private Context context;

    NewUserRegister(Context context){
        this.context = context;
        RQ = getRQ();
    }

    public static synchronized NewUserRegister getInstance(Context context){
        if(NUR == null){
            NUR = new NewUserRegister(context);
        }
        return NUR;
    }

    public RequestQueue getRQ() {
        if(RQ == null){
            RQ = Volley.newRequestQueue(context.getApplicationContext());
        }
        return RQ;
    }
    public <T>void addRequstQueue(Request<T> request){
        RQ.add(request);
    }
}
class CheakUserData{
    public boolean cheakNow(String phone,String pass){
        return true;
    }
}
