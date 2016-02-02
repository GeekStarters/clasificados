package and.clasificados.com.Actividades;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;

import and.clasificados.com.MainActivity;
import and.clasificados.com.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Registro  extends com.blunderer.materialdesignlibrary.activities.Activity{

    ImageView registro;
    @Override
    protected int getContentView() {
        return R.layout.activity_registro;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContentView();
        registro=(ImageView) findViewById(R.id.button_registro);
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(getString(R.string.exito_registro))
                        .setContentText(getString(R.string.sub_exito_registro))
                        .setConfirmText(getString(R.string.login))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                startActivity(new Intent(getApplicationContext(), Login.class));
                            }
                        });
            }
        });
    }
    @Override
    protected boolean enableActionBarShadow() {
        return false;
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return null;
    }
}
