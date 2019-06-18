package com.example.usuario.app.myroodent;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.LocaleList;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.cert.Extension;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import de.hdodenhof.circleimageview.CircleImageView;

import static java.util.stream.DoubleStream.of;

public class RegistrosActivity extends AppCompatActivity {

    private static final AtomicInteger count = new AtomicInteger(1);
    private static final AtomicInteger count_01 = new AtomicInteger(1);
    private static final String TAG = "mensajes_log_registros";
    public FirebaseAuth mFirebaseAuth;
    @BindView(R.id.btnAnfibio)
    ImageView btnAnfibio;
    @BindView(R.id.btnAve)
    ImageView btnAve;
    @BindView(R.id.btnMamifero)
    ImageView btnMamifero;
    @BindView(R.id.btnReptil)
    ImageView btnReptil;
    @BindView(R.id.btnDesc)
    ImageView btnDesc;
    @BindView(R.id.btn_home)
    Button btn_home;

    @BindView(R.id.btn_map)
    Button btn_map;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AlertDialog alert = null;
    private DatabaseReference mDatabase;

    public static final String PREFERENCIAS = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        persistenciaDatos();
        ButterKnife.bind(this);
        final String doc = getIntent().getStringExtra("doc");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mFirebaseAuth = FirebaseAuth.getInstance();

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
                String doc = getIntent().getStringExtra("dac");
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
                String doc = getIntent().getStringExtra("dac");
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
                String doc = getIntent().getStringExtra("dac");
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
                String doc = getIntent().getStringExtra("dac");
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

    private void persistenciaDatos() {
        ////////////////Persistencia sin conexion FireStore///////////////////////////
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

    }

    public void on_btn_home(View view) {
        btn_home.setBackgroundResource(R.mipmap.icon_home_white);
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    public void on_btn_map(View view) {
        btn_map.setBackgroundResource(R.mipmap.icon_map_white);
        Intent i = new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(i);
    }

}

