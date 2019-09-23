package com.dewansoft.app.chatapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Friends extends Fragment {
    private DatabaseReference DB_REF;
    private DatabaseReference user_DB_REF;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, container, false);

        DB_REF = FirebaseDatabase.getInstance().getReference().child("Friends").child(new SessionManager(getContext()).getPhone());
        user_DB_REF = FirebaseDatabase.getInstance().getReference().child("Users");
        DB_REF.keepSynced(true);
        user_DB_REF.keepSynced(true);

        recyclerView = v.findViewById(R.id.friends_recycle_view_id);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(DB_REF,Users.class).build();
        FirebaseRecyclerAdapter<Users,FriendViewHolder> adapter = new FirebaseRecyclerAdapter<Users, FriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FriendViewHolder holder, int position, @NonNull final Users model) {
                final String user_key = new SessionManager(getContext()).getPhone().toString();
                final String friends_key = getRef(position).getKey();
                user_DB_REF.child(friends_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        holder.fnd_name.setText(dataSnapshot.child("name").getValue().toString());
                        holder.fnd_title.setText(dataSnapshot.child("story").getValue().toString());
                        Picasso.get().load(dataSnapshot.child("image").getValue().toString()).error(R.drawable.person).fit().into(holder.fnd_pro_pic);
                        holder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(),Friends_Profile.class);
                                intent.putExtra("user_id",friends_key);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.friend_view,parent,false);
                FriendViewHolder v = new FriendViewHolder(view1);
                return v;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder{
        TextView fnd_name,fnd_title;
        ImageView fnd_pro_pic;
        View view;
        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            fnd_name = itemView.findViewById(R.id.friend_identity);
            fnd_title = itemView.findViewById(R.id.friend_status);
            fnd_pro_pic = itemView.findViewById(R.id.friend_pro_img);
        }
    }
}
