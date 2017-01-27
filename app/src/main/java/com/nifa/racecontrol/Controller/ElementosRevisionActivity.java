package com.nifa.racecontrol.Controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nifa.racecontrol.Model.Constante;
import com.nifa.racecontrol.Model.Revision;

import com.nifa.racecontrol.Model.TecnicaWS;
import com.nifa.racecontrol.Model.WsResp;
import com.nifa.racecontrol.R;

import java.util.ArrayList;

/**
 * Created by Bringa on 29/07/2016.
 */
public class ElementosRevisionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private  ArrayList<Revision> listRevision;
    RevisionAdapter revisionBaseAdapter;
    String TAG = "ElementosRevisionActivity";
    private android.widget.SearchView mSearchView;
    private FloatingActionButton fbtnAdd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnica_elementos);

        inicializar();

        if (savedInstanceState == null){
            BuscarRevisionTask buscarRevisionTask = new BuscarRevisionTask();
            buscarRevisionTask.execute();
        }
        else
        {
            listRevision = savedInstanceState.getParcelableArrayList("savedData");


          refrescarGrilla();
        }

        createToolBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);


        MenuItem searchItem = menu.findItem(R.id.action_search);

        mSearchView = (android.widget.SearchView) searchItem.getActionView();

        mSearchView.setQueryHint("Buscar...");
        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        mSearchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                revisionBaseAdapter.getFilter().filter(s);

                return false;
            }
        });



        return  super.onCreateOptionsMenu(menu);

    }



    private  void inicializar() {

        this.fbtnAdd = (FloatingActionButton) findViewById(R.id.fbtnAdd);
        this.fbtnAdd.setVisibility(View.GONE);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    protected void onRestart() {
        super.onRestart();

        BuscarRevisionTask buscarRevisionTask = new BuscarRevisionTask();
        buscarRevisionTask.execute();
    }

    /*
        @Override
    public void onBackPressed() {
        //super.onBackPressed();

        /*
          AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estas seguro que desea salir de la revisión técnica?")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ElementosRevisionActivity.this.finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();



     */

    private void refrescarGrilla() {

        if (listRevision != null )
        {
            try {
                //RevisionBaseAdapter revisionBaseAdapter = new RevisionBaseAdapter();
                revisionBaseAdapter = new RevisionAdapter();
                ListView lv = (ListView) findViewById(R.id.lvElemTecnica);

                lv.setAdapter(revisionBaseAdapter);

                lv.setOnItemClickListener(this);
            }
            catch (Exception ex)
            {
                Log.e(TAG,"EXCEPTION: " + ex.getMessage());
            }


        }

    }

    public  class  BuscarRevisionTask extends AsyncTask {
        ProgressDialog progress;
        long idTecnica;




        public BuscarRevisionTask() {
            Constante constante = new Constante(getApplicationContext());
            this.idTecnica = constante.getSelectTecnica().getIdTecnica();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = ProgressDialog.show(ElementosRevisionActivity.this , "Buscando revision", "Espere por favor");


        }

        @Override
        protected Object doInBackground(Object[] params) {

            listRevision = TecnicaWS.getRevision(idTecnica);


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            refrescarGrilla();
            progress.dismiss();



        }
    }

    public  class  SaveRevisionTask extends AsyncTask {
        ProgressDialog progress;
        WsResp resp;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = ProgressDialog.show(ElementosRevisionActivity.this , "Guardar", "Espere por favor");


        }

        @Override
        protected Object doInBackground(Object[] params) {

            resp = TecnicaWS.saveRevision(revisionBaseAdapter.getData());


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (resp.ok)  refrescarGrilla();


            Toast.makeText(getApplicationContext(),resp.mensaje,Toast.LENGTH_LONG).show();
            progress.dismiss();



        }
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

        getSupportActionBar().setDisplayShowTitleEnabled(false);





        // getSupportActionBar().setIcon(R.mipmap.aumax);
        //getSupportActionBar().setTitle(this.revisionSelec.getNombre());

    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("savedData", listRevision);
        super.onSaveInstanceState(outState);
    }


    public class RevisionAdapter extends BaseAdapter implements Filterable {

        private static final String TAG = "CustomAdapterRevision";
        private  int convertViewCounter = 0;
        private ArrayList<Revision> listFilterRevision;
        private RevisionFilter revisionFilter;

        public ArrayList<Revision> getData() {
            return listRevision;
        }

        private LayoutInflater inflater = null;

        Dialog customDialog = null;

        public RevisionAdapter() {

            this.inflater = LayoutInflater.from(ElementosRevisionActivity.this);
            listFilterRevision = listRevision;
            getFilter();

        }


        class ViewHolder {

             TextView tvNombre;
             TextView tvPrecinto;
             TextView tvUltimoPrecinto;
             ImageButton imgBtnEdit;
             TextView tvObservacionRevision;


        }



        @Override
        public int getCount() {
            Log.v(TAG, "in getCount()");
            return listFilterRevision.size();
        }

        @Override
        public Object getItem(int position) {
            Log.v(TAG, "in getItem() for position " + position);
            return listFilterRevision.get(position);
        }

        @Override
        public long getItemId(int position) {
            Log.v(TAG, "in getItemId() for position " + position);
            return listFilterRevision.get(position).getIdRevision();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            Log.v(TAG, "in getView for position " + position + ", convertView is "
                    + ((convertView == null) ? "null" : "being recycled"));

            if (convertView == null) {
                try {
                    convertView = inflater.inflate(R.layout.item_revision, null);

                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
                convertViewCounter++;

                Log.v(TAG, convertViewCounter + " convertViews have been created");

                holder = new ViewHolder();

                holder.tvNombre = (TextView) convertView.findViewById(R.id.tvNombreRevision);
                holder.tvPrecinto =  (TextView) convertView.findViewById(R.id.tvPrecinto);
                holder.tvUltimoPrecinto = (TextView) convertView.findViewById(R.id.tvUltimoPrecinto);

               // holder.cbOk = (CheckBox) convertView.findViewById(R.id.cbRevisionOk);
                //if (Sesion.is(Constante.idPerfilUsuario.USUARIO)) holder.cbOk.setEnabled(false);
                //holder.cbOk.setOnClickListener(checkListener);

                holder.imgBtnEdit = (ImageButton) convertView.findViewById(R.id.imgBtnEdit);
                holder.imgBtnEdit.setOnClickListener(btnListener);

                holder.tvObservacionRevision = (TextView) convertView.findViewById(R.id.tvObservacionRevision);



                convertView.setTag(holder);

            } else holder = (ViewHolder) convertView.getTag();

            // Para porde hacer click en el checkbox
            Revision revision = (Revision) getItem(position);
            //holder.cbOk.setTag(d);
            holder.imgBtnEdit.setTag(revision);

            holder.tvNombre.setText(revision.getNombre());

            String tmpPrecinto = "Precinto: ";
            String tmpUltimoPrecinto = " - ";

            //Si tiene precinto
            if (revision.precinto != null)
            {
                tmpPrecinto += revision.getPrecinto().getIdPrecinto();
                tmpUltimoPrecinto = "Ultimo Precinto: " + revision.precinto.getCarrera().getNombre();

            }

            holder.tvPrecinto.setText(tmpPrecinto);
            holder.tvUltimoPrecinto.setText(tmpUltimoPrecinto);


            Constante constante  = new Constante(getApplicationContext());

           //String strPrecinto =  Constante.empty(d.getPrecinto()) ? "-" : d.getPrecinto();

            //holder.tvPrecinto.setText("Precinto: " + strPrecinto);
           // holder.cbOk.setChecked(d.isOk());

            //holder.tvObservacionRevision.setText(d.getObservacion());


            //Ocultar la observacion si es usuario
            if (constante.getSesionUsuario().is(Constante.idPerfilUsuario.USUARIO.id()) )
            {
                holder.tvObservacionRevision.setVisibility(View.GONE);
            }
            else // sino es usuario'
            {
                int obsPendientes=  revision.getCantObserPrendientes();
                // si no tiene observaciones pendientes
                if ( obsPendientes ==0)
                {
                    holder.tvObservacionRevision.setVisibility(View.GONE);
                }
               else
                {
                    //Tiene observacion
                    holder.tvObservacionRevision.setText("Observaciones " + Integer.toString(obsPendientes));
                    holder.tvObservacionRevision.setVisibility(View.VISIBLE);
                }

            }

            return convertView;
        }

        private View.OnClickListener checkListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Revision d = (Revision) v.getTag();
                d.setOk(!d.isOk());
            }
        };

        private View.OnClickListener btnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Revision d = (Revision) v.getTag();
                //mostrarObservacion(d);
                Intent intent = new Intent(getApplicationContext(),RevisionActivity.class);
                intent.putExtra("revisionSelect",d);

                startActivity(intent);


            }
        };


        @Override
        public Filter getFilter() {
            if (revisionFilter == null ) revisionFilter = new RevisionFilter();
            return revisionFilter;
        }

       private  class RevisionFilter extends Filter
       {


           @Override
           protected FilterResults performFiltering(CharSequence constraint) {
               FilterResults filterResults = new FilterResults();
               if (constraint!=null && constraint.length()>0) {
                   ArrayList<Revision> tempList = new ArrayList<Revision>();

                    for (Revision revision : listRevision)
                    {
                        if (revision.getNombre().toLowerCase().contains(constraint.toString().toLowerCase()))
                        {
                            tempList.add(revision);
                        }
                    }
                   filterResults.count = tempList.size();
                   filterResults.values = tempList;
               }
               else
               {
                   filterResults.count = listRevision.size();
                   filterResults.values = listRevision;
               }
               return filterResults;
           }

           @Override
           protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
               listFilterRevision = (ArrayList<Revision>) filterResults.values;
               notifyDataSetChanged();
           }
       }



    }

}
