package and.clasificados.com.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import and.clasificados.com.MainActivity;
import and.clasificados.com.R;
import and.clasificados.com.actividades.Registro;
import and.clasificados.com.fragmentos.Inicio;
import and.clasificados.com.views.EditTextLight;

public class Login extends AppCompatActivity{

    Button registro, sesion;
    ImageView fb;
    TextView olvide;
    EditTextLight usuario, contra;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        agregarToolbar();
        registro = (Button) findViewById(R.id.button_registro);
        sesion = (Button) findViewById(R.id.button_session);
        fb = (ImageView) findViewById(R.id.button_fb);
        olvide = (TextView) findViewById(R.id.olvide);
        usuario = (EditTextLight) findViewById(R.id.user);
        contra = (EditTextLight) findViewById(R.id.pass);
        contra.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL) {
                    login();
                    return true;
                }
                return false;
            }
        });

        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_fb:
                        Toast.makeText(getApplicationContext(), "Iremos a Facebook", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.button_registro:
                        registro();
                        break;
                    case R.id.button_session:
                        login();
                        break;
                    case R.id.olvide:
                        Toast.makeText(getApplicationContext(), "Iremos a cambiar contraseña", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        registro.setOnClickListener(onclick);
        fb.setOnClickListener(onclick);
        sesion.setOnClickListener(onclick);
        olvide.setOnClickListener(onclick);
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_equis);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.title_activity_login);

    }

    public void registro() {
        startActivity(new Intent(this, Registro.class));
    }

    public void login() {
        usuario.setError(null);
        contra.setError(null);
        String idUser = usuario.getText().toString();
        String password = contra.getText().toString();

        boolean cancel = false;
        View focusView = null;
        try {
            //  Usuario usuario = new Usuario();
            if (!password.equals(idUser)) {
                contra.setError(getString(R.string.error_incorrect_password));
                focusView = contra;
                cancel = true;
            }
            if (cancel) {
                focusView.requestFocus();
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
        } catch (Exception e) {
            usuario.setError(getString(R.string.error_invalid_email));
            focusView = usuario;
        }
    }

}