package com.nifa.racecontrol.Controller;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nifa.racecontrol.Model.Constante;
import com.nifa.racecontrol.Model.Observacion;
import com.nifa.racecontrol.Model.Precinto;
import com.nifa.racecontrol.Model.Revision;
import com.nifa.racecontrol.Model.TecnicaWS;
import com.nifa.racecontrol.Model.WsResp;
import com.nifa.racecontrol.R;

import java.util.Date;

/**
 * Created by Bringa on 27/09/2016.
 */
public class RevisionActivity extends AppCompatActivity {
    private final String KEY_SELECT = "KEY_REVISION";
    private final String TAG = "RevisionActivity";

    //Revision seleccionada
    private Revision revisionSelec = null;
    private TextView tvPrecinto;
    private  TextView tvUltimoPrecinto;
    private  TextView tvCantObservaciones;
    private ImageButton imgBtnEdit;
    private ListView lvObservaciones;
    private FloatingActionButton fbtnAddObs;
    private ObservacionAdapter observacionAdapter;
    private boolean guardarCambios = false;

    Dialog customDialog = null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_revision);






        if (savedInstanceState == null){
            Intent  i = getIntent();
            i.setExtrasClassLoader(Revision.class.getClassLoader());

            this.revisionSelec = (Revision) i.getParcelableExtra("revisionSelect");
        }
        else
        {
            this.revisionSelec = (Revision) savedInstanceState.getParcelable(KEY_SELECT);
        }


        inicializar();
        createToolBar();
        consultar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_revision,menu);


        return  super.onCreateOptionsMenu(menu);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_cancelar:
                Log.i(TAG,"Click menu_cancelar");

                if (guardarCambios)
                {
                    //Pregunto si quiero guardar los cambios realizados
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("¿Desea guardar los cambios realizados?")
                            .setCancelable(false)
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                   GuardarRevisionTask task = new GuardarRevisionTask(true);
                                    task.execute();
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
                else   finish();


                break;

            case  R.id.menu_guardar:
                Log.i(TAG,"Click menu_guardar");
                GuardarRevisionTask task = new GuardarRevisionTask(true);
                task.execute();
                break;

        }
        return true;
    }

    private void inicializar()
    {
        this.tvPrecinto = (TextView) findViewById(R.id.tvPrecinto);
        this.tvUltimoPrecinto = (TextView) findViewById(R.id.tvUltimoPrecinto);

        this.imgBtnEdit = (ImageButton) findViewById(R.id.imgBtnEdit);
        this.imgBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nvoNumPrecinto();
            }
        });

        this.tvCantObservaciones = (TextView) findViewById(R.id.tvCantObservaciones);


        this.fbtnAddObs = (FloatingActionButton) findViewById(R.id.fbtnAddObs);
        this.fbtnAddObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getApplicationContext(),"click float button",Toast.LENGTH_LONG).show();
                addObservacion();


            }
        });


    }


    private void consultar()
    {
        if (this.revisionSelec==null) return;

        String tmpPrecinto = "-" ;
        String tmpUltimoPrecinto = "-";
        String tmpCantidadObs = "0";

        if (this.revisionSelec.precinto != null)
        {
            tmpPrecinto = this.revisionSelec.precinto.getIdPrecinto();
            tmpUltimoPrecinto = "Último precinto: " + this.revisionSelec.precinto.getCarrera().getNombre();

        }

        getSupportActionBar().setTitle(this.revisionSelec.getNombre());

        this.tvPrecinto.setText(tmpPrecinto);
        this.tvUltimoPrecinto.setText(tmpUltimoPrecinto);

        if (this.revisionSelec.getListObservacion() != null) tmpCantidadObs = Integer.toString(this.revisionSelec.getListObservacion().size());
        this.tvCantObservaciones.setText(tmpCantidadObs);


        refrescarGrilla();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(KEY_SELECT, this.revisionSelec);

        super.onSaveInstanceState(outState);
    }

    private void refrescarGrilla()
    {

        // si tiene observaciones
        if (this.revisionSelec.getListObservacion() != null)
        {
            observacionAdapter = new ObservacionAdapter();
            lvObservaciones = (ListView) findViewById( R.id.lvObservaciones);

            this.lvObservaciones.setAdapter(observacionAdapter);

        }

    }


    public void nvoNumPrecinto()
    {
        // con este tema personalizado evitamos los bordes por defecto
        customDialog = new Dialog(RevisionActivity.this, R.style.Theme_Dialog_Translucent);
        //deshabilitamos el título por defecto
        //customDialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        //obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        customDialog.setContentView(R.layout.dialog_precintado);

        final EditText etNumPrecinto = (EditText) customDialog.findViewById(R.id.etNumPrecintado);



        Button btnGuardar = (Button) customDialog.findViewById(R.id.btnGuardarPrecintado);

        Constante constante  = new Constante(getApplicationContext());
        if (constante.getSesionUsuario().is(Constante.idPerfilUsuario.USUARIO.id())) btnGuardar.setEnabled(false);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();

                String strNumPrecinto = etNumPrecinto.getText().toString();

                if (strNumPrecinto.matches(""))
                {
                    Toast.makeText(getApplicationContext(),"Ingrese el nuevo precinto",Toast.LENGTH_LONG).show();
                    return;
                }

                VerificacionPrecinto verificacionPrecinto = new VerificacionPrecinto(strNumPrecinto);

                verificacionPrecinto.execute();


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

    /**
     * Permite agregar una nueva observacion a la revision
     */
    public void addObservacion()
    {
        // con este tema personalizado evitamos los bordes por defecto
        customDialog = new Dialog(RevisionActivity.this, R.style.Theme_Dialog_Translucent);
        //deshabilitamos el título por defecto
        //customDialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        //obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        customDialog.setContentView(R.layout.dialog_observacion);

        final EditText etObs = (EditText) customDialog.findViewById(R.id.etObservacionRevision);



        Button btnGuardar = (Button) customDialog.findViewById(R.id.btnGuardarPrecintado);

        Constante constante  = new Constante(getApplicationContext());
        if (constante.getSesionUsuario().is(Constante.idPerfilUsuario.USUARIO.id())) btnGuardar.setEnabled(false);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();

                String strObs = etObs.getText().toString();

                if (strObs.matches(""))
                {
                    Toast.makeText(getApplicationContext(),"Ingrese una nueva observación",Toast.LENGTH_LONG).show();
                    return;
                }

              //Agrego la nueva observacion a la lista de observaciones de la revision seleccionada
                //creo la nueva observacion
                Date curDate = new Date();
                Constante constante = new Constante(getApplicationContext());
                int idUsuario = constante.getSesionUsuario().idUsuario;

                //el idObservacion es 0 porque todavia no se guardo la observacion
                Observacion obs = new Observacion(strObs,0,revisionSelec.getIdRevision(),new Date(), constante.getSesionUsuario().idUsuario,constante.getSelectCarrera());


                //agrego la observacion a la lista

                revisionSelec.getListObservacion().add(obs);

                String cantObs = Integer.toString( revisionSelec.getListObservacion().size());
               tvCantObservaciones.setText(cantObs);


                observacionAdapter.notifyDataSetChanged();
                guardarCambios = true;

            }
        });

        etObs.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        getSupportActionBar().setTitle(this.revisionSelec.getNombre());

    }


    public class ObservacionAdapter extends BaseAdapter
    {

        private LayoutInflater inflater = null;

        public ObservacionAdapter() {

            this.inflater = LayoutInflater.from(RevisionActivity.this);
        }

        class ViewHolder {

            TextView tvCarreraObservacion;
            TextView tvDrescipcionObservacion;
            CheckBox cbObservacionOK;

        }




        @Override
        public int getCount() {

            return revisionSelec.getListObservacion() == null ? 0: revisionSelec.getListObservacion().size();
        }

        @Override
        public Object getItem(int position) {
            return revisionSelec.getListObservacion() == null ? null: revisionSelec.getListObservacion().get(position);
        }

        @Override
        public long getItemId(int position) {
            return revisionSelec.getListObservacion() == null ? null: revisionSelec.getListObservacion().get(position).getIdObservacion();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;



            if (convertView == null) {

                convertView = inflater.inflate(R.layout.item_observacion, null);

                holder = new ViewHolder();

                holder.tvCarreraObservacion = (TextView) convertView.findViewById(R.id.tvCarreraObservacion);
                holder.tvDrescipcionObservacion = (TextView) convertView.findViewById(R.id.tvDrescipcionObservacion);
                holder.cbObservacionOK = (CheckBox) convertView.findViewById(R.id.cbObservacionOK);
                holder.cbObservacionOK.setOnClickListener(checkListener);



                convertView.setTag(holder);

            }else holder = (ViewHolder) convertView.getTag();

            Observacion obs = (Observacion) getItem(position);

            holder.tvCarreraObservacion.setText(obs.getCarrera().getNombre());
            holder.tvDrescipcionObservacion.setText(obs.getDescripcion());
            holder.cbObservacionOK.setChecked(obs.isOk());
            holder.cbObservacionOK.setTag(obs);

            //holder.tvDrescipcionObservacion


            return convertView;


        }

        private View.OnClickListener checkListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observacion d = (Observacion) v.getTag();

                // si la observacion no estaba confirmada
                if (!d.isOk())
                {
                   // guardar el id de revision en la cual se confirmo la revision
                    d.setIdRevisionOk(revisionSelec.getIdRevision());
                    //guardo el id usuario que lo confirmo
                    Constante constante = new Constante(getApplicationContext());
                    int idUsuario = constante.getSesionUsuario().idUsuario;
                    d.setIdUsuarioOk(idUsuario);
                }
                d.setOk(!d.isOk());
            }
        };
    }


    public class  VerificacionPrecinto extends AsyncTask
    {
        String idPrecinto;
        WsResp resp;

        public VerificacionPrecinto(String idPrecinto) {

            this.idPrecinto = idPrecinto;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            resp = TecnicaWS.verificacionPrecinto(this.idPrecinto);



            return null;
        }

        @Override
        protected void onPostExecute(Object o) {

            if (resp.ok)
            {
                //color el nuevo numero de precinto agregado
                tvPrecinto.setText(idPrecinto);
                tvUltimoPrecinto.setText("Nuevo precinto");

                Constante constante  = new Constante(getApplicationContext());

                Precinto precinto    = new Precinto(idPrecinto,revisionSelec.getIdRevision(),new Date(),
                        constante.getSesionUsuario().getIdUsuario(),constante.getSelectCarrera());

                //remplazo el precinto
                revisionSelec.setPrecinto(precinto);

                customDialog.dismiss();
                guardarCambios = true;
            }
            else
            {
                Toast.makeText(getApplicationContext(),resp.mensaje,Toast.LENGTH_LONG).show();
            }



            super.onPostExecute(o);
        }


    }

    public  class  GuardarRevisionTask extends  AsyncTask
    {
        private boolean cerrarRevision;
        private WsResp resp;

        public GuardarRevisionTask(boolean cerrarRevision) {
            this.cerrarRevision = cerrarRevision;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            resp = TecnicaWS.guardarRevision(revisionSelec);



            guardarCambios = false;

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (resp != null)
            {
                Toast.makeText(getApplicationContext(),resp.mensaje,Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(getApplicationContext(),"Error: resp == null",Toast.LENGTH_SHORT).show();

            // si luego de guardar tiene que cerrar la pantalla y si la respuesta fue exitosa
            // cierro la actividad
            if (cerrarRevision && resp.ok) finish();



        }


    }

}
