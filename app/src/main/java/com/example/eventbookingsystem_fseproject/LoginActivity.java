package com.example.eventbookingsystem_fseproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail, editTextPassword;
    private Button button_login, button_register;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    // pt auto login
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextParola);

        button_register = findViewById(R.id.buttonRegister);
        button_login = findViewById(R.id.buttonLogin);

        button_login.setOnClickListener(this);
        button_register.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);

        // Pentru auto login

        // pun email gol ca sa fie ceva in el sa nu fie null ( altfel eroare )
        preferences = getSharedPreferences("auto_login", MODE_PRIVATE);
        editor = preferences.edit();

        try {
            if (preferences.getString("remember", "").equals("true")) {

                String pref_email = preferences.getString("email", "");
                String pref_password = preferences.getString("password", "");
                mAuth.signInWithEmailAndPassword(pref_email, pref_password);

                Toast.makeText(this, "Autentificat automat cu: " + pref_email, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

        } catch (Exception e) {
            System.out.println("Userul nu are date de autentificare salvate");

        }


        getSupportActionBar().hide();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRegister:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;

            case R.id.buttonLogin:
                userLogin();
                break;

            default:
                break;
        }
    }

    public void userLogin() {
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Completează e-mail!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Completează parolă!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    // Pentru auto login

                    // preferences = getSharedPreferences("auto_login", MODE_PRIVATE);
                    //editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.apply();

                    final String user_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    /*intent.putExtras(b);*/
                    startActivity(intent);


                } else {
                    Toast toast = Toast.makeText(LoginActivity.this, "E-mail/parolă greșite!", Toast.LENGTH_SHORT);
                    toast.show();
                }

                }
                                                                                }

        );

    }
}

