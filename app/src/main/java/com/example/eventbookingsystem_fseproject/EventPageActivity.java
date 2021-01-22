package com.example.eventbookingsystem_fseproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EventPageActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables
    StorageReference storageRef;
    FirebaseStorage storage;

    // Informatiile despre evenimentul clickuit, transmise din adaptor:
    String adapter_title, adapter_description, adapter_location, adapter_date, adapter_time, adapter_id;

    //UI
    private ImageView ep_poster;
    private String event_id;
    private Button button_detalii_bilete;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventpage);

        getSupportActionBar().setTitle((Html.fromHtml("<font face=\"Lato\">" + "Detalii eveniment" + "</font>")));

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        button_detalii_bilete = findViewById(R.id.button_detalii_bilete);
        button_detalii_bilete.setOnClickListener(this);


        // sunt transmise din adapter informatiile fiecarui eveniment, ca sa nu astept sa le ia iar din BD, daca tot le-a luat in adapter
        Intent intent = getIntent();
        adapter_title = intent.getStringExtra("ev_title");
        adapter_description = intent.getStringExtra("ev_description");
        adapter_location = intent.getStringExtra("ev_location");
        adapter_date = intent.getStringExtra("ev_date");
        adapter_time = intent.getStringExtra("ev_time");
        adapter_id = intent.getStringExtra("ev_id");


        TextView ep_title = findViewById(R.id.ep_title);
        ep_title.setText(adapter_title);

        TextView ep_description = findViewById(R.id.ep_description);
        ep_description.setText(adapter_description);

        TextView ep_date = findViewById(R.id.ep_date);
        ep_date.setText(adapter_date);

        TextView ep_time = findViewById(R.id.ep_time);
        ep_time.setText(adapter_time);

        ep_poster = findViewById(R.id.ep_poster);
        event_id = adapter_id;


        StorageReference pozaRef;
        pozaRef = storageRef.child("Events").child(event_id).child("img_0");

        pozaRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(ep_poster);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_detalii_bilete:
                Intent intent = new Intent(this, BuyTicketActivity.class);
                // transmit locatia pentru a putea accesa harta cu locuri corespunzatoare locatiei din DB
                intent.putExtra("ev_location", adapter_location);
                startActivity(intent);
                break;
        }

    }
}
