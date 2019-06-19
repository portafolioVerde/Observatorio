package com.example.usuario.app.myroodent;

/**
 * La clase UserInfo.java contiene los parametros y
 * constructores que se usan en la clase MapsActivity.java
 */
public class UserInfo {
    /**
     * Se declaran las variables y posteriormente se generan
     * los constructores de cada parametro que se va a utilizar
     */
    double latitude, longitude;

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

    /**
     * Instancia de los parametros que se usan para enviar las coordenadas a la BD
     */
    public UserInfo(String especieC, double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.especieC = especieC;
    }
}
