package com.example.usuario.app.myroodent;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final int RC_FROM_GALLERY = 124;

    private static final String PROVEEDOR_DESCONOCIDO = "Proveedor Desconocido";
    private static final String PASSWORD_FIREBASE = "password";
    private static final String FACEBOOK = "facebook.com";
    private static final String GOOGLE = "google.com";

    private static final String MY_PHOTO_AUTH = "my_photo_auth";
    private static final String PATH_PROFILE = "path_profile";
    public LocationManager ubicacion;

    @BindView(R.id.imgPhotoProfile)
    CircleImageView imgPhotoProfile;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvProvider)
    TextView tvProvider;
    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    ImageButton continuar, regresar;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);

        continuar = findViewById(R.id.btnSi);
        regresar = findViewById(R.id.btnNo);
        ButterKnife.bind(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSetDataUser(user.getDisplayName(), user.getEmail(), user.getProviders() != null ?
                            user.getProviders().get(0) : PROVEEDOR_DESCONOCIDO);
                    loadImage(user.getPhotoUrl());
                } else {
                    onSignedOutCleanup();
                    AuthUI.IdpConfig facebookIdp = new AuthUI.IdpConfig.FacebookBuilder()
                            .setPermissions(Arrays.asList("user_friends", "user_gender"))
                            .build();
                    AuthUI.IdpConfig googleIdp = new AuthUI.IdpConfig.GoogleBuilder()
                            //.setScopes(Arrays.asList(Scopes.GAMES))
                            .build();
                    startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setTosUrl("http://databaseremote.esy.es/RegisterLite/html/privacidad.html")
                            .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),
                                    facebookIdp, googleIdp))
                            .setTheme(R.style.GreenTheme)
                            .setLogo(R.drawable.mamiferoactivo)
                            .build(), RC_SIGN_IN);
                }
            }
        };

    }

    private void onSignedOutCleanup() {
        onSetDataUser("", "", "");
    }

    private void onSetDataUser(String userName, String email, String provider) {
        tvUserName.setText(userName);
        tvEmail.setText(email);

        int drawableRes;
        switch (provider) {
            case PASSWORD_FIREBASE:
                drawableRes = R.drawable.ic_firebase;
                break;
            case FACEBOOK:
                drawableRes = R.drawable.ic_facebook_box;
                break;
            case GOOGLE:
                drawableRes = R.drawable.ic_google_plus;
                break;
            default:
                drawableRes = R.drawable.ic_block_helper;
                provider = PROVEEDOR_DESCONOCIDO;
                break;
        }
        tvProvider.setCompoundDrawablesRelativeWithIntrinsicBounds(drawableRes, 0, 0, 0);
        tvProvider.setText(provider);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Bienvenido... ", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Algo falló, intente de nuevo.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == RC_FROM_GALLERY && resultCode == RESULT_OK) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference reference = storage.getReference().child(PATH_PROFILE).child(MY_PHOTO_AUTH);
            Uri selectedImageUri = data.getData();

            if (selectedImageUri != null) {
                reference.putFile(selectedImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (user != null) {


                                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                                    .setPhotoUri(uri)
                                                    .build();
                                            user.updateProfile(request)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                loadImage(user.getPhotoUrl());
                                                            }

                                                        }
                                                    });
                                        }
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(MainActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private void loadImage(Uri photoUrl) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();

        Glide.with(MainActivity.this)
                .load(photoUrl)
                .apply(options)
                .into(imgPhotoProfile);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @OnClick(R.id.imgPhotoProfile)
    public void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RC_FROM_GALLERY);
    }

    public void noContinuar(View view) {
        regresar.setBackgroundResource(R.drawable.nohover);
        onBackPressed();
    }

    public void siContinuar(View view) {
        Intent i = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(i);
        finish();
    }


    public void regresarEspecies(View view) {
        Intent o = new Intent(this,EspeciesActivity.class);
        startActivity(o);
    }

    public void ir_mapa(View view) {
        Intent o = new Intent(this,MapsActivity.class);
        startActivity(o);
    }
}
