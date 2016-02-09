package and.clasificados.com.actividades;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import and.clasificados.com.R;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class  Registro extends AppCompatActivity{

ImageView registro;


@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        registro=(ImageView) findViewById(R.id.button_registro);
        registro.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(getString(R.string.exito_registro))
                .setContentText(getString(R.string.sub_exito_registro))
                .setConfirmText(getString(R.string.login))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sDialog) {
                startActivity(new Intent(getApplicationContext(), Login.class));}
                });
        }
        });
        agregarToolbar();
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

}
