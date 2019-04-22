package com.example.usuario.app.myroodent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
public class Main2Activity extends AppCompatActivity {

    Button btnAlerta;
    ImageButton btnVolver;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);

        btnVolver = findViewById(R.id.btnVolver);
        btnAlerta = findViewById(R.id.btnAlerta);

        mFirebaseAuth = FirebaseAuth.getInstance();

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btnAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAlerta.setBackgroundResource(R.drawable.botonmuerto);
                Intent intent = new Intent(Main2Activity.this,RegistrosActivity.class);
                startActivity(intent);
            }
        });
    }
}
