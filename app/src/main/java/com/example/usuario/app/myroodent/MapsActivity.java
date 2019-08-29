package com.example.usuario.app.myroodent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * La Clase MapsActivity.java carga los reportes generales de cada usuario
 * desde la base de datos RealtimeDatabase
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener,GoogleMap.OnMapClickListener {

    private static final String TAG = "Mensaje:";
    private static  final int defaultZoom = 10; //Variable para asignar un zoom default en el mapa
    public FirebaseAuth mFirebaseAuth; //Escucha el estado de autenticación del usuario
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance(); //Instancia de la base de datos ddonde se encuentran los reportes
    private GoogleMap mMap; // Instancia de variable tipo GoogleMap
    private ChildEventListener mChildEventListener; //En algun momento se utilizó para unificar las dos bases de datos
/**
 * Instancia de la variable tipo LatLng para ubicar la camara del mapa en una posicion predeterminada
 */
    private LatLngBounds PuertoTriunfo = new LatLngBounds(
            new LatLng(6.003979, -75.021556), new LatLng(6.003979, -75.021556));
    DatabaseReference mUsers; //Referencia de la base datos
    Marker marker; //Instancia de variables tipo Marker para gestionar los marcadores ubicados en el mapa
    @BindView(R.id.btn_Regresar_Home) Button btn_Regresar_Home;
    @BindView(R.id.btn_Regresar_Lista) Button btn_Regresar_Lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Metodo que bloquea la rotación de la pantalla
        ButterKnife.bind(this); //referencia de la libreria ButterKnife

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        /**
         * Metodo persistencia de datos que provee firebase para
         * gestionar lecturas y escrituras de Firebase.
         */
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);
        /**
         * En algunos casos puede ser necesario establecer
         * un idioma este metodo funciona en algunas versiones de
         * Android unicamente.
         */
        /*Locale locale = new Locale("es_419");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getApplicationContext().getResources().updateConfiguration(config, null);*/

        // Obtenga SupportMapFragment y reciba una notificación cuando el mapa esté listo para ser utilizado.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ChildEventListener mChildEventListener;
        /**
         * La variable mUsers referencia donde el documento
         * y la coleccion donde se encuentran almacenados
         * los reportes de localización
         */
        mUsers = FirebaseDatabase.getInstance().getReference("Users/Users/"+mFirebaseAuth.getCurrentUser().getDisplayName());
        mUsers.push().setValue(marker);

        btn_Regresar_Lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Regresar_Lista.setBackgroundResource(R.mipmap.icon_list_white);
                Intent intent = new Intent(getApplicationContext(),EspeciesActivity.class);
                startActivity(intent);
            }
        });

        btn_Regresar_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Regresar_Home.setBackgroundResource(R.mipmap.icon_home_white);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
    /**
     *
     * Metodo onMapReady carga el mapa gracias a la Api de google maps
     * que se solicitó en https://console.cloud.google.com
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnMapClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s: dataSnapshot.getChildren()){

                    UserInfo info = s.getValue(UserInfo.class);

                    LatLng location = new LatLng(info.longitude,info.latitude);

                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_foreground)).anchor(0.0f,1.0f).position(location).title(info.especieC));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PuertoTriunfo.getCenter(), defaultZoom));

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onLocationChanged(Location location) {

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public void onMapClick(LatLng latLng) {

    }
}
