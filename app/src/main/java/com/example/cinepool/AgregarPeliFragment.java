package com.example.cinepool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AgregarPeliFragment extends DialogFragment {
    String id_peli;
    Button btn_addPeli;
    EditText nombrePeli, directorPeli, duracionPeli, generoPeli, idiomaPeli, resumenPeli;
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            id_peli = getArguments().getString("id_peli");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_agregar_peli, container, false);
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        nombrePeli = v.findViewById(R.id.nombrePeli);
        directorPeli = v.findViewById(R.id.directorPeli);
        duracionPeli = v.findViewById(R.id.duracionPeli);
        generoPeli = v.findViewById(R.id.generoPeli);
        idiomaPeli = v.findViewById(R.id.idiomaPeli);
        resumenPeli = v.findViewById(R.id.resumenPeli);
        btn_addPeli = v.findViewById(R.id.btn_addPeli);

        if (id_peli==null || id_peli==""){
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
                        Toast.makeText(getContext(), "Ingresar los Datos", Toast.LENGTH_SHORT).show();
                    }else {
                        postPeli(nomPeli, direcPeli, duraPeli, genePeli, idioPeli, resuPeli);
                    }
                }
            });
        }else {
            getPeli();
            btn_addPeli.setText("Actualizar");
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
                        Toast.makeText(getContext(), "Ingresar los Datos", Toast.LENGTH_SHORT).show();
                    }else {
                        updatePeli(nomPeli, direcPeli, duraPeli, genePeli, idioPeli, resuPeli);
                    }
                }
            });
        }

        return v;
    }

    private void updatePeli(String nomPeli, String direcPeli, String duraPeli, String genePeli, String idioPeli, String resuPeli) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombrePeli", nomPeli);
        map.put("directorPeli", direcPeli);
        map.put("duracionPeli", duraPeli);
        map.put("generoPeli", genePeli);
        map.put("idiomaPeli", idioPeli);
        map.put("resumenPeli", resuPeli);

        mfirestore.collection("peli").document(id_peli).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Se Actualizo los Datos Correctamente", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al Actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postPeli(String nomPeli, String direcPeli, String duraPeli, String genePeli, String idioPeli, String resuPeli) {
        String idUser = mAuth.getCurrentUser().getUid();
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
                Toast.makeText(getContext(), "Se Agrego los Datos Correctamente", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al Ingresar los Datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPeli(){
        mfirestore.collection("peli").document(id_peli).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String noPeli = documentSnapshot.getString("nombrePeli");
                String diPeli = documentSnapshot.getString("directorPeli");
                String duPeli = documentSnapshot.getString("duracionPeli");
                String gePeli = documentSnapshot.getString("generoPeli");
                String idPeli = documentSnapshot.getString("idiomaPeli");
                String rePeli = documentSnapshot.getString("resumenPeli");

                nombrePeli.setText(noPeli);
                directorPeli.setText(diPeli);
                duracionPeli.setText(duPeli);
                generoPeli.setText(gePeli);
                idiomaPeli.setText(idPeli);
                resumenPeli.setText(rePeli);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al Obtener los Datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}