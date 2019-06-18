package com.example.usuario.app.myroodent;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener,GoogleMap.OnMapClickListener {

    private static final String TAG = "Mensaje:";
    private static  final int defaultZoom = 10;
    public FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private GoogleMap mMap;
    private ChildEventListener mChildEventListener;
    private LatLngBounds PuertoTriunfo = new LatLngBounds(
            new LatLng(6.003979, -75.021556), new LatLng(6.003979, -75.021556));
    DatabaseReference mUsers;
    Marker marker;
    @BindView(R.id.btn_Regresar_Home)
    Button btn_Regresar_Home;
    @BindView(R.id.btn_Regresar_Lista)
    Button btn_Regresar_Lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        //////////////////////////////////////////////
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);
        //////////////////////////////////////////////


        Locale locale = new Locale("es_419");//Convertidor de idioma local
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getApplicationContext().getResources().updateConfiguration(config, null);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ChildEventListener mChildEventListener;
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

                    //Log.d("Mensaje","Coordenadas: "+location);
                    //mMap.addMarker(new MarkerOptions().position(location).title(info.especieC))
                      //      .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
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

    public void btn_RegresarMap(View view) {


    }

    public void btn_RegresaraRegistros(View view) {

        Intent intent = new Intent(this,RegistrosActivity.class);
        startActivity(intent);
    }

    public void btn_RegresaraReportesLista(View view) {

    }

    public void aumentar_zoom(View view) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PuertoTriunfo.getCenter(), defaultZoom+2));
    }

    public void disminuir_zoom(View view) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PuertoTriunfo.getCenter(), defaultZoom-2));
    }
}
