package com.nifa.racecontrol.Model;

/**
 * Created by Bringa on 04/08/2016.
 */
public class Precintado {

    public int idTecnica ;
    public int idPrecintado;
    public String nombre ;
    public int numero ;


    public int getIdTecnica() {
        return idTecnica;
    }

    public void setIdTecnica(int idTecnica) {
        this.idTecnica = idTecnica;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdPrecintado() {
        return idPrecintado;
    }

    public void setIdPrecintado(int idPrecintado) {
        this.idPrecintado = idPrecintado;
    }
}
