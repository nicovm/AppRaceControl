package com.nifa.racecontrol.Controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nifa.racecontrol.Model.CabeceraLayout;
import com.nifa.racecontrol.Model.Constante;
import com.nifa.racecontrol.Model.Neumatico;
import com.nifa.racecontrol.Model.Piloto;
import com.nifa.racecontrol.Model.PilotoWS;
import com.nifa.racecontrol.Model.Revision;
import com.nifa.racecontrol.R;

import java.util.ArrayList;

/**
 * Created by Bringa on 05/07/2016.
 */

public class PilotoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<Piloto> listPiloto;
    private android.widget.SearchView mSearchView;
    private PilotoBaseAdapter myAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion);

        CabeceraLayout cl = (CabeceraLayout) findViewById(R.id.cabSeleccion);
        cl.setIdActivity(Constante.IdActivity.PILOTO);

        cl.setOnClickListenerCab(new CabeceraLayout.OnClickListenerCab() {
            @Override
            public void onClick(Class c) {
                Intent intent = new Intent(getApplicationContext(),c);
                startActivity(intent);
            }
        });

        Constante constante = new Constante(getApplicationContext());

        BuscarPilotosTask buscarPilotosTask = new BuscarPilotosTask(constante.getSelectCategoria().getIdCategoria());
        buscarPilotosTask.execute();

        inicializar();
        createToolBar();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);


        MenuItem searchItem = menu.findItem(R.id.action_search);

        mSearchView = (android.widget.SearchView) searchItem.getActionView();

        mSearchView.setQueryHint("Buscar piloto...");
        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        mSearchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                myAdapter.getFilter().filter(s);

                return false;
            }
        });



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
    Constante constante = new Constante(getApplicationContext());

        constante.setSelectPiloto(listPiloto.get(position));
        Intent intent = new Intent(this,TecnicaActivity.class);
        startActivity(intent);

    }

    private void  inicializar()
    {
        FloatingActionButton fbtnAdd = (FloatingActionButton) findViewById(R.id.fbtnAdd);
        fbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Nuevo usuario",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void refrescarGrilla() {

        // TextView tvSinRegistros = (TextView) findViewById(R.id.tvSinRegistroPilotos);
        if (listPiloto != null )
        {
            // if (listPiloto.size() == 0 )  tvSinRegistros.setVisibility(View.VISIBLE);


            myAdapter = new PilotoBaseAdapter();

            ListView lv = (ListView) findViewById(R.id.lvSeleccion);

            lv.setAdapter(myAdapter);

            lv.setOnItemClickListener(this);


        }
        else {
            //tvSinRegistros.setVisibility(View.VISIBLE);
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
        // getSupportActionBar().setIcon(R.mipmap.aumax);


    }


    public  class  BuscarPilotosTask extends AsyncTask {
        ProgressDialog progress;
        int idCategoria;



        public BuscarPilotosTask(int idCategoria) {
            this.idCategoria = idCategoria;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = ProgressDialog.show(PilotoActivity.this , "Buscando pilotos en la categoria", "Espere por favor");


        }

        @Override
        protected Object doInBackground(Object[] params) {

            listPiloto = PilotoWS.getAll(PilotoActivity.this,idCategoria );


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            refrescarGrilla();
            progress.dismiss();



        }
    }

    public  class PilotoBaseAdapter extends BaseAdapter implements Filterable
    {

        private ArrayList<Piloto> listFilter;
        private ItemFilter itemFilter;

        public PilotoBaseAdapter() {
            listFilter = listPiloto;
            getFilter();
        }

        class ViewHolder {

            TextView tvTitulo;
            TextView tvSubTitulo;

        }



        @Override
        public int getCount() {
            return listFilter == null ? 0 : listFilter.size();
        }

        @Override
        public Object getItem(int position) {
            return listFilter.get(position);
        }

        @Override
        public long getItemId(int position) {
            return listFilter.get(position).getIdPiloto();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {

                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = vi.inflate( R.layout.item_info,null);

                holder = new ViewHolder();
                holder.tvTitulo = (TextView)convertView.findViewById(R.id.tvTituloInfo);
                holder.tvSubTitulo = (TextView) convertView.findViewById(R.id.tvSubInfo);

                convertView.setTag(holder);

            }  else holder = (ViewHolder) convertView.getTag();


            Piloto  piloto = (Piloto)getItem(position);

            holder.tvTitulo.setText(piloto.getNombre() + " " + piloto.getApellido());
            holder.tvSubTitulo.setText("DNI: " + Integer.toString(piloto.getDni()));

            ImageView ivInfo = (ImageView) convertView.findViewById(R.id.imgInfo);
            ivInfo.setImageResource(R.drawable.img_pilotos);


            return convertView;
        }


        @Override
        public Filter getFilter() {
            if (itemFilter == null ) itemFilter = new ItemFilter();
            return itemFilter;
        }
        private  class ItemFilter extends Filter
        {


            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint!=null && constraint.length()>0) {
                    ArrayList<Piloto> tempList = new ArrayList<Piloto>();

                    for (Piloto piloto : listPiloto)
                    {
                        if (piloto.getNombre().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                piloto.getApellido().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                Integer.toString(piloto.getDni()).contains(constraint.toString().toLowerCase()))
                        {
                            tempList.add(piloto);
                        }
                    }
                    filterResults.count = tempList.size();
                    filterResults.values = tempList;
                }
                else
                {
                    filterResults.count = listPiloto.size();
                    filterResults.values = listPiloto;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFilter = (ArrayList<Piloto>) filterResults.values;
                notifyDataSetChanged();
            }
        }

    }


}
