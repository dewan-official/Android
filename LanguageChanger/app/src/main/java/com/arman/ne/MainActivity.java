package com.arman.ne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button bn_btn, en_btn;
    private TextView f_name, l_name, mobile, address;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bn_btn = findViewById(R.id.bn_btn_id);
        en_btn = findViewById(R.id.en_btn_id);

        f_name = findViewById(R.id.first_name);
        l_name = findViewById(R.id.last_name);
        mobile = findViewById(R.id.mobile_num);
        address = findViewById(R.id.address_id);
        preferences = getApplicationContext().getSharedPreferences("sessioninfo", MODE_PRIVATE);
        if (preferences.getString("lan", "").toString()!=""){
            preferences.edit().putString("lan", "bn").commit();
            Locale locale = new Locale(preferences.getString("lan", ""));
            Configuration configuration = new Configuration();
            configuration.locale = locale;
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
            bn_btn.setText(getResources().getString(R.string.bangla));
            en_btn.setText(getResources().getString(R.string.english));
            f_name.setText(getResources().getString(R.string.f_name));
            l_name.setText(getResources().getString(R.string.l_name));
            mobile.setText(getResources().getString(R.string.mobile));
            address.setText(getResources().getString(R.string.address));
        }

        bn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().putString("lan", "bn").commit();
                Locale locale = new Locale(preferences.getString("lan", ""));
                Configuration configuration = new Configuration();
                configuration.locale = locale;
                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                bn_btn.setText(getResources().getString(R.string.bangla));
                en_btn.setText(getResources().getString(R.string.english));
                f_name.setText(getResources().getString(R.string.f_name));
                l_name.setText(getResources().getString(R.string.l_name));
                mobile.setText(getResources().getString(R.string.mobile));
                address.setText(getResources().getString(R.string.address));

            }
        });

        en_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().putString("lan", "en").commit();
                Locale locale = new Locale(preferences.getString("lan", ""));
                Configuration configuration = new Configuration();
                configuration.locale = locale;
                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                bn_btn.setText(getResources().getString(R.string.bangla));
                en_btn.setText(getResources().getString(R.string.english));
                f_name.setText(getResources().getString(R.string.f_name));
                l_name.setText(getResources().getString(R.string.l_name));
                mobile.setText(getResources().getString(R.string.mobile));
                address.setText(getResources().getString(R.string.address));

            }
        });
    }
}
