package com.dewansoft.app.mcqapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity implements View.OnClickListener {
    TextView textView;
    RadioButton btn_1,btn_2,btn_3,btn_4;
    Button previous,next;
    Cursor cursor;
    String[] ques;
    String[] op_1;
    String[] op_2;
    String[] op_3;
    String[] op_4;
    String[] result;
    int count = 0;
    int x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        textView = findViewById(R.id.text_ques);
        btn_1 = findViewById(R.id.option_1);
        btn_1.setOnClickListener(this);
        btn_2 = findViewById(R.id.option_2);
        btn_2.setOnClickListener(this);
        btn_3 = findViewById(R.id.option_3);
        btn_3.setOnClickListener(this);
        btn_4 = findViewById(R.id.option_4);
        btn_4.setOnClickListener(this);
        previous = findViewById(R.id.previous_btn);
        previous.setOnClickListener(this);
        next = findViewById(R.id.next_btn);
        next.setOnClickListener(this);
        next.setEnabled(false);
        database db = new database(Home.this);
        cursor = db.getData(getIntent().getStringExtra("tb_name"));
        if(cursor != null){
            x = cursor.getCount();
            if(x>0){
                ques = new String[x];
                op_1 = new String[x];
                op_2 = new String[x];
                op_3 = new String[x];
                op_4 = new String[x];
                result = new String[x];
                for(int i=0;cursor.moveToNext();i++){
                    ques[i] = cursor.getString(1).toString();
                    op_1[i] = cursor.getString(2).toString();
                    op_2[i] = cursor.getString(3).toString();
                    op_3[i] = cursor.getString(4).toString();
                    op_4[i] = cursor.getString(5).toString();
                }
            }
            setQuestion();
        }
    }

    public void setQuestion(){
        textView.setText(ques[count]);
        btn_1.setText(op_1[count]);
        btn_2.setText(op_2[count]);
        btn_3.setText(op_3[count]);
        btn_4.setText(op_4[count]);
    }

    @Override
    public void onClick(View view) {
        if(btn_1 == view){
            result[count] = "1";
            next.setEnabled(true);
        }if(btn_2 == view){
            result[count] = "2";
            next.setEnabled(true);
        }if(btn_3 == view){
            result[count] = "3";
            next.setEnabled(true);
        }if(btn_4 == view){
            result[count] = "4";
            next.setEnabled(true);
        }if(previous == view){
            if(count>0){
                count--;
                setQuestion();
                if(result[count].equals("1")){
                    btn_1.setChecked(true);
                }
                if(result[count].equals("2")){
                    btn_2.setChecked(true);
                }
                if(result[count].equals("3")){
                    btn_3.setChecked(true);
                }
                if(result[count].equals("4")){
                    btn_4.setChecked(true);
                }
            }
        }if(next == view){
            if(x>(count+1)){
                count++;
                setQuestion();
                next.setEnabled(false);
                btn_1.setChecked(false);
                btn_2.setChecked(false);
                btn_3.setChecked(false);
                btn_4.setChecked(false);
            }else {
               next.setText("Submit");
               Intent intent = new Intent(Home.this,ResultView.class);
               intent.putExtra("result",result);
               intent.putExtra("tb_name",getIntent().getStringExtra("tb_name"));
               startActivity(intent);
               finish();
            }
        }
    }
}
