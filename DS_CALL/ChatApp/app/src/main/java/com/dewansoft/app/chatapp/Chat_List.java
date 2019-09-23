package com.dewansoft.app.chatapp;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Chat_List extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference DB_REF;
    private DatabaseReference user_DB_REF;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat__list, container, false);

        //RecycleView
        recyclerView = v.findViewById(R.id.chat_list_view_id);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Firebase Reference
        DB_REF = FirebaseDatabase.getInstance().getReference().child("Friends").child(new SessionManager(getContext()).getPhone());
        user_DB_REF = FirebaseDatabase.getInstance().getReference().child("Users");
        DB_REF.keepSynced(true);
        user_DB_REF.keepSynced(true);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(DB_REF,Users.class).build();
        FirebaseRecyclerAdapter<Users,Chat_List.chatViewHolder> adapter = new FirebaseRecyclerAdapter<Users, Chat_List.chatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final Chat_List.chatViewHolder holder, int position, @NonNull final Users model) {
                final String user_key = new SessionManager(getContext()).getPhone().toString();
                final String friends_key = getRef(position).getKey();
                user_DB_REF.child(friends_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        holder.fnd_name.setText(dataSnapshot.child("name").getValue().toString());
                        holder.fnd_title.setText(dataSnapshot.child("story").getValue().toString());
                        holder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getActivity(),Chatting.class);
                                i.putExtra("destination_id",friends_key);
                                startActivity(i);
                            }
                        });
                        holder.online.setText(dataSnapshot.child("status").getValue().toString());
                        if(dataSnapshot.child("status").getValue().toString().equals("online")){
                            //holder.online.setTextColor(Andr);
                        }else {
                            //holder.online.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
                        }
                        if(!dataSnapshot.child("name").getValue().toString().equals("null")){
                            Picasso.get().load(dataSnapshot.child("image").getValue().toString()).error(R.drawable.person).fit().into(holder.fnd_pro_pic);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public Chat_List.chatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.chat_list_single_item_view,parent,false);
                Chat_List.chatViewHolder v = new Chat_List.chatViewHolder(view1);
                return v;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class chatViewHolder extends RecyclerView.ViewHolder{
        TextView fnd_name,fnd_title,online;
        ImageView fnd_pro_pic;
        View view;
        public chatViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            fnd_name = itemView.findViewById(R.id.chat_list_user_identity);
            fnd_title = itemView.findViewById(R.id.chat_list_user_status);
            fnd_pro_pic = itemView.findViewById(R.id.chat_list_user_img);
            online = itemView.findViewById(R.id.chat_list_user_active);
        }
    }
}
