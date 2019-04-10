package com.example.usuario.app.myroodent;

import android.support.v7.app.AppCompatActivity;

import java.util.Date;

import static java.text.DateFormat.getDateInstance;

public class ReporteEspecie {

    String email,direccion,especie,nombre,doc;
    //Integer id;
    Long id;
    /*int image;*/
    String fechaYhora;

    public ReporteEspecie(){

    }

    public ReporteEspecie(/*int image,*/String doc,Long id, String email, String direccion, String especie, String nombre, String fechaYhora) {
        this.doc = doc;
        this.id = id;
        this.email = email;
        this.direccion = direccion;
        this.especie = especie;
        this.nombre = nombre;
        this.fechaYhora = fechaYhora;
        //this.image = image;
    }
    public String getDoc(){
        return doc;
    }

    public void setDoc(String doc){
        this.doc = doc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaYhora() {

        return fechaYhora;
    }

    public void setFechaYhora(String fechaYhora) {

        this.fechaYhora = fechaYhora;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }





    /*public int getImage(){
        return  getImage();
    }
    public void setImage(int image){
        this.image = image;
    }*/



}

