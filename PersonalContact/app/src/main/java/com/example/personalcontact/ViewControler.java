package com.example.personalcontact;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ViewControler extends RecyclerView.Adapter<ViewControler.ViewHolder> {
    private ArrayList<DataProcessor> dataList;
    private Context context;
    private LayoutInflater layoutInflater;
    private View v;
    public ViewControler(Context context, ArrayList<DataProcessor> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        v = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String conName = dataList.get(position).getName();
        String conPhone = dataList.get(position).getPhone();
        final String conImage = dataList.get(position).getImage();
        holder.name.setText(conName);
        holder.phone.setText(conPhone);
        Picasso.get().load(conImage).error(R.drawable.person).fit().into(holder.imageView);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = db.getReference("Contacts").child(user.getUid()).child(dataList.get(position).getKey());
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        holder.menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.menu_btn);
                popupMenu.inflate(R.menu.list_item_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.con_edit_option:
                                final AlertDialog al = new AlertDialog.Builder(context).create();
                                View v = al.getLayoutInflater().inflate(R.layout.new_contact_entry, null, false);
                                al.setView(v);
                                Button cancel = v.findViewById(R.id.con_entry_cancel_btn);
                                Button imgSeclect = v.findViewById(R.id.con_image_select_btn);
                                Button submit = v.findViewById(R.id.con_entry_submit_btn);
                                final EditText conName = v.findViewById(R.id.con_entry_name);
                                final EditText conPhone = v.findViewById(R.id.con_entry_phone);
                                TextView textView = v.findViewById(R.id.title_id);
                                textView.setText("EDIT CONTACT");
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        al.dismiss();
                                    }
                                });
                                imgSeclect.setVisibility(View.GONE);
                                submit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(conName.getText().toString().isEmpty() || conPhone.getText().toString().isEmpty()){
                                            Toast.makeText(context, "Fillup Name And Phone Number", Toast.LENGTH_LONG).show();
                                        }else {
                                            mRef.child("Name").setValue(conName.getText().toString());
                                            mRef.child("Phone").setValue(conPhone.getText().toString());
                                            Toast.makeText(context, "Successfully Added!", Toast.LENGTH_LONG).show();
                                            al.dismiss();
                                        }
                                    }
                                });
                                conName.setText(dataList.get(position).getName());
                                conPhone.setText(dataList.get(position).getPhone());
                                al.show();
                                break;
                            case R.id.con_delete_option:
                                alert.setTitle("Confirmation Dialog Box");
                                alert.setMessage("\nAre you Sure to delete Contact?");
                                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mRef.removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                Toast.makeText(context,"Delete Contact" , Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                                alert.setNegativeButton("Cancel", null);
                                alert.create().show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name, phone;
        private ImageView menu_btn;
        private CircleImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = v.findViewById(R.id.con_name);
            phone = v.findViewById(R.id.con_phone);
            imageView = v.findViewById(R.id.con_pro_img);
            menu_btn = v.findViewById(R.id.item_menu);
        }
    }
}
