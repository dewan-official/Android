package com.dewansoft.app.chatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class Friend_Request_Sender {
    private String sender_id,recever_id;
    private Task<Void> DB_REF;
    private Context context;

    public Friend_Request_Sender(Context context,String sender_id, String recever_id) {
        this.context = context;
        this.sender_id = sender_id;
        this.recever_id = recever_id;
    }
    private boolean sent;
    public boolean sentRequest(){
        DB_REF = FirebaseDatabase.getInstance().getReference().child("friend_requests").child(recever_id).child(sender_id).child("request_time").setValue("now").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context,"Request Sent",Toast.LENGTH_SHORT).show();
                    sent = true;
                }else {
                    Toast.makeText(context,"Request Not Sent",Toast.LENGTH_SHORT).show();
                    sent = false;
                }
            }
        });
        return sent;
    }
    public boolean cancelRequest(){
        DB_REF = FirebaseDatabase.getInstance().getReference().child("friend_requests").child(recever_id).child(sender_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context,"Request cancel.",Toast.LENGTH_SHORT).show();
                    sent = true;
                }else {
                    Toast.makeText(context,"Request Not cancel.",Toast.LENGTH_SHORT).show();
                    sent = false;
                }
            }
        });
        return sent;
    }
}
/*
DB_REF = FirebaseDatabase.getInstance().getReference().child("friend_request").child(sender_id).child(recever_id).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    DB_REF = FirebaseDatabase.getInstance().getReference().child("friend_request").child(recever_id).child(sender_id).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(context,"Request Sent",Toast.LENGTH_SHORT).show();
                                sent = true;
                            }else {
                                Toast.makeText(context,"Request Not Sent",Toast.LENGTH_SHORT).show();
                                sent = false;
                            }
                        }
                    });
                }else {
                    Toast.makeText(context,"Some Problem Occur..",Toast.LENGTH_SHORT).show();
                    sent = false;
                }
            }
        });
*/
