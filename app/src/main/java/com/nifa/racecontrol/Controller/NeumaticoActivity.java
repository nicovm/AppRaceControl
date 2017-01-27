package com.nifa.racecontrol.Controller;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nifa.racecontrol.Model.Constante;
import com.nifa.racecontrol.Model.Neumatico;
import com.nifa.racecontrol.Model.Observacion;
import com.nifa.racecontrol.Model.Revision;
import com.nifa.racecontrol.Model.TecnicaWS;
import com.nifa.racecontrol.Model.WsResp;
import com.nifa.racecontrol.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bringa on 20/08/2016.
 */
public class NeumaticoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener  {

    private final String KEY_SELECT = "KEY_NEUMATICOS";
    private ArrayList<Neumatico> listNeumatico;
    Dialog customDialog = null;
    NeumaticoAdapter neumaticoBaseAdapter;
    private android.widget.SearchView mSearchView;
    private FloatingActionButton fbtnAdd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnica_elementos);

        if (savedInstanceState == null)
        {
            BuscarNeumaticoTask  neumaticoTask = new BuscarNeumaticoTask();
            neumaticoTask.execute();
        }
        else
        {
            listNeumatico = savedInstanceState.getParcelableArrayList(KEY_SELECT);
            refrescarGrilla();
        }

        inicializar();
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
               // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                neumaticoBaseAdapter.getFilter().filter(s);

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
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Neumatico n =   listNeumatico.get(position);

        mostrarNeumatico(n);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {



        outState.putParcelableArrayList(KEY_SELECT, listNeumatico);

        super.onSaveInstanceState(outState);
    }





    private  void inicializar()
    {
        this.fbtnAdd = (FloatingActionButton) findViewById(R.id.fbtnAdd);
        this.fbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //nuevo neumatico
                mostrarNeumatico(null);

            }
        });
    }

    private void refrescarGrilla() {


        if (listNeumatico != null )
        {
            //RevisionBaseAdapter revisionBaseAdapter = new RevisionBaseAdapter();
            neumaticoBaseAdapter = new NeumaticoAdapter();
            ListView lv = (ListView) findViewById(R.id.lvElemTecnica);

            lv.setAdapter(neumaticoBaseAdapter);

            Constante constante = new Constante(getApplicationContext());
            //SI es usuario no permito que pueda hacer click en la lista
            if (constante.getSesionUsuario().is(Constante.idPerfilUsuario.USUARIO.id())) return;

            lv.setOnItemClickListener(this);

        }

    }

    public void mostrarNeumatico(final Neumatico neumatico)
    {
        final boolean nvoNeumatico ;
        // con este tema personalizado evitamos los bordes por defecto
        customDialog = new Dialog(NeumaticoActivity.this, R.style.Theme_Dialog_Translucent);
        //deshabilitamos el título por defecto
        //customDialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        //obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        customDialog.setContentView(R.layout.dialog_neumatico);

        final EditText etNumNeumatico = (EditText) customDialog.findViewById(R.id.etNumNeumatico);
        final RadioButton rbNuevo = (RadioButton) customDialog.findViewById(R.id.rbNuevo);
        final RadioButton rbUsado = (RadioButton) customDialog.findViewById(R.id.rbUsado);
        //final TextView tvErrorTipoNeumatico = (TextView) customDialog.findViewById(R.id.tvErrorTipoNeumatico);
        //tvErrorTipoNeumatico.setVisibility(View.GONE);



        final Neumatico currentNeumatico;

        if (neumatico != null) {

            final String strNumPrecinto = neumatico.getNumNeumatico() == 0 ? "" : Integer.toString(neumatico.getNumNeumatico());
            etNumNeumatico.setText(strNumPrecinto);

            RadioButton rb = neumatico.isNuevo() ? rbNuevo : rbUsado;
            rb.setChecked(true);

            currentNeumatico = neumatico;
            nvoNeumatico = false;

        }
        else
        {
            currentNeumatico = new Neumatico();
            nvoNeumatico = true;
        }

        Button btnGuardar = (Button) customDialog.findViewById(R.id.btnGuardarNeumatico);


        Constante constante  = new Constante(getApplicationContext());

        if (constante.getSesionUsuario().is(Constante.idPerfilUsuario.USUARIO.id())) btnGuardar.setEnabled(false);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //si no selecciono ningun tipo de neumatico
                if ( !rbNuevo.isChecked() && !rbUsado.isChecked() )
                {
                    Toast.makeText(getApplicationContext(),"Seleccione un tipo de neumático",Toast.LENGTH_SHORT).show();
                    return;
                }

                customDialog.dismiss();


                String strNumNeumatico = etNumNeumatico.getText().toString();
                int nvoNumNeumatico = Integer.parseInt(strNumNeumatico);
                boolean nuevo = rbNuevo.isChecked();

                if (strNumNeumatico.matches("")) currentNeumatico.setNumNeumatico(0);
                else {
                    AddNeumaticoTask addNeumaticoTask;

                    if (nvoNeumatico) addNeumaticoTask  = new AddNeumaticoTask(nvoNumNeumatico,nuevo);
                    else addNeumaticoTask = new AddNeumaticoTask(nvoNumNeumatico,nuevo,neumatico.getNumNeumatico());


                    addNeumaticoTask.execute();
                }
            }
        });

        etNumNeumatico.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    customDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });



        Button btnCancelar = (Button) customDialog.findViewById(R.id.btnCancelarNeumatico);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();

            }
        });

        customDialog.show();

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



    public  class  BuscarNeumaticoTask extends AsyncTask {
        ProgressDialog progress;
        int idPiloto;
        int idCarrera;



        public BuscarNeumaticoTask() {
            Constante constante = new Constante(getApplicationContext());

            this.idPiloto = constante.getSelectPiloto().getIdPiloto();
            this.idCarrera = constante.getSelectCarrera().getIdCarrera();
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = ProgressDialog.show(NeumaticoActivity.this , "Buscando neumaticos", "Espere por favor");


        }

        @Override
        protected Object doInBackground(Object[] params) {

            listNeumatico = TecnicaWS.getAllNeumatico(this.idPiloto,this.idCarrera);


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            refrescarGrilla();
            progress.dismiss();



        }
    }

    public  class  AddNeumaticoTask extends AsyncTask {
        ProgressDialog progress;
        int idPiloto;
        int idCarrera;
        WsResp wsResp;
        int oldNeumatico;
        int newNeumatico;
        boolean agregarNeumatico;
        boolean nuevo;



        public AddNeumaticoTask(int newNeumatico , boolean nuevo) {
            Constante constante = new Constante(getApplicationContext());

            this.idPiloto = constante.getSelectPiloto().getIdPiloto();
            this.idCarrera = constante.getSelectCarrera().getIdCarrera();
            this.newNeumatico = newNeumatico;
            this.agregarNeumatico = true ;
            this.nuevo = nuevo;
        }

        public AddNeumaticoTask(int newNeumatico, boolean nuevo ,int oldNeumatico) {
            Constante constante  = new Constante(getApplicationContext());

            this.idPiloto = constante.getSelectPiloto().getIdPiloto();
            this.idCarrera = constante.getSelectCarrera().getIdCarrera();
            this.newNeumatico = newNeumatico;
            this.oldNeumatico = oldNeumatico;
            this.agregarNeumatico = false ;
            this.nuevo = nuevo;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = ProgressDialog.show(NeumaticoActivity.this , "Buscando neumaticos", "Espere por favor");


        }

        @Override
        protected Object doInBackground(Object[] params) {
            // si esta agregando un neumatico
            if (agregarNeumatico) wsResp = TecnicaWS.addNeumatico(this.newNeumatico,nuevo,this.idPiloto,this.idCarrera);
            else wsResp = TecnicaWS.setNeumatico(this.oldNeumatico,nuevo,this.newNeumatico); //modificando un neumatico


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            String msj;

            if (wsResp != null) msj = wsResp.mensaje;
            else msj = "[wsResp = null]";

            Toast.makeText(getApplicationContext(), msj,Toast.LENGTH_LONG).show();
            progress.dismiss();

            if (wsResp.ok)
            {
                BuscarNeumaticoTask  neumaticoTask = new BuscarNeumaticoTask();
                neumaticoTask.execute();
            }


        }
    }


    public  class  NeumaticoAdapter extends BaseAdapter implements Filterable
    {
        private static final String TAG = "CustomAdapterNeumatico";
        private  int convertViewCounter = 0;
        private LayoutInflater inflater = null;
        private ArrayList<Neumatico> listFilterNeumatico;
        private NeumaticoFilter neumaticoFilter;


        public NeumaticoAdapter() {

            inflater =  LayoutInflater.from(getApplicationContext());
            listFilterNeumatico = listNeumatico;
            getFilter();
        }

        @Override
        public Filter getFilter() {
            if (neumaticoFilter == null ) neumaticoFilter = new NeumaticoFilter();
            return neumaticoFilter;
        }

        class ViewHolder
        {
            TextView tvNumNeumatico;
            TextView tvTipoNeumatico;
            TextView tvFecha;
        }

        @Override
        public int getCount() {
            Log.v(TAG, "in getCount()");
            return listFilterNeumatico.size();
        }

        @Override
        public Object getItem(int position) {
            Log.v(TAG, "in getItem() for position " + position);
            return listFilterNeumatico.get(position);
        }

        @Override
        public long getItemId(int position) {
            Log.v(TAG, "in getItemId() for position " + position);
            return listFilterNeumatico.get(position).getNumNeumatico();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            Log.v(TAG, "in getView for position " + position + ", convertView is "
                    + ((convertView == null) ? "null" : "being recycled"));

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_neumatico, null);
                convertViewCounter++;

                Log.v(TAG, convertViewCounter + " convertViews have been created");

                holder = new ViewHolder();

                holder.tvNumNeumatico = (TextView) convertView.findViewById(R.id.tvNumNeumatico);
                holder.tvFecha = (TextView) convertView.findViewById(R.id.tvFechaNeumatico);
                holder.tvTipoNeumatico = (TextView) convertView.findViewById(R.id.tvTipoNeumatico);


                convertView.setTag(holder);

            } else  holder = (ViewHolder) convertView.getTag();

            Neumatico d = (Neumatico) getItem(position);

            holder.tvNumNeumatico.setText(Integer.toString(d.getNumNeumatico()));

            holder.tvTipoNeumatico.setText(d.getTipoNeumatico());

            holder.tvFecha.setText(d.getFechaString());

            return convertView;
        }


        private  class NeumaticoFilter extends Filter
        {


            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint!=null && constraint.length()>0) {
                    ArrayList<Neumatico> tempList = new ArrayList<Neumatico>();

                    for (Neumatico neumatico : listNeumatico)
                    {
                        if (Integer.toString(neumatico.getNumNeumatico()).contains(constraint.toString().toLowerCase()))
                        {
                            tempList.add(neumatico);
                        }
                    }
                    filterResults.count = tempList.size();
                    filterResults.values = tempList;
                }
                else
                {
                    filterResults.count = listNeumatico.size();
                    filterResults.values = listNeumatico;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFilterNeumatico = (ArrayList<Neumatico>) filterResults.values;
                notifyDataSetChanged();
            }
        }

    }








}
