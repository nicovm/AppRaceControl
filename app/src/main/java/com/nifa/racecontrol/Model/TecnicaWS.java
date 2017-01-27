package com.nifa.racecontrol.Model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by Bringa on 29/07/2016.
 */
public class TecnicaWS {

    public  static  final String TAG = "TecnicaWS";

    public  static Tecnica getTecnica (int idCarrera , int idPiloto, int idCategoria)
    {
        String METHOD_NAME = "getTecnicaWS";
        String SOAP_ACTION = Constante.WS_SOAP_ACTION + METHOD_NAME ;

        //Declaracion de variables para consuymir el web service
        SoapObject request=null;
        SoapSerializationEnvelope envelope=null;
        SoapPrimitive resultsRequestSOAP=null;

        Gson gson = new Gson();

        request = new SoapObject(Constante.NAMESPACE, METHOD_NAME);

        PropertyInfo parametro;
        //Creo el parametro Parametro fecha desde
        parametro = new PropertyInfo();
        parametro.setName("idPiloto");
        parametro.setValue(idPiloto);
        //Agrego parametro
        request.addProperty(parametro);

        parametro = new PropertyInfo();
        parametro.setName("idCarrera");
        parametro.setValue(idCarrera);
        //Agrego parametro
        request.addProperty(parametro);

        parametro = new PropertyInfo();
        parametro.setName("idCategoria");
        parametro.setValue(idCategoria);
        //Agrego parametro
        request.addProperty(parametro);

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet

        envelope.setOutputSoapObject(request);

        //Objeto que representa el modelo de transporte
        //Recibe la URL del ws
        // HttpTransportSE transporte = new HttpTransportSE(URL);
        HttpTransportSE transporte = new HttpTransportSE(Constante.GET_SOAP_ADDRESS);

        try {
            transporte.call(SOAP_ACTION, envelope);
            //Se crea un objeto SoapPrimitive y se obtiene la respuesta
            //de la peticion
            //Log.i(TAG, "(SoapPrimitive)envelope.getResponse()");
            resultsRequestSOAP = (SoapPrimitive)envelope.getResponse();

            String tmp = resultsRequestSOAP.toString();

            return jsonToTecnica(tmp);

        }
        catch (Exception ex) {
            Log.e(TAG  + "getTecnica" ,ex.getMessage());
        }

        return  null;

    }

    public  static ArrayList<Revision> getRevision (long idTecnica)
    {
        String METHOD_NAME = "getRevisionWS";
        String SOAP_ACTION = Constante.WS_SOAP_ACTION + METHOD_NAME ;

        //Declaracion de variables para consuymir el web service
        SoapObject request=null;
        SoapSerializationEnvelope envelope=null;
        SoapPrimitive resultsRequestSOAP=null;

        Gson gson = new Gson();

        request = new SoapObject(Constante.NAMESPACE, METHOD_NAME);

        PropertyInfo parametro;
        //Creo el parametro Parametro fecha desde
        parametro = new PropertyInfo();
        parametro.setName("idTecnica");
        parametro.setValue(idTecnica);
        //Agrego parametro
        request.addProperty(parametro);



        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet

        envelope.setOutputSoapObject(request);

        //Objeto que representa el modelo de transporte
        //Recibe la URL del ws
        // HttpTransportSE transporte = new HttpTransportSE(URL);
        HttpTransportSE transporte = new HttpTransportSE(Constante.GET_SOAP_ADDRESS,20000);

        try {
            transporte.call(SOAP_ACTION, envelope);
            //Se crea un objeto SoapPrimitive y se obtiene la respuesta
            //de la peticion
            //Log.i(TAG, "(SoapPrimitive)envelope.getResponse()");
            resultsRequestSOAP = (SoapPrimitive)envelope.getResponse();

            String tmp = resultsRequestSOAP.toString();

            return jsonToListRevision(tmp);

        }
        catch (Exception ex) {
            Log.e(TAG + "getRevision", ex.getMessage());
        }

        return  null;

    }

    public  static ArrayList<Precintado> getPrecintado (Context context, int idCarrera , int idPiloto)
    {
        String METHOD_NAME = "getPrecintadoWS";
        String SOAP_ACTION = Constante.WS_SOAP_ACTION + METHOD_NAME ;

        //Declaracion de variables para consuymir el web service
        SoapObject request=null;
        SoapSerializationEnvelope envelope=null;
        SoapPrimitive resultsRequestSOAP=null;

        Gson gson = new Gson();

        request = new SoapObject(Constante.NAMESPACE, METHOD_NAME);

        PropertyInfo parametro;
        //Creo el parametro Parametro fecha desde
        parametro = new PropertyInfo();
        parametro.setName("idPiloto");
        parametro.setValue(idPiloto);
        //Agrego parametro
        request.addProperty(parametro);

        parametro = new PropertyInfo();
        parametro.setName("idCarrera");
        parametro.setValue(idCarrera);
        //Agrego parametro
        request.addProperty(parametro);

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet

        envelope.setOutputSoapObject(request);

        //Objeto que representa el modelo de transporte
        //Recibe la URL del ws
        // HttpTransportSE transporte = new HttpTransportSE(URL);
        HttpTransportSE transporte = new HttpTransportSE(Constante.GET_SOAP_ADDRESS);

        try {
            transporte.call(SOAP_ACTION, envelope);
            //Se crea un objeto SoapPrimitive y se obtiene la respuesta
            //de la peticion
            //Log.i(TAG, "(SoapPrimitive)envelope.getResponse()");
            resultsRequestSOAP = (SoapPrimitive)envelope.getResponse();

            String tmp = resultsRequestSOAP.toString();

            return jsonToListPrecintado(tmp);

        }
        catch (Exception ex) {

        }

        return  null;

    }

    public  static  ArrayList<Neumatico> getAllNeumatico(int idPiloto ,int idCarrera)
    {
        String METHOD_NAME = "getAllNeumaticoWS";
        String SOAP_ACTION = Constante.WS_SOAP_ACTION + METHOD_NAME ;

        //Declaracion de variables para consuymir el web service
        SoapObject request=null;
        SoapSerializationEnvelope envelope=null;
        SoapPrimitive resultsRequestSOAP=null;

        Gson gson = new Gson();

        request = new SoapObject(Constante.NAMESPACE, METHOD_NAME);

        PropertyInfo parametro;
        //Creo el parametro Parametro fecha desde
        parametro = new PropertyInfo();
        parametro.setName("idPiloto");
        parametro.setValue(idPiloto);
        //Agrego parametro
        request.addProperty(parametro);

        parametro = new PropertyInfo();
        parametro.setName("idCarrera");
        parametro.setValue(idCarrera);
        //Agrego parametro
        request.addProperty(parametro);

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet

        envelope.setOutputSoapObject(request);

        //Objeto que representa el modelo de transporte
        //Recibe la URL del ws
        // HttpTransportSE transporte = new HttpTransportSE(URL);
        HttpTransportSE transporte = new HttpTransportSE(Constante.GET_SOAP_ADDRESS);

        try {

            transporte.call(SOAP_ACTION, envelope);
            //Se crea un objeto SoapPrimitive y se obtiene la respuesta
            //de la peticion
            //Log.i(TAG, "(SoapPrimitive)envelope.getResponse()");
            resultsRequestSOAP = (SoapPrimitive)envelope.getResponse();

            String tmp = resultsRequestSOAP.toString();

            return jsonToListNeumatico(tmp);

        }
        catch (Exception ex) {

        }

        return  null;


    }

    public  static  WsResp addNeumatico(int numNeumatico, boolean nuevo ,int idPiloto ,int idCarrera)
    {
        String METHOD_NAME = "addNeumaticoWS";
        String SOAP_ACTION = Constante.WS_SOAP_ACTION + METHOD_NAME ;

        //Declaracion de variables para consuymir el web service
        SoapObject request=null;
        SoapSerializationEnvelope envelope=null;
        SoapPrimitive resultsRequestSOAP=null;

        Gson gson = new Gson();

        request = new SoapObject(Constante.NAMESPACE, METHOD_NAME);

        PropertyInfo parametro;


        parametro = new PropertyInfo();
        parametro.setName("numNeumatico");
        parametro.setValue(numNeumatico);
        //Agrego parametro
        request.addProperty(parametro);

        parametro = new PropertyInfo();
        parametro.setName("nuevo");
        parametro.setValue(nuevo);
        //Agrego parametro
        request.addProperty(parametro);

        parametro = new PropertyInfo();
        parametro.setName("idPiloto");
        parametro.setValue(idPiloto);
        //Agrego parametro
        request.addProperty(parametro);

        parametro = new PropertyInfo();
        parametro.setName("idCarrera");
        parametro.setValue(idCarrera);
        //Agrego parametro
        request.addProperty(parametro);

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet

        envelope.setOutputSoapObject(request);

        //Objeto que representa el modelo de transporte
        //Recibe la URL del ws
        // HttpTransportSE transporte = new HttpTransportSE(URL);
        HttpTransportSE transporte = new HttpTransportSE(Constante.GET_SOAP_ADDRESS);

        try {
            transporte.call(SOAP_ACTION, envelope);
            //Se crea un objeto SoapPrimitive y se obtiene la respuesta
            //de la peticion
            //Log.i(TAG, "(SoapPrimitive)envelope.getResponse()");
            resultsRequestSOAP = (SoapPrimitive)envelope.getResponse();

            String tmp = resultsRequestSOAP.toString();

            return jsonToWsResp(tmp);

        }
        catch (Exception ex) {

        }

        return  null;

    }

    public  static  WsResp setNeumatico(int oldNumNeumatico,boolean nuevo,int newNumNeumatico)
    {
        String METHOD_NAME = "setNeumaticoWS";
        String SOAP_ACTION = Constante.WS_SOAP_ACTION + METHOD_NAME ;

        //Declaracion de variables para consuymir el web service
        SoapObject request=null;
        SoapSerializationEnvelope envelope=null;
        SoapPrimitive resultsRequestSOAP=null;

        Gson gson = new Gson();

        request = new SoapObject(Constante.NAMESPACE, METHOD_NAME);

        PropertyInfo parametro;


        parametro = new PropertyInfo();
        parametro.setName("oldNumNeumatico");
        parametro.setValue(oldNumNeumatico);
        //Agrego parametro
        request.addProperty(parametro);

        parametro = new PropertyInfo();
        parametro.setName("nuevo");
        parametro.setValue(nuevo);
        //Agrego parametro
        request.addProperty(parametro);


        parametro = new PropertyInfo();
        parametro.setName("newNumNeumatico");
        parametro.setValue(newNumNeumatico);
        //Agrego parametro
        request.addProperty(parametro);

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet

        envelope.setOutputSoapObject(request);

        //Objeto que representa el modelo de transporte
        //Recibe la URL del ws
        // HttpTransportSE transporte = new HttpTransportSE(URL);
        HttpTransportSE transporte = new HttpTransportSE(Constante.GET_SOAP_ADDRESS);

        try {
            transporte.call(SOAP_ACTION, envelope);
            //Se crea un objeto SoapPrimitive y se obtiene la respuesta
            //de la peticion
            //Log.i(TAG, "(SoapPrimitive)envelope.getResponse()");
            resultsRequestSOAP = (SoapPrimitive)envelope.getResponse();

            String tmp = resultsRequestSOAP.toString();

            return jsonToWsResp(tmp);

        }
        catch (Exception ex) {

        }

        return  null;

    }


    public  static WsResp savePrecintado (List<Precintado> listPrecintado)
    {
        String METHOD_NAME = "savePrecintadoWS";
        String SOAP_ACTION = Constante.WS_SOAP_ACTION + METHOD_NAME ;

        //Declaracion de variables para consuymir el web service
        SoapObject request=null;
        SoapSerializationEnvelope envelope=null;
        SoapPrimitive resultsRequestSOAP=null;

        Gson gson = new Gson();

        request = new SoapObject(Constante.NAMESPACE, METHOD_NAME);

        PropertyInfo parametro;
        //Creo el parametro Parametro fecha desde
        parametro = new PropertyInfo();
        parametro.setName("jsonPrecintado");
        parametro.setValue(gson.toJson(listPrecintado));
        //Agrego parametro
        request.addProperty(parametro);



        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet

        envelope.setOutputSoapObject(request);

        //Objeto que representa el modelo de transporte
        //Recibe la URL del ws
        // HttpTransportSE transporte = new HttpTransportSE(URL);
        HttpTransportSE transporte = new HttpTransportSE(Constante.GET_SOAP_ADDRESS);

        try {
            transporte.call(SOAP_ACTION, envelope);
            //Se crea un objeto SoapPrimitive y se obtiene la respuesta
            //de la peticion
            //Log.i(TAG, "(SoapPrimitive)envelope.getResponse()");
            resultsRequestSOAP = (SoapPrimitive)envelope.getResponse();

            String tmp = resultsRequestSOAP.toString();

            return  jsonToWsResp(tmp);

        }
        catch (Exception ex) {

        }

        return  null;

    }

    public  static WsResp saveRevision (List<Revision> listRevision)
    {
        String METHOD_NAME = "saveRevisionWS";
        String SOAP_ACTION = Constante.WS_SOAP_ACTION + METHOD_NAME ;

        //Declaracion de variables para consuymir el web service
        SoapObject request=null;
        SoapSerializationEnvelope envelope=null;
        SoapPrimitive resultsRequestSOAP=null;

        Gson gson = new Gson();

        request = new SoapObject(Constante.NAMESPACE, METHOD_NAME);

        PropertyInfo parametro;
        //Creo el parametro Parametro fecha desde
        parametro = new PropertyInfo();
        parametro.setName("jsonRevision");
        parametro.setValue(gson.toJson(listRevision));
        //Agrego parametro
        request.addProperty(parametro);



        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet

        envelope.setOutputSoapObject(request);

        //Objeto que representa el modelo de transporte
        //Recibe la URL del ws
        // HttpTransportSE transporte = new HttpTransportSE(URL);
        HttpTransportSE transporte = new HttpTransportSE(Constante.GET_SOAP_ADDRESS);

        try {
            transporte.call(SOAP_ACTION, envelope);
            //Se crea un objeto SoapPrimitive y se obtiene la respuesta
            //de la peticion
            //Log.i(TAG, "(SoapPrimitive)envelope.getResponse()");
            resultsRequestSOAP = (SoapPrimitive)envelope.getResponse();

            String tmp = resultsRequestSOAP.toString();

            return  jsonToWsResp(tmp);
        }
        catch (Exception ex) {

        }
        return  null;
    }

    public  static  WsResp verificacionPrecinto(String idPrecinto)
    {
        String METHOD_NAME = "verificarPrecinto";
        String SOAP_ACTION = Constante.WS_SOAP_ACTION + METHOD_NAME ;

        //Declaracion de variables para consuymir el web service
        SoapObject request=null;
        SoapSerializationEnvelope envelope=null;
        SoapPrimitive resultsRequestSOAP=null;

        Gson gson = new Gson();

        request = new SoapObject(Constante.NAMESPACE, METHOD_NAME);

        PropertyInfo parametro;
        //Creo el parametro Parametro fecha desde
        parametro = new PropertyInfo();
        parametro.setName("idPrecinto");
        parametro.setValue(idPrecinto);
        //Agrego parametro
        request.addProperty(parametro);



        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet

        envelope.setOutputSoapObject(request);

        //Objeto que representa el modelo de transporte
        //Recibe la URL del ws
        // HttpTransportSE transporte = new HttpTransportSE(URL);
        HttpTransportSE transporte = new HttpTransportSE(Constante.GET_SOAP_ADDRESS);

        try {
            transporte.call(SOAP_ACTION, envelope);
            //Se crea un objeto SoapPrimitive y se obtiene la respuesta
            //de la peticion
            //Log.i(TAG, "(SoapPrimitive)envelope.getResponse()");
            resultsRequestSOAP = (SoapPrimitive)envelope.getResponse();

            String tmp = resultsRequestSOAP.toString();

            return  jsonToWsResp(tmp);

        }
        catch (Exception ex) {
           return  new WsResp(false,"Exception: " + ex.getMessage()) ;
        }


    }

    /**
     * Permite guardar la revision
     * @param revision: que se quiere guardar
     * @return Objeto Web Servite respueste(WsrESP) para la confirmacion de la operación
     */
    public  static  WsResp guardarRevision(Revision revision)
    {
        String METHOD_NAME = "guardarRevision";
        String SOAP_ACTION = Constante.WS_SOAP_ACTION + METHOD_NAME ;

        //Declaracion de variables para consuymir el web service
        SoapObject request=null;
        SoapSerializationEnvelope envelope=null;
        SoapPrimitive resultsRequestSOAP=null;

        Gson gson = new Gson();

        request = new SoapObject(Constante.NAMESPACE, METHOD_NAME);

        String jsonRevision = gson.toJson(revision);
        PropertyInfo parametro;
        //Creo el parametro Parametro fecha desde
        parametro = new PropertyInfo();
        parametro.setName("jsonRevision");
        parametro.setValue(jsonRevision);
        //Agrego parametro
        request.addProperty(parametro);



        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet

        envelope.setOutputSoapObject(request);

        //Objeto que representa el modelo de transporte
        //Recibe la URL del ws
        // HttpTransportSE transporte = new HttpTransportSE(URL);
        HttpTransportSE transporte = new HttpTransportSE(Constante.GET_SOAP_ADDRESS);

        try {
            transporte.call(SOAP_ACTION, envelope);
            //Se crea un objeto SoapPrimitive y se obtiene la respuesta
            //de la peticion
            //Log.i(TAG, "(SoapPrimitive)envelope.getResponse()");
            resultsRequestSOAP = (SoapPrimitive)envelope.getResponse();

            String tmp = resultsRequestSOAP.toString();

            return  jsonToWsResp(tmp);

        }
        catch (SocketTimeoutException ex)
        {
            return  new WsResp(false, "No se pudo establecer conexión, intente nuevamente");
        }
        catch (Exception ex) {
            return  new WsResp(false,"Exception: " + ex.getMessage()) ;
        }


    }





    public static   ArrayList<Revision> jsonToListRevision(String json) {

        ArrayList<Revision> list = null;

        try {

            //se crea el objeto que ayuda deserealizar la cadena JSON
            Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy'T'HH:mm:ss").create();

            //Obtenemos el tipo de un ArrayList<AndroidSO>
            Type lstT = new TypeToken<ArrayList<Revision>>(){}.getType();

            //Deserealizamos la cadena JSON para que se convertida a un ArrayList<AndroidOS>
            list = gson.fromJson(json, lstT);

        }
        catch (Exception ex) {

        }

        return  list;


    }

    public static   ArrayList<Precintado> jsonToListPrecintado(String json) {

        ArrayList<Precintado> list = null;

        try {

            //se crea el objeto que ayuda deserealizar la cadena JSON
            Gson gson = new Gson();

            //Obtenemos el tipo de un ArrayList<AndroidSO>
            Type lstT = new TypeToken<ArrayList<Precintado>>(){}.getType();

            //Deserealizamos la cadena JSON para que se convertida a un ArrayList<AndroidOS>
            list = gson.fromJson(json, lstT);

        }
        catch (Exception ex) {

        }

        return  list;


    }

    public static   Tecnica jsonToTecnica(String json) {

        Tecnica tecnica = null;

        try {


            //Obtenemos el tipo de un ArrayList<AndroidSO>
            Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy'T'HH:mm:ss").create();

            //Deserealizamos la cadena JSON para que se convertida a un ArrayList<AndroidOS>
            tecnica =  gson.fromJson(json,Tecnica.class);

        }
        catch (Exception ex) {

        }

        return  tecnica;

    }

    public static   ArrayList<Neumatico> jsonToListNeumatico(String json) {

        ArrayList<Neumatico> list = null;

        try {

            //se crea el objeto que ayuda deserealizar la cadena JSON
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

            //Obtenemos el tipo de un ArrayList<AndroidSO>
            Type lstT = new TypeToken<ArrayList<Neumatico>>(){}.getType();

            //Deserealizamos la cadena JSON para que se convertida a un ArrayList<AndroidOS>
            list = gson.fromJson(json, lstT);

        }
        catch (Exception ex) {

            String msj = ex.getMessage();

        }

        return  list;


    }

    public static WsResp  jsonToWsResp(String json) {

        WsResp wsResp = null;

        try {

            //se crea el objeto que ayuda deserealizar la cadena JSON
            Gson gson = new Gson();


            //Deserealizamos la cadena JSON para que se convertida a un ArrayList<AndroidOS>
            wsResp = gson.fromJson(json,WsResp.class);

        }
        catch (Exception ex) {

            String msj = ex.getMessage();

        }

        return  wsResp;


    }







}
