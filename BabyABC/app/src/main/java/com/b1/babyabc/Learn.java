package com.b1.babyabc;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Learn extends AppCompatActivity {

    private Button leftArrow, rightArrow;
    private TextView textLetter;
    private String[] letters;
    private int count = 0;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        leftArrow = findViewById(R.id.left_arrow_btn);
        rightArrow = findViewById(R.id.right_arrow_btn);
        textLetter = findViewById(R.id.text_id);

        if(getIntent().getStringExtra("type").equals("caps")){
            textLetter.setText("A");
            letters = new String[]{
                  "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
                  "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
            };
        }else {
            textLetter.setText("a");
            letters = new String[]{
                    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
                    "r", "s", "t", "u", "v", "w", "x", "y", "z"
            };
        }
        if(count==0){
            leftArrow.setVisibility(View.INVISIBLE);
        }

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count==1){
                    leftArrow.setVisibility(View.VISIBLE);
                }
                if(count==25){
                    rightArrow.setVisibility(View.INVISIBLE);
                }
                textLetter.setText(letters[count]);
                playSound(letters[count].toLowerCase());
            }
        });
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                if(count== 0){
                    leftArrow.setVisibility(View.INVISIBLE);
                    rightArrow.setVisibility(View.VISIBLE);
                }
                if(count==24){
                    rightArrow.setVisibility(View.VISIBLE);
                }
                textLetter.setText(letters[count]);
                playSound(letters[count].toLowerCase());
            }
        });
    }

    private  void playSound(String letter){
        switch (letter){
            case "a":
                mp = MediaPlayer.create(this, R.raw.a);
                break;
            case "b":
                mp = MediaPlayer.create(this, R.raw.b);
                break;
            case "c":
                mp = MediaPlayer.create(this, R.raw.c);
                break;
            case "d":
                mp = MediaPlayer.create(this, R.raw.d);
                break;
            case "e":
                mp = MediaPlayer.create(this, R.raw.e);
                break;
            case "f":
                mp = MediaPlayer.create(this, R.raw.f);
                break;
            case "g":
                mp = MediaPlayer.create(this, R.raw.g);
                break;
            case "h":
                mp = MediaPlayer.create(this, R.raw.h);
                break;
            case "i":
                mp = MediaPlayer.create(this, R.raw.i);
                break;
            case "j":
                mp = MediaPlayer.create(this, R.raw.j);
                break;
            case "k":
                mp = MediaPlayer.create(this, R.raw.k);
                break;
            case "l":
                mp = MediaPlayer.create(this, R.raw.l);
                break;
            case "m":
                mp = MediaPlayer.create(this, R.raw.m);
                break;
            case "n":
                mp = MediaPlayer.create(this, R.raw.n);
                break;
            case "o":
                mp = MediaPlayer.create(this, R.raw.o);
                break;
            case "p":
                mp = MediaPlayer.create(this, R.raw.p);
                break;
            case "q":
                mp = MediaPlayer.create(this, R.raw.q);
                break;
            case "r":
                mp = MediaPlayer.create(this, R.raw.r);
                break;
            case "s":
                mp = MediaPlayer.create(this, R.raw.s);
                break;
            case "t":
                mp = MediaPlayer.create(this, R.raw.t);
                break;
            case "u":
                mp = MediaPlayer.create(this, R.raw.u);
                break;
            case "v":
                mp = MediaPlayer.create(this, R.raw.v);
                break;
            case "w":
                mp = MediaPlayer.create(this, R.raw.w);
                break;
            case "x":
                mp = MediaPlayer.create(this, R.raw.x);
                break;
            case "y":
                mp = MediaPlayer.create(this, R.raw.y);
                break;
            case "z":
                mp = MediaPlayer.create(this, R.raw.z);
                break;
                default:
                    break;
        }
        mp.start();
    }
}
