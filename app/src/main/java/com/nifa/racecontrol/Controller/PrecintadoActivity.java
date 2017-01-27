package com.nifa.racecontrol.Controller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nifa.racecontrol.Model.Constante;
import com.nifa.racecontrol.Model.Precintado;

import com.nifa.racecontrol.Model.TecnicaWS;
import com.nifa.racecontrol.Model.WsResp;
import com.nifa.racecontrol.R;

import java.util.List;

/**
 * Created by Bringa on 04/08/2016.
 */
public class PrecintadoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<Precintado> listPrecintado;
    Dialog customDialog = null;
    PrecintadoAdapter precintadoBaseAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnica_elementos);

        BuscarPrecintadoTask buscarPrecintadoTask = new BuscarPrecintadoTask();
        buscarPrecintadoTask.execute();

        inicializar();

    }

    private  void inicializar()
    {

        /*
         Button btnGuardar = (Button) findViewById(R.id.btnGuardarAgrElemTecnica);

        if (Sesion.is(Constante.idPerfilUsuario.USUARIO))
        {
            btnGuardar.setVisibility(View.GONE);
            return;
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SavePrecintadoTask savePrecintadoTask = new SavePrecintadoTask();

                savePrecintadoTask.execute();

            }
        });

        if (Sesion.usuarioSesion.idPerfilUsuario
                == Constante.idPerfilUsuario.USUARIO.id()) btnGuardar.setVisibility(View.GONE);
         */


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Precintado precintado   = listPrecintado.get(position);
        mostrarNumPrecinto(precintado);

    }

    private void refrescarGrilla() {

        if (listPrecintado != null )
        {
            //RevisionBaseAdapter revisionBaseAdapter = new RevisionBaseAdapter();
             precintadoBaseAdapter = new PrecintadoAdapter();
            ListView lv = (ListView) findViewById(R.id.lvElemTecnica);

            lv.setAdapter(precintadoBaseAdapter);

            Constante constante  = new Constante(getApplicationContext());
            //SI es usuario no permito que pueda hacer click en la lista
           if (constante.getSesionUsuario().is(Constante.idPerfilUsuario.USUARIO.id())) return;

            lv.setOnItemClickListener(this);

        }

    }

    public void mostrarNumPrecinto(final Precintado precintado)
    {
        // con este tema personalizado evitamos los bordes por defecto
        customDialog = new Dialog(PrecintadoActivity.this, R.style.Theme_Dialog_Translucent);
        //deshabilitamos el título por defecto
        //customDialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        //obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        customDialog.setContentView(R.layout.dialog_precintado);

         final EditText etNumPrecinto = (EditText) customDialog.findViewById(R.id.etNumPrecintado);

        final String strNumPrecinto  = precintado.getNumero() == 0 ? "": Integer.toString(precintado.getNumero());
        etNumPrecinto.setText(strNumPrecinto);

        Button btnGuardar = (Button) customDialog.findViewById(R.id.btnGuardarPrecintado);

        Constante constante  = new Constante(getApplicationContext());
        if (constante.getSesionUsuario().is(Constante.idPerfilUsuario.USUARIO.id())) btnGuardar.setEnabled(false);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();

               String strNumPrecinto = etNumPrecinto.getText().toString();

                if (strNumPrecinto.matches("")) precintado.setNumero(0);
                else precintado.setNumero(Integer.parseInt(etNumPrecinto.getText().toString()));

                precintadoBaseAdapter.notifyDataSetChanged();
            }
        });

        etNumPrecinto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    customDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        Button btnCancelar = (Button) customDialog.findViewById(R.id.btnCancelarPrecintado);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();

            }
        });

        customDialog.show();

    }

    public  class  BuscarPrecintadoTask extends AsyncTask {
        ProgressDialog progress;
        int idPiloto;
        int idCarrera;



        public BuscarPrecintadoTask() {
            Constante constante = new Constante(getApplicationContext());
            this.idPiloto = constante.getSelectPiloto().getIdPiloto();
            this.idCarrera = constante.getSelectCarrera().getIdCarrera();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = ProgressDialog.show(PrecintadoActivity.this , "Buscando revision", "Espere por favor");


        }

        @Override
        protected Object doInBackground(Object[] params) {

            listPrecintado = TecnicaWS.getPrecintado(PrecintadoActivity.this,idCarrera,idPiloto);


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            refrescarGrilla();
            progress.dismiss();



        }
    }

    public  class  SavePrecintadoTask extends AsyncTask {
        ProgressDialog progress;
            WsResp resp;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = ProgressDialog.show(PrecintadoActivity.this , "Guardar", "Espere por favor");


        }

        @Override
        protected Object doInBackground(Object[] params) {

            resp = TecnicaWS.savePrecintado(listPrecintado);

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


    public  class  PrecintadoAdapter extends BaseAdapter
    {
        private static final String TAG = "CustomAdapterPrecintado";
        private  int convertViewCounter = 0;
        private LayoutInflater inflater = null;

        public PrecintadoAdapter() {

            inflater =  LayoutInflater.from(getApplicationContext());
        }

        class ViewHolder
        {
            TextView tvNombre;
            TextView tvNunPrecinto;

        }

        @Override
        public int getCount() {
            Log.v(TAG, "in getCount()");
            return listPrecintado.size();
        }

        @Override
        public Object getItem(int position) {
            Log.v(TAG, "in getItem() for position " + position);
            return listPrecintado.get(position);
        }

        @Override
        public long getItemId(int position) {
            Log.v(TAG, "in getItemId() for position " + position);
            return listPrecintado.get(position).getIdPrecintado();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            Log.v(TAG, "in getView for position " + position + ", convertView is "
                    + ((convertView == null) ? "null" : "being recycled"));

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_precintado, null);
                convertViewCounter++;

                Log.v(TAG, convertViewCounter + " convertViews have been created");

                holder = new ViewHolder();

                holder.tvNombre = (TextView) convertView.findViewById(R.id.tvNombrePrecintado);
                holder.tvNunPrecinto = (TextView) convertView.findViewById(R.id.tvNumPrecinto);

                convertView.setTag(holder);

            } else  holder = (ViewHolder) convertView.getTag();

            Precintado d = (Precintado) getItem(position);

            holder.tvNombre.setText(d.getNombre());


            String numeroPrecinto = d.getNumero() == 0 ? "-" : Integer.toString(d.getNumero());

            holder.tvNunPrecinto.setText(numeroPrecinto);

            return convertView;
        }
    }




}
