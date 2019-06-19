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
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
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
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.data.model.PhoneNumber;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.internal.Sleeper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * La clase MainActivity.java es ventana principal de interacción con el usuario
 *
 */
public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final int RC_FROM_GALLERY = 124;
    private static final String PROVEEDOR_DESCONOCIDO = "Proveedor Desconocido";
    private static final String PASSWORD_FIREBASE = "password";
    private static final String FACEBOOK = "facebook.com";
    private static final String GOOGLE = "google.com";
    private static final String MY_PHOTO_AUTH = "my_photo_auth";
    private static final String PATH_PROFILE = "path_profile";
    private static final String VACIO = "";
    /**
     * Instancia objeto tipo LocationManager.
     */
    public LocationManager ubicacion;
    /**
     * Instancia objeto de tipo LocationManager.
     */
    LocationManager locationManager;
    /**
     * Se utilizaron estas variables en algun momento para
     * mostrar la foto de perfil el nombre y el correo del usuario
     * en esta misma ventana
     */
    /*@BindView(R.id.imgPhotoProfile)
    CircleImageView imgPhotoProfile;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvProvider)
    TextView tvProvider;*/
    /**
     * Instancia sharedPref para guardar los valores de las
     * variables que se definen acontinuación en caso que no exista conexion a internet
     */
    private SharedPreferences sharedPref;
    /**
     * Instancia de las varibles tDireccion tLat tLng que se encuentran invisibles
     * para el usuario ya que no es necesario mostrarlas en el menú incial constantemente
     */
    @BindView(R.id.btnReportar)
    ImageButton continuar;
    @BindView(R.id.textViewDireccion)
    TextView tDireccion;
    @BindView(R.id.tvLat)
    TextView tLat;
    @BindView(R.id.tvLon)
    TextView tLng;
    @BindView(R.id.btn_Lista)
    Button btn_Lista;
    @BindView(R.id.btn_Map)
    ImageButton btn_Map;

    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance(); //Bd donde se almacenan los reportes de cada usuario
    public static final String campoVacio = "campoVacio"; //Se creó esta variable en caso que el GPS no encuentre dirección válida
    public static final AtomicInteger count = new AtomicInteger(1); //Variable tipo entera incrementable para FirebaseFirestore
    public static final AtomicInteger count_01 = new AtomicInteger(1); //Variable tipo entera incrementable para DatabaseRealtime
    public DatabaseReference mDatabase; //BD donde se almacenan las coordenadas de cada reporte
    public FirebaseAuth mFirebaseAuth; //Instancia FirebaseAuth
    public FirebaseAuth.AuthStateListener mAuthStateListener; //Instancia AuthStateListener escucha los cambios de estado de inicio de sesión

    AlertDialog alert = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Metodo para bloquear la rotación de pantalla

        mFirebaseAuth = FirebaseAuth.getInstance(); //Instancia FirebaseAuth
        ButterKnife.bind(this); //Instancia Butterknife
        tDireccion.setText(campoVacio); // Setear la variable tDirección evita el error de enviar lso datos vacios la direccion no puede ser null
        detectarConx(); // Switch para utilizar ultima direccion
        persistenciaDatos(); // Metodo persistencia de datos que provee Firebae
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users/"); //Instancia BD donde se almacenan las coordenadas de cada reporte
        detectarGPSActivo(); //Detecta GPS Activo por medio
        localizacion(); //Solicita permisos COARSE & FINE LOCATION para obtener ubicación precisa
        registrarLocalizacion(); //Obtiene coordenadas y convierte en AddressLine los datos
        //Fabric.with(this, new Crashlytics()); //metodo de crashlytics envia datos en tiempo real a Firebase para detectar posibles fallos
        mAuthStateListener = new FirebaseAuth.AuthStateListener() { //Este metodo identifica el estado de Autenticación
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSetDataUser(user.getDisplayName(), user.getEmail(), user.getProviders() != null ?
                            user.getProviders().get(0) : PROVEEDOR_DESCONOCIDO);
                    //loadImage(user.getPhotoUrl());
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
                            .setLogo(R.drawable.reportar_activo)
                            .build(), RC_SIGN_IN);
                }
            }
        };

        btn_Lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Lista.setBackgroundResource(R.mipmap.icon_list_orange);
                Intent i = new Intent(getApplicationContext(),EspeciesActivity.class);
                //btn_Lista.setBackgroundResource(R.mipmap.icon_list_blue);
                startActivity(i);

            }
        });
        btn_Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Map.setBackgroundResource(R.mipmap.icon_map_orange);
                Intent i =new Intent(getApplicationContext(),MapsActivity.class);
                //btn_Map.setBackgroundResource(R.mipmap.icon_map_blue);
                startActivity(i);

            }
        });
        //detectarConx();
        continuar.setOnClickListener(new View.OnClickListener() { //Metodo para enviar reportes de avistamientos por medio del boton en pantalla
            @Override
            public void onClick(View v) {
                enviarReporte();
            }
        });
    }

/**
 * El metodo dispatchKeyEvent se encarga de escuchar si
 * se presiona el boton central del auricular
 * que se encuentre conectado en el puerto 3.5mm
 */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action ,keycode;
        action = event.getAction();
        keycode = event.getKeyCode();

        switch(keycode){

            case KeyEvent.KEYCODE_HEADSETHOOK:
            {
                if (KeyEvent.ACTION_UP==action){
                    enviarReporte();
                    continuar.setBackgroundResource(R.drawable.reportar_activo);

                    //Toast.makeText(this, "Almorn", Toast.LENGTH_SHORT).show();
                    //enviarReporte();
                    //count++;
                    //String num = String.valueOf(count);
                    //number.setText(num);
                }
                break;
            }
            /*case KeyEvent.KEYCODE_VOLUME_DOWN:
            {
                if(KeyEvent.ACTION_DOWN==action){
                    enviarReporte();
                    continuar.setBackgroundResource(R.drawable.reportar_activo);
                    finish();
                    //count = 0;
                    //String num = String.valueOf(count);
                    //number.setText(num);
                }
                break;
            }*/
        }
        return super.dispatchKeyEvent(event);
    }

    private void detectarGPSActivo() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertNoGps();
        }

    }

    private void AlertNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El GPS se encuentra apagado, ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,@SuppressWarnings("unused")final int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,@SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();

    }

    /**
     * Enviar reporte.
     */
    public void enviarReporte() {
        //continuar.setBackgroundResource(R.drawable.reportar_inactivo);
        //alertaEspecie();
        Locale locale = Locale.getDefault();
        Calendar today = Calendar.getInstance();
        String especieC = "Avistamiento";

        if(tLat.getText().equals("") && tLng.getText().equals("")){
            //Setea LatLng´s del GPS
            //Toast.makeText(this, "HOla", Toast.LENGTH_SHORT).show();

        }else{
            //Setea LatLng´s de SharedPreferences
            SharedPreferences prefs = getSharedPreferences((getString(R.string.preference_file_key)),MODE_PRIVATE);
            String direc = prefs.getString((getString(R.string.direccion_key)),"");
            String nueva_lat = prefs.getString("latitudG","");
            String nueva_lng = prefs.getString("longitudG","");
            tDireccion.setText(direc);
            tLat.setText(nueva_lat);
            tLng.setText(nueva_lng);

        }
        double latitu = Double.valueOf(""+tLat.getText().toString());
        double longitu = Double.valueOf(""+tLng.getText().toString());

        UserInfo userInfo = new UserInfo(especieC,longitu,latitu);
        mDatabase.child("Users/").child(mFirebaseAuth.getCurrentUser().getDisplayName()).child("/"+count_01.getAndIncrement()).setValue(userInfo);

        Map<String, Object> r = new HashMap<>();
        r.put("id", count.getAndIncrement());
        r.put("nombre", mFirebaseAuth.getCurrentUser().getDisplayName());
        r.put("email", mFirebaseAuth.getCurrentUser().getEmail());
        r.put("direccion", tDireccion.getText());
        //r.put("latitud", ""+tLat.getText().toString());
        //r.put("longitud", ""+tLng.getText().toString());

        //r.put("lat",  tLat.getText());
        r.put("hora",  today.get(Calendar.HOUR) + ":" + today.get(Calendar.MINUTE) + " " + today.getDisplayName(Calendar.AM_PM, Calendar.LONG,
                locale));
        r.put("especie", "");
        r.put("subEspecie", "");
        r.put("fecha", today.get(Calendar.DAY_OF_MONTH) + " de " + today.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                locale) + " de " + today.get(Calendar.YEAR));

        mFirestore.collection("Data")
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
                        mFirestore.collection("Data").document(mFirebaseAuth.getCurrentUser()
                                .getDisplayName())
                                .collection("Reportes").document(docRef)
                                .update(e);
                        //Log.d("ingreso de datos", "DocumentSnapshot added with ID: " + documentReference.getId());
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
        Intent intent = new Intent(getApplicationContext(), EspeciesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //sonido notificacion////////////////////////////////////////////////////////////////////////////////////////////////////////
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Observatorio Móvil")
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
        Toast.makeText(this, "Acabaste de reportar un avistamiento cerca a "+tDireccion.getText(), Toast.LENGTH_LONG).show();
    }

    private void alertaEspecie() {

        final AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setMessage("Se reportó la especie que acabas de ver cerca de "+tDireccion.getText().toString()+", deseas completar tu reporte ahora?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,final int which) {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                Intent i = new Intent(getApplicationContext(),EspeciesActivity.class);
                                String doc = getIntent().getStringExtra("doc");// Se obtiene el IdDoc del itemList seleccionado
                                i.putExtra("doc",doc);
                                startActivity(i);
                            }
                        }, 3000);


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,final int id) {

                        continuar.setBackgroundResource(R.drawable.reportar_activo);
                        dialog.cancel();
                    }
                });

        alert = build.create();
        alert.show();

    }

    private void persistenciaDatos() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);
    }

    private void detectarConx() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean hayConexion = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();

        if (hayConexion) {
            tLat.setText(R.string.vacia);
            tLng.setText(R.string.vacia);
            tDireccion.setText(R.string.vacia);

        } else {
            persistenciaDatos();
            SharedPreferences prefs = getSharedPreferences((getString(R.string.preference_file_key)),MODE_PRIVATE);
            if (tDireccion.getText().toString().equals("")){
                String direc = prefs.getString((getString(R.string.direccion_key)),"");
                tDireccion.setText(direc);

            }else{
                tDireccion.setText(getString(R.string.vacia));
                tLat.setText(R.string.vacia);
                tLng.setText(R.string.vacia);


            }


        }

    }
    private void onSignedOutCleanup() {
        onSetDataUser("", "", "");
    }

    private void onSetDataUser(String userName, String email, String provider) {
        //tvUserName.setText(userName);
        //tvEmail.setText(email);

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
     //   tvProvider.setCompoundDrawablesRelativeWithIntrinsicBounds(drawableRes, 0, 0, 0);
       // tvProvider.setText(provider);
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


                                            /*UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
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
                                                    });*/
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

    /*private void loadImage(Uri photoUrl) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();

        Glide.with(MainActivity.this)
                .load(photoUrl)
                .apply(options)
                .into(imgPhotoProfile);

    }*/

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

    /**
     * Se comentó este metodo porque no es necesario
     * mostrar datos personales en la pantalla del usuario
     */
    /*
    @OnClick(R.id.imgPhotoProfile)
    public void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RC_FROM_GALLERY);
    }*/

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
        ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, new MainActivity.milocalizacionListener());
    }



    private class milocalizacionListener implements LocationListener {
        @Override
        @Optional
        public void onLocationChanged(Location location) {

            //System.out.println("La direccion ha cambiado");
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {

                List<Address> direccion = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                tDireccion.setText(direccion.get(0).getAddressLine(0));

                Double lat = location.getLatitude();
                String latS = String.valueOf(lat);
                tLat.setText(latS);

                Double lng = location.getLongitude();
                String lngS = String.valueOf(lng);
                tLng.setText(lngS);

                Context context = getApplicationContext();

                sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.direccion_key), direccion.get(0).getAddressLine(0));
                editor.putString("latitudG",latS);
                editor.putString("longitudG",lngS);
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
