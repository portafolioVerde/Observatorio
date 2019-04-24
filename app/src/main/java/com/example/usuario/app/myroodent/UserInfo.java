package com.example.usuario.app.myroodent;

public class UserInfo {
    public double latitude,longitude;
    String direccionC;

    public UserInfo(){

    }

    public UserInfo(String direccionC,double latitude,double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.direccionC = direccionC;
    }
}
