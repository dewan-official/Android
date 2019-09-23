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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class Request extends Fragment {
    private DatabaseReference DB_REF;
    private DatabaseReference user_DB_REF;
    private DatabaseReference fnd_DB_REF;
    private StorageReference storageReference;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        DB_REF = FirebaseDatabase.getInstance().getReference().child("friend_requests").child(new SessionManager(getContext()).getPhone());
        user_DB_REF = FirebaseDatabase.getInstance().getReference().child("Users");
        fnd_DB_REF = FirebaseDatabase.getInstance().getReference().child("Friends");

        recyclerView = view.findViewById(R.id.friend_request_recyclerview_id);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(DB_REF,Users.class).build();
        FirebaseRecyclerAdapter<Users,RequestViewHolder> adapter = new FirebaseRecyclerAdapter<Users, RequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RequestViewHolder holder, int position, @NonNull final Users model) {
                final String user_key = new SessionManager(getContext()).getPhone().toString();
                final String request_user_key = getRef(position).getKey();
                final String date = new DateClass(getContext()).getDate();
                DB_REF.child(request_user_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user_DB_REF.child(request_user_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot1) {
                                holder.request_u_name.setText(dataSnapshot1.child("name").getValue().toString());
                                holder.request_u_title.setText(dataSnapshot1.child("story").getValue().toString());
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(),Friends_Profile.class);
                                        intent.putExtra("user_id",request_user_key);
                                        startActivity(intent);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        holder.request_accept_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                fnd_DB_REF.child(user_key).child(request_user_key).child("friendship_day").setValue(date).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            fnd_DB_REF.child(request_user_key).child(user_key).child("friendship_day").setValue(date).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    FirebaseDatabase.getInstance().getReference().child("friend_requests").child(user_key).child(request_user_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(getContext(),"Request Accepted.",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                        holder.request_cancel_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DB_REF.child(user_key).child(request_user_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            FirebaseDatabase.getInstance().getReference().child("friend_requests").child(user_key).child(request_user_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(getContext(),"Request Cancel.",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
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
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.request_person_view,parent,false);
                RequestViewHolder v = new RequestViewHolder(view1);
                return v;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class RequestViewHolder extends RecyclerView.ViewHolder{
        TextView request_u_name,request_u_title;
        Button request_accept_btn,request_cancel_btn;
        ImageView request_u_pic;
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            request_u_name = itemView.findViewById(R.id.request_user_identity);
            request_u_title = itemView.findViewById(R.id.request_user_status);
            request_u_pic = itemView.findViewById(R.id.request_user_img);
            request_accept_btn = itemView.findViewById(R.id.request_accept_btn);
            request_cancel_btn = itemView.findViewById(R.id.request_cancel_btn);
        }
    }
}
