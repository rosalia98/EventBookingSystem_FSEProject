package com.example.eventbookingsystem_fseproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventbookingsystem_fseproject.R;
import com.example.eventbookingsystem_fseproject.Seat;

import java.util.ArrayList;
import java.util.List;

public class SeatListAdapter extends RecyclerView.Adapter<SeatListAdapter.MyViewHolder> {

    Context mContext;
    List<Seat> mSeat = new ArrayList<>();
    OnItemClickAscultare mAscultor;


    // CONSTRUCTOR PENTRU CAND NU VREI NEAPARAT SA FOLOSESTI SI CLICKUL PT RECYCLERVIEW
    public SeatListAdapter(Context mContext, List<Seat> mSeat) {
        this.mContext = mContext;
        this.mSeat = mSeat;
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
        v = LayoutInflater.from(mContext).inflate(R.layout.seat_picked_list, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v, mAscultor);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


/*
        // Cu holder.tv_expFiller nu am ce sa fac, e doar un filler
        holder.tv_numeCategorie.setText("Cat.: " + mSeat.get(position).getCat_name());

        String randuri = Integer.toString(mSeat.get(position).getRows());
        holder.tv_nrRanduri.setText("Randuri: " + randuri);

        String locuri = Integer.toString(mSeat.get(position).getSeats_per_row());
        holder.tv_locuriPeRand.setText("Locuri/rand: " + locuri);


        String pretzLoc = Double.toString(mSeat.get(position).getPrice_per_seat());
        holder.tv_pretLoc.setText("Pret: " + pretzLoc + " lei");

        holder.categorieListLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println("DEBUG ADAPTER..............S-a DAT LONG CLICK PE UN ITEM !!!!!!!!!!!!1");
                return false;
            }
        });
*/


    }

    @Override
    public int getItemCount() {
        return mSeat.size();
    }

    public void actualizeazaClick(OnItemClickAscultare ascultor, int pos) {
        //ascultor.onItemClickPersonal(pos);
        // nu ne intereseaza pozitia asa ca pun orice
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        //private final LinearLayout categorieListLayout;
        private final TextView tv_numeCategorie; // de la textview_data
        //private final TextView tv_nrRanduri;
        //private final TextView tv_locuriPeRand;
        private final TextView tv_pretLoc;


        OnItemClickAscultare ascultor;

        public MyViewHolder(View itemView, OnItemClickAscultare ascultor) {
            super(itemView);
            this.ascultor = ascultor;

            //categorieListLayout = itemView.findViewById(R.id.SeatCategoryListLayout);
            tv_numeCategorie = itemView.findViewById(R.id.seatNumeCategorie);

//            tv_nrRanduri = itemView.findViewById(R.id.seatSpinnerSeatNr);
            //          tv_locuriPeRand = itemView.findViewById(R.id.seatSpinnerRowNr);
            tv_pretLoc = itemView.findViewById(R.id.seatPrice);


        }

    }


}

