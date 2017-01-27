package com.nifa.racecontrol.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.method.HideReturnsTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nifa.racecontrol.Controller.CarreraActivity;
import com.nifa.racecontrol.Controller.CategoriasActivity;
import com.nifa.racecontrol.Controller.PilotoActivity;
import com.nifa.racecontrol.Controller.TorneosActivity;
import com.nifa.racecontrol.R;

/**
 * Created by Bringa on 16/07/2016.
 */
public class CabeceraLayout extends LinearLayout {

    private OnClickListenerCab event;
    private Context context;


    public CabeceraLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ((LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.item_cabecera, this, true);

        this.context = context;

    }

    public void setIdActivity(Constante.IdActivity idActivity)
    {
        // En primer lugar inflamos la vista de nuestro control personalizado. Al método iniflate
        // le pasamos el layout de nuestro control, el ViewGroup al que pertenecerá la vista (this)
        // y si se debe añadir a este ViewGroup (en este caso sí).



        Button btnTorneo = (Button) findViewById(R.id.btnTorneo);
        btnTorneo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event == null) return;
                event.onClick(TorneosActivity.class);
            }
        });

        Button btnCategoria = (Button) findViewById(R.id.btnCategoria);
        btnCategoria.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               if (event == null) return;
               event.onClick(CategoriasActivity.class);
           }
         });

        Button btnCarrera = (Button) findViewById(R.id.btnCarrera);
        btnCarrera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event == null) return;
                event.onClick(CarreraActivity.class);
            }
        });

        Button btnPiloto = (Button) findViewById(R.id.btnPiloto);
        btnPiloto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event == null) return;
                event.onClick(PilotoActivity.class);
            }
        });


        btnTorneo.setVisibility(GONE);
        btnCategoria.setVisibility(GONE);
        btnCarrera.setVisibility(GONE);
        btnPiloto.setVisibility(GONE);

        setCabeceraLayout(btnTorneo ,idActivity , Constante.IdActivity.TORNEO );
        setCabeceraLayout(btnCategoria ,idActivity , Constante.IdActivity.CATEGORIA );
        setCabeceraLayout(btnCarrera ,idActivity , Constante.IdActivity.CARRERA );
        setCabeceraLayout(btnPiloto ,idActivity , Constante.IdActivity.PILOTO );


    }

    private  void setCabeceraLayout(Button btn,
                                    Constante.IdActivity idActivityContext,
                                    Constante.IdActivity idActivity)
    {
        if (idActivityContext.id() >= idActivity.id()) {
            //TextView tv = (TextView) findViewById(R.id.tvCategoriaSelect);
            String text;

            if (idActivityContext.id() == idActivity.id())
            {
                text = "SELECCIONE " + idActivityContext.getNombre();
                btn.setTextColor(Color.WHITE);
                btn.setBackgroundResource(R.drawable.shape_seleccion_cabecera);

            }
            else text = getNombreSelect(idActivity);

            btn.setText(text);

            btn.setVisibility(VISIBLE);
        }
    }

    private String getNombreSelect(Constante.IdActivity idActivity)
    {
        Constante constante = new Constante(this.context);
        if (idActivity.id() == Constante.IdActivity.TORNEO.id()) return constante.getSelectTorneo().getNombre();
        if (idActivity.id() == Constante.IdActivity.CATEGORIA.id()) return constante.getSelectCategoria().getNombre();
        if (idActivity.id() == Constante.IdActivity.CARRERA.id()) return constante.getSelectCarrera().getNombre();
        if (idActivity.id() == Constante.IdActivity.PILOTO.id()) return constante.getSelectPiloto().getNombre();

        return  "Error";

    }

    public void setOnClickListenerCab(OnClickListenerCab event)
    {
        this.event = event;
    }

    public  interface OnClickListenerCab
    {
        void onClick(Class c);
    }
}
