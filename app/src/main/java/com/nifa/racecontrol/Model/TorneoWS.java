package com.nifa.racecontrol.Model;

import android.content.Context;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.jar.JarEntry;

/**
 * Created by Bringa on 30/06/2016.
 * Clase que permite realizar las consultar WS
 */
public class TorneoWS {

    private static String TAG = "TorneoWS";

    public static ArrayList<Torneo> getAll(Context context)
    {
        String METHOD_NAME = "getAllToneosWS";
        String SOAP_ACTION = Constante.WS_SOAP_ACTION + METHOD_NAME ;

        //Declaracion de variables para consuymir el web service
        SoapObject request=null;
        SoapSerializationEnvelope envelope=null;
        SoapPrimitive resultsRequestSOAP=null;

        Gson gson = new Gson();

        request = new SoapObject(Constante.NAMESPACE, METHOD_NAME);

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
            Log.i(TAG, "(SoapPrimitive)envelope.getResponse()");
            resultsRequestSOAP = (SoapPrimitive)envelope.getResponse();

            String tmp = resultsRequestSOAP.toString();

            return jsonToListTorneos(tmp);

        }
        catch (Exception ex) {

        }

        return  null;

    }

    public static   ArrayList<Torneo> jsonToListTorneos(String json) {

        ArrayList<Torneo> listTorneo = null;

        try {

            //se crea el objeto que ayuda deserealizar la cadena JSON
            Gson gson = new Gson();

            //Obtenemos el tipo de un ArrayList<AndroidSO>
            Type lstT = new TypeToken<ArrayList<Torneo>>(){}.getType();

            //Deserealizamos la cadena JSON para que se convertida a un ArrayList<AndroidOS>
            listTorneo = gson.fromJson(json, lstT);

        }
        catch (Exception ex) {

        }

        return  listTorneo;


    }





}
