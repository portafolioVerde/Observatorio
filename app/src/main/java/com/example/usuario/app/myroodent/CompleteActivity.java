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


/**
 * La clase Complete activity.java
 * Se encarga de obtener el ID del documento que seleccionó
 * el usuario en la lista de reportes para completar los registros,
 * luego de obtener el ID se muestran las subespecies según la especie
 * seleccionada anteriormente. Se implementó la liberia butterknife
 * "https://jakewharton.github.io/butterknife/", el metodo traerDoc();
 * se encarga de ubicar los elementos según la especie. Y se envia la
 * información registrada también a la clase RegistradoActivity.java
 *
 */
public class CompleteActivity extends AppCompatActivity implements  View.OnClickListener{
/**
 * mFirebasAuth es el metodo que proporciona la autenticación
 * basada en correo electronico y contraseña
 */
public FirebaseAuth mFirebaseAuth;
private FirebaseAuth.AuthStateListener mAuthStateListener;
private static final String TAG = "Tarea";
private static final String REPTIL = "Reptil";
private static final String AVE = "Ave";
private static final String ANFIBIO = "Anfibio";
private static final String MAMIFERO = "Mamifero";
private static final String OTRO = "Desconocida";

/**
 * Se instancian las variables de tipo Button
 * para navegar por la app hacia la clase MapsActivity.java
 * y EspeciesActivity.java también se instancian los textView
 * y los ImageView que muestra las subespecies y la información
 * del reporte que se esta procesando.
 */

FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
//@BindView(R.id.tvDireccion) TextView tvDireccion;
//@BindView(R.id.tvEspecie) TextView tvEspecie;
//@BindView(R.id.tvFecha) TextView tvFecha;
//@BindView(R.id.tvHora) TextView tvHora;
//@BindView(R.id.tvMensaje) TextView tvMensaje;
//@BindView(R.id.btnDos) ImageView btnDos;
//@BindView(R.id.btnTres) ImageView btnTres;
//@BindView(R.id.btnCuatro) ImageView btnCuatro;
//@BindView(R.id.btnCinco) ImageView btnCinco;
////@BindView(R.id.btnSeis) ImageView btnSeis;
//@BindView(R.id.btnSiete) ImageView btnSiete;
//@BindView(R.id.btnOcho) ImageView btnOcho;
//@BindView(R.id.btn_ir_menu) Button btn_ir_menu;
//@BindView(R.id.btn_ir_lista) Button btn_ir_lista;
TextView tvDireccion,tvEspecie,tvFecha,tvHora,tvMensaje;
ImageView btnDos,btnTres,btnCuatro,btnCinco,btnSeis,btnSiete,btnOcho;
Button btn_ir_menu,btn_ir_lista;
/**
 * También se instancia la clase ReporteEspecie.java
 * para comunicarse con la información de cada usuario.
 */
public List<ReporteEspecie> reportesEspecies;
@Override
protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_complete);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    tvDireccion = findViewById(R.id.tvDireccion);
    tvEspecie = findViewById(R.id.tvEspecie);
    tvFecha = findViewById(R.id.tvFecha);
    tvHora = findViewById(R.id.tvHora);
    tvMensaje = findViewById(R.id.tvMensaje);

    btnDos = findViewById(R.id.btnDos);
    btnDos.setOnClickListener(this);
    btnTres = findViewById(R.id.btnTres);
    btnTres.setOnClickListener(this);
    btnCuatro = findViewById(R.id.btnCuatro);
    btnCuatro.setOnClickListener(this);
    btnCinco = findViewById(R.id.btnCinco);
    btnCinco.setOnClickListener(this);
    btnSeis = findViewById(R.id.btnSeis);
    btnSeis.setOnClickListener(this);
    btnSiete = findViewById(R.id.btnSiete);
    btnSiete.setOnClickListener(this);
    btnOcho = findViewById(R.id.btnOcho);
    btnOcho.setOnClickListener(this);
    btn_ir_menu = findViewById(R.id.btn_ir_menu);
    btn_ir_menu.setOnClickListener(this);
    btn_ir_lista = findViewById(R.id.btn_ir_lista);
    btn_ir_lista.setOnClickListener(this);

    //ButterKnife.bind(this);
    persistenciaDatosFirebase();

    mFirebaseAuth = FirebaseAuth.getInstance();
    mFirestore = FirebaseFirestore.getInstance();
    reportesEspecies = new ArrayList<>();
    traerDoc();

    }

    private void persistenciaDatosFirebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);
    }

    public void traerDoc(){

    final String doc = getIntent().getStringExtra("doc");
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

                            final String obsusuario = mFirebaseAuth.getCurrentUser().getDisplayName();
                            final String obstvEspecie = tvEspecie.getText().toString();
                            final String obstvFecha = tvFecha.getText().toString();
                            final String obstvHora = tvHora.getText().toString();
                            final String obstvDireccion = tvDireccion.getText().toString();
                            if(tvEspecie.getText().equals(ANFIBIO)){
                                btnSeis.setImageResource(R.drawable.sapo_activo);
                                btnSeis.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //btnSeis.setBackgroundResource(R.drawable.sapo_inactivo);
                                        Intent intent = new Intent(getApplicationContext(),RegistradoActivity.class);
                                        intent.putExtra("doc",document.getId());
                                        intent.putExtra("usuario",obsusuario);
                                        intent.putExtra("especie",obstvEspecie);
                                        intent.putExtra("subEspecie","Sapo");
                                        intent.putExtra("fecha",obstvFecha);
                                        intent.putExtra("hora",obstvHora);
                                        intent.putExtra("direccion",obstvDireccion);
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
                                        intent.putExtra("usuario",obsusuario);
                                        intent.putExtra("especie",obstvEspecie);
                                        intent.putExtra("fecha",obstvFecha);
                                        intent.putExtra("hora",obstvHora);
                                        intent.putExtra("direccion",obstvDireccion);
                                        intent.putExtra("subEspecie","Rana");
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
                                            intent.putExtra("usuario",obsusuario);
                                            intent.putExtra("especie",obstvEspecie);
                                            intent.putExtra("fecha",obstvFecha);
                                            intent.putExtra("hora",obstvHora);
                                            intent.putExtra("direccion",obstvDireccion);
                                            intent.putExtra("subEspecie","Gallinazo");
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
                                            intent.putExtra("usuario",obsusuario);
                                            intent.putExtra("especie",obstvEspecie);
                                            intent.putExtra("fecha",obstvFecha);
                                            intent.putExtra("hora",obstvHora);
                                            intent.putExtra("direccion",obstvDireccion);
                                            intent.putExtra("subEspecie","Rapaz");
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
                                                intent.putExtra("usuario",obsusuario);
                                                intent.putExtra("especie",obstvEspecie);
                                                intent.putExtra("fecha",obstvFecha);
                                                intent.putExtra("hora",obstvHora);
                                                intent.putExtra("direccion",obstvDireccion);
                                                intent.putExtra("subEspecie","Lagarto");
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
                                                intent.putExtra("usuario",obsusuario);
                                                intent.putExtra("especie",obstvEspecie);
                                                intent.putExtra("fecha",obstvFecha);
                                                intent.putExtra("hora",obstvHora);
                                                intent.putExtra("direccion",obstvDireccion);
                                                intent.putExtra("subEspecie","Culebra");
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
                                                intent.putExtra("usuario",obsusuario);
                                                intent.putExtra("especie",obstvEspecie);
                                                intent.putExtra("fecha",obstvFecha);
                                                intent.putExtra("hora",obstvHora);
                                                intent.putExtra("direccion",obstvDireccion);
                                                intent.putExtra("subEspecie","Tortuga");
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
                                                    intent.putExtra("usuario",obsusuario);
                                                    intent.putExtra("especie",obstvEspecie);
                                                    intent.putExtra("fecha",obstvFecha);
                                                    intent.putExtra("hora",obstvHora);
                                                    intent.putExtra("direccion",obstvDireccion);
                                                    intent.putExtra("subEspecie","Roedor");
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
                                                    intent.putExtra("usuario",obsusuario);
                                                    intent.putExtra("especie",obstvEspecie);
                                                    intent.putExtra("fecha",obstvFecha);
                                                    intent.putExtra("hora",obstvHora);
                                                    intent.putExtra("direccion",obstvDireccion);
                                                    intent.putExtra("subEspecie","Felino");
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
                                                    intent.putExtra("usuario",obsusuario);
                                                    intent.putExtra("especie",obstvEspecie);
                                                    intent.putExtra("fecha",obstvFecha);
                                                    intent.putExtra("hora",obstvHora);
                                                    intent.putExtra("direccion",obstvDireccion);
                                                    intent.putExtra("subEspecie","Primate");
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
                                                    intent.putExtra("usuario",obsusuario);
                                                    intent.putExtra("especie",obstvEspecie);
                                                    intent.putExtra("fecha",obstvFecha);
                                                    intent.putExtra("hora",obstvHora);
                                                    intent.putExtra("direccion",obstvDireccion);
                                                    intent.putExtra("subEspecie","Comadreja");
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
                                                    intent.putExtra("usuario",obsusuario);
                                                    intent.putExtra("especie",obstvEspecie);
                                                    intent.putExtra("fecha",obstvFecha);
                                                    intent.putExtra("hora",obstvHora);
                                                    intent.putExtra("direccion",obstvDireccion);
                                                    intent.putExtra("subEspecie","Zariguella");
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
                                                    intent.putExtra("usuario",obsusuario);
                                                    intent.putExtra("especie",obstvEspecie);
                                                    intent.putExtra("fecha",obstvFecha);
                                                    intent.putExtra("hora",obstvHora);
                                                    intent.putExtra("direccion",obstvDireccion);
                                                    intent.putExtra("subEspecie","Zorro");
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
                                                    intent.putExtra("usuario",obsusuario);
                                                    intent.putExtra("especie",obstvEspecie);
                                                    intent.putExtra("fecha",obstvFecha);
                                                    intent.putExtra("hora",obstvHora);
                                                    intent.putExtra("direccion",obstvDireccion);
                                                    intent.putExtra("subEspecie","Oso");
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
                                                        intent.putExtra("usuario",obsusuario);
                                                        intent.putExtra("especie",obstvEspecie);
                                                        intent.putExtra("fecha",obstvFecha);
                                                        intent.putExtra("hora",obstvHora);
                                                        intent.putExtra("direccion",obstvDireccion);
                                                        intent.putExtra("subEspecie","Desconocida");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ir_lista:
                Intent i1 = new Intent(getApplicationContext(),EspeciesActivity.class);
                startActivity(i1);
                break;
            case R.id.btn_ir_menu:
                Intent i2 = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i2);
                break;
        }
    }
}
