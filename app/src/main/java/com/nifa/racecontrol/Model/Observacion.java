package com.nifa.racecontrol.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Bringa on 18/09/2016.
 */
public class Observacion implements Parcelable {


    private long idObservacion ;
    public long idRevision;
    private long idRevisionOk;
    private boolean ok ;
    private String descripcion;
    private Date fecha;
    private Date fechaOk;
    private long idUsuario;
    private long idUsuarioOk;
    private long idCarrera;
    private Carrera Carrera;


    public Observacion(String descripcion, long idObservacion, long idRevision, Date fecha, long idUsuario, Carrera carrera) {
        this.descripcion = descripcion;
        this.idObservacion = idObservacion;
        this.idRevision = idRevision;
        this.fecha = fecha;
        this.idUsuario = idUsuario;
        this.Carrera = carrera;
        this.idCarrera = carrera.getIdCarrera();
    }

    public Observacion(Parcel in) {

        idObservacion = in.readLong();
        idRevision = in.readLong();
        idRevisionOk = in.readLong();
        ok = in.readByte() != 0;
        descripcion = in.readString();
        idUsuario = in.readLong();
        idUsuarioOk = in.readLong();
        Carrera = (Carrera) in.readValue(Carrera.class.getClassLoader());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(getIdObservacion());
        dest.writeLong(getIdRevision());
        dest.writeLong(getIdRevisionOk());
        dest.writeInt(isOk()? 1:0);
        dest.writeString(getDescripcion());
        dest.writeLong(getIdUsuario());
        dest.writeLong(getIdUsuarioOk());
        dest.writeValue(Carrera);

    }

    public static final Creator<Observacion> CREATOR = new Creator<Observacion>() {
        @Override
        public Observacion createFromParcel(Parcel source) {
            return new Observacion(source);
        }

        @Override
        public Observacion[] newArray(int size) {
            return new Observacion[size];
        }
    };


    public long getIdObservacion() {
        return idObservacion;
    }

    public void setIdObservacion(long idObservacion) {
        this.idObservacion = idObservacion;
    }

    public long getIdUsuarioOk() {
        return idUsuarioOk;
    }

    public void setIdUsuarioOk(long idUsuarioOk) {
        this.idUsuarioOk = idUsuarioOk;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFechaOk() {
        return fechaOk;
    }

    public void setFechaOk(Date fechaOk) {
        this.fechaOk = fechaOk;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }



    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public long getIdRevisionOk() {
        return idRevisionOk;
    }

    public void setIdRevisionOk(long idRevisionOk) {
        this.idRevisionOk = idRevisionOk;
    }

    public long getIdRevision() {
        return idRevision;
    }

    public void setIdRevision(long idRevision) {
        this.idRevision = idRevision;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public long getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(long idCarrera) {
        this.idCarrera = idCarrera;
    }

    public com.nifa.racecontrol.Model.Carrera getCarrera() {
        return Carrera;
    }

    public void setCarrera(com.nifa.racecontrol.Model.Carrera carrera) {
        Carrera = carrera;
    }
}
