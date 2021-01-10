package com.example.eventbookingsystem_fseproject.ui.add_event;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.eventbookingsystem_fseproject.Event;
import com.example.eventbookingsystem_fseproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

// era extends Fragment inainte sa fie DialogFragment
public class AddEventFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    //CONSTANTS

    // valoarea se da random
    private static final int WRITE_REQUEST_CODE = 8778;
    private final String user_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private final ArrayList<ImageView> lPoze = new ArrayList<>();
    //Variables
    private FirebaseFirestore firestore;
    private DocumentReference mUser;
    private String eveniment_categorie, adresa_string;
    private StorageReference mStorageRef;
    private StorageReference mUserStorageRef;

    //UI
    private EditText editTitlu, editDescriere, editData, editTimp;
    private Button buttonPickDate;
    private Button buttonPickTime;
    private Button buttonAddEvent;

    private LinearLayout layoutFotografii;
    private ImageView imageAdd;
    private NotificationsViewModel notificationsViewModel;

    private View fragment_root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_event, container, false);
        fragment_root = inflater.inflate(R.layout.fragment_add_event, container, false);

        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                /* textView.setText(s);*/
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        mUser = firestore.collection("Clients").document(user_uid);

/*        // retrieve adresa scurtaa userului pentru a o putea seta in obiectul Cerere
        mUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                adresa_string = documentSnapshot.get("adresa_string").toString();
            }
        });*/

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mUserStorageRef = mStorageRef.child(user_uid);


        initializareCampuri();
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Fotografiază", "Alege din Galerie", "Anulare"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Adaugă poze");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Fotografiază")) {

                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Alege din Galerie")) {


                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions, WRITE_REQUEST_CODE);



                   /* Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);*/

                } else if (options[item].equals("Anulare")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                // 0 = Fotografiaza
                case 0:
                    if (resultCode == RESULT_OK && data != null) {

                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        adaugaImagine(selectedImage, 90, 4);

                    }

                    break;
                // 1 = alege fotografie
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);

                                adaugaImagine(picturePath, 90, 4);
                                cursor.close();

                            }
                        }

                    }
                    break;
            }
        }
    }

    public void initializareCampuri() {

        editTitlu = getView().findViewById(R.id.editTitluEv);
        editDescriere = getView().findViewById(R.id.editDescriereEv);
        editData = getView().findViewById(R.id.editDataEv);
        editTimp = getView().findViewById(R.id.editTimeEv);

        final Spinner spinner_domeniu = getView().findViewById(R.id.spinnerDomeniuEv);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categorii, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_domeniu.setAdapter(adapter);

        // pentru a seta font si culoare la spinner
        //Typeface type = Typeface.createFromAsset(getAssets(), "font/lato.ttf");

        spinner_domeniu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                // pentru design
                ((TextView) spinner_domeniu.getChildAt(0)).setTextSize(18);

                eveniment_categorie = spinner_domeniu.getSelectedItem().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                eveniment_categorie = spinner_domeniu.getSelectedItem().toString();
            }
        });

        layoutFotografii = getView().findViewById(R.id.FotografiiLayout);

        imageAdd = getView().findViewById(R.id.buttonAddPhotosEv);
        imageAdd.setOnClickListener(this);

        buttonPickDate = getView().findViewById(R.id.buttonPickDateEv);
        buttonPickDate.setOnClickListener(this);

        buttonPickTime = getView().findViewById(R.id.buttonPickTimeEv);
        buttonPickTime.setOnClickListener(this);

        buttonAddEvent = getView().findViewById(R.id.buttonAddEvent);
        buttonAddEvent.setOnClickListener(this);


    }

    public void incarcaCerere() {

        String titlu = editTitlu.getText().toString().trim();
        String descriere = editDescriere.getText().toString().trim();

        boolean ok = true;

        if (titlu.length() < 6) {
            editTitlu.setError("Titlu prea scurt!");
            editTitlu.requestFocus();
            ok = false;
            return;
        }

        if (descriere.length() < 3) {
            editDescriere.setError("Descriere prea scurtă!");
            editDescriere.requestFocus();
            ok = false;
            return;
        }

        if (lPoze.size() < 1) {
            Toast toast = Toast.makeText(getContext(), "Adaugă mai multe poze!", Toast.LENGTH_SHORT);
            toast.show();
            ok = false;
        }

        if (ok) {

            Event e1 = new Event(titlu, eveniment_categorie, descriere, 2020, 2, 5, 19,
                    30, 44.4355, 26.0952, 3);

            // TODO Revizuieste aceste comentarii
            //  Cerere c1 = new Cerere(user_uid, eveniment_categorie, titlu, descriere, adresa_string);

            //String event_id = user_uid + "_p_" + c1.getDay() + "_" + c1.getMonth() + "_" + c1.getYear();
            // c1.setCerere_id(event_id);


            // Salvare imagini in storage
            for (ImageView imgview : lPoze) {

                Bitmap bitmap = ((BitmapDrawable) imgview.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 35, baos);
                byte[] imageInByte = baos.toByteArray();

                //mUserStorageRef.child(event_id).child("img_" + lPoze.indexOf(imgview)).putBytes(imageInByte);
            }


            // c1.setPhotoStoragePath(mUserStorageRef.child(event_id).toString());


            // Salvare cerere in firebase
            // In cont client:
//TODO REVIZUIESTE COMENTARII
            firestore.collection("Clients").document(user_uid)
                    //.collection("Cereri").document(event_id)
                    .set(e1)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast toast = Toast.makeText(getContext(), "Cererea a fost plasată!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast toast = Toast.makeText(getContext(), "Eroare! Reîncearcă!", Toast.LENGTH_SHORT);
                    toast.show();

                }
            });

            //In colectia Cereri
            //firestore.collection("Cereri").document(event_id).set(e1);


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WRITE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Granted.
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);


                } else {
                    //Denied.
                    Toast.makeText(getContext(), "Permisiune media refuzată.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // metoda pt cand se alege din galerie
    public void adaugaImagine(String picturePath, int dimens, int dimensMargin) {

        // ADAUGARE dinamica imagini
        ImageView imgview = new ImageView(getContext());


        // SET THE IMAGEVIEW DIMENSIONS
        float density = getResources().getDisplayMetrics().density;
        int finalDimens = (int) (dimens * density);

        LinearLayout.LayoutParams imgvwDimens = new LinearLayout.LayoutParams(finalDimens, finalDimens);
        imgview.setLayoutParams(imgvwDimens);

        // SET SCALETYPE
        imgview.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // SET THE MARGIN

        float densityMargin = getResources().getDisplayMetrics().density;
        int finalDimensMargin = (int) (dimensMargin * densityMargin);

        LinearLayout.LayoutParams imgvwMargin = new LinearLayout.LayoutParams(finalDimens, finalDimens);
        imgvwMargin.setMargins(finalDimensMargin, finalDimensMargin, finalDimensMargin, finalDimensMargin);


        Bitmap original_img = BitmapFactory.decodeFile(picturePath);


        // Compresez si Uploadez poza in Firebase folosind id user

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        original_img.compress(Bitmap.CompressFormat.JPEG, 35, baos);
        byte[] data_byte = baos.toByteArray();

        // SET YOUR IMAGE SOURCE TO YOUR NEW IMAGEVIEW HERE

        Bitmap compressed_img = BitmapFactory.decodeByteArray(data_byte, 0, data_byte.length);
        imgview.setImageBitmap(compressed_img);


        // ADD THE NEW IMAGEVIEW WITH THE PROFILE PICTURE LOADED TO THE LINEARLAYOUT
        layoutFotografii.addView(imgview, imgvwMargin);

        // Adaugam in arraylist
        lPoze.add(imgview);


    }

    // metoda pt cand se face poza
    public void adaugaImagine(Bitmap selectedImage, int dimens, int dimensMargin) {

        // ADAUGARE dinamica imagini
        ImageView imgview = new ImageView(getContext());

        // SET THE IMAGEVIEW DIMENSIONS
        float density = getResources().getDisplayMetrics().density;
        int finalDimens = (int) (dimens * density);

        LinearLayout.LayoutParams imgvwDimens = new LinearLayout.LayoutParams(finalDimens, finalDimens);
        imgview.setLayoutParams(imgvwDimens);

        // SET SCALETYPE
        imgview.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // SET THE MARGIN

        float densityMargin = getResources().getDisplayMetrics().density;
        int finalDimensMargin = (int) (dimensMargin * densityMargin);

        LinearLayout.LayoutParams imgvwMargin = new LinearLayout.LayoutParams(finalDimens, finalDimens);
        imgvwMargin.setMargins(finalDimensMargin, finalDimensMargin, finalDimensMargin, finalDimensMargin);


        // Compresez si Uploadez poza in Firebase folosind id user

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 35, baos);
        byte[] data_byte = baos.toByteArray();

        // SET YOUR IMAGE SOURCE TO YOUR NEW IMAGEVIEW HERE

        Bitmap compressed_img = BitmapFactory.decodeByteArray(data_byte, 0, data_byte.length);
        imgview.setImageBitmap(compressed_img);


        // ADD THE NEW IMAGEVIEW WITH THE PROFILE PICTURE LOADED TO THE LINEARLAYOUT
        layoutFotografii.addView(imgview, imgvwMargin);

        // Adaugam in arraylist
        lPoze.add(imgview);


    }


    public void alegeData() {

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();

        int startYear = c.get(Calendar.YEAR);
        int startMonth = c.get(Calendar.MONTH) + 1;
        int startDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog.OnDateSetListener dateListener;

        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editData.setText(dayOfMonth + "." + monthOfYear + "." + year);
            }
        };

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(getActivity(), dateListener, startYear, startMonth, startDay);

        datePickerDialog.show();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonPickDateEv:
                alegeData();

                break;

            case R.id.buttonAddPhotosEv:
                selectImage(getContext());
                break;

            case R.id.buttonAddEvent:
                incarcaCerere();
                break;


        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {


    }


}