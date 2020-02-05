package com.dewan.oli.ullah;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<Students> list;
    CustomAdapter(List<Students> list){
        this.list = list;
    }
    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        holder.textView1.setText("ID: "+list.get(position).getId());
        holder.textView2.setText("Student ID: "+list.get(position).getStudent_id());
        holder.textView3.setText("Class ID: "+list.get(position).getClass_id());
        holder.textView4.setText("Name: "+list.get(position).getStudent_name());
        holder.textView5.setText("Father Name: "+list.get(position).getStudent_f_name());
        holder.textView6.setText("Mother Name: "+list.get(position).getStudent_m_name());
        holder.textView7.setText("Age: "+list.get(position).getAge());
        holder.textView8.setText("Date Of Birth: "+list.get(position).getDate_of_birth());
        holder.textView9.setText("Class Name"+list.get(position).getClass_name());
        holder.textView10.setText("Address"+list.get(position).getStudent_address());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9, textView10;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.id_id);
            textView2 = itemView.findViewById(R.id.student_id_id);
            textView3 = itemView.findViewById(R.id.class_id_id);
            textView4 = itemView.findViewById(R.id.student_name_id);
            textView5 = itemView.findViewById(R.id.student_f_name_id);
            textView6 = itemView.findViewById(R.id.student_m_name_id);
            textView7 = itemView.findViewById(R.id.age_id);
            textView8 = itemView.findViewById(R.id.dob_id);
            textView9 = itemView.findViewById(R.id.class_name_id);
            textView10 = itemView.findViewById(R.id.student_address_id);
        }
    }
}
