package com.example.happy934.tempideabox.resultInCardView;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.happy934.tempideabox.R;
import com.example.happy934.tempideabox.ResultViewActivity;

public class RecyclerViewResult extends AppCompatActivity {

    static RecyclerView recyclerView;
    static Context context;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_result);
        recyclerView = (RecyclerView)findViewById(R.id.myRecyclerView);
        context = getApplicationContext();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Adapter adapter = new Adapter(ResultViewActivity.dataSet);
        recyclerView.setAdapter(adapter);

    }
}
