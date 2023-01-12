package com.example.proyecto_scros.Objetos;

import android.widget.Toast;

import com.example.proyecto_scros.VerProyecto;

public class Actividad {

    String id_actividad,
            uid_usuario,
            uid_proyecto,
            titulo,
            descripcion,
            fecha_actividad,
            fecha,
            estado;

    public Actividad(){

    }

    public Actividad(String id_actividad, String uid_usuario, String uid_proyecto, String titulo, String descripcion, String fecha_actividad, String fecha, String estado) {
        this.id_actividad = id_actividad;
        this.uid_usuario = uid_usuario;
        this.uid_proyecto = uid_proyecto;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_actividad = fecha_actividad;
        this.fecha = fecha;
        this.estado = estado;
    }

    public String getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(String id_actividad) {
        this.id_actividad = id_actividad;
    }

    public String getUid_usuario() {
        return uid_usuario;
    }

    public void setUid_usuario(String uid_usuario) {
        this.uid_usuario = uid_usuario;
    }

    public String getUid_proyecto() {
        return uid_proyecto;
    }

    public void setUid_proyecto(String uid_proyecto) {
        this.uid_proyecto = uid_proyecto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_actividad() {
        return fecha_actividad;
    }

    public void setFecha_actividad(String fecha_actividad) {
        this.fecha_actividad = fecha_actividad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
