package com.nifa.racecontrol.Model;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bringa on 29/07/2016.
 */
public class Revision implements Parcelable {

    private  int idTecnica;
    private int idRevision;
    private String nombre;
    public  boolean ok;
    public ArrayList<Observacion> listObservacion;
    public  Precinto precinto;



    protected Revision(Parcel in) {
        //in.readParcelable(Revision.class.getClassLoader());
        idTecnica = in.readInt();
        idRevision = in.readInt();
        nombre = in.readString();
        ok = in.readByte() != 0;

        //in.readTypedList(listObservacion,Observacion.CREATOR);
        // in.readList(listObservacion,Observacion.class.getClassLoader());

        if (in.readByte() == 0x01) {
            listObservacion = new ArrayList<Observacion>();
            in.readList(listObservacion, Observacion.class.getClassLoader());
        } else {
            listObservacion = null;
        }

        precinto = (Precinto) in.readValue(Precinto.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getIdTecnica());
        dest.writeInt(getIdRevision());
        dest.writeString(getNombre());
        dest.writeInt(isOk()? 1:0);
        if (listObservacion == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(listObservacion);
        }
        dest.writeValue(precinto);

    }

    public static final Creator<Revision> CREATOR = new Creator<Revision>() {
        @Override
        public Revision createFromParcel(Parcel in) {
            return new Revision(in);
        }

        @Override
        public Revision[] newArray(int size) {
            return new Revision[size];
        }
    };


    public int getIdTecnica() {
        return idTecnica;
    }

    public void setIdTecnica(int idTecnica) {
        this.idTecnica = idTecnica;
    }

    public int getIdRevision() {
        return idRevision;
    }

    public void setIdRevision(int idRevision) {
        this.idRevision = idRevision;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public ArrayList<Observacion> getListObservacion() {
       if ( this.listObservacion == null) this.listObservacion = new ArrayList<Observacion>();
        return listObservacion;
    }

    public void setListObservacion(ArrayList<Observacion> listObservacion) {
        this.listObservacion = listObservacion;
    }

    public Precinto getPrecinto() {
        return precinto;
    }

    public void setPrecinto(Precinto precinto) {
        this.precinto = precinto;
    }

    /**
     * Permite obtener la cantidad de observaciones pendientes
     */
    public  int getCantObserPrendientes()
    {
        if (listObservacion == null) return 0;
        int cant = 0;
        for (Observacion obs: this.listObservacion) {

            if ( obs.isOk() == false) cant++;
        }

        return  cant;
    }


}
