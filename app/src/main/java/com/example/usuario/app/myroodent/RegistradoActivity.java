package com.example.usuario.app.myroodent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * La Clase RegistradoActivity.java se encarga de
 * mostrar y confirmar los datos que el usuario registró
 * anteriormente
 */
public class RegistradoActivity extends AppCompatActivity {

    /**
     * Instancias de las variables que se utilizaron para
     * obtener los datos registrados en la actividad anterior,
     * estos datos se envian a Firebase
     */

    @BindView(R.id.tvNombre) TextView tvNombre;
    @BindView(R.id.tvHora) TextView tvHora;
    @BindView(R.id.tvFecha) TextView tvFecha;
    @BindView(R.id.tvDireccionRegistrada) TextView tvDireccionRegistrada;
    @BindView(R.id.tvSubEspecieRegistrada) TextView tvSubEspecieRegistrada;
    @BindView(R.id.tvEspecieRegistrada) TextView tvEspecieRegistrada;
    @BindView(R.id.regresardRegistros) Button regresardRegistros;
    @BindView(R.id.regresar_Menu) Button regresar_Menu;

    /**
     * Se Instancia el metodo que se encarga de escuchar
     * el estado de autenticacion del usuario y posteriormente
     * se instancia la base de datos donde se guardan lso reportes
     * de cada avistamiento.
     */
    public FirebaseAuth mFirebaseAuth;

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrado);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Metodo para bloquear la rotación de pantalla

        ButterKnife.bind(this); //Libreria ButterKnife
        /**
         * Persistencia de Datos de Firebase
         * Si se completaron registros y no habia conexion
         * a internet en el momento este metodo enviara los datos
         * posteriormente cuando exista una conexión estable a internet.
         */
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder() //Persistencia de Datos
                .setPersistenceEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);

        Locale locale = new Locale("es_419");//Metodo para establecer idioma en algunos dispositivos donde es necesario
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getApplicationContext().getResources().updateConfiguration(config, null);

        Bundle extras = getIntent().getExtras();//Traer datos con Bundle Extras
        final String subEspecie = extras.getString("subEspecie"); //Subespecie que se seleccionó en CompleteActivity.java
        final String data = extras.getString("doc"); //Id del docuemento donde se guardaron los datos de la ventana anterior

        mFirebaseAuth = FirebaseAuth.getInstance();//Instanciar FirebaesAuth

        mFirestore = FirebaseFirestore.getInstance();//Instanciar FireStore

        /**
         * Referenciar Documento donde se añadira campo SubEspecie
         */
        DocumentReference ref = mFirestore.collection("Data")
                .document(mFirebaseAuth.getCurrentUser().getDisplayName())
                .collection("Reportes")
                .document(data);
        /**
         * Condicional que valida el campo SubEspecie
         * y envia el dato SubEspecie seleccionado anteriormente
         */
        if(tvSubEspecieRegistrada!=null){

            ref.update("subEspecie",subEspecie)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Se ha completado el registro con la SubEspecie "+subEspecie+" que acabas de seleccionar", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Mensaje", "Intentelo Nuevamente...", e);
                        }
                    });

        }
        /**
         * Inicialmente se obtuvo el ID del documento que el usuario esta
         * completando y en este metodo se obtienen los campos existentes en
         * ese documento.
         */
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        tvEspecieRegistrada.setText(document.get("especie").toString());
                        tvSubEspecieRegistrada.setText(document.get("subEspecie").toString());
                        tvFecha.setText(document.get("fecha").toString());
                        tvHora.setText(document.get("hora").toString());
                        tvDireccionRegistrada.setText(document.get("direccion").toString());
                        tvNombre.setText(mFirebaseAuth.getCurrentUser().getDisplayName());

                    } else {
                        Log.d("Mensaje", "No se encontró el reporte");
                    }
                } else {
                    Log.d("Mensaje", "Intenta Nuevamente... ", task.getException());
                }
            }
        });

        regresardRegistros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EspeciesActivity.class);
                startActivity(intent);
            }
        });
        regresar_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
