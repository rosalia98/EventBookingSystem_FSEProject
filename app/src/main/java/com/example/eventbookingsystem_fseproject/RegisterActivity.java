package com.example.eventbookingsystem_fseproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, Serializable {

    private EditText editEmail, editParola, editRepParola, editPrenume, editNume,
            editTelefon, editAdresa;
    private Button buttonRegisterUser;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializareCampuri();
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    private void initializareCampuri() {
        //getSupportActionBar().hide();
        getSupportActionBar().setTitle((Html.fromHtml("<font face=\"Lato\">" + "Cont Client" + "</font>")));

        TextView tv1 = (TextView) findViewById(R.id.textEmail);
        tv1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_mail_outline_24, 0, 0, 0);
        TextView tv2 = (TextView) findViewById(R.id.textParola);
        tv2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_pass_key_24, 0, 0, 0);
        TextView tv3 = (TextView) findViewById(R.id.textRepetaParola);
        tv3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_pass_key_24, 0, 0, 0);
        TextView tv4 = (TextView) findViewById(R.id.textTelefon);
        tv4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_local_phone_24, 0, 0, 0);
        TextView tv5 = (TextView) findViewById(R.id.textAdresa);
        tv5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_person_pin_circle_24, 0, 0, 0);
        TextView tv6 = (TextView) findViewById(R.id.textPrenume);
        tv6.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_person_24, 0, 0, 0);

        buttonRegisterUser = findViewById(R.id.buttonRegisterUser);
        buttonRegisterUser.setOnClickListener(this);

        editEmail = findViewById(R.id.textEmail);
        editParola = findViewById(R.id.textParola);
        editRepParola = findViewById(R.id.textRepetaParola);
        editPrenume = findViewById(R.id.textPrenume);
        editNume = findViewById(R.id.textNume);
        editTelefon = findViewById(R.id.textTelefon);
        editAdresa = findViewById(R.id.textAdresa);
        progressBar = findViewById(R.id.progressBar);


        final Spinner spinner_orase = findViewById(R.id.spinner_orase);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.orase_disponibile, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_orase.setAdapter(adapter);

        // pentru a seta font si culoare la spinner
        //Typeface type = Typeface.createFromAsset(getAssets(), "font/lato.ttf");

        spinner_orase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {

                //Typeface type = Typeface.createFromAsset(getAssets(), "res/fonts/lato.xml");
                //((TextView) spinner_orase.getChildAt(0)).setTypeface(type);
                ((TextView) spinner_orase.getChildAt(0)).setTextSize(18);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


    }

    private void registerUserClient(final View v) {

        boolean isOkay = true;

        final String email = editEmail.getText().toString().trim();
        String password = editParola.getText().toString().trim();
        String repeat_password = editRepParola.getText().toString().trim();
        final String prenume = editPrenume.getText().toString().trim();
        final String nume = editNume.getText().toString().trim();
        final String telefon = editTelefon.getText().toString().trim();
        final String adresa = editAdresa.getText().toString().trim();


        if (email.isEmpty() || prenume.isEmpty() || nume.isEmpty() || telefon.isEmpty() ||
                adresa.isEmpty()) {

            Toast toast = Toast.makeText(this, "Completează toate câmpurile!", Toast.LENGTH_SHORT);
            toast.show();
            isOkay = false;
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editEmail.setError("Introdu un e-mail valid!");
                editEmail.requestFocus();
                isOkay = false;
            } else {
                if (password.length() < 6) {
                    editParola.setError("Parola trebuie sa aibă minim 6 caractere!");
                    editParola.requestFocus();
                    isOkay = false;
                } else {
                    if (!password.equals(repeat_password)) {
                        editRepParola.setError("Parolele nu corespund!");
                        editRepParola.requestFocus();
                        isOkay = false;
                    }

                }
            }

        }

        if (isOkay) {
            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final User userclient = new User(prenume, nume, email, telefon, adresa);

                                firestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .set(userclient)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Toast.makeText(RegisterActivity.this,
                                                        "Contul a fost creat!", Toast.LENGTH_LONG).show();

                                                progressBar.setVisibility(View.GONE);

                                                // apoi userul este logat si redirectionat catre MAIN

                                               /* Bundle b = new Bundle();

                                                b.putString("user_address",adresa);
                                                b.putString("user_name",prenume+" "+nume);*/

                                                Intent intent = new Intent(v.getContext(), MainActivity.class);
                                                /*intent.putExtras(b);*/
                                                startActivity(intent);

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterActivity.this,
                                                        "Eroare! Reîncearcă", Toast.LENGTH_LONG).show();
                                            }
                                        });

                            }

                        }
                    });

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegisterUser:
                registerUserClient(v);
                break;


        }
    }
}

