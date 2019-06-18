package com.example.usuario.app.myroodent;

import com.google.firebase.database.IgnoreExtraProperties;

public class UserInfo {
    double latitude,longitude;
    String especieC;

    public UserInfo(){

    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEspecieC() {
        return especieC;
    }

    public void setEspecieC(String especieC) {
        this.especieC = especieC;
    }

    public UserInfo(String especieC, double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.especieC = especieC;
    }
}
