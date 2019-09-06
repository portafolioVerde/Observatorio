package com.example.usuario.app.myroodent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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

/**
 * La Clase RegistradoActivity.java se encarga de
 * mostrar y confirmar los datos que el usuario registró
 * anteriormente
 */
public class RegistradoActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * Instancias de las variables que se utilizaron para
     * obtener los datos registrados en la actividad anterior,
     * estos datos se envian a Firebase
     */
    TextView tvNombre,tvHora,tvFecha,tvDireccionRegistrada,tvSubEspecieRegistrada,tvEspecieRegistrada;
    Button regresardRegistros,regresar_Menu;
    /**
     * Se Instancia el metodo que se encarga de escuchar
     * el estado de autenticacion del usuario y posteriormente
     * se instancia la base de datos donde se guardan lso reportes
     * de cada avistamiento.
     */
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore mFirestore;
    Bitmap background;
    BitmapDrawable bg;
    RelativeLayout bgimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrado);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Metodo para bloquear la rotación de pantalla

        tvNombre = findViewById(R.id.tvNombre);
        tvHora = findViewById(R.id.tvHora);
        tvFecha = findViewById(R.id.tvFecha);
        tvDireccionRegistrada = findViewById(R.id.tvDireccionRegistrada);
        tvSubEspecieRegistrada = findViewById(R.id.tvSubEspecieRegistrada);
        tvEspecieRegistrada = findViewById(R.id.tvEspecieRegistrada);

        regresardRegistros = findViewById(R.id.regresardRegistros);
        regresardRegistros.setOnClickListener(this);
        regresar_Menu = findViewById(R.id.regresar_Menu);
        regresar_Menu.setOnClickListener(this);

        persistenciaDatos();
        establecerIdioma();
        traerDatos();
    }

    private void persistenciaDatos() {
        /*
         * Persistencia de Datos de Firebase
         * Si se completaron registros y no habia conexion
         * a internet en el momento este metodo enviara los datos
         * posteriormente cuando exista una conexión estable a internet.
         */
        //mFirebaseAuth = FirebaseAuth.getInstance();//Instanciar FirebaesAuth
        mFirestore = FirebaseFirestore.getInstance();//Instanciar FireStore
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder() //Persistencia de Datos
                .setPersistenceEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);
    }

    private void establecerIdioma() {
        Locale locale = new Locale("es_419");//Metodo para establecer idioma en algunos dispositivos donde es necesario
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getApplicationContext().getResources().updateConfiguration(config, null);
    }

    private void traerDatos() {
        //Bundle extras = getIntent().getExtras();//Traer datos con Bundle Extras
        //final String subEspecie = extras.getString("subEspecie"); //Subespecie que se seleccionó en CompleteActivity.java
        //final String data = extras.getString("doc"); //Id del docuemento donde se guardaron los datos de la ventana anterior

        final String data = getIntent().getExtras().getString("doc");
        final String subEspecie = getIntent().getExtras().getString("subEspecie");
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
                            //Log.w("Mensaje", "Intentelo Nuevamente...", e);
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
                        //Log.d("Mensaje", "No se encontró el reporte");
                    }
                } else {
                    //Log.d("Mensaje", "Intenta Nuevamente... ", task.getException());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regresar_Menu:
                Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.regresardRegistros:
                Intent intent2 = new Intent(getApplicationContext(),EspeciesActivity.class);
                startActivity(intent2);
                break;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        //setBackground();

    }

    /*private void setBackground(RelativeLayout i, int sourceid) {
        unloadBackground();
        bgimg = i;
        loadBackground(sourceid);
    }*/
    public void loadBackground(int id) {
        background = BitmapFactory.decodeStream(getResources().openRawResource(id));
        bg = new BitmapDrawable(background);
        bgimg.setBackgroundDrawable(bg);
    }
}
