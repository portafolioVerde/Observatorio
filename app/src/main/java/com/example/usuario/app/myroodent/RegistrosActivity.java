package com.example.usuario.app.myroodent;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.LocaleList;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.cert.Extension;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static java.util.stream.DoubleStream.of;

public class RegistrosActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final int RC_FROM_GALLERY = 124;
    private static final String PROVEEDOR_DESCONOCIDO = "Proveedor Desconocido";
    private static final String PASSWORD_FIREBASE = "password";
    private static final String FACEBOOK = "facebook.com";
    private static final String GOOGLE = "google.com";
    private static final String MY_PHOTO_AUTH = "my_photo_auth";
    private static final String PATH_PROFILE = "path_profile";
    public LocationManager ubicacion;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private AvistAdapter avistAdapter;

    private static final AtomicInteger count = new AtomicInteger(1);

    public FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @BindView(R.id.btnVolver)
    ImageButton btnVolver;

    @BindView(R.id.btnAnfibio)
    ImageView btnAnfibio;
    @BindView(R.id.btnAve)
    ImageView btnAve;
    @BindView(R.id.btnMamifero)
    ImageView btnMamifero;
    @BindView(R.id.btnReptil)
    ImageView btnReptil;
    @BindView(R.id.btnDesc)
    ImageView btnDesc;
    @BindView(R.id.txtDireccion)
    TextView tDireccion;
    @BindView(R.id.txtLat)
    TextView tLat;
    @BindView(R.id.txtLng)
    TextView tLng;

    TextView txtLat,txtLng;

    private List<ReporteEspecie> reporteEspecie;

    LocationManager locationManager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AlertDialog alert = null;

    public static final String PREFERENCIAS = "MyPrefs";
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);



        //getDatos(Locale.getDefault());
        checkIfLocationOpened();

        ////////////////Persistencia sin conexion FireStore///////////////////////////
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        ///////////////////////////////////////////////////////////////////////////////

        ParoImpar();

        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        registrarLocalizacion();
        localizacion();

        detectarConx();
    }//FinOnCreate

    private void detectarConx() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean hayConexion = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();

        if (hayConexion) {

        } else {
            SharedPreferences prefs = getSharedPreferences((getString(R.string.preference_file_key)),MODE_PRIVATE);
            String direc = prefs.getString((getString(R.string.direccion_key)),"");
            tDireccion.setText(direc);
        }

    }

    private boolean checkIfLocationOpened() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        System.out.println("Provider contains=> " + provider);
        if (provider.contains("gps") || provider.contains("network")){
            //Toast.makeText(getApplicationContext(), "Ya se encuentra activo el GPS", Toast.LENGTH_SHORT).show();
        }else{
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("El GPS se encuentra apagado, ¿Desea activarlo?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            alert = builder.create();
            alert.show();
        }
        return false;

    }

    public void AlertNoGps() {

    }

    public void ConvertidordeCoordenadas() {

        GPStracker g = new GPStracker(getApplicationContext());


        Location l = g.getLocation();
        if (l != null) {
            double lat = l.getLatitude();
            double lon = l.getLongitude();

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> direccion = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
                //System.out.println(direccion.get(0).getAddressLine(0));

                //tDireccion.setText(direccion.get(0).getAddressLine(0));

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Sin GPS", Toast.LENGTH_SHORT).show();
        }

    }

    //Metodo para detectar id par o impar
    public void ParoImpar() {
        if (count.get() % 2 == 0) {
            //Log.d("ParoImpar","= Par");
        } else {
            //Log.d("ParoImpar","= Impar");
        }

    }

    //Metodo para detectar gps activo


    //Metodo que obtiene fecha y hora
    public void times(View v) {

    }

    //Metodo Anfibio
    public void onAnfibio(View view) {
        Locale locale = Locale.getDefault();
        Calendar today = Calendar.getInstance();

        Map<String, Object> r = new HashMap<>();
        r.put("id", count.getAndIncrement());
        r.put("nombre", mFirebaseAuth.getCurrentUser().getDisplayName());
        r.put("email", mFirebaseAuth.getCurrentUser().getEmail());
        r.put("direccion", tDireccion.getText());
        r.put("lat",  tLat.getText());
        r.put("lng",  tLng.getText());
        r.put("especie", "Anfibio");
        r.put("subEspecie", "");
        r.put("fechaYhora", today.get(Calendar.DAY_OF_MONTH) + " de " + today.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                locale) + " de " + today.get(Calendar.YEAR) + " a las " + today.get(Calendar.HOUR) + ":" + today.get(Calendar.MINUTE) + " " + today.getDisplayName(Calendar.AM_PM, Calendar.LONG,
                locale));

        db.collection("Data")
                .document(mFirebaseAuth.getCurrentUser()
                        .getDisplayName())
                .collection("Reportes")
                .add(r)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        String docRef = documentReference.getId();//Envio del id del documento creado
                        Map<String, Object> e = new HashMap<>();
                        e.put("doc", docRef);
                        db.collection("Data").document(mFirebaseAuth.getCurrentUser()
                                .getDisplayName())
                                .collection("Reportes").document(docRef)
                                .update(e);
                        Log.d("ingreso de datos", "DocumentSnapshot added with ID: " + documentReference.getId());
                        /////////////////////Se guarda el documento seleccionado
                        /*SharedPreferences pref = getSharedPreferences("PREFS",Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("DOCREF",docRef.toString() );
                        edit.commit();*/
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ingreso de datos", "Error adding document", e);
                    }
                });


        //notificacion push//////////////////////////////////////////////////////////////////////////////////////////////////////
        Intent intent = new Intent(this, EspeciesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //sonido notificacion////////////////////////////////////////////////////////////////////////////////////////////////////////
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Ingresa a MyRoodent")
                .setContentText("Tienes reportes por completar!! ")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.normal_channel_id);
            String channelName = getString(R.string.normal_channel_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 200, 50});
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            notificationBuilder.setChannelId(channelId);
        }

        if (notificationManager != null) {
            notificationManager.notify("", 0, notificationBuilder.build());

        }
        btnAnfibio.setBackgroundResource(R.drawable.anfibioactivo);//Cambio de boton

        Intent i = new Intent(this, Main2Activity.class);
        i.putExtra("ultimaDireccion",tDireccion.getText());
        startActivity(i);
    }

    //Metodo Mamifero
    public void onMamifero(View view) {
        Locale locale = Locale.getDefault();
        Calendar today = Calendar.getInstance();
        Map<String, Object> r = new HashMap<>();
        r.put("id", count.getAndIncrement());
        r.put("nombre", mFirebaseAuth.getCurrentUser().getDisplayName());
        r.put("email", mFirebaseAuth.getCurrentUser().getEmail());
        r.put("direccion", tDireccion.getText());
        r.put("lat",  tLat.getText());
        r.put("lng",  tLng.getText());
        r.put("especie", "Mamifero");
        r.put("subEspecie", "");
        r.put("fechaYhora", today.get(Calendar.DAY_OF_MONTH) + " de " + today.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                locale) + " de " + today.get(Calendar.YEAR) + " a las " + today.get(Calendar.HOUR) + ":" + today.get(Calendar.MINUTE) + " " + today.getDisplayName(Calendar.AM_PM, Calendar.LONG,
                locale));
        // Add a new document with a generated ID
        db.collection("Data")
                .document(mFirebaseAuth.getCurrentUser()
                        .getDisplayName())
                .collection("Reportes")
                .add(r)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String docRef = documentReference.getId();//Envio del id del documento creado
                        Map<String, Object> e = new HashMap<>();
                        e.put("doc", docRef);
                        db.collection("Data").document(mFirebaseAuth.getCurrentUser()
                                .getDisplayName())
                                .collection("Reportes").document(docRef)
                                .update(e);
                        Log.d("ingreso de datos", "DocumentSnapshot added with ID: " + documentReference.getId());
                        /////////////////////Se guarda el documento seleccionado
                        /*SharedPreferences pref = getSharedPreferences("PREFS",Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("DOCREF",docRef.toString() );
                        edit.commit();*/
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ingreso de datos", "Error adding document", e);
                    }
                });
        Intent intent = new Intent(this, EspeciesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //sonido notificacion
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Ingresa a MyRoodent")
                .setContentText("Tienes reportes por completar!! ")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.normal_channel_id);
            String channelName = getString(R.string.normal_channel_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 200, 50});
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            notificationBuilder.setChannelId(channelId);
        }

        if (notificationManager != null) {
            notificationManager.notify("", 0, notificationBuilder.build());

        }
        btnMamifero.setBackgroundResource(R.drawable.mamiferoactivo);
        Toast.makeText(this, "Se agregó la direccion donde acabas de ver un Mamífero", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, Main2Activity.class);
        startActivity(i);


    }

    //Metodo Ave
    public void onAve(View view) {
        Locale locale = Locale.getDefault();
        Calendar today = Calendar.getInstance();
        Map<String, Object> r = new HashMap<>();
        r.put("id", count.getAndIncrement());
        r.put("nombre", mFirebaseAuth.getCurrentUser().getDisplayName());
        r.put("email", mFirebaseAuth.getCurrentUser().getEmail());
        r.put("direccion", tDireccion.getText());
        r.put("lat",  tLat.getText());
        r.put("lng",  tLng.getText());
        r.put("especie", "Ave");
        r.put("subEspecie", "");
        r.put("fechaYhora", today.get(Calendar.DAY_OF_MONTH) + " de " + today.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                locale) + " de " + today.get(Calendar.YEAR) + " a las " + today.get(Calendar.HOUR) + ":" + today.get(Calendar.MINUTE) + " " + today.getDisplayName(Calendar.AM_PM, Calendar.LONG,
                locale));

        // Add a new document with a generated ID
        db.collection("Data")
                .document(mFirebaseAuth.getCurrentUser()
                        .getDisplayName())
                .collection("Reportes")
                .add(r)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String docRef = documentReference.getId();//Envio del id del documento creado
                        Map<String, Object> e = new HashMap<>();
                        e.put("doc", docRef);
                        db.collection("Data").document(mFirebaseAuth.getCurrentUser()
                                .getDisplayName())
                                .collection("Reportes").document(docRef)
                                .update(e);
                        /*SharedPreferences pref = getSharedPreferences("PREFS",Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("DOCREF",docRef.toString() );
                        edit.commit();*/
                        Log.d("ingreso de datos", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ingreso de datos", "Error adding document", e);
                    }
                });
        Intent intent = new Intent(this, EspeciesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //sonido notificacion
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Ingresa a MyRoodent")
                .setContentText("Tienes reportes por completar!!")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.normal_channel_id);
            String channelName = getString(R.string.normal_channel_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 200, 50});
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            notificationBuilder.setChannelId(channelId);
        }

        if (notificationManager != null) {
            notificationManager.notify("", 0, notificationBuilder.build());

        }
        btnAve.setBackgroundResource(R.drawable.aveactiva);
        Toast.makeText(this, "Se agregó la direccion donde acabas de ver un Ave", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, Main2Activity.class);
        startActivity(i);


    }

    //Metodo Reptil
    public void onReptil(View view) {
        Locale locale = Locale.getDefault();
        Calendar today = Calendar.getInstance();
        Map<String, Object> r = new HashMap<>();
        r.put("id", count.getAndIncrement());
        r.put("nombre", mFirebaseAuth.getCurrentUser().getDisplayName());
        r.put("email", mFirebaseAuth.getCurrentUser().getEmail());
        r.put("direccion", tDireccion.getText());
        r.put("lat",  tLat.getText());
        r.put("lng",  tLng.getText());
        r.put("especie", "Reptil");
        r.put("subEspecie", "");
        r.put("fechaYhora", today.get(Calendar.DAY_OF_MONTH) + " de " + today.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                locale) + " de " + today.get(Calendar.YEAR) + " a las " + today.get(Calendar.HOUR) + ":" + today.get(Calendar.MINUTE) + " " + today.getDisplayName(Calendar.AM_PM, Calendar.LONG,
                locale));

        // Add a new document with a generated ID
        db.collection("Data")
                .document(mFirebaseAuth
                        .getCurrentUser()
                        .getDisplayName())
                .collection("Reportes")
                .add(r)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String docRef = documentReference.getId();//Envio del id del documento creado
                        Map<String, Object> e = new HashMap<>();
                        e.put("doc", docRef);
                        db.collection("Data").document(mFirebaseAuth.getCurrentUser()
                                .getDisplayName())
                                .collection("Reportes").document(docRef)
                                .update(e);
                        /*SharedPreferences pref = getSharedPreferences("PREFS",Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("DOCREF",docRef.toString() );
                        edit.commit();*/
                        Log.d("ingreso de datos", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ingreso de datos", "Error adding document", e);
                    }
                });
        Intent intent = new Intent(this, EspeciesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //sonido notificacion
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Ingresa a MyRoodent")
                .setContentText("Tienes reportes por completar!! ")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.normal_channel_id);
            String channelName = getString(R.string.normal_channel_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 200, 50});
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            notificationBuilder.setChannelId(channelId);
        }

        if (notificationManager != null) {
            notificationManager.notify("", 0, notificationBuilder.build());

        }
        btnReptil.setBackgroundResource(R.drawable.reptilactivo);
        Toast.makeText(this, "Se agregó la direccion donde acabas de ver un Reptil", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, Main2Activity.class);
        startActivity(i);


    }

    //Metodo Especie Desconocida
    public void onDesc(View view) {
        Locale locale = Locale.getDefault();
        Calendar today = Calendar.getInstance();
        Map<String, Object> r = new HashMap<>();
        r.put("id", count.getAndIncrement());
        r.put("nombre", mFirebaseAuth.getCurrentUser().getDisplayName());
        r.put("email", mFirebaseAuth.getCurrentUser().getEmail());
        r.put("direccion", tDireccion.getText());
        r.put("lat",  tLat.getText());
        r.put("lng",  tLng.getText());
        r.put("especie", "Desconocida");
        r.put("fechaYhora", today.get(Calendar.DAY_OF_MONTH) + " de " + today.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                locale) + " de " + today.get(Calendar.YEAR) + " a las " + today.get(Calendar.HOUR) + ":" + today.get(Calendar.MINUTE) + " " + today.getDisplayName(Calendar.AM_PM, Calendar.LONG,
                locale));


        // Add a new document with a generated ID
        db.collection("Data")
                .document(mFirebaseAuth.getCurrentUser().getDisplayName())
                .collection("Reportes Desconocidos")
                .add(r)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String docRef = documentReference.getId();//Envio del id del documento creado
                        Map<String, Object> e = new HashMap<>();
                        e.put("doc", docRef);
                        db.collection("Data").document(mFirebaseAuth.getCurrentUser()
                                .getDisplayName())
                                .collection("Reportes").document(docRef)
                                .update(e);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ingreso de datos", "Error adding document", e);
                    }
                });
        Intent intent = new Intent(this, EspeciesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //sonido notificacion
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Ingresa a MyRoodent")
                .setContentText("Tienes reportes por completar!! ")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.normal_channel_id);
            String channelName = getString(R.string.normal_channel_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 200, 50});
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            notificationBuilder.setChannelId(channelId);
        }

        if (notificationManager != null) {
            notificationManager.notify("", 0, notificationBuilder.build());

        }
        btnDesc.setBackgroundResource(R.drawable.animaldesconocidoactivo);
        Toast.makeText(this, "Se agregó la direccion donde acabas de ver una especie Desconocida", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, Main2Activity.class);
        startActivity(i);


    }

    //Metodo que obtiene ubicacion
    private void localizacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);
        }
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = ubicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);

    }

    //Metodo que actualiza ubicacion en tiempo real
    private void registrarLocalizacion() {
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, new milocalizacionListener());
    }

    //Metodo para volver a la actividad anterior
    public void devolverMenu(View view) {
        Intent i = new Intent(this, Main2Activity.class);
        startActivity(i);
    }

    //Convertidor Geocoder para obtener direccion precisa
    private class milocalizacionListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {

            //System.out.println("La direccion ha cambiado");
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> direccion = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                Double lat = direccion.get(0).getLatitude();
                Double lng = direccion.get(0).getLongitude();
                String latS = String.valueOf(lat);
                String lngS = String.valueOf(lng);

                tDireccion.setText(direccion.get(0).getAddressLine(0));

                tLat.setText(latS);
                tLng.setText(lngS);

                Context context = getApplicationContext();

                sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.direccion_key), direccion.get(0).getAddressLine(0));
                editor.apply();

            } catch (IOException e) {
                e.printStackTrace();
            }

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
    }
}

