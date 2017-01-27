package com.nifa.racecontrol.Model;

import java.util.Date;

/**
 * Created by Bringa on 25/07/2016.
 */
public class Tecnica {

    public long idTecnica ;
    public long idPiloto ;
    public long idCarrera ;
    public String observacion;
    public Date fecha ;


    public long getIdTecnica() {
        return idTecnica;
    }

    public void setIdTecnica(long idTecnica) {
        this.idTecnica = idTecnica;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public long getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(long idCarrera) {
        this.idCarrera = idCarrera;
    }

    public long getIdPiloto() {
        return idPiloto;
    }

    public void setIdPiloto(long idPiloto) {
        this.idPiloto = idPiloto;
    }
}
