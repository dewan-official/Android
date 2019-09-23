package com.dewansoft.app.mcqapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Context context;
    private String[] data,totalques;

    public CustomAdapter() {
    }

    public CustomAdapter(Context context, String[] data, String[] totalques) {
        this.context = context;
        this.data = data;
        this.totalques = totalques;
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview,parent,false);
        ViewHolder v = new ViewHolder(view);
        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, final int position) {
        holder.ques.setText(data[position]);
        holder.totalQ.setText(totalques[position]);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            database db = new database(context);
            boolean xx = db.deleteTable(data[position]);
            if (xx == true){
                int xy = db.deleteRow(data[position]);
                if (xy>0){
                    Toast.makeText(context,"Delete Successfully.",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,"Not Delete.",Toast.LENGTH_SHORT).show();
                }
            }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,Home.class);
                i.putExtra("tb_name",data[position]);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ques,totalQ;
        ImageButton delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ques = itemView.findViewById(R.id.ques);
            totalQ = itemView.findViewById(R.id.total_ques);
            delete = itemView.findViewById(R.id.delete_btn);
        }
    }
}
