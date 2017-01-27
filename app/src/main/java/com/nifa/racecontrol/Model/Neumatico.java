package com.nifa.racecontrol.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bringa on 20/08/2016.
 */
public class Neumatico implements Parcelable {

    public int numNeumatico;
    public int idPiloto;
    public int idCarrera;
    public Date fecha ;
    public boolean nuevo;


    public int getNumNeumatico() {
        return numNeumatico;
    }

    public void setNumNeumatico(int numNeumatico) {
        this.numNeumatico = numNeumatico;
    }

    public int getIdPiloto() {
        return idPiloto;
    }

    public void setIdPiloto(int idPiloto) {
        this.idPiloto = idPiloto;
    }

    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getFechaString()
    {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String reportDate = df.format(fecha);

        return reportDate;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isNuevo() {
        return nuevo;
    }

    public void setNuevo(boolean nuevo) {
        this.nuevo = nuevo;
    }

    public  String getTipoNeumatico()
    {
        return  nuevo ? "Nuevo":"Usado";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.numNeumatico);
        dest.writeInt(this.idPiloto);
        dest.writeInt(this.idCarrera);
        dest.writeLong(this.fecha != null ? this.fecha.getTime() : -1);
        dest.writeByte(this.nuevo ? (byte) 1 : (byte) 0);
    }

    public Neumatico() {
    }

    protected Neumatico(Parcel in) {
        this.numNeumatico = in.readInt();
        this.idPiloto = in.readInt();
        this.idCarrera = in.readInt();
        long tmpFecha = in.readLong();
        this.fecha = tmpFecha == -1 ? null : new Date(tmpFecha);
        this.nuevo = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Neumatico> CREATOR = new Parcelable.Creator<Neumatico>() {
        @Override
        public Neumatico createFromParcel(Parcel source) {
            return new Neumatico(source);
        }

        @Override
        public Neumatico[] newArray(int size) {
            return new Neumatico[size];
        }
    };
}
