package com.example.usuario.app.myroodent;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MyApp extends Application {
    private ImageView img; // the image
    private RelativeLayout bgimg; // layout of the activity
    private Bitmap nav; // the image in the Bitmap format
    private Bitmap background; // background in the Bitmap format
    private BitmapDrawable bg; // background in the Drawable format
}
