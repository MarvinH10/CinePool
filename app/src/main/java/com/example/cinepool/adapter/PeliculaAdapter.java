package com.example.cinepool.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinepool.CrearPeliActivity;
import com.example.cinepool.PagarPeliActivity;
import com.example.cinepool.R;
import com.example.cinepool.model.Pelicula;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class PeliculaAdapter extends FirestoreRecyclerAdapter<Pelicula, PeliculaAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PeliculaAdapter(@NonNull FirestoreRecyclerOptions<Pelicula> options, Activity activity, FragmentManager fm) {
        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Pelicula pelicula) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        viewHolder.nombrePeli.setText(pelicula.getNombrePeli());
        viewHolder.directorPeli.setText(pelicula.getDirectorPeli());
        viewHolder.duracionPeli.setText(pelicula.getDuracionPeli());
        viewHolder.generoPeli.setText(pelicula.getGeneroPeli());
        viewHolder.idiomaPeli.setText(pelicula.getIdiomaPeli());
        viewHolder.resumenPeli.setText(pelicula.getResumenPeli());
        String photoPeli = pelicula.getPhoto();
        try {
            if (!photoPeli.equals(""))
                Picasso.with(activity.getApplicationContext())
                        .load(photoPeli)
                        .resize(150, 150)
                        .into(viewHolder.photo_peli);
        }catch (Exception e){
            Log.d("Exception", "e: "+e);
        }

        viewHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, CrearPeliActivity.class);
                i.putExtra("id_peli", id);
                activity.startActivity(i);

                //Send data fragment
                //AgregarPeliFragment agregarPeliFragment = new AgregarPeliFragment();
                //Bundle bundle = new Bundle();
                //bundle.putString("id_peli", id);
                //agregarPeliFragment.setArguments(bundle);
                //agregarPeliFragment.show(fm, "Fragment Abierto");
            }
        });

        viewHolder.btn_comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, PagarPeliActivity.class);
                i.putExtra("id_peli", id);
                activity.startActivity(i);
            }
        });

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePeli(id);
            }
        });
    }

    private void deletePeli(String id) {
        mFirestore.collection("peli").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Eliminado Correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pelicula_single, parent, false);
        return new ViewHolder(v);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombrePeli, directorPeli, duracionPeli, generoPeli, idiomaPeli, resumenPeli;
        ImageView btn_delete, btn_comprar, btn_edit, photo_peli;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombrePeli = itemView.findViewById(R.id.view_nombre);
            directorPeli = itemView.findViewById(R.id.view_director);
            duracionPeli = itemView.findViewById(R.id.view_duracion);
            generoPeli = itemView.findViewById(R.id.view_genero);
            idiomaPeli = itemView.findViewById(R.id.view_idioma);
            resumenPeli = itemView.findViewById(R.id.view_resumen);
            photo_peli = itemView.findViewById(R.id.photo);
            btn_delete = itemView.findViewById(R.id.btn_eliminar);
            btn_comprar = itemView.findViewById(R.id.btn_alquilar);
            btn_edit = itemView.findViewById(R.id.btn_editar);
        }
    }
}
