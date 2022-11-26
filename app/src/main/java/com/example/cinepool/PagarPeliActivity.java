package com.example.cinepool;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class PagarPeliActivity extends AppCompatActivity {
    Button btn_pago;
    EditText pago_nomyape, pago_nrotarjeta, pago_direccion, pago_correo;
    LinearLayout linearLayout_image_btn;
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    StorageReference storageReference;
    String storage_path = "pago/*";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagar_peli);
        this.setTitle("Pago de Pelicula");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        String id = getIntent().getStringExtra("id_peli");
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        linearLayout_image_btn = findViewById(R.id.images_btn);
        pago_nomyape = findViewById(R.id.pago_nomyape);
        pago_nrotarjeta = findViewById(R.id.pago_nrotarjeta);
        pago_direccion = findViewById(R.id.pago_direccion);
        pago_correo = findViewById(R.id.pago_correo);
        btn_pago = findViewById(R.id.btn_alquilar);

        if (id == null || id == ""){
            linearLayout_image_btn.setVisibility(View.GONE);
            btn_pago.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nomPago = pago_nomyape.getText().toString().trim();
                    String tarPago = pago_nrotarjeta.getText().toString().trim();
                    String direPago = pago_direccion.getText().toString().trim();
                    String emaPago = pago_correo.getText().toString().trim();

                    if (nomPago.isEmpty() && tarPago.isEmpty() && direPago.isEmpty() && emaPago.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Por favor, Ingrese sus Datos", Toast.LENGTH_SHORT).show();
                    }else {
                        postPago(nomPago, tarPago, direPago, emaPago);
                    }
                }
            });
        }else {
            btn_pago.setText("Alquilar");
            getPago(id);
            btn_pago.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nomPago = pago_nomyape.getText().toString().trim();
                    String tarPago = pago_nrotarjeta.getText().toString().trim();
                    String direPago = pago_direccion.getText().toString().trim();
                    String emaPago = pago_correo.getText().toString().trim();

                    if (nomPago.isEmpty() && tarPago.isEmpty() && direPago.isEmpty() && emaPago.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Por favor, Ingrese sus Datos", Toast.LENGTH_SHORT).show();
                    }else {
                        updatePago(nomPago, tarPago, direPago, emaPago, id);
                    }
                }
            });
        }
    }
    private void updatePago(String nomPago, String tarPago, String direPago, String emaPago, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("pago_nomyape", nomPago);
        map.put("pago_nrotarjeta", tarPago);
        map.put("pago_direccion", direPago);
        map.put("pago_correo", emaPago);

        mfirestore.collection("pago").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Se Realizo el Pago", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al Pagar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postPago(String nomPago, String tarPago, String direPago, String emaPago) {
        String idUser = mAuth.getCurrentUser().getUid();
        DocumentReference id = mfirestore.collection("pago").document();

        Map<String, Object> map = new HashMap<>();
        map.put("id_user", idUser);
        map.put("pago_nomyape", nomPago);
        map.put("pago_nrotarjeta", tarPago);
        map.put("pago_direccion", direPago);
        map.put("pago_correo", emaPago);

        mfirestore.collection("pago").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Se Realizo el Pago Correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al Pagar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPago(String id){
        mfirestore.collection("peli").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String noPago = documentSnapshot.getString("pago_nomyape");
                String taPago = documentSnapshot.getString("pago_nrotarjeta");
                String diPago = documentSnapshot.getString("pago_direccion");
                String emPago = documentSnapshot.getString("pago_correo");

                pago_nomyape.setText(noPago);
                pago_nrotarjeta.setText(taPago);
                pago_direccion.setText(diPago);
                pago_correo.setText(emPago);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al Pagar", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}