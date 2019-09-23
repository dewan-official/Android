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

public class Find_Friends extends Fragment {
    private DatabaseReference DB_REF;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_find_friend, container, false);

        DB_REF = FirebaseDatabase.getInstance().getReference().child("Users");
        DB_REF.keepSynced(true);

        recyclerView = v.findViewById(R.id.find_fnd_view_id);
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
        final FirebaseRecyclerAdapter<Users,FindFndViewHolder> adapter = new FirebaseRecyclerAdapter<Users, FindFndViewHolder>(options) {
            @Override
            protected void onBindViewHolder(final FindFndViewHolder holder, int position, final Users model) {
                final String user_key = getRef(position).getKey().toString();
                DB_REF.child(user_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        holder.username.setText(model.getName());
                        holder.status.setText(model.getStory());
                        Picasso.get().load(model.getImage()).error(R.drawable.person).fit().into(holder.imageView);
                        holder.add_friend_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new Friend_Request_Sender(getContext(),new SessionManager(getContext()).getPhone(),user_key).sentRequest();
                                holder.add_friend_btn.setEnabled(false);
                            }
                        });
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(),Friends_Profile.class);
                                intent.putExtra("user_id",user_key);
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
            public FindFndViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.find_fnd_single_item_view,parent,false);
                FindFndViewHolder v = new FindFndViewHolder(view);
                return v;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public static class FindFndViewHolder extends RecyclerView.ViewHolder{
        TextView username,status;
        ImageView imageView,add_friend_btn;
        public FindFndViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.find_fnd_user_identity);
            status = itemView.findViewById(R.id.find_fnd_user_status);
            imageView = itemView.findViewById(R.id.find_fnd_user_img);
            add_friend_btn = itemView.findViewById(R.id.add_fnd_btn_id);
        }
    }
}
