package com.example.cinepool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class CrearPeliActivity extends AppCompatActivity {
    ImageView photo_peli;
    Button btn_addPeli, btn_cu_photo, btn_r_photo;
    LinearLayout linearLayout_image_btn;
    EditText nombrePeli, directorPeli, duracionPeli, generoPeli, idiomaPeli, resumenPeli;
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;

    StorageReference storageReference;
    String storage_path = "peli/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";
    String idd;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_peli);
        this.setTitle("Pelicula");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        String id = getIntent().getStringExtra("id_peli");
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        linearLayout_image_btn = findViewById(R.id.images_btn);
        nombrePeli = findViewById(R.id.nombrePeli);
        directorPeli = findViewById(R.id.directorPeli);
        duracionPeli = findViewById(R.id.duracionPeli);
        generoPeli = findViewById(R.id.generoPeli);
        idiomaPeli = findViewById(R.id.idiomaPeli);
        resumenPeli = findViewById(R.id.resumenPeli);
        photo_peli = findViewById(R.id.peli_photo);
        btn_cu_photo = findViewById(R.id.btn_photo);
        btn_r_photo = findViewById(R.id.btn_remove_photo);
        btn_addPeli = findViewById(R.id.btn_addPeli);

        btn_cu_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();
            }
        });

        btn_r_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("photo", "");
                mfirestore.collection("peli").document(idd).update(map);
                Toast.makeText(CrearPeliActivity.this, "Foto eliminada", Toast.LENGTH_SHORT).show();
            }
        });

        if (id == null || id == ""){
            linearLayout_image_btn.setVisibility(View.GONE);
            btn_addPeli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nomPeli = nombrePeli.getText().toString().trim();
                    String direcPeli = directorPeli.getText().toString().trim();
                    String duraPeli = duracionPeli.getText().toString().trim();
                    String genePeli = generoPeli.getText().toString().trim();
                    String idioPeli = idiomaPeli.getText().toString().trim();
                    String resuPeli = resumenPeli.getText().toString().trim();

                    if (nomPeli.isEmpty() && direcPeli.isEmpty() && duraPeli.isEmpty() && genePeli.isEmpty() && idioPeli.isEmpty() && resuPeli.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Ingresar los Datos", Toast.LENGTH_SHORT).show();
                    }else {
                        postPeli(nomPeli, direcPeli, duraPeli, genePeli, idioPeli, resuPeli);
                    }
                }
            });
        }else {
            btn_addPeli.setText("Actualizar");
            getPeli(id);
            btn_addPeli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nomPeli = nombrePeli.getText().toString().trim();
                    String direcPeli = directorPeli.getText().toString().trim();
                    String duraPeli = duracionPeli.getText().toString().trim();
                    String genePeli = generoPeli.getText().toString().trim();
                    String idioPeli = idiomaPeli.getText().toString().trim();
                    String resuPeli = resumenPeli.getText().toString().trim();

                    if (nomPeli.isEmpty() && direcPeli.isEmpty() && duraPeli.isEmpty() && genePeli.isEmpty() && idioPeli.isEmpty() && resuPeli.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Ingresar los Datos", Toast.LENGTH_SHORT).show();
                    }else {
                        updatePeli(nomPeli, direcPeli, duraPeli, genePeli, idioPeli, resuPeli, id);
                    }
                }
            });
        }
    }

    private void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");

        startActivityForResult(i, COD_SEL_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == COD_SEL_IMAGE){
                image_url = data.getData();
                subirPhoto(image_url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirPhoto(Uri image_url) {
        progressDialog.setMessage("Actualizando foto");
        progressDialog.show();
        String rute_storage_photo = storage_path + "" + photo + "" + mAuth.getUid() +""+ idd;
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                if (uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String download_uri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("photo", download_uri);
                            mfirestore.collection("pet").document(idd).update(map);
                            Toast.makeText(CrearPeliActivity.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CrearPeliActivity.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePeli(String nomPeli, String direcPeli, String duraPeli, String genePeli, String idioPeli, String resuPeli, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombrePeli", nomPeli);
        map.put("directorPeli", direcPeli);
        map.put("duracionPeli", duraPeli);
        map.put("generoPeli", genePeli);
        map.put("idiomaPeli", idioPeli);
        map.put("resumenPeli", resuPeli);

        mfirestore.collection("peli").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Se Actualizo los Datos Correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al Actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postPeli(String nomPeli, String direcPeli, String duraPeli, String genePeli, String idioPeli, String resuPeli) {
        String idUser = mAuth.getCurrentUser().getUid();
        DocumentReference id = mfirestore.collection("peli").document();

        Map<String, Object> map = new HashMap<>();
        map.put("id_user", idUser);
        map.put("nombrePeli", nomPeli);
        map.put("directorPeli", direcPeli);
        map.put("duracionPeli", duraPeli);
        map.put("generoPeli", genePeli);
        map.put("idiomaPeli", idioPeli);
        map.put("resumenPeli", resuPeli);

        mfirestore.collection("peli").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Se Agrego los Datos Correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al Ingresar los Datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPeli(String id){
        mfirestore.collection("peli").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String noPeli = documentSnapshot.getString("nombrePeli");
                String diPeli = documentSnapshot.getString("directorPeli");
                String duPeli = documentSnapshot.getString("duracionPeli");
                String gePeli = documentSnapshot.getString("generoPeli");
                String idPeli = documentSnapshot.getString("idiomaPeli");
                String rePeli = documentSnapshot.getString("resumenPeli");
                String photoPeli = documentSnapshot.getString("photo");

                nombrePeli.setText(noPeli);
                directorPeli.setText(diPeli);
                duracionPeli.setText(duPeli);
                generoPeli.setText(gePeli);
                idiomaPeli.setText(idPeli);
                resumenPeli.setText(rePeli);

                try {
                    if(!photoPeli.equals("")){
                        Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();
                        Picasso.with(CrearPeliActivity.this)
                                .load(photoPeli)
                                .resize(150, 150)
                                .into(photo_peli);
                    }
                }catch (Exception e){
                    Log.v("Error", "e: " + e);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al Obtener los Datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}