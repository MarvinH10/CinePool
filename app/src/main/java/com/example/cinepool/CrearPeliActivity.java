package com.example.cinepool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CrearPeliActivity extends AppCompatActivity {
    Button btn_addPeli;
    EditText nombrePeli, directorPeli, duracionPeli, generoPeli, idiomaPeli, resumenPeli;
    private FirebaseFirestore mfirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_peli);

        this.setTitle("Agregar Pelicula");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mfirestore = FirebaseFirestore.getInstance();

        nombrePeli = findViewById(R.id.nombrePeli);
        directorPeli = findViewById(R.id.directorPeli);
        duracionPeli = findViewById(R.id.duracionPeli);
        generoPeli = findViewById(R.id.generoPeli);
        idiomaPeli = findViewById(R.id.idiomaPeli);
        resumenPeli = findViewById(R.id.resumenPeli);
        btn_addPeli = findViewById(R.id.btn_addPeli);

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
    }

    private void postPeli(String nomPeli, String direcPeli, String duraPeli, String genePeli, String idioPeli, String resuPeli) {
        Map<String, Object> map = new HashMap<>();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}