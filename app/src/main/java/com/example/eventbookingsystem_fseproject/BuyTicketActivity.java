package com.example.eventbookingsystem_fseproject;

import android.content.Intent;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.eventbookingsystem_fseproject.adapters.SeatListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class BuyTicketActivity extends AppCompatActivity {

    //Variables
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference pozaRef;
    int nr_tickets;
    char sectiune_selectata = '0';

    ArrayList<Seat> lLocuriAlese;

    // Use of two rectangles to define the sectionA or B in the seatmap picture
    // int left, int top, int right, int bottom

    //left The X coordinate of the left side of the rectangle
    //top The Y coordinate of the top of the rectangle
    //right The X coordinate of the right side of the rectangle
    //bottom The Y coordinate of the bottom of the rectangle

    // all coordinates are seatmap picture coordinates, not screen coordinates
    Rect sectionA = new Rect(175, 15, 4070, 1480);
    Rect sectionB = new Rect(520, 1481, 3620, 3660);


    //locatia transmisa de EventPageActivity
    String ev_location;

    //UI

    EditText tickets_count;
    RecyclerView recyclerLocuriAlese;

    // siv = scaling image view
    SubsamplingScaleImageView siv_harta_locuri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyticket);

        getSupportActionBar().setTitle((Html.fromHtml("<font face=\"Lato\">" + "Cumpara bilet" + "</font>")));

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        recyclerLocuriAlese = findViewById(R.id.recyclerLocuriAlese);

        lLocuriAlese = new ArrayList<>();


        // Adaug listener pentru cand se modifica textul din edittext, adica cand userul pune nr de bilete dorit

        //https://stackoverflow.com/questions/24054419/how-to-add-on-change-listener-in-edittext
        tickets_count = findViewById(R.id.editNrBilete);
        tickets_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int before, int count, int after) {

                if (charSequence.length() == 1) {

                    //daca nu s-a selectat seciunea mai intai
                    if (sectiune_selectata == '0') {

                        Toast toast = Toast.makeText(getApplicationContext(), "Click pe sectiunea dorita mai intai!", Toast.LENGTH_SHORT);
                        toast.show();
                        tickets_count.setText("");

                    }
                }

                if (charSequence.length() > 1) {

                    Toast toast = Toast.makeText(getApplicationContext(), "Numar maxim: 9 bilete!", Toast.LENGTH_SHORT);
                    toast.show();
                    tickets_count.setText("1");
                }

                if (!tickets_count.getText().toString().isEmpty()) {
                    nr_tickets = Integer.parseInt(tickets_count.getText().toString());
                } else
                    nr_tickets = 0;


            }

            @Override
            public void afterTextChanged(Editable editable) {

                lLocuriAlese = new ArrayList<>();

                // afisez obiecte Seat in functie de nr de locuri alese

                for (int i = 0; i < nr_tickets; i++) {
                    Seat s1 = new Seat();
                    lLocuriAlese.add(s1);


                }

                SeatListAdapter recyclerAdapter = new SeatListAdapter(getApplicationContext(), lLocuriAlese);
                recyclerLocuriAlese.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerLocuriAlese.setAdapter(recyclerAdapter);


            }
        });


        Intent intent = getIntent();
        ev_location = intent.getStringExtra("ev_location");


        // "Cosmetizez" string-ul locatie, ca sa fie la fel ca in DB Storage
        // exemplu: locatia Sun Plaza trebuie sa ajunga "sun_plaza", asa cum e in DB Storage
        // Dupa "cosmetizare" e facil sa accesez harta corespunzatoare locatiei cu un path

        ev_location = ev_location.trim().toLowerCase().replace(" ", "_");


        // initializare harta si setare sursa imagine din Firestore Storage
        siv_harta_locuri = findViewById(R.id.scaleHartaLocuri);
        siv_harta_locuri.setImage(ImageSource.resource(R.drawable.seatmap));

        System.out.println("DEBUGBOSS.........BuyTicketActivity.....ev_location " + ev_location);
        // Toate pozele cu harta locurilor din Venues se vor numi "seatmap"


        pozaRef = storageRef.child("Venues").child(ev_location).child("seatmap.jpg");

        pozaRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                System.out.println("DEBUGBOSS.........intra pe succes, deci gaseste poza");

                //TODO: Incarca imaginea din Storage DB
                //https://github.com/davemorrissey/subsampling-scale-image-view

                // siv_harta_locuri.setImage(ImageSource.uri(uri));
                //siv_harta_locuri.setImage(ImageSource.uri(uri.getPath()));
                //siv_harta_locuri.setImage(ImageSource.resource(R.drawable.seatmap));

            }
        });


        // Pentru SELECTIA CATEGORIEI aflu mai intai coordonatele imaginii in locul in care se da click
        //   https://github.com/davemorrissey/subsampling-scale-image-view/wiki/09.-Events


        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (siv_harta_locuri.isReady()) {
                    PointF sCoord = siv_harta_locuri.viewToSourceCoord(e.getX(), e.getY());
                    //System.out.println("DEBUGBOSS..... coordonate imagine din BuyTicketActivity.. x: " + sCoord.x + "  y: " + sCoord.y);


                    int x_int = Math.round(sCoord.x);
                    int y_int = Math.round(sCoord.y);

                    if (sectionA.contains(x_int, y_int)) {
                        sectiune_selectata = 'A';

                    } else if (sectionB.contains(x_int, y_int)) {
                        sectiune_selectata = 'B';

                    }


                    Toast toast = Toast.makeText(getApplicationContext(), "Sectiune selectata: " + sectiune_selectata, Toast.LENGTH_SHORT);
                    toast.show();
                }
                return true;
            }
        });

        siv_harta_locuri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });


    }

    // metoda
    public void citesteCoordonateClick() {

    }
}
