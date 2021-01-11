package com.example.eventbookingsystem_fseproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventbookingsystem_fseproject.Event;
import com.example.eventbookingsystem_fseproject.EventPageActivity;
import com.example.eventbookingsystem_fseproject.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {

    //Variables
    Context mContext;
    List<Event> mEventList = new ArrayList<>();
    OnItemClickAscultare mAscultor;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference photosFolder;

    Uri image_uri;


    // CONSTRUCTOR PENTRU CAND NU VREI NEAPARAT SA FOLOSESTI SI CLICKUL PT RECYCLERVIEW
    public EventListAdapter(Context mContext, List<Event> mEventList) {
        this.mContext = mContext;
        this.mEventList = mEventList;

    }

    // CONSTRUCTOR PENTRU CAND VREI SA FOLOSESTI SI CLICKUL PT RECYCLERVIEW
    // public IstoricListAdapter(Context mContext, List<Preparat> mPreparate, OnItemClickAscultare ascultor) {
    //    this.mContext = mContext;
    //    this.mPreparate = mPreparate;
    //    this.mAscultor = ascultor;    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.eventlist, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v, mAscultor);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.tv_titlu.setText(mEventList.get(position).getTitle());

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        // Vreau sa afisez doar o parte din descriere/adresa in lista.
        String descriere_org = mEventList.get(position).getDescription();
        holder.tv_descriere.setText(mEventList.get(position).getDescription());

        if (descriere_org.length() > 90) {
            descriere_org = descriere_org.substring(0, 90) + "...";
        }
        //TODO Revizuie daca lasam sau nu descrierea
        //holder.tv_descriere.setText(descriere_org);


        String locatie_org = mEventList.get(position).getLocation_string();

        if (locatie_org.length() > 20) {
            locatie_org = locatie_org.substring(0, 20) + "...";
        }
        holder.tv_locatie.setText(locatie_org);


        holder.tv_categorie.setText(mEventList.get(position).getGenre());
        holder.tv_date.setText(mEventList.get(position).getData_string());
        holder.tv_time.setText(mEventList.get(position).getTime_string());


        String event_id = mEventList.get(position).getId();

        StorageReference pozaRef;
        pozaRef = storageRef.child("Events").child(event_id).child("img_0");

        pozaRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                image_uri = uri;

                Glide.with(mContext)
                        .load(uri)
                        .into(holder.iv_poza);

            }
        });


        holder.eventListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("DEBUG ADAPTER..............S-a DAT  CLICK PE" + holder.tv_titlu.getText().toString() + "  !!!!!!!!!!!!1");

                Intent intent = new Intent(view.getContext(), EventPageActivity.class);
                intent.putExtra("ev_title", holder.tv_titlu.getText().toString());
                intent.putExtra("ev_description", holder.tv_descriere.getText().toString());
                intent.putExtra("ev_date", holder.tv_date.getText().toString());
                intent.putExtra("ev_time", holder.tv_time.getText().toString());


                view.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    public void actualizeazaClick(OnItemClickAscultare ascultor, int pos) {
        //ascultor.onItemClickPersonal(pos);
        // nu ne intereseaza pozitia asa ca pun orice
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout eventListLayout;
        private final TextView tv_titlu; // de la textview_data
        private final TextView tv_descriere;
        private final TextView tv_categorie;
        private final TextView tv_locatie;
        private final TextView tv_date;
        private final TextView tv_time;
        private final ImageView iv_poza;


        OnItemClickAscultare ascultor;

        public MyViewHolder(View itemView, OnItemClickAscultare ascultor) {
            super(itemView);
            this.ascultor = ascultor;

            eventListLayout = itemView.findViewById(R.id.evlist_LinearLayout);
            tv_titlu = itemView.findViewById(R.id.evlist_title);
            tv_descriere = itemView.findViewById(R.id.evlist_description);
            tv_categorie = itemView.findViewById(R.id.evlist_genre);
            tv_locatie = itemView.findViewById(R.id.evlist_location);
            tv_date = itemView.findViewById(R.id.evlist_date);
            tv_time = itemView.findViewById(R.id.evlist_time);
            iv_poza = itemView.findViewById(R.id.evlist_image);

        }

    }


}

