package com.dewansoft.app.chatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Friends_Profile extends AppCompatActivity {
    private Toolbar mtoolbar;
    private TextView userName,userTitle;
    private Button sentRequestBtn;
    private CircleImageView user_img;
    private DatabaseReference DB_REF;
    private String current_status;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends__profile);

        mtoolbar = findViewById(R.id.fndToolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String user_id = getIntent().getStringExtra("user_id").toString();
        DB_REF = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        if(!user_id.isEmpty()){
            userName = findViewById(R.id.u_pro_user_name);
            userTitle = findViewById(R.id.u_pro_user_title);
            sentRequestBtn = findViewById(R.id.u_pro_sent_request_btn);
            user_img = findViewById(R.id.u_pro_pic);
            current_status = "not_friend";
            DB_REF.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    getSupportActionBar().setTitle(dataSnapshot.child("name").getValue().toString());
                    try {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String title = dataSnapshot.child("story").getValue().toString();
                        userName.setText(name);
                        userTitle.setText(title);
                        if(!dataSnapshot.child("image").getValue().toString().isEmpty()){
                            Picasso.get().load(dataSnapshot.child("image").getValue().toString()).error(R.drawable.person).fit().into(user_img);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            sentRequestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sentRequestBtn.setEnabled(false);
                    if(current_status.equals("not_friend")){
                        new Friend_Request_Sender(Friends_Profile.this,new SessionManager(Friends_Profile.this).getPhone(),user_id).sentRequest();
                        sentRequestBtn.setText("Cancel Friend Request");
                        current_status = "friends";
                        sentRequestBtn.setEnabled(true);
                    }else {
                        new Friend_Request_Sender(Friends_Profile.this,new SessionManager(Friends_Profile.this).getPhone(),user_id).cancelRequest();
                        sentRequestBtn.setText("Sent Friend Request");
                        current_status = "not_friend";
                    }
                }
            });
        }else {
            Intent i= new Intent(this,Home.class);
            i.putExtra("fragment","1");
            startActivity(i);
        }
    }
}
