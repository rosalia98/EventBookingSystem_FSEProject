package com.example.eventbookingsystem_fseproject.ui.profile;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.eventbookingsystem_fseproject.R;
import com.example.eventbookingsystem_fseproject.adapters.ProfileListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    //Constants

    // valoarea se da random
    private static final int WRITE_REQUEST_CODE = 8778;

    //Variables
    private FirebaseFirestore firestore;
    private StorageReference mUserStorageRef;
    private StorageReference mPozaRef;
    private String user_uid;

    private TextView user_name;
    private ListView list;
    private String[] maintitle, subtitle;
    private Integer[] imgid;
    private ImageView user_profile;


    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
       /* dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firestore = FirebaseFirestore.getInstance();
        mUserStorageRef = FirebaseStorage.getInstance().getReference().child("Users").child(user_uid);
        mPozaRef = mUserStorageRef.child("profile_picture").child("profilepic");

        creeazaListView();


        user_name = getView().findViewById(R.id.pf_username);
        //user_name.setText(firebaseUser.getDisplayName());

        user_profile = getView().findViewById(R.id.pr_picture);
        user_profile.setOnClickListener(this);

        // Accesez numele userului din database:

        firestore.collection("Users").document(user_uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    //Log.w(TAG, "Listen failed.", error);
                    Toast.makeText(getActivity(), "Listen failed.",
                            Toast.LENGTH_LONG).show();
                    return;
                }


                if (doc.get("prenume") != null || doc.get("nume") != null) {
                    String nume = doc.get("nume").toString();
                    String prenume = doc.get("prenume").toString();
                    user_name.setText(prenume + " " + nume);


                }

            }
        });


        mPozaRef.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getContext())
                                .load(uri)
                                .into(user_profile);
                        return;
                    }


                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Alege poza profil!",
                        Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();


    }

    public void creeazaListView() {

        maintitle = new String[]{
                "Evenimentele Mele", "Informatiile Contului", "Feedback"
        };


        subtitle = new String[]{
                "Vizualizeaza comanda plasata si istoricul comenzilor tale", "Vizualizeaza rezervarea ta si istoricul rezervarilor tale",
                "Vizualizeaza si modifica informatiile contului tau", "Spune-ne parerea ta despre restaurantul Sienna"
        };

        imgid = new Integer[]{
                R.drawable.ic_baseline_date_calendar,
                R.drawable.ic_baseline_edit_24, R.drawable.ic_baseline_feedback
        };


        ProfileListAdapter adapter = new ProfileListAdapter(getActivity(), maintitle, subtitle, imgid);
        list = getView().findViewById(R.id.pf_list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Modifica intenturile cand faci paginile

/*                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(), altceva.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), altceva.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), altceva.class));
                        break;
                    case 3:
                        startActivity(new IntentgetActivity(), altceva.class));
                        break;
                }*/

            }
        });

    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Alege din Galerie", "Anulare"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Adaugă poza profil");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Alege din Galerie")) {

                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions, WRITE_REQUEST_CODE);

                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Anulare")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    // metoda pt cand se alege din galerie
    public void adaugaImagine(String picturePath) {

        Bitmap original_img = BitmapFactory.decodeFile(picturePath);

        // Compresez

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        original_img.compress(Bitmap.CompressFormat.JPEG, 35, baos);
        byte[] data_byte = baos.toByteArray();

        // Si Uploadez poza in Firebase folosind mPozaRef

        mPozaRef.putBytes(data_byte);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
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

                                adaugaImagine(picturePath);
                                cursor.close();

                            }
                        }

                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pr_picture:
                selectImage(getContext());

                break;

        }
    }
}