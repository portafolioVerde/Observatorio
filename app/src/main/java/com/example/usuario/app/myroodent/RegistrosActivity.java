package com.example.usuario.app.myroodent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * La Clase RegistrosActivity.java se encarga de mostrar
 * al usuario cinco botones donde debe seleccionar una
 * posible especie vista en la vía, posteriormente se envia
 * el Id del documento donde se agregó la especie
 * para completar la subespecie y luego confirmar los datos
 */
public class RegistrosActivity extends AppCompatActivity {

    public FirebaseAuth mFirebaseAuth; //Escucha los estados de autenticacion de cada usuario
    /**
     * Se instancian las variables de tipo Button para
     * enviar asi los campos del registro seleccionado
     * y capturar la especie vista por el usuario.
     */
    @BindView(R.id.btnAnfibio) ImageView btnAnfibio;
    @BindView(R.id.btnAve) ImageView btnAve;
    @BindView(R.id.btnMamifero) ImageView btnMamifero;
    @BindView(R.id.btnReptil) ImageView btnReptil;
    @BindView(R.id.btnDesc) ImageView btnDesc;
    @BindView(R.id.btn_home) Button btn_home;
    @BindView(R.id.btn_map) Button btn_map;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Es el metodo que bloquea la rotacion de la pantalla
        persistenciaDatos(); //Persistencia de datos que provee Firebase
        ButterKnife.bind(this); //Referencia de la libreria ButterKnife

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users"); //Referencia de la RealtimeDatabase
        mFirebaseAuth = FirebaseAuth.getInstance(); //Referencia de FirebaseAuth que escucha el estado de autenticación
        /**
         * Cada uno de los siguientes metodos ejecuta el mismo
         * codigo con la diferencia que cambia la especie seleccionada
         * se asignó un evento click a cada boton que se encuentra en
         * pantalla para que el usuario seleccione la especie con más
         * probabilidades de ser la avistada
         */
        btnAnfibio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //final String doc = getIntent().getStringExtra("dac");
                btnAnfibio.setBackgroundResource(R.drawable.anfibio_inactivo);
                String doc = getIntent().getExtras().getString("dac");// Se obtiene el IdDoc del itemList seleccionado

                @Nullable
                final DocumentReference ref = db.collection("Data")
                        .document(mFirebaseAuth.getCurrentUser().getDisplayName())
                        .collection("Reportes")
                        .document(doc);

                db.collection("Data")
                        .document(mFirebaseAuth.getCurrentUser().getDisplayName()).collection("Reportes")
                        .whereEqualTo("doc",doc)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (final QueryDocumentSnapshot document : task.getResult()){
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("especie", "Anfibio");

                                        ref.update(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.w("Mensaje", "Especie Agregada"+aVoid);
                                                    }
                                                });
                                        Intent i = new Intent(getApplicationContext(), CompleteActivity.class);
                                        i.putExtra("especie",document.getString("especie"));
                                        i.putExtra("doc",document.getString("doc"));
                                        startActivity(i);

                                    }
                                }
                            }
                        });
            }
        });
        btnAve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAve.setBackgroundResource(R.drawable.ave_inactivo);
                //String doc = getIntent().getExtras().getString("dac");// Se obtiene el IdDoc del itemList seleccionado
                //String doc = getIntent().getStringExtra("dac");
                String doc = getIntent().getExtras().getString("dac");
                @Nullable
                final DocumentReference ref = db.collection("Data")
                        .document(mFirebaseAuth.getCurrentUser().getDisplayName())
                        .collection("Reportes")
                        .document(doc);

                db.collection("Data")
                        .document(mFirebaseAuth.getCurrentUser().getDisplayName()).collection("Reportes")
                        .whereEqualTo("doc",doc)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (final QueryDocumentSnapshot document : task.getResult()){
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("especie", "Ave");

                                        ref.update(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.w("Mensaje", "Especie Agregada"+aVoid);
                                                    }
                                                });
                                        Intent i = new Intent(getApplicationContext(), CompleteActivity.class);
                                        i.putExtra("especie",document.getString("especie"));
                                        i.putExtra("doc",document.getString("doc"));
                                        startActivity(i);

                                    }
                                }
                            }
                        });
            }
        });
        btnMamifero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMamifero.setBackgroundResource(R.drawable.mamifero_inactivo);
                //String doc = getIntent().getExtras().getString("dac");// Se obtiene el IdDoc del itemList seleccionado
                //String doc = getIntent().getStringExtra("dac");
                String doc = getIntent().getExtras().getString("dac");
                @Nullable
                final DocumentReference ref = db.collection("Data")
                        .document(mFirebaseAuth.getCurrentUser().getDisplayName())
                        .collection("Reportes")
                        .document(doc);

                db.collection("Data")
                        .document(mFirebaseAuth.getCurrentUser().getDisplayName()).collection("Reportes")
                        .whereEqualTo("doc",doc)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (final QueryDocumentSnapshot document : task.getResult()){
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("especie", "Mamifero");

                                        ref.update(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.w("Mensaje", "Especie Agregada"+aVoid);
                                                    }
                                                });
                                        Intent i = new Intent(getApplicationContext(), CompleteActivity.class);
                                        i.putExtra("especie",document.getString("especie"));
                                        i.putExtra("doc",document.getString("doc"));
                                        startActivity(i);

                                    }
                                }
                            }
                        });
            }
        });
        btnReptil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnReptil.setBackgroundResource(R.drawable.reptil_inactivo);
                //String doc = getIntent().getExtras().getString("dac");// Se obtiene el IdDoc del itemList seleccionado
                //String doc = getIntent().getStringExtra("dac");
                String doc = getIntent().getExtras().getString("dac");
                @Nullable
                final DocumentReference ref = db.collection("Data")
                        .document(mFirebaseAuth.getCurrentUser().getDisplayName())
                        .collection("Reportes")
                        .document(doc);

                db.collection("Data")
                        .document(mFirebaseAuth.getCurrentUser().getDisplayName()).collection("Reportes")
                        .whereEqualTo("doc",doc)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (final QueryDocumentSnapshot document : task.getResult()){
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("especie", "Reptil");

                                        ref.update(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.w("Mensaje", "Especie Agregada"+aVoid);
                                                    }
                                                });
                                        Intent i = new Intent(getApplicationContext(), CompleteActivity.class);
                                        i.putExtra("especie",document.getString("especie"));
                                        i.putExtra("doc",document.getString("doc"));
                                        startActivity(i);
                                    }
                                }
                            }
                        });
            }
        });
        btnDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDesc.setBackgroundResource(R.drawable.desconocido_inactivo);
                //String doc = getIntent().getExtras().getString("dac");// Se obtiene el IdDoc del itemList seleccionado
                //String doc = getIntent().getStringExtra("dac");
                String doc = getIntent().getExtras().getString("dac");
                @Nullable
                final DocumentReference ref = db.collection("Data")
                        .document(mFirebaseAuth.getCurrentUser().getDisplayName())
                        .collection("Reportes")
                        .document(doc);

                db.collection("Data")
                        .document(mFirebaseAuth.getCurrentUser().getDisplayName()).collection("Reportes")
                        .whereEqualTo("doc",doc)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (final QueryDocumentSnapshot document : task.getResult()){
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("especie", "Desconocida");

                                        ref.update(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.w("Mensaje", "Especie Agregada"+aVoid);
                                                    }
                                                });
                                        Intent i = new Intent(getApplicationContext(), CompleteActivity.class);
                                        i.putExtra("especie",document.getString("especie"));
                                        i.putExtra("doc",document.getString("doc"));
                                        startActivity(i);
                                    }
                                }
                            }
                        });
            }
        });
    }

    /**
     * Persistencia datos gestiona las lecturas
     * y escrituras del usuario cuando no cuenta con
     * una conexion a internet estable.
     */
    private void persistenciaDatos() {

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

    }

    /**
     * Este botón llama a la clase MainActivity.java
     */
    public void on_btn_home(View view) {
        btn_home.setBackgroundResource(R.mipmap.icon_home_white);
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    /**
     * Este botón llama a la clase MapsActivity.java
     */
    public void on_btn_map(View view) {
        btn_map.setBackgroundResource(R.mipmap.icon_map_white);
        Intent i = new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(i);
    }

}

