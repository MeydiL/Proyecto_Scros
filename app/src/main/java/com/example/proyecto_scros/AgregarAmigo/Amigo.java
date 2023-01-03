package com.example.proyecto_scros.AgregarAmigo;

public class Amigo {
    String uid, usuario, correo,nombre, apellidoPat, apellidoMat; //usuarios

    String  uid_amigo, usuario_amigo, correo_amigo, nombre_amigo, apePat_amigo, apeMat_amigo; //amigo

    public Amigo() {
    }


    /* --DATOS DE USUARIOS DISPONIBLES EN LA BD*/
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPat() {
        return apellidoPat;
    }

    public void setApellidoPat(String apellidoPat) {
        this.apellidoPat = apellidoPat;
    }

    public String getApellidoMat() {
        return apellidoMat;
    }

    public void setApellidoMat(String apellidoMat) {
        this.apellidoMat = apellidoMat;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    /* ------------------DATOS DE AMIGOS AGREDADOS------------*/

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
