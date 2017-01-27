package com.nifa.racecontrol.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Bringa on 18/09/2016.
 */
public class Precinto implements Parcelable {

    public String idPrecinto;
    public long idRevision ;
    public Date fecha;
    public long idUsuario ;
    private Carrera Carrera;
    private long idCarrera;


    public Precinto(String idPrecinto, long idRevision, Date fecha, long idUsuario, com.nifa.racecontrol.Model.Carrera carrera) {
        this.idPrecinto = idPrecinto;
        this.idRevision = idRevision;
        this.fecha = fecha;
        this.idUsuario = idUsuario;
        this.Carrera = carrera;
        this.idCarrera = carrera.getIdCarrera();
    }

    protected Precinto(Parcel in) {
        idPrecinto = in.readString();
        idRevision = in.readLong();
        long tmpFecha = in.readLong();
        fecha = tmpFecha != -1 ? new Date(tmpFecha) : null;
        idUsuario = in.readLong();
        Carrera = (Carrera) in.readValue(Carrera.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idPrecinto);
        dest.writeLong(idRevision);
        dest.writeLong(fecha != null ? fecha.getTime() : -1L);
        dest.writeLong(idUsuario);
        dest.writeValue(Carrera);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Precinto> CREATOR = new Parcelable.Creator<Precinto>() {
        @Override
        public Precinto createFromParcel(Parcel in) {
            return new Precinto(in);
        }

        @Override
        public Precinto[] newArray(int size) {
            return new Precinto[size];
        }
    };


    public String getIdPrecinto() {
        return idPrecinto;
    }

    public void setIdPrecinto(String idPrecinto) {
        this.idPrecinto = idPrecinto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public long getIdRevision() {
        return idRevision;
    }

    public void setIdRevision(long idRevision) {
        this.idRevision = idRevision;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public com.nifa.racecontrol.Model.Carrera getCarrera() {
        return Carrera;
    }

    public void setCarrera(com.nifa.racecontrol.Model.Carrera carrera) {
        Carrera = carrera;
    }
}

