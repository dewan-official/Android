package com.dewan.oli.ullah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycle_view_id);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager lm = new LinearLayoutManager(MainActivity.this);
        lm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(lm);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api_Interface st = retrofit.create(Api_Interface.class);

        Call<Results> call_st = st.getStudentList();

        call_st.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                CustomAdapter adapter = new CustomAdapter(response.body().getStudentLists());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Log.e("dkhgslig", t.getMessage());
            }
        });
    }
}
