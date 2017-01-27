package com.nifa.racecontrol.Controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
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
import com.nifa.racecontrol.Model.Categoria;
import com.nifa.racecontrol.Model.Constante;
import com.nifa.racecontrol.Model.DrawerItem;
import com.nifa.racecontrol.Model.Torneo;
import com.nifa.racecontrol.Model.TorneoWS;
import com.nifa.racecontrol.R;

import java.util.ArrayList;

/**
 * Created by Bringa on 30/06/2016.
 */
public class TorneosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<Torneo> listTorneos;
    String tagTitles[];
    DrawerLayout drawerLayout;
    ListView drawerList;
    ArrayList<DrawerItem> drawerItems;
    private  TorneosBaseAdapter myAdapter;
    private android.widget.SearchView mSearchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion);

        CabeceraLayout cl = (CabeceraLayout) findViewById(R.id.cabSeleccion);
        cl.setIdActivity(Constante.IdActivity.TORNEO);



        BuscarTorneoTask buscarTorneoTask = new BuscarTorneoTask();
        buscarTorneoTask.execute();

        inicializar();
        createToolBar();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);


        MenuItem searchItem = menu.findItem(R.id.action_search);

        mSearchView = (android.widget.SearchView) searchItem.getActionView();

        mSearchView.setQueryHint("Buscar categoría...");
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

        Torneo  torneo = listTorneos.get(position);
        //Constante.SELECT_TORNEO = torneo;
        Constante constante  = new Constante(getApplicationContext());
        constante.setSelectTorneo(torneo);
        Intent intent = new Intent(this,CategoriasActivity.class);
        startActivity(intent);

    }

    private void  inicializar()
    {
        FloatingActionButton  fbtnAdd = (FloatingActionButton) findViewById(R.id.fbtnAdd);
        fbtnAdd.setVisibility(View.GONE);

    }

    private void refrescarGrilla() {

        if (listTorneos != null )
        {
            myAdapter = new TorneosBaseAdapter();

            ListView lvTorneo = (ListView) findViewById(R.id.lvSeleccion);

            lvTorneo.setAdapter(myAdapter);

            lvTorneo.setOnItemClickListener(this);

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


    public class DrawerListAdapter extends  BaseAdapter {


        @Override
        public int getCount() {
            return drawerItems.size();
        }

        @Override
        public Object getItem(int position) {
            return drawerItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)parent.getContext().
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_menu, null);
            }


            ImageView icon = (ImageView) convertView.findViewById(R.id.imgItemMenu);
            TextView name = (TextView) convertView.findViewById(R.id.tvItemMenu);

            DrawerItem item = drawerItems.get(position);
            icon.setImageResource(item.getIconId());
            name.setText(item.getName());

            return convertView;
        }
    }


    public  class  BuscarTorneoTask extends AsyncTask {
        ArrayList<Torneo> torneos;
        ProgressDialog progress;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = ProgressDialog.show(TorneosActivity.this , "Buscando Torneos", "Espere por favor");


        }

        @Override
        protected Object doInBackground(Object[] params) {

            listTorneos = TorneoWS.getAll(TorneosActivity.this);


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            refrescarGrilla();
            progress.dismiss();



        }
    }

    public  class TorneosBaseAdapter extends BaseAdapter implements Filterable
    {
        private ArrayList<Torneo> listFilter;
        private ItemFilter itemFilter;

        public TorneosBaseAdapter() {

            listFilter = listTorneos;
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
            return listFilter.get(position).getIdTorneo();
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

            } else holder = (ViewHolder) convertView.getTag();

            //ListView lvTorneos = (ListView) findViewById(R.id.lvTorneos);

            TextView tvTitulo = (TextView) convertView.findViewById(R.id.tvTituloInfo);

            Torneo torneo = (Torneo) getItem(position);

            tvTitulo.setText(torneo.getNombre());
            ImageView ivInfo = (ImageView) convertView.findViewById(R.id.imgInfo);
            ivInfo.setImageResource(R.drawable.img_torneos);

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
                    ArrayList<Torneo> tempList = new ArrayList<Torneo>();

                    for (Torneo torneo : listTorneos)
                    {
                        if (torneo.getNombre().toLowerCase().contains(constraint.toString().toLowerCase()))
                        {
                            tempList.add(torneo);
                        }
                    }
                    filterResults.count = tempList.size();
                    filterResults.values = tempList;
                }
                else
                {
                    filterResults.count = listTorneos.size();
                    filterResults.values = listTorneos;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFilter = (ArrayList<Torneo>) filterResults.values;
                notifyDataSetChanged();
            }
        }
    }






}
