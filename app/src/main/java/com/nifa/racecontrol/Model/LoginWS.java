package com.nifa.racecontrol.Model;

import android.util.Log;

import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Bringa on 26/08/2016.
 */
public class LoginWS {

    private static String TAG = "LoginWS";

    public static WsResp loginWS(String email , String password)
        {
        String METHOD_NAME = "loginWS";
        String SOAP_ACTION = Constante.WS_SOAP_ACTION + METHOD_NAME ;

        //Declaracion de variables para consuymir el web service
        SoapObject request=null;
        SoapSerializationEnvelope envelope=null;
        SoapPrimitive resultsRequestSOAP=null;

        Gson gson = new Gson();

        request = new SoapObject(Constante.NAMESPACE, METHOD_NAME);

        PropertyInfo parametro;


        parametro = new PropertyInfo();
        parametro.setName("email");
        parametro.setValue(email);
        //Agrego parametro
        request.addProperty(parametro);


        parametro = new PropertyInfo();
        parametro.setName("password");
        parametro.setValue(password);
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
        catch (SocketTimeoutException ex)
        {
            return  new WsResp(false, "No se pudo establecer conexión, intente nuevamente");
        }
        catch (Exception ex) {
            return  new WsResp(false,"Exception: " + ex.getMessage()) ;
        }

    }

    public  static  Usuario getOneUsuarioWS(String email)
    {
        String METHOD_NAME = "getOneUsuarioWS";
        String SOAP_ACTION = Constante.WS_SOAP_ACTION + METHOD_NAME ;

        //Declaracion de variables para consuymir el web service
        SoapObject request=null;
        SoapSerializationEnvelope envelope=null;
        SoapPrimitive resultsRequestSOAP=null;

        Gson gson = new Gson();

        request = new SoapObject(Constante.NAMESPACE, METHOD_NAME);

        PropertyInfo parametro;


        parametro = new PropertyInfo();
        parametro.setName("email");
        parametro.setValue(email);
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

            return jsonToUsuario(tmp);

        }

        catch (Exception ex) {
            Log.e(TAG,ex.getMessage());
        }

       return null;
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

    public static Usuario jsonToUsuario(String json) {

        Usuario usuario = null;

        try {

            //se crea el objeto que ayuda deserealizar la cadena JSON
            Gson gson = new Gson();


            //Deserealizamos la cadena JSON para que se convertida a un ArrayList<AndroidOS>
            usuario = gson.fromJson(json,Usuario.class);

        }
        catch (Exception ex) {

            String msj = ex.getMessage();

        }

        return  usuario;


    }

}
