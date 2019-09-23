package com.dewansoft.app.chatapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class PhoneCallFragment extends Fragment implements View.OnClickListener {
    private Button one,two,three,four,five,six,seven,eight,nine,zero,star,hash,call,backSpace;
    private EditText num;
    private String text;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_phone_call, container, false);
        num = v.findViewById(R.id.num_id);
        num.setShowSoftInputOnFocus(false);

        backSpace = v.findViewById(R.id.backspace_btn);
        backSpace.setOnClickListener(this);
        backSpace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                num.setText("");
                return false;
            }
        });

        one = v.findViewById(R.id.one_btn);
        one.setOnClickListener(this);

        two = v.findViewById(R.id.two_btn);
        two.setOnClickListener(this);

        three = v.findViewById(R.id.three_btn);
        three.setOnClickListener(this);

        four = v.findViewById(R.id.four_btn);
        four.setOnClickListener(this);

        five = v.findViewById(R.id.five_btn);
        five.setOnClickListener(this);

        six = v.findViewById(R.id.six_btn);
        six.setOnClickListener(this);

        seven = v.findViewById(R.id.seven_btn);
        seven.setOnClickListener(this);

        eight = v.findViewById(R.id.eight_btn);
        eight.setOnClickListener(this);

        nine = v.findViewById(R.id.nine_btn);
        nine.setOnClickListener(this);

        star = v.findViewById(R.id.star_btn);
        star.setOnClickListener(this);

        zero = v.findViewById(R.id.zero_btn);
        zero.setOnClickListener(this);
        zero.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                addWhat("+");
                return false;
            }
        });

        hash = v.findViewById(R.id.hash_btn);
        hash.setOnClickListener(this);

        call = v.findViewById(R.id.call_btn);
        call.setOnClickListener(this);
        return v;
    }
    public void onClick(View view) {
        text = num.getText().toString();
        if(view == backSpace){
            if(!text.isEmpty()){
                int xx = num.getSelectionStart();
                try {
                    if(xx>0){
                        char x[] = text.toCharArray();
                        String yy="";
                        for(int i=0;i<text.length();i++){
                            if(i==xx-1){
                                continue;
                            }else {
                                yy+=x[i];
                            }
                        }
                        text = yy;
                        num.setText(text);
                        num.setSelection(xx-1);
                    }
                }catch (Exception e){

                }
            }
        }
        if(view == one){
            addWhat("1");
        }
        if(view == two){
            addWhat("2");
        }
        if(view == three){
            addWhat("3");
        }
        if(view == four){
            addWhat("4");
        }
        if(view == five){
            addWhat("5");
        }
        if(view == six){
            addWhat("6");
        }
        if(view == seven){
            addWhat("7");
        }
        if(view == eight){
            addWhat("8");
        }
        if(view == nine){
            addWhat("9");
        }
        if(view == star){
            addWhat("*");
        }
        if(view == zero){
            addWhat("0");
        }
        if(view == hash){
            addWhat("#");
        }
        if(view == call){
            try{
                Intent i = new Intent();
                i.setAction(i.ACTION_CALL);
                i.setData(Uri.parse("tel:"+text));
                startActivity(i);
            }catch (Exception e){
                num.setText(""+e);
            }
        }
    }
    private void addWhat(String xy){
        try {
            int xx = num.getSelectionStart();
            if(text.isEmpty()){
                num.setText(""+xy);
                xx++;
                num.setSelection(xx);
            }else {
                text = addChar(text,xy,xx);
                num.setText(text);
                xx++;
                num.setSelection(xx);
            }
        }catch (Exception e){}
    }
    private String addChar(String x,String y,int z){
        String textNext="";
        int temp = z;
        char a[];
        if(z==0){
            textNext = y+""+x;
        }else if(z==x.length()){
            textNext = x+""+y;
        }else {
            a = x.toCharArray();
            for(int i=0;i<x.length();i++){
                if(i != temp){
                    textNext += a[i];
                }else {
                    textNext += y;
                    i--;
                    temp = -1;
                }
            }
        }
        return textNext;
    }
}
