package com.example.usuario.app.myroodent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.master.glideimageview.GlideImageView;

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
@BindView(R.id.tvFecha)
TextView tvFecha;
@BindView(R.id.tvHora)
TextView tvHora;
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
@BindView(R.id.btn_ir_menu)
Button btn_ir_menu;
@BindView(R.id.btn_ir_lista)
Button btn_ir_lista;
public List<ReporteEspecie> reportesEspecies;

@Override
protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_complete);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


    ButterKnife.bind(this);
    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build();
    mFirestore.setFirestoreSettings(settings);

    mFirebaseAuth = FirebaseAuth.getInstance();
    mFirestore = FirebaseFirestore.getInstance();
    reportesEspecies = new ArrayList<>();

    traerDoc();
    btn_ir_lista.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(),EspeciesActivity.class);
            startActivity(i);
        }
    });
    btn_ir_menu.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
    });
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
                            tvFecha.setText(document.get("fecha").toString());
                            tvHora.setText(document.get("hora").toString());

                            if(tvEspecie.getText().equals(ANFIBIO)){
                                btnSeis.setImageResource(R.drawable.sapo_activo);
                                btnSeis.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //btnSeis.setBackgroundResource(R.drawable.sapo_inactivo);
                                        Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                        intent.putExtra("doc",document.getId());
                                        intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                        intent.putExtra("especie",tvEspecie.getText());
                                        intent.putExtra("subEspecie","Sapo");
                                        intent.putExtra("fecha",tvFecha.getText().toString());
                                        intent.putExtra("hora",tvHora.getText().toString());
                                        intent.putExtra("direccion",tvDireccion.getText());
                                        startActivity(intent);
                                    }
                                });
                                btnOcho.setImageResource(R.drawable.rana_activo);
                                btnOcho.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //btnOcho.setBackgroundResource(R.drawable.rana_inactivo);
                                        Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                        intent.putExtra("doc",document.getId());
                                        intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                        intent.putExtra("especie",tvEspecie.getText());
                                        intent.putExtra("subEspecie","Rana");
                                        intent.putExtra("fecha",tvFecha.getText().toString());
                                        intent.putExtra("hora",tvHora.getText().toString());
                                        intent.putExtra("direccion",tvDireccion.getText());
                                        startActivity(intent);
                                    }
                                });
                            }else{
                                if(tvEspecie.getText().equals(AVE)){
                                    btnSeis.setImageResource(R.drawable.gallinazo_activo_2);
                                    btnSeis.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //btnSeis.setBackgroundResource(R.drawable.gallinazo_inactivo);
                                            Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                            intent.putExtra("doc",document.getId());
                                            intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                            intent.putExtra("especie",tvEspecie.getText());
                                            intent.putExtra("subEspecie","Gallinazo");
                                            intent.putExtra("fecha",tvFecha.getText().toString());
                                            intent.putExtra("hora",tvHora.getText().toString());
                                            intent.putExtra("direccion",tvDireccion.getText());
                                            startActivity(intent);
                                        }
                                    });
                                    btnOcho.setImageResource(R.drawable.rapaces_activo);
                                    btnOcho.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //btnOcho.setBackgroundResource(R.drawable.rapaces_inactivo);
                                            Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                            intent.putExtra("doc",document.getId());
                                            intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                            intent.putExtra("especie",tvEspecie.getText());
                                            intent.putExtra("subEspecie","Rapaz");
                                            intent.putExtra("fecha",tvFecha.getText().toString());
                                            intent.putExtra("hora",tvHora.getText().toString());
                                            intent.putExtra("direccion",tvDireccion.getText());
                                            startActivity(intent);
                                        }
                                    });

                                }else{
                                    if(tvEspecie.getText().equals(REPTIL)) {
                                        btnSeis.setImageResource(R.drawable.lagarto_activo);
                                        btnSeis.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //btnSeis.setBackgroundResource(R.drawable.lagarto_inactivo);
                                                Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                intent.putExtra("doc",document.getId());
                                                intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                intent.putExtra("especie",tvEspecie.getText());
                                                intent.putExtra("subEspecie","Lagarto");
                                                intent.putExtra("fecha",tvFecha.getText().toString());
                                                intent.putExtra("hora",tvHora.getText().toString());
                                                intent.putExtra("direccion",tvDireccion.getText());
                                                startActivity(intent);
                                            }
                                        });
                                        btnSiete.setImageResource(R.drawable.culebra_activo);
                                        btnSiete.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //btnSiete.setBackgroundResource(R.drawable.culebra2_inactivo);
                                                Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                intent.putExtra("doc",document.getId());
                                                intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                intent.putExtra("especie",tvEspecie.getText());
                                                intent.putExtra("subEspecie","Culebra");
                                                intent.putExtra("fecha",tvFecha.getText().toString());
                                                intent.putExtra("hora",tvHora.getText().toString());
                                                intent.putExtra("direccion",tvDireccion.getText());
                                                startActivity(intent);
                                            }
                                        });
                                        btnOcho.setImageResource(R.drawable.tortuga_activo);
                                        btnOcho.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //btnOcho.setBackgroundResource(R.drawable.rana_inactivo);
                                                Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                intent.putExtra("doc",document.getId());
                                                intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                intent.putExtra("especie",tvEspecie.getText());
                                                intent.putExtra("subEspecie","Tortuga");
                                                intent.putExtra("fecha",tvFecha.getText().toString());
                                                intent.putExtra("hora",tvHora.getText().toString());
                                                intent.putExtra("direccion",tvDireccion.getText());
                                                startActivity(intent);
                                            }
                                        });

                                    }else{
                                        if(tvEspecie.getText().equals(MAMIFERO)) {
                                            btnOcho.setImageResource(R.drawable.roedores_activo);
                                            btnOcho.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //btnOcho.setBackgroundResource(R.drawable.roedores_inactivo);
                                                    Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                    intent.putExtra("doc",document.getId());
                                                    intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                    intent.putExtra("especie",tvEspecie.getText());
                                                    intent.putExtra("subEspecie","Roedor");
                                                    intent.putExtra("fecha",tvFecha.getText().toString());
                                                    intent.putExtra("hora",tvHora.getText().toString());
                                                    intent.putExtra("direccion",tvDireccion.getText());
                                                    startActivity(intent);
                                                }
                                            });
                                            btnDos.setImageResource(R.drawable.felino_activo);
                                            btnDos.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //btnDos.setBackgroundResource(R.drawable.felino_inactivo);
                                                    Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                    intent.putExtra("doc",document.getId());
                                                    intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                    intent.putExtra("especie",tvEspecie.getText());
                                                    intent.putExtra("subEspecie","Felino");
                                                    intent.putExtra("fecha",tvFecha.getText().toString());
                                                    intent.putExtra("hora",tvHora.getText().toString());
                                                    intent.putExtra("direccion",tvDireccion.getText());
                                                    startActivity(intent);
                                                }
                                            });
                                            btnTres.setImageResource(R.drawable.primate_activo);
                                            btnTres.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //btnTres.setBackgroundResource(R.drawable.primate_inactivo);
                                                    Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                    intent.putExtra("doc",document.getId());
                                                    intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                    intent.putExtra("especie",tvEspecie.getText());
                                                    intent.putExtra("subEspecie","Primate");
                                                    intent.putExtra("fecha",tvFecha.getText().toString());
                                                    intent.putExtra("hora",tvHora.getText().toString());
                                                    intent.putExtra("direccion",tvDireccion.getText());
                                                    startActivity(intent);
                                                }
                                            });
                                            btnCuatro.setImageResource(R.drawable.comadreja_activo);
                                            btnCuatro.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //btnCuatro.setBackgroundResource(R.drawable.comadreja_inactivo);
                                                    Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                    intent.putExtra("doc",document.getId());
                                                    intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                    intent.putExtra("especie",tvEspecie.getText());
                                                    intent.putExtra("subEspecie","Comadreja");
                                                    intent.putExtra("fecha",tvFecha.getText().toString());
                                                    intent.putExtra("hora",tvHora.getText().toString());
                                                    intent.putExtra("direccion",tvDireccion.getText());
                                                    startActivity(intent);
                                                }
                                            });
                                            btnCinco.setImageResource(R.drawable.zariguella_activo);
                                            btnCinco.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //btnCinco.setBackgroundResource(R.drawable.zariguella_inactivo);
                                                    Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                    intent.putExtra("doc",document.getId());
                                                    intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                    intent.putExtra("especie",tvEspecie.getText());
                                                    intent.putExtra("subEspecie","Zariguella");
                                                    intent.putExtra("fecha",tvFecha.getText().toString());
                                                    intent.putExtra("hora",tvHora.getText().toString());
                                                    intent.putExtra("direccion",tvDireccion.getText());
                                                    startActivity(intent);
                                                }
                                            });
                                            btnSeis.setImageResource(R.drawable.zorro_activo);
                                            btnSeis.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //btnSeis.setBackgroundResource(R.drawable.zorro_inactivo);
                                                    Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                    intent.putExtra("doc",document.getId());
                                                    intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                    intent.putExtra("especie",tvEspecie.getText());
                                                    intent.putExtra("subEspecie","Zorro");
                                                    intent.putExtra("fecha",tvFecha.getText().toString());
                                                    intent.putExtra("hora",tvHora.getText().toString());
                                                    intent.putExtra("direccion",tvDireccion.getText());
                                                    startActivity(intent);
                                                }
                                            });
                                            btnSiete.setImageResource(R.drawable.oso_activo);
                                            btnSiete.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //btnSiete.setBackgroundResource(R.drawable.oso_inactivo);
                                                    Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                    intent.putExtra("doc",document.getId());
                                                    intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                    intent.putExtra("especie",tvEspecie.getText());
                                                    intent.putExtra("subEspecie","Oso");
                                                    intent.putExtra("fecha",tvFecha.getText().toString());
                                                    intent.putExtra("hora",tvHora.getText().toString());
                                                    intent.putExtra("direccion",tvDireccion.getText());
                                                    startActivity(intent);
                                                }
                                            });

                                        }else{
                                            if (tvEspecie.getText().equals(OTRO)){
                                                btnSiete.setImageResource(R.drawable.desconocido_activo);
                                                btnSiete.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                                        intent.putExtra("doc",document.getId());
                                                        intent.putExtra("usuario",mFirebaseAuth.getCurrentUser().getDisplayName());
                                                        intent.putExtra("especie",tvEspecie.getText());
                                                        intent.putExtra("subEspecie","Desconocida");
                                                        intent.putExtra("fecha",tvFecha.getText().toString());
                                                        intent.putExtra("hora",tvHora.getText().toString());
                                                        intent.putExtra("direccion",tvDireccion.getText());
                                                        startActivity(intent);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            });
    }
}
