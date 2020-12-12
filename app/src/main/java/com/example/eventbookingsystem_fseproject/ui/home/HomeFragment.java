package com.example.eventbookingsystem_fseproject.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.eventbookingsystem_fseproject.Event;
import com.example.eventbookingsystem_fseproject.LoginActivity;
import com.example.eventbookingsystem_fseproject.R;
import com.google.firebase.auth.FirebaseAuth;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private Button btn_logout;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        Event e1 = new Event("Concert Smiley", "Concert", "Smiley rupe din nou trendingu si ne " +
                "canta noul sau album de succes", 2020, 2, 5, 19,
                30, 44.4355, 26.0952, 3);

        System.out.println("DEBUG_HOME " + e1.getName());
        System.out.println("DEBUG_HOME " + e1.getDescription());

        e1.printDate();

        e1.setPriceCategory(1, "A", 150, 8);
        e1.setPriceCategory(2, "B", 100, 10);
        e1.setPriceCategory(3, "C", 70, 14);

        e1.printAllSeats();
        System.out.println(e1.getTotalSeats());


        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btn_logout = getView().findViewById(R.id.button_logout);
        btn_logout.setOnClickListener(this);


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