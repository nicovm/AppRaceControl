package com.nifa.racecontrol.Model;

import java.io.Serializable;

/**
 * Created by Bringa on 30/06/2016.
 */
public class Torneo implements Serializable {

    private int idTorneo;
    private String nombre;
    private String fecha;
    private int idCliente;



    public int getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(int idTorneo) {
        this.idTorneo = idTorneo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}
