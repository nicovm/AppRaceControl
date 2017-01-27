package com.nifa.racecontrol.Model;

/**
 * Created by Bringa on 24/08/2016.
 */
public class WsResp {

    public boolean ok ;
    public String mensaje;

    public WsResp(boolean ok, String mensaje) {
        this.ok = ok;
        this.mensaje = mensaje;
    }
}
