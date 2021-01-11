package com.example.eventbookingsystem_fseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EventPageActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables

    //UI


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventpage);


        Intent intent = getIntent();
        String adapter_title = intent.getStringExtra("ev_title");

        TextView ep_title = findViewById(R.id.ep_title);
        ep_title.setText(adapter_title);

    }

    @Override
    public void onClick(View view) {

    }
}
