package com.b1.babyabc;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button capsBtn, smallBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        capsBtn = findViewById(R.id.caps_btn);
        smallBtn = findViewById(R.id.small_btn);
        capsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Learn.class);
                i.putExtra("type", "caps");
                startActivity(i);
            }
        });
        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Learn.class);
                i.putExtra("type", "small");
                startActivity(i);
            }
        });
    }
}
