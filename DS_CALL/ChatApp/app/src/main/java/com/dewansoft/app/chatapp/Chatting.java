package com.dewansoft.app.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chatting extends AppCompatActivity {
    private Toolbar mtoolbar;
    private TextInputEditText message;
    private ImageButton image_btn,sent_btn;
    private DatabaseReference DB_REF;
    private RecyclerView recyclerView;
    private List<Message> list = new ArrayList<>();
    private String current_user_id,destination;
    ChattingAdapter adapter;
    private CircleImageView toolbarImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        mtoolbar = findViewById(R.id.chatting_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        DB_REF = FirebaseDatabase.getInstance().getReference().child("Messages");
        DB_REF.keepSynced(true);
        current_user_id = new SessionManager(Chatting.this).getPhone();
        destination = getIntent().getStringExtra("destination_id").toString();

        recyclerView = findViewById(R.id.chatting_recycleView_id);
        LinearLayoutManager LN = new LinearLayoutManager(Chatting.this);
        LN.setOrientation(LinearLayoutManager.VERTICAL);
        LN.setStackFromEnd(true);
        LN.scrollToPosition(0);
        //LN.setReverseLayout(true);
        recyclerView.setLayoutManager(LN);
        recyclerView.setHasFixedSize(true);

        message = findViewById(R.id.messeage_box_id);
        image_btn = findViewById(R.id.img_message_id);
        sent_btn = findViewById(R.id.message_sent_btn);
        toolbarImg = findViewById(R.id.toolbarImg);
        FirebaseDatabase.getInstance().getReference().child("Users").child(destination).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getSupportActionBar().setTitle(dataSnapshot.child("name").getValue().toString());
                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).error(R.drawable.person).fit().into(toolbarImg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mess = message.getText().toString();
                if(mess.isEmpty()){
                    //Do nothing
                }else {
                    final HashMap<String , String> hashMap = new HashMap<String ,String>();
                    hashMap.put("message",mess);
                    hashMap.put("time","null");
                    hashMap.put("mode","sent");
                    DB_REF.child(current_user_id).child(destination).push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                hashMap.put("mode","receive");
                                DB_REF.child(destination).child(current_user_id).push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            message.setText("");
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        loadMessages();
        adapter = new ChattingAdapter(list, Chatting.this,destination);
        recyclerView.setAdapter(adapter);
    }

    public void loadMessages(){
        DB_REF.child(current_user_id).child(destination).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                list.add(message);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(list.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
