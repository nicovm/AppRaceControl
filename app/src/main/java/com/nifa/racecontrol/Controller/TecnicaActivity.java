package com.nifa.racecontrol.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.nifa.racecontrol.Model.CabeceraLayout;
import com.nifa.racecontrol.Model.Constante;
import com.nifa.racecontrol.Model.Tecnica;
import com.nifa.racecontrol.Model.TecnicaWS;
import com.nifa.racecontrol.R;

/**
 * Created by Bringa on 24/07/2016.
 */
public class TecnicaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener , View.OnClickListener {

    Button btnRevision;
    Button btnNeumatico;
    private android.widget.SearchView mSearchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnica);

        CabeceraLayout cl = (CabeceraLayout) findViewById(R.id.cabTecnica);
        cl.setIdActivity(Constante.IdActivity.TECNICA);

        cl.setOnClickListenerCab(new CabeceraLayout.OnClickListenerCab() {
            @Override
            public void onClick(Class c) {
                Intent intent = new Intent(getApplicationContext(),c);
                startActivity(intent);
            }
        });

        inicializar();

        BuscarTecnicaTask tecnicaTask = new BuscarTecnicaTask();
        tecnicaTask.execute();
        createToolBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchItem.setVisible(false);
        searchItem.setEnabled(false);

        //mSearchView = (android.widget.SearchView) searchItem.getActionView();
        //mSearchView.setVisibility(View.GONE);

        return  super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_cerrar_sesion:
                Constante constante = new Constante(getApplicationContext());
                //borro el usuario que inicio sesión
                constante.setSesionUsuario(null);

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;


        switch (v.getId())
        {
            case R.id.btnRevision:
                intent = new Intent(this,ElementosRevisionActivity.class);

                break;

            //case R.id.btnElementosPrecintados:
            //  intent = new Intent(this,PrecintadoActivity.class);
            // break;

            case R.id.btnNeumaticos:
                intent = new Intent(this,NeumaticoActivity.class);
                break;
        }

        if (intent != null  ) startActivity(intent);

    }

    private  void inicializar() {
        btnRevision = (Button) findViewById(R.id.btnRevision);
        btnRevision.setOnClickListener(this);

        btnNeumatico = (Button) findViewById(R.id.btnNeumaticos);
        btnNeumatico.setOnClickListener(this);


    }


    private  void createToolBar()
    {
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // getSupportActionBar().setIcon(R.mipmap.aumax);


    }

    public  class  BuscarTecnicaTask extends AsyncTask {
        ProgressDialog progress;
        Tecnica tecnica;
        int idPiloto;
        int idCarrera;
        int idCategoria;



        public BuscarTecnicaTask() {
            Constante constante = new Constante(getApplicationContext());
            this.idPiloto = constante.getSelectPiloto().getIdPiloto();
            this.idCarrera = constante.getSelectCarrera().getIdCarrera();
            this.idCategoria =  constante.getSelectCategoria().getIdCategoria();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = ProgressDialog.show(TecnicaActivity.this, "Buscando tecnica", "Espere por favor");


        }

        @Override
        protected Object doInBackground(Object[] params) {

            tecnica = TecnicaWS.getTecnica( idCarrera,idPiloto,idCategoria);


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            progress.dismiss();
            Constante constante = new Constante(getApplicationContext());

            if (tecnica != null)
            {
                constante.setSelectTecnica(tecnica);
                getSupportActionBar().setTitle("Tecnica: " + Long.toString(tecnica.getIdTecnica()));
            }




        }
    }





}
