package com.arman.databaseapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DeleteItem{
    private AppData db;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private Button add_btn;
    private AlertDialog alertDialog;
    private CustomAdapter adapter;
    private List<User> list = new ArrayList<>();
    public static List<Batch_table> batch_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_btn = findViewById(R.id.add_btn_id);
        add_btn.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recycleview_id);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(MainActivity.this);
        lm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(lm);


        spinner = findViewById(R.id.spinner_id);


        db = Room.databaseBuilder(MainActivity.this, AppData.class, "school_system").allowMainThreadQueries().build();
//        list = db.userDao().getAll();
//        for(int i=1; i<5;i++){
//            Batch_table cv = new Batch_table();
//            cv.bacthName = "Batch-"+i;
//            db.userDao().insert2(cv);
//        }

        batch_items = db.userDao().getBatchAll();
        final String[] items = new String[batch_items.size()+1];

        for(int i=0; i<batch_items.size(); i++){
            if(i==0){
                items[0] = "All";
            }
            items[i+1] = batch_items.get(i).bacthName;
        }

        final ArrayAdapter<String> adapterx = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapterx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterx);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    list = db.userDao().getAll();
                    if(list.size()>0){
                        add_btn.setVisibility(View.GONE);
                    }
                    recycleViewSetter();
                    adapter.notifyDataSetChanged();
                }else {
                    if(list.size()>0){
                        add_btn.setVisibility(View.GONE);
                    }
                    list = db.userDao().getSpacific(batch_items.get(position-1).bacthName);
                    recycleViewSetter();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        if(list.size()<1){
            add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    View view = getLayoutInflater().inflate(R.layout.add_data_layout, null, false);

                    final TextInputEditText fname = view.findViewById(R.id.first_name_id);
                    final TextInputEditText lname = view.findViewById(R.id.last_name_id);
                    final Spinner spinner1 = view.findViewById(R.id.add_spinner);
                    String[] nitems = items;
                    nitems[0] = "None";
                    ArrayAdapter<String> adap = new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_spinner_item, nitems);
                    adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(adap);
                    Button submit = view.findViewById(R.id.submit_btn);

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String fiName = fname.getText().toString();
                            String laName = lname.getText().toString();
                            String bat = spinner1.getSelectedItem().toString();
                            if(fiName.isEmpty() || laName.isEmpty()){
                                Toast.makeText(MainActivity.this, "Please Enter First and Last Name",Toast.LENGTH_SHORT).show();
                            }else {
                                User obj = new User();
                                obj.firstName = fiName;
                                obj.lastName = laName;
                                obj.batch_name = bat;
                                db.userDao().insert(obj);
                                Toast.makeText(MainActivity.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                list.add(obj);
                                if(list.size()>0){
                                    add_btn.setVisibility(View.GONE);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                    Button cancel = view.findViewById(R.id.cancel_btn);
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
            add_btn.setVisibility(View.VISIBLE);
        }else{
            add_btn.setVisibility(View.GONE);
        }

    }

    @Override
    public void delete_item(final int id, final int position) {
        final int jfg = id;
        final int pgd = position;
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Confirmation Dialog");
        alert.setMessage("Are you sure to delete this item?");
        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.userDao().delete(jfg);
                list.remove(pgd);
                if(list.size()<1){
                    add_btn.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.create().show();
    }

    @Override
    public void update_item(String fname, String lname, String batch, int position, int id) {
        User obj = new User();
        obj.firstName = fname;
        obj.lastName = lname;
        obj.batch_name = batch;
        db.userDao().update(fname, lname, batch, id);
        Toast.makeText(MainActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
//        alertDialog.dismiss();
        list.set(position, obj);
        if(list.size()>0){
            add_btn.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void insert_item(String fname, String lname, String batch, int position) {
        User obj = new User();
        obj.firstName = fname;
        obj.lastName = lname;
        obj.batch_name = batch;
        db.userDao().insert(obj);
        Toast.makeText(MainActivity.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
//        alertDialog.dismiss();
        list.add(obj);
        adapter.notifyDataSetChanged();
        recycleViewSetter();
    }

    public void recycleViewSetter(){
        adapter = new CustomAdapter(MainActivity.this, list, this);
        recyclerView.setAdapter(adapter);
    }
}