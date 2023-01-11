package com.example.proyecto_scros.Objetos;

public class Amigo {

    String  uid_amigo, usuario_amigo, correo_amigo, nombre_amigo, apePat_amigo, apeMat_amigo; //amigo

    public Amigo() {
    }

    public Amigo(String uid_amigo, String usuario_amigo, String correo_amigo, String nombre_amigo, String apePat_amigo, String apeMat_amigo) {
        this.uid_amigo = uid_amigo;
        this.usuario_amigo = usuario_amigo;
        this.correo_amigo = correo_amigo;
        this.nombre_amigo = nombre_amigo;
        this.apePat_amigo = apePat_amigo;
        this.apeMat_amigo = apeMat_amigo;
    }

    public String getUid_amigo() {
        return uid_amigo;
    }

    public void setUid_amigo(String uid_amigo) {
        this.uid_amigo = uid_amigo;
    }

    public String getUsuario_amigo() {
        return usuario_amigo;
    }

    public void setUsuario_amigo(String usuario_amigo) {
        this.usuario_amigo = usuario_amigo;
    }

    public String getCorreo_amigo() {
        return correo_amigo;
    }

    public void setCorreo_amigo(String correo_amigo) {
        this.correo_amigo = correo_amigo;
    }

    public String getNombre_amigo() {
        return nombre_amigo;
    }

    public void setNombre_amigo(String nombre_amigo) {
        this.nombre_amigo = nombre_amigo;
    }

    public String getApePat_amigo() {
        return apePat_amigo;
    }

    public void setApePat_amigo(String apePat_amigo) {
        this.apePat_amigo = apePat_amigo;
    }

    public String getApeMat_amigo() {
        return apeMat_amigo;
    }

    public void setApeMat_amigo(String apeMat_amigo) {
        this.apeMat_amigo = apeMat_amigo;
    }
}
