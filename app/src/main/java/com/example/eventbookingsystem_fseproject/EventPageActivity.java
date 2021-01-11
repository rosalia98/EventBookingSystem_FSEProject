package com.example.eventbookingsystem_fseproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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

        getSupportActionBar().setTitle((Html.fromHtml("<font face=\"Lato\">" + "Detalii eveniment" + "</font>")));


        Intent intent = getIntent();
        String adapter_title = intent.getStringExtra("ev_title");
        String adapter_description = intent.getStringExtra("ev_description");
        String adapter_date = intent.getStringExtra("ev_date");
        String adapter_time = intent.getStringExtra("ev_time");


        TextView ep_title = findViewById(R.id.ep_title);
        ep_title.setText(adapter_title);

        TextView ep_description = findViewById(R.id.ep_description);
        ep_description.setText(adapter_description);

        TextView ep_date = findViewById(R.id.ep_date);
        ep_date.setText(adapter_date);

        TextView ep_time = findViewById(R.id.ep_time);
        ep_time.setText(adapter_time);

    }

    @Override
    public void onClick(View view) {

    }
}
