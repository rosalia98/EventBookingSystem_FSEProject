package com.example.eventbookingsystem_fseproject.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventbookingsystem_fseproject.Event;
import com.example.eventbookingsystem_fseproject.LoginActivity;
import com.example.eventbookingsystem_fseproject.R;
import com.example.eventbookingsystem_fseproject.adapters.EventListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements View.OnClickListener {

    //Variables
    private HomeViewModel homeViewModel;

    private FirebaseFirestore firestore;
    private ArrayList<Event> lEveniment = new ArrayList<>();

    //UI
    private RecyclerView recyclerEvenimente;
    private Button btn_logout;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);





        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btn_logout = getView().findViewById(R.id.button_logout);
        btn_logout.setOnClickListener(this);

        firestore = FirebaseFirestore.getInstance();
        recyclerEvenimente = getView().findViewById(R.id.recyclerEvenimente);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Folosesc acest listener care asculta cand se schimba ceva in cereri.
        // Cum se schimba ceva, lCerere este actualizat.

        firestore.collection("Events").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    //Log.w(TAG, "Listen failed.", error);
                    Toast.makeText(getActivity(), "Listen failed.",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                lEveniment = new ArrayList<>();

                for (QueryDocumentSnapshot doc : value) {
                    if (doc.get("id") != null) {

                        // System.out.println("DEBUG Listener DB.............." + doc.get("cerere_id"));

                        Event e1 = new Event();
                        e1 = doc.toObject(Event.class);
                        lEveniment.add(e1);

                        System.out.println("DEBUG..... HomeFragment....e1 title.." + e1.getTitle());


                    }
                }


                EventListAdapter recyclerAdapter = new EventListAdapter(getContext(), lEveniment);
                recyclerEvenimente.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerEvenimente.setAdapter(recyclerAdapter);

            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_logout:

                // pentru auto login
                SharedPreferences preferences = this.getActivity().getSharedPreferences("auto_login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("remember", "false");
                editor.putString("email", "");
                editor.putString("password", "");

                editor.apply();

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                break;

        }
    }
}