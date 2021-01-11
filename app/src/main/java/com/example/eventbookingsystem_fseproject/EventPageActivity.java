package com.example.eventbookingsystem_fseproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
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
    //UI
    private ImageView ep_poster;
    private String event_id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventpage);

        getSupportActionBar().setTitle((Html.fromHtml("<font face=\"Lato\">" + "Detalii eveniment" + "</font>")));

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        Intent intent = getIntent();
        String adapter_title = intent.getStringExtra("ev_title");
        String adapter_description = intent.getStringExtra("ev_description");
        String adapter_date = intent.getStringExtra("ev_date");
        String adapter_time = intent.getStringExtra("ev_time");
        String adapter_id = intent.getStringExtra("ev_id");


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
    public void onClick(View view) {

    }
}
