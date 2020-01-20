package com.arman.class75;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button reset, plus, minus, save_btn;
    private TextView textView;
    private EditText editText;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view_id);
        reset = findViewById(R.id.reset_btn_id);
        plus = findViewById(R.id.plus_btn_id);
        minus = findViewById(R.id.minus_btn_id);
        save_btn = findViewById(R.id.save_btn);
        editText = findViewById(R.id.input_id);

        preferences = getSharedPreferences("value", MODE_PRIVATE);

        if(preferences.getInt("x_val", 0) == 0){
            preferences.edit().putInt("x_val", 0).commit();
            textView.setText("0");
        }else {
            textView.setText(String.valueOf(preferences.getInt("x_val", 0)));
        }

        if(preferences.getString("st_val", "") != null || ! preferences.getString("st_val", "").isEmpty()){
            editText.setText(preferences.getString("st_val", ""));
        }

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().putInt("x_val", 0).commit();
                preferences.edit().putString("st_val", "").commit();
                textView.setText("0");
                editText.setText("");
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().putInt("x_val", preferences.getInt("x_val", 0) + 1).commit();
                textView.setText(String.valueOf( preferences.getInt("x_val", 0)));
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferences.getInt("x_val", 0) != 0){
                    preferences.edit().putInt("x_val", preferences.getInt("x_val", 0) - 1).commit();
                }
                textView.setText(String.valueOf( preferences.getInt("x_val", 0)));
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st_val = editText.getText().toString();
                if(st_val.isEmpty()){
                    Toast.makeText(MainActivity.this, "Emter Some Text", Toast.LENGTH_SHORT).show();
                }else {
                    preferences.edit().putString("st_val", st_val).commit();
                    Toast.makeText(MainActivity.this, "Successfully Saved!", Toast.LENGTH_SHORT).show();
                    editText.setText("");
                }

            }
        });
    }
}
