package com.dewansoft.app.mcqapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

public class customListView extends BaseAdapter {
    Context context;
    String[] question;
    String[] option_1;
    String[] option_2;
    String[] option_3;
    String[] option_4;
    String[] result;
    String[] u_result;
    LayoutInflater layoutInflater;
    customListView(Context context,String[] question,String[] option_1,String[] option_2,String[] option_3,String[] option_4,String[] result,String[] u_result){
        this.context = context;
        this.question = question;
        this.option_1 = option_1;
        this.option_2 = option_2;
        this.option_3 = option_3;
        this.option_4 = option_4;
        this.result = result;
        this.u_result = u_result;
    }
    @Override
    public int getCount() {
        return question.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_result_view,viewGroup,false);
        }

        TextView ques = view.findViewById(R.id.text_ques_r);
        TextView resilt = view.findViewById(R.id.result_r);
        RadioButton btn_1 = view.findViewById(R.id.option_1_r);
        RadioButton btn_2 = view.findViewById(R.id.option_2_r);
        RadioButton btn_3 = view.findViewById(R.id.option_3_r);
        RadioButton btn_4 = view.findViewById(R.id.option_4_r);
        ques.setText(question[i]);
        btn_1.setText(option_1[i]);
        btn_2.setText(option_2[i]);
        btn_3.setText(option_3[i]);
        btn_4.setText(option_4[i]);

        btn_2.setEnabled(false);
        btn_3.setEnabled(false);
        btn_4.setEnabled(false);
        btn_1.setEnabled(false);

        if(result[i].equals(u_result[i])){
            resilt.setText("Correct");
        }else {
            resilt.setText("Wrong");
            resilt.setTextColor(R.color.red);
        }
        if(u_result[i].equals("1")){
            btn_1.setChecked(true);
        }
        if(u_result[i].equals("2")){
            btn_2.setChecked(true);
        }
        if(u_result[i].equals("3")){
            btn_3.setChecked(true);
        }
        if(u_result[i].equals("4")){
            btn_4.setChecked(true);
        }

        return view;
    }
}
