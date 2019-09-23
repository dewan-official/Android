package com.dewansoft.app.chatapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.squareup.picasso.Picasso;

public class Contact extends Fragment {
    private DatabaseReference DB_REF;
    private RecyclerView recyclerView;
    private ImageButton add_contact;
    private AlertDialog bulder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact, container, false);

        DB_REF = FirebaseDatabase.getInstance().getReference().child("Contacts").child(new SessionManager(getContext()).getPhone());

        recyclerView = v.findViewById(R.id.contact_recycler_view_id);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        add_contact = v.findViewById(R.id.add_new_contact_btn);
        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AddContact.class));
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(DB_REF,Users.class).build();
        FirebaseRecyclerAdapter<Users,ContactViewHolder> adapter = new FirebaseRecyclerAdapter<Users, ContactViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactViewHolder holder, int position, @NonNull final Users model) {
                final String con_u_key = getRef(position).getKey().toString();
                DB_REF.child(con_u_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        holder.contact_u_name.setText(model.getName());
                        holder.contact_u_phone.setText(model.getPhone());
                        holder.call_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent call_intent = new Intent();
                                call_intent.setData(Uri.parse("tel:"+model.getPhone()));
                                call_intent.setAction(Intent.ACTION_CALL);
                                startActivity(call_intent);
                            }
                        });
                        Picasso.get().load(model.getImage()).error(R.drawable.person).fit().into(holder.contact_u_pic);
                        holder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                View v = getLayoutInflater().inflate(R.layout.activity_contact_profile,null, false);
                                bulder = new AlertDialog.Builder(getActivity()).create();
                                bulder.setView(v);
                                ImageView con_u_img = v.findViewById(R.id.con_de_img);
                                TextView con_u_name = v.findViewById(R.id.con_de_name);
                                TextView con_u_phone = v.findViewById(R.id.con_de_phone);
                                TextView con_u_email = v.findViewById(R.id.con_de_email);
                                TextView con_u_dis = v.findViewById(R.id.con_de_discreption);
                                Button cancel_btn = v.findViewById(R.id.con_de_cancel_btn);
                                ImageButton delete_btn = v.findViewById(R.id.con_de_delete_btn);                                if(!model.getImage().isEmpty()){
                                    Picasso.get().load(model.getImage()).error(R.drawable.back).fit().into(con_u_img);
                                }
                                con_u_name.setText(model.getName());
                                if(dataSnapshot.hasChild("email")){
                                    con_u_email.setText(model.getEmail());
                                }
                                con_u_phone.setText(model.getPhone());
                                if(dataSnapshot.hasChild("description")){
                                    con_u_dis.setText(dataSnapshot.child("description").getValue().toString());
                                }
                                cancel_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        bulder.dismiss();
                                    }
                                });
                                delete_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        DB_REF.child(con_u_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    bulder.dismiss();
                                                    Toast.makeText(getContext(),"Delete Successfully.",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                                bulder.setCancelable(false);
                                bulder.show();
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
            public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(getContext()).inflate(R.layout.contact_single_item_view,parent,false);
                ContactViewHolder holderView = new ContactViewHolder(v);
                return holderView;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{
        TextView contact_u_name,contact_u_phone;
        ImageButton call_btn;
        ImageView contact_u_pic;
        View view;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            contact_u_name = itemView.findViewById(R.id.contact_u_identity);
            contact_u_phone = itemView.findViewById(R.id.contact_u_phone);
            contact_u_pic = itemView.findViewById(R.id.contact_u_proImg);
            call_btn = itemView.findViewById(R.id.call_from_contact_btn);
        }
    }
}
