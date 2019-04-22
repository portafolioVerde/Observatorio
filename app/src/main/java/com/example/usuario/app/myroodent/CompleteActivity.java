package com.example.usuario.app.myroodent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CompleteActivity extends AppCompatActivity {

public FirebaseAuth mFirebaseAuth;
private FirebaseAuth.AuthStateListener mAuthStateListener;
private static final String TAG = "Tarea";
private static final String REPTIL = "Reptil";
private static final String AVE = "Ave";
private static final String ANFIBIO = "Anfibio";
private static final String MAMIFERO = "Mamifero";
private static final String OTRO = "Desconocida";

FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

@BindView(R.id.tvDireccion)
TextView tvDireccion;
@BindView(R.id.tvEspecie)
TextView tvEspecie;
@BindView(R.id.tvFechayHora)
TextView tvFechayHora;
@BindView(R.id.tvMensaje)
TextView tvMensaje;

@BindView(R.id.btnDos)
ImageView btnDos;
@BindView(R.id.btnTres)
ImageView btnTres;
@BindView(R.id.btnCuatro)
ImageView btnCuatro;
@BindView(R.id.btnCinco)
ImageView btnCinco;
@BindView(R.id.btnSeis)
ImageView btnSeis;
@BindView(R.id.btnSiete)
ImageView btnSiete;
@BindView(R.id.btnOcho)
ImageView btnOcho;

@BindView(R.id.tvOcho)
TextView tvOcho;
@BindView(R.id.tvSiete)
TextView tvSiete;
@BindView(R.id.tvSeis)
TextView tvSeis;
@BindView(R.id.tvCinco)
TextView tvCinco;
@BindView(R.id.tvCuatro)
TextView tvCuatro;
@BindView(R.id.tvTres)
TextView tvTres;
@BindView(R.id.tvDos)
TextView tvDos;

public List<ReporteEspecie> reportesEspecies;

@Override
protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_complete);

    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build();
    mFirestore.setFirestoreSettings(settings);

    mFirebaseAuth = FirebaseAuth.getInstance();
    mFirestore = FirebaseFirestore.getInstance();
    reportesEspecies = new ArrayList<>();
    ButterKnife.bind(this);

    traerDoc();

    }

    public void traerDoc(){

    String doc = getIntent().getStringExtra("doc");

    mFirestore.collection("Data")
            .document(mFirebaseAuth.getCurrentUser().getDisplayName()).collection("Reportes")
            .whereEqualTo("doc",doc)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {
                        for (final QueryDocumentSnapshot document : task.getResult()) {

                            tvDireccion.setText(document.get("direccion").toString());
                            tvEspecie.setText(document.get("especie").toString());
                            tvFechayHora.setText(document.get("fechaYhora").toString());

                            if(tvEspecie.getText().equals(ANFIBIO)){
                                btnSeis.setImageResource(R.drawable.sapos);
                                tvSeis.setText("Sapo");
                                btnSeis.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                        intent.putExtra("doc",document.getId());
                                        intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                        intent.putExtra("especie",tvEspecie.getText());
                                        intent.putExtra("subEspecie","Sapo");
                                        intent.putExtra("fechaYhora",tvFechayHora.getText().toString());
                                        intent.putExtra("direccion",tvDireccion.getText());
                                        startActivity(intent);
                                    }
                                });
                                btnOcho.setImageResource(R.drawable.rana);
                                tvOcho.setText("Rana");
                                btnOcho.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                        intent.putExtra("doc",document.getId());
                                        intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                        intent.putExtra("especie",tvEspecie.getText());
                                        intent.putExtra("subEspecie","Rana");
                                        intent.putExtra("fechaYhora",tvFechayHora.getText().toString());
                                        intent.putExtra("direccion",tvDireccion.getText());
                                        startActivity(intent);
                                    }
                                });

                            }else{
                                if(tvEspecie.getText().equals(AVE)){

                                    btnSeis.setImageResource(R.drawable.gallinazo);
                                    tvSeis.setText("Gallinazo");
                                    btnSeis.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                            intent.putExtra("doc",document.getId());
                                            intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                            intent.putExtra("especie",tvEspecie.getText());
                                            intent.putExtra("subEspecie","Gallinazo");
                                            intent.putExtra("fechaYhora",tvFechayHora.getText().toString());
                                            intent.putExtra("direccion",tvDireccion.getText());
                                            startActivity(intent);
                                        }
                                    });
                                    btnOcho.setImageResource(R.drawable.rapaces);
                                    tvOcho.setText("Rapaz");
                                    btnOcho.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                            intent.putExtra("doc",document.getId());
                                            intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                            intent.putExtra("especie",tvEspecie.getText());
                                            intent.putExtra("subEspecie","Rapaz");
                                            intent.putExtra("fechaYhora",tvFechayHora.getText().toString());
                                            intent.putExtra("direccion",tvDireccion.getText());
                                            startActivity(intent);
                                        }
                                    });

                                }else{
                                    if(tvEspecie.getText().equals(REPTIL)){
                                        btnSeis.setImageResource(R.drawable.lagarto);
                                        tvSeis.setText("Lagarto");
                                        btnSeis.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                intent.putExtra("doc",document.getId());
                                                intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                intent.putExtra("especie",tvEspecie.getText());
                                                intent.putExtra("subEspecie","Lagarto");
                                                intent.putExtra("fechaYhora",tvFechayHora.getText().toString());
                                                intent.putExtra("direccion",tvDireccion.getText());
                                                startActivity(intent);
                                            }
                                        });
                                        btnSiete.setImageResource(R.drawable.culebra);
                                        tvSiete.setText("Culebra");
                                        btnSiete.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                intent.putExtra("doc",document.getId());
                                                intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                intent.putExtra("especie",tvEspecie.getText());
                                                intent.putExtra("subEspecie","Culebra");
                                                intent.putExtra("fechaYhora",tvFechayHora.getText().toString());
                                                intent.putExtra("direccion",tvDireccion.getText());
                                                startActivity(intent);
                                            }
                                        });
                                        btnOcho.setImageResource(R.drawable.reptilactivo);
                                        tvOcho.setText("Tortuga");
                                        btnOcho.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                intent.putExtra("doc",document.getId());
                                                intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                intent.putExtra("especie",tvEspecie.getText());
                                                intent.putExtra("subEspecie","Tortuga");
                                                intent.putExtra("fechaYhora",tvFechayHora.getText().toString());
                                                intent.putExtra("direccion",tvDireccion.getText());
                                                startActivity(intent);
                                            }
                                        });

                                    }else{
                                        if(tvEspecie.getText().equals(MAMIFERO)){
                                            btnOcho.setImageResource(R.drawable.roedores);
                                            tvOcho.setText("Roedor");
                                            btnOcho.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                    intent.putExtra("doc",document.getId());
                                                    intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                    intent.putExtra("especie",tvEspecie.getText());
                                                    intent.putExtra("subEspecie","Roedor");
                                                    intent.putExtra("fechaYhora",tvFechayHora.getText().toString());
                                                    intent.putExtra("direccion",tvDireccion.getText());
                                                    startActivity(intent);
                                                }
                                            });
                                            btnDos.setImageResource(R.drawable.felino);
                                            tvDos.setText("Felino");
                                            btnDos.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                    intent.putExtra("doc",document.getId());
                                                    intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                    intent.putExtra("especie",tvEspecie.getText());
                                                    intent.putExtra("subEspecie","Felino");
                                                    intent.putExtra("fechaYhora",tvFechayHora.getText().toString());
                                                    intent.putExtra("direccion",tvDireccion.getText());
                                                    startActivity(intent);
                                                }
                                            });
                                            btnTres.setImageResource(R.drawable.primates);
                                            tvTres.setText("Primate");
                                            btnTres.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                    intent.putExtra("doc",document.getId());
                                                    intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                    intent.putExtra("especie",tvEspecie.getText());
                                                    intent.putExtra("subEspecie","Primate");
                                                    intent.putExtra("fechaYhora",tvFechayHora.getText().toString());
                                                    intent.putExtra("direccion",tvDireccion.getText());
                                                    startActivity(intent);
                                                }
                                            });
                                            btnCuatro.setImageResource(R.drawable.comadreja);
                                            tvCuatro.setText("Comadreja");
                                            btnCuatro.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                    intent.putExtra("doc",document.getId());
                                                    intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                    intent.putExtra("especie",tvEspecie.getText());
                                                    intent.putExtra("subEspecie","Comadreja");
                                                    intent.putExtra("fechaYhora",tvFechayHora.getText().toString());
                                                    intent.putExtra("direccion",tvDireccion.getText());
                                                    startActivity(intent);
                                                }
                                            });
                                            btnCinco.setImageResource(R.drawable.zariguella);
                                            tvCinco.setText("Zarigüella");
                                            btnCinco.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                    intent.putExtra("doc",document.getId());
                                                    intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                    intent.putExtra("especie",tvEspecie.getText());
                                                    intent.putExtra("subEspecie","Zariguella");
                                                    intent.putExtra("fechaYhora",tvFechayHora.getText().toString());
                                                    intent.putExtra("direccion",tvDireccion.getText());
                                                    startActivity(intent);
                                                }
                                            });
                                            btnSeis.setImageResource(R.drawable.zorro);
                                            tvSeis.setText("Zorro");
                                            btnSeis.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                    intent.putExtra("doc",document.getId());
                                                    intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                    intent.putExtra("especie",tvEspecie.getText());
                                                    intent.putExtra("subEspecie","Zorro");
                                                    intent.putExtra("fechaYhora",tvFechayHora.getText().toString());
                                                    intent.putExtra("direccion",tvDireccion.getText());
                                                    startActivity(intent);
                                                }
                                            });
                                            btnSiete.setImageResource(R.drawable.oso);
                                            tvSiete.setText("Oso");
                                            btnSiete.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                    intent.putExtra("doc",document.getId());
                                                    intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                    intent.putExtra("especie",tvEspecie.getText());
                                                    intent.putExtra("subEspecie","Oso");
                                                    intent.putExtra("fechaYhora",tvFechayHora.getText().toString());
                                                    intent.putExtra("direccion",tvDireccion.getText());
                                                    startActivity(intent);
                                                }
                                            });

                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Se presento un incidente al obtener los datos", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    public void btn_Regresar(View view) {
        onBackPressed();
    }
}
