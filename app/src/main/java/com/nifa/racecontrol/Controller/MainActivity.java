package com.nifa.racecontrol.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nifa.racecontrol.Model.Constante;
import com.nifa.racecontrol.Model.LoginWS;
import com.nifa.racecontrol.Model.Torneo;
import com.nifa.racecontrol.Model.Usuario;
import com.nifa.racecontrol.Model.WsResp;
import com.nifa.racecontrol.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Constante constante   = new Constante(getApplicationContext());
        Usuario usuarioSesion = constante.getSesionUsuario();

        // tiene un usuario iniciado
        if (usuarioSesion != null)
        {
            Intent intent = new Intent(getApplicationContext(), TorneosActivity.class);
            startActivity(intent);
        }

        Button btnIniciar = (Button) findViewById(R.id.btnIniciar);

        btnIniciar.setOnClickListener(this);

        inicializar();

    }

    private  void inicializar() {

        etEmail = (EditText) findViewById(R.id.etEmailLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);

        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ingresar();
                }

                return false;
            }
        });


    }

    private  boolean camposCompletos()
    {
        if (etEmail.getText().toString().matches("") || etPassword.getText().toString().matches("") )
        {
            Toast.makeText(this,"Por favor complete los campos obligatorios",Toast.LENGTH_LONG).show();
            return  false;
        }
        return  true;
    }

    private  void ingresar()
    {
        // si no estan los campos completos
        if (!camposCompletos() ) return;
        //intent = new Intent(this,TorneosActivity.class);
        LoginTask loginTask =  new LoginTask(this.etEmail.getText().toString(),this.etPassword.getText().toString());
        loginTask.execute();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case  R.id.btnIniciar:
                ingresar();
                break;
        }

        //if (intent != null  ) startActivity(intent);

    }

    public  class  LoginTask extends AsyncTask {
        ProgressDialog progress;
        String email;
        String password;
        WsResp wsResp;
        Usuario usuario;

        public LoginTask(String email , String password)
        {
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = ProgressDialog.show(MainActivity.this , "Autenticando", "Espere por favor");


        }

        @Override
        protected Object doInBackground(Object[] params) {

            this.wsResp = LoginWS.loginWS(this.email,this.password);


            if (wsResp.ok)
            {
               usuario  = LoginWS.getOneUsuarioWS(this.email);

            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);



            if (wsResp.ok) {
                if (usuario != null) {
                    Constante constante = new Constante(getApplicationContext());
                    constante.setSesionUsuario(usuario);
                    Intent intent = new Intent(getApplicationContext(), TorneosActivity.class);

                    startActivity(intent);

                    finish();
                } else Toast.makeText(getApplicationContext(), "[Error] usuarioSesion = null", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),wsResp.mensaje,Toast.LENGTH_LONG).show();
            }

            progress.dismiss();




        }
    }


}
