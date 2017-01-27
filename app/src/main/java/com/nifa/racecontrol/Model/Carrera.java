package com.nifa.racecontrol.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bringa on 10/07/2016.
 */
public class Carrera implements Parcelable {

    private int idCarrera;
    private  int idTorneo;
    private String nombre;
    private  String fecha;


    protected Carrera(Parcel in) {
        idCarrera = in.readInt();
        idTorneo = in.readInt();
        nombre = in.readString();
        fecha = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idCarrera);
        dest.writeInt(idTorneo);
        dest.writeString(nombre);
        dest.writeString(fecha);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Carrera> CREATOR = new Parcelable.Creator<Carrera>() {
        @Override
        public Carrera createFromParcel(Parcel in) {
            return new Carrera(in);
        }

        @Override
        public Carrera[] newArray(int size) {
            return new Carrera[size];
        }
    };


    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }

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
}
