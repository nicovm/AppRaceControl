package com.nifa.racecontrol.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.security.PublicKey;

/**
 * Created by Bringa on 30/06/2016.
 */
public class Constante {

    private Context context;
    private SharedPreferences settings;
    private final String PREFS_FILE = "sharedConstante";
    private final String KEY_SESION_USUARIO = "SESION_USUARIO";
    private final String KEY_SELECT_TORNEO = "SELECT_TORNEO";
    private final String KEY_SELECT_CATEGORIA = "SELECT_CATEGORIA";
    private final String KEY_SELECT_CARRERA = "SELECT_CARRERA";
    private final String KEY_SELECT_PILOTO = "SELECT_PILOTO";
    private final String KEY_SELECT_TECNICA = "SELECT_TECNICA";

   private  Gson gson;

    public static  final   String WS_SOAP_ACTION = "http://racecontrol.com/webservices/";
    public static final String NAMESPACE = "http://racecontrol.com/webservices/";
										   
   // public static final String GET_SOAP_ADDRESS =  "http://192.168.226.30/WsRaceControl/RaceControlWS.asmx";
    public static final String GET_SOAP_ADDRESS =  "http://nicobringa-001-site1.atempurl.com/RaceControlWS.asmx";

    public Constante(Context context)
    {
        this.context = context;
        this.settings = this.context.getSharedPreferences(PREFS_FILE,context.MODE_PRIVATE);
        gson = new Gson();
    }

    public Usuario getSesionUsuario() {

        String json = this.settings.getString(this.KEY_SESION_USUARIO,"");
        Usuario usuario = gson.fromJson(json,Usuario.class);
        return usuario;
    }

    public  void setSesionUsuario(Usuario usuario) {
        SharedPreferences.Editor editor = settings.edit();
        String json = gson.toJson(usuario);
        editor.putString(KEY_SESION_USUARIO,json);
        editor.apply();
    }

    public  Torneo getSelectTorneo() {


        String json = this.settings.getString(this.KEY_SELECT_TORNEO,"");
        Torneo torneo = gson.fromJson(json,Torneo.class);

        return torneo;
    }

    public  void setSelectTorneo(Torneo selectTorneo) {

        SharedPreferences.Editor editor = settings.edit();
        String json = gson.toJson(selectTorneo);
        editor.putString(KEY_SELECT_TORNEO,json);
        editor.apply();

    }

    public  Categoria getSelectCategoria() {
        String json = this.settings.getString(this.KEY_SELECT_CATEGORIA,"");
        Categoria categoria = gson.fromJson(json,Categoria.class);
        return categoria;
    }

    public  void setSelectCategoria(Categoria selectCategoria) {

        SharedPreferences.Editor editor = settings.edit();
        String json = gson.toJson(selectCategoria);
        editor.putString(KEY_SELECT_CATEGORIA,json);
        editor.apply();
    }

    public  Carrera getSelectCarrera() {
        String json = this.settings.getString(this.KEY_SELECT_CARRERA,"");
        Carrera carrera = gson.fromJson(json,Carrera.class);

        return carrera;
    }

    public  void setSelectCarrera(Carrera selectCarrera) {
        SharedPreferences.Editor editor = settings.edit();
        String json = gson.toJson(selectCarrera);
        editor.putString(KEY_SELECT_CARRERA,json);
        editor.apply();
    }

    public Piloto getSelectPiloto() {

        String json = this.settings.getString(this.KEY_SELECT_PILOTO,"");
        Piloto piloto = gson.fromJson(json,Piloto.class);
        return piloto;
    }

    public  void setSelectPiloto(Piloto selectPiloto) {
        SharedPreferences.Editor editor = settings.edit();
        String json = gson.toJson(selectPiloto);
        editor.putString(KEY_SELECT_PILOTO,json);
        editor.apply();
    }

    public Tecnica getSelectTecnica() {

        String json = this.settings.getString(this.KEY_SELECT_TECNICA,"");
        Tecnica tecnica = gson.fromJson(json,Tecnica.class);
        return tecnica;
    }

    public  void setSelectTecnica(Tecnica selectTecnica) {
        SharedPreferences.Editor editor = settings.edit();
        String json = gson.toJson(selectTecnica);
        editor.putString(KEY_SELECT_TECNICA,json);
        editor.apply();
    }

    public enum IdActivity {
        TORNEO("TORNEO",1),
        CATEGORIA("CATEGORIA",2),
        CARRERA("CARRERA",3),
        PILOTO("PILOTO",4),
        TECNICA("TECNICA",5);


        private int id;
        private String nombre;

        IdActivity(String nombre, int id ) {
            this.id = id;
            this.nombre = nombre;
        }

        public int id() {
            return id;
        }

        public  String getNombre()
        {
            return this.nombre;
        }


    }

    public enum  idPerfilUsuario
    {
        SUPERVISOR("Supervisor",1),
        OPERADOR("Operador Tecnico",2),
        USUARIO("Usuario",3);

        private int id;
        private String nombre;

        idPerfilUsuario(String nombre, int id ) {
            this.id = id;
            this.nombre = nombre;
        }

        public int id() {
            return id;
        }

        public  String getNombre()
        {
            return this.nombre;
        }

    }


    public static  boolean empty(final String s)
    {
        boolean resp = s == null || s.trim().isEmpty();
        return resp;
    }


    //"http://192.168.0.14/WsRaceControl/RaceControlWS.asmx";








}
