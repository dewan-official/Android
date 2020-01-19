package com.arman.databaseapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<User> all;
    private DeleteItem deleteItem;
    private AlertDialog alertDialog;
    private Context context;

    public CustomAdapter(Context context,List<User> all, DeleteItem deleteItem) {
        this.all = all;
        this.deleteItem = deleteItem;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.textView1.setText(String.valueOf(all.get(position).uid));
        holder.textView2.setText(all.get(position).firstName+" "+all.get(position).lastName);
        holder.textView3.setText(all.get(position).batch_name);

        final String[] items = new String[MainActivity.batch_items.size()];

        for(int i=0; i<MainActivity.batch_items.size(); i++){
            items[i] = MainActivity.batch_items.get(i).bacthName;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(context).create();
                View view = LayoutInflater.from(context).inflate(R.layout.options_layout, null, false);
                Button add_btn = view.findViewById(R.id.add_new_data_btn_id);
                Button update_btn = view.findViewById(R.id.update_data_btn_id);
                Button delete_btn = view.findViewById(R.id.delete_data_btn_id);
                Button cancel = view.findViewById(R.id.d_cancel);
                add_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        final AlertDialog dialog = new AlertDialog.Builder(context).create();
                        View view1 = LayoutInflater.from(context).inflate(R.layout.add_data_layout, null, false);
                        final TextInputEditText textInputEditText = view1.findViewById(R.id.first_name_id);
                        final TextInputEditText textInputEditText1 = view1.findViewById(R.id.last_name_id);
                        Button cancel = view1.findViewById(R.id.cancel_btn);
                        Button submit = view1.findViewById(R.id.submit_btn);

                        final Spinner spinner = view1.findViewById(R.id.add_spinner);
                        ArrayAdapter<String> adapterx = new ArrayAdapter<String>(context,
                                android.R.layout.simple_spinner_item, items);
                        adapterx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapterx);

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String f = textInputEditText.getText().toString();
                                String l = textInputEditText1.getText().toString();
                                String bat = spinner.getSelectedItem().toString();
                                if(f.isEmpty() || l.isEmpty()){
                                    Toast.makeText(context, "Please Enter First and Last Name",Toast.LENGTH_SHORT).show();
                                }else {
                                    deleteItem.insert_item(f, l,bat, all.get(position).uid);
                                    dialog.dismiss();
                                }
                            }
                        });
                        dialog.setCancelable(false);
                        dialog.setView(view1);
                        dialog.show();
                    }
                });

                update_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        final AlertDialog dialog = new AlertDialog.Builder(context).create();
                        View view1 = LayoutInflater.from(context).inflate(R.layout.update_layout, null, false);
                        final TextInputEditText textInputEditText = view1.findViewById(R.id.update_f_name);
                        final TextInputEditText textInputEditText1 = view1.findViewById(R.id.update_l_name);
                        textInputEditText.setText(all.get(position).firstName);
                        textInputEditText1.setText(all.get(position).lastName);
                        Button cancel = view1.findViewById(R.id.update_d_cancel);
                        Button submit = view1.findViewById(R.id.update_d_update);

                        final Spinner spinner = view1.findViewById(R.id.update_spinner);
                        ArrayAdapter<String> adapterx = new ArrayAdapter<String>(context,
                                android.R.layout.simple_spinner_item, items);
                        adapterx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapterx);
                        spinner.setSelection(filterName(all.get(position).batch_name)-1);

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String f = textInputEditText.getText().toString();
                                String l = textInputEditText1.getText().toString();
                                String bat = spinner.getSelectedItem().toString();
                                if(f.isEmpty() || l.isEmpty()){
                                    Toast.makeText(context, "Please Enter First and Last Name",Toast.LENGTH_SHORT).show();
                                }else {
                                    deleteItem.update_item(f, l, bat, position, all.get(position).uid);
                                    dialog.dismiss();
                                }
                            }
                        });
                        dialog.setCancelable(false);
                        dialog.setView(view1);
                        dialog.show();
                    }
                });
                delete_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteItem.delete_item(all.get(position).uid, position);
                        alertDialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.setView(view);
                alertDialog.show();
            }
        });
    }

    public int filterName(String name){
        String[] cc = name.split("-");
        return Integer.parseInt(cc[1]);
    }
    @Override
    public int getItemCount() {
        return all.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView1, textView2, textView3;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.id_id);
            textView2 = itemView.findViewById(R.id.name_id);
            textView3 = itemView.findViewById(R.id.batch_id);
        }
    }
}