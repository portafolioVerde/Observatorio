package com.example.usuario.app.myroodent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.common.collect.Maps;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Empty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EspeciesActivity extends RegistrosActivity {

    private static final String TAG = "FireLog";
    private RecyclerView mMainList;
    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    public AvistAdapter avistAdapter;
    public List<ReporteEspecie> reporteEspecies;
    Button btn_regresar_home_x;
    Button btn_regresar_map_x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_especies);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btn_regresar_home_x = findViewById(R.id.btn_regresar_home_x);
        btn_regresar_map_x = findViewById(R.id.btn_regresar_map_x);


        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);

        reporteEspecies = new ArrayList<>();
        avistAdapter = new AvistAdapter(reporteEspecies);

        mMainList = (RecyclerView) findViewById(R.id.main_list);

        mMainList.setHasFixedSize(true);
        mMainList.setLayoutManager(new LinearLayoutManager(this));
        mMainList.setAdapter(avistAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        btn_regresar_home_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_regresar_home_x.setBackgroundResource(R.mipmap.icon_home_white);
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);

            }
        });
        btn_regresar_map_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_regresar_map_x.setBackgroundResource(R.mipmap.icon_map_white);
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
            }
        });

        mFirestore.collection("Data")
                .document(mFirebaseAuth.getCurrentUser().getDisplayName())
                .collection("Reportes")
                .whereEqualTo("subEspecie","" )
                .orderBy("id")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots,FirebaseFirestoreException e) {
                        if(e !=null){
                            Log.d(TAG, "Error: "+e.getMessage());
                        }
                        for (final DocumentChange doc : documentSnapshots.getDocumentChanges()){
                            if(doc.getType()== DocumentChange.Type.ADDED){
                                final ReporteEspecie reporteEspecie = doc.getDocument().toObject(ReporteEspecie.class);
                                reporteEspecies.add(reporteEspecie);
                                avistAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}

