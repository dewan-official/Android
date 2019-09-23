package com.dewansoft.app.chatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.ViewHolder> {
    private Context context;
    private List<Message> listMess = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private String destination;

    public ChattingAdapter(List<Message> listMess, Context context,String destination) {
        this.listMess = listMess;
        this.context = context;
        this.destination = destination;
    }


    private View v;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        v = layoutInflater.inflate(R.layout.sender_sms_style,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Message u = listMess.get(position);
        holder.messageTextView.setText(u.getMessage());
        if(u.getMode().equals("sent")){
            holder.messageTextView.setBackgroundResource(R.drawable.sender_message);
            holder.change.setGravity(Gravity.END);
            holder.messageTextView.setGravity(Gravity.END);
            holder.image.setVisibility(View.INVISIBLE);
        }else {
            holder.messageTextView.setBackgroundResource(R.drawable.reciver_sms_back);
            holder.messageTextView.setGravity(Gravity.START);
            holder.change.setGravity(Gravity.START);
            FirebaseDatabase.getInstance().getReference().child("Users").child(destination).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Picasso.get().load(dataSnapshot.child("image").getValue().toString()).error(R.drawable.person).fit().into(holder.image);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listMess.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        LinearLayout change;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.sent_text_id);
            image = itemView.findViewById(R.id.sms_user_img);
            change = itemView.findViewById(R.id.sent_card_id);
        }
    }
}
