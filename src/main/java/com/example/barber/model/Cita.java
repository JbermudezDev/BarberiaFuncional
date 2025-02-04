package com.example.barber.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Cita {
    private String cedula;
    private String nombre;
    private String servicio;
    private LocalDate fecha;
    private LocalTime hora;

    // Constructor, getters y setters
    public Cita(String cedula, String nombre, String servicio, LocalDate fecha, LocalTime hora) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.servicio = servicio;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
}