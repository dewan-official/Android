package com.dewansoft.app.mcqapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

public class CreateExam extends Fragment implements View.OnClickListener {
    EditText ques,op_1,op_2,op_3,op_4,ans;
    Button reset,insert,finish;
   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_create_exam, container, false);
       ques = v.findViewById(R.id.inser_ques);
       op_1 = v.findViewById(R.id.inser_option_1);
       op_2 = v.findViewById(R.id.inser_option_2);
       op_3 = v.findViewById(R.id.inser_option_3);
       op_4 = v.findViewById(R.id.inser_option_4);
       ans = v.findViewById(R.id.inser_ans);
       reset = v.findViewById(R.id.clear_btn);
       insert = v.findViewById(R.id.insert_btn);
       finish = v.findViewById(R.id.finish_btn);
       reset.setOnClickListener(this);
       insert.setOnClickListener(this);
       finish.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
       String q = ques.getText().toString();
       String op1 = op_1.getText().toString();
       String op2 = op_2.getText().toString();
       String op3 = op_3.getText().toString();
       String op4 = op_4.getText().toString();
       String an = ans.getText().toString();
        if(view == reset){
            ques.setText("");
            op_1.setText("");
            op_2.setText("");
            op_3.setText("");
            op_4.setText("");
            ans.setText("");
        }
        if(view == finish){
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.frame_id,new ExamList()).commit();
        }
        if(view == insert){
            if(q.isEmpty() || op1.isEmpty() || op2.isEmpty() || op3.isEmpty() || op4.isEmpty() || an.isEmpty()){
                Toast.makeText(getActivity(),"You Can't Put Any Empty Field",Toast.LENGTH_SHORT).show();
            }else {
                String[] data = new String[]{q,op1,op2,op3,op4,an};
                database db = new database(getActivity());
                String tb_name = new sessionManager(getActivity()).gettb_name();
                long cheak = db.insertData(data,tb_name);
                if(cheak == -1){
                    Toast.makeText(getActivity(),"Data Not Inserted",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(),"Data Inserted",Toast.LENGTH_SHORT).show();
                }
                ques.setText("");
                op_1.setText("");
                op_2.setText("");
                op_3.setText("");
                op_4.setText("");
                ans.setText("");
            }
        }
    }
}
