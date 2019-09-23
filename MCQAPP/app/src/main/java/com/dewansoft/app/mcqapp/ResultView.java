package com.dewansoft.app.mcqapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ResultView extends AppCompatActivity {
    ListView listView;
    Button btn;
    TextView textView;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_view);
        btn = findViewById(R.id.goto_home);
        textView = findViewById(R.id.text_id);
        listView = findViewById(R.id.list_view_result);
        String tb_name = getIntent().getStringExtra("tb_name");
        String[] data = getIntent().getStringArrayExtra("result");
        database db = new database(ResultView.this);
        Cursor cursor = db.getData(tb_name);
        int x = cursor.getCount();
        if(x>0){
            String[] ques = new String[x];
            String[] op_1 = new String[x];
            String[] op_2 = new String[x];
            String[] op_3 = new String[x];
            String[] op_4 = new String[x];
            String[] result = new String[x];
            for(int i=0;cursor.moveToNext();i++){
                ques[i] = cursor.getString(1).toString();
                op_1[i] = cursor.getString(2).toString();
                op_2[i] = cursor.getString(3).toString();
                op_3[i] = cursor.getString(4).toString();
                op_4[i] = cursor.getString(5).toString();
                result[i] = cursor.getString(6).toString();
                if(result[i].equals(data[i])){
                    count++;
                }
            }
            customListView clv = new customListView(ResultView.this,ques,op_1,op_2,op_3,op_4,result,data);
            listView.setAdapter(clv);
        }
        textView.setText("Total Question : "+data.length+"\nRight Answer : "+count+"\nWrong Answer : "+((data.length)-count));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResultView.this,MainActivity.class));
                finish();
            }
        });
    }
}
