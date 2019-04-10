package com.example.usuario.app.myroodent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Empty;

import java.util.ArrayList;
import java.util.List;

public class EspeciesActivity extends RegistrosActivity {

    private static final String TAG = "FireLog";
    private RecyclerView mMainList;
    private FirebaseFirestore mFirestore;
    public AvistAdapter avistAdapter;
    public List<ReporteEspecie> reporteEspecies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_especies);

        reporteEspecies = new ArrayList<>();
        avistAdapter = new AvistAdapter(reporteEspecies);

        mMainList = (RecyclerView) findViewById(R.id.main_list);

        mMainList.setHasFixedSize(true);
        mMainList.setLayoutManager(new LinearLayoutManager(this));
        mMainList.setAdapter(avistAdapter);

        mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("Data")
                .document(mFirebaseAuth.getCurrentUser().getDisplayName())
                .collection("Reportes")
                .whereEqualTo("subEspecie","" )
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

    public void ir_menu(View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}

