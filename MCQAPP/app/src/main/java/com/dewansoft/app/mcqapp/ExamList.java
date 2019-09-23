package com.dewansoft.app.mcqapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ExamList extends Fragment {
    private CustomAdapter ca;
    Button insert_btn;
    AlertDialog alertDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exam_list, container, false);
        insert_btn = v.findViewById(R.id.exam_create_id);
        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            alertDialog = new AlertDialog.Builder(getActivity()).create();
                View view1 = getLayoutInflater().inflate(R.layout.alert,null);
                alertDialog.setView(view1);
                final Button button = view1.findViewById(R.id.ok_btn);
                final Button cancel = view1.findViewById(R.id.cancel_btn_a);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                final EditText editText = view1.findViewById(R.id.tb_name);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String xyz = editText.getText().toString();
                        if(xyz.isEmpty()){
                        }else {
                            if(xyz.contains(" ")||xyz.contains("-")||xyz.contains(".")||xyz.contains(",")){

                            }else {
                                sessionManager sm = new sessionManager(getActivity());
                                sm.settb_name(xyz);
                                database db = new database(getActivity());
                                long x = db.insertTableName(xyz);
                                if(x == -1){
                                    Toast.makeText(getActivity(),"Data Insertion Failed",Toast.LENGTH_SHORT).show();
                                }else{
                                    boolean cheak = db.customTableCreate(xyz);
                                    if(cheak == true){
                                        FragmentManager fm = getFragmentManager();
                                        fm.beginTransaction().replace(R.id.frame_id,new CreateExam()).commit();
                                        alertDialog.dismiss();
                                    }
                                }
                            }
                        }
                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });
        database db = new database(getActivity());
        Cursor cursor = db.getData("tablenames");
        int x = cursor.getCount();
        RecyclerView recyclerView = v.findViewById(R.id.recycleView_id);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        if(x>0){
            final String[] data = new String[x];
            final String[] totalQues = new String[x];
            int j = 0;
            for(int i=0;cursor.moveToNext();i++){
                data[i] = cursor.getString(1);
                Cursor cursor1 = db.getData(data[i]);
                if(cursor1.getCount() == 0){
                    database dbx = new database(getActivity());
                    boolean xx = dbx.deleteTable(data[i]);
                    if (xx == true){
                        dbx.deleteRow(data[i]);
                    }
                }else {
                    totalQues[j] = String.valueOf(cursor1.getCount());
                    j++;
                }
            }
            cursor.close();
            ca =new CustomAdapter(getContext(),data,totalQues);
        }
        ca.notifyDataSetChanged();
        recyclerView.setAdapter(ca);
        return v;
    }
}
