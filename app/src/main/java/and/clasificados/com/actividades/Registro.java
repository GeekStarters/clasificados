package and.clasificados.com.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import and.clasificados.com.MainActivity;
import and.clasificados.com.R;
import and.clasificados.com.fragmentos.Inicio;
import and.clasificados.com.views.EditTextLight;


public class  Registro extends AppCompatActivity {

    Button registro;
    ImageView fb;
    TextView terminos;
    EditTextLight nombre, apellido,usuario, pass1, pass2,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        agregarToolbar();
        registro = (Button)findViewById(R.id.registro);
        fb=(ImageView)findViewById(R.id.button_fb2);
        nombre=(EditTextLight)findViewById(R.id.nombre);
        apellido=(EditTextLight)findViewById(R.id.apellido);
        email=(EditTextLight)findViewById(R.id.email);
        terminos = (TextView)findViewById(R.id.terminos);
        terminos.setText(Html.fromHtml("Al registrarme acepto los <font color='#F8672f'>Terminos</font> y <font color='#F8672f'>Condiciones</font>"));
        usuario=(EditTextLight)findViewById(R.id.user_name);
        pass1=(EditTextLight)findViewById(R.id.contra);
        pass2=(EditTextLight)findViewById(R.id.password2);
        View.OnClickListener onclick= new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId()){
                    case R.id.button_fb2:
                        Toast.makeText(getApplicationContext(),"Iremos a Facebook", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.registro:
                        registro();
                        break;
                    case R.id.terminos:
                        Toast.makeText(getApplicationContext(),"Saldra un alert con los terminos y condiciones", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        terminos.setOnClickListener(onclick);
        registro.setOnClickListener(onclick);
        fb.setOnClickListener(onclick);
    }

    public void registro(){
        usuario.setError(null);
        nombre.setError(null);
        apellido.setError(null);
        pass1.setError(null);
        pass2.setError(null);
        email.setError(null);
        String user=usuario.getText().toString();
        String nom=nombre.getText().toString();
        String correo=email.getText().toString();
        String apel=apellido.getText().toString();
        String contra=pass1.getText().toString();
        String contra2=pass2.getText().toString();
        boolean cancel = false;
        boolean bloqueo = false;
        View focusView=null; View focusView1=null; View focusView2=null;View focusView3=null; View focusView4=null; View focusView5=null; View focusView6 = null;
        try {
            if (user.isEmpty()) {
                usuario.setError(getString(R.string.no_vacios));
                focusView1 = usuario;
                bloqueo = true;
            } else {
                if (nom.isEmpty()) {
                    nombre.setError(getString(R.string.no_vacios));
                    focusView2 = nombre;
                    bloqueo = true;
                } else {
                    if (correo.isEmpty()) {
                        email.setError(getString(R.string.no_vacios));
                        focusView3 = email;
                        bloqueo = true;
                    } else {
                        if (apel.isEmpty()) {
                            apellido.setError(getString(R.string.no_vacios));
                            focusView4 = apellido;
                            bloqueo = true;
                        } else {
                            if (contra.isEmpty()) {
                                pass1.setError(getString(R.string.no_vacios));
                                focusView5 = pass1;
                                bloqueo = true;
                            } else {
                                if (contra2.isEmpty()) {
                                    pass2.setError(getString(R.string.no_vacios));
                                    focusView6 = pass1;
                                    bloqueo = true;
                                }
                            }
                        }
                    }
                }
            }
            if (!contra.isEmpty()&&!contra2.isEmpty()) {
                if (!contra.equals(contra2)) {
                    pass2.setError(getString(R.string.no_coinciden));
                    focusView = pass2;
                    cancel = true;
                }
            }
            if (cancel||bloqueo) {
                focusView.requestFocus();
                focusView1.requestFocus();
                focusView2.requestFocus();
                focusView3.requestFocus();
                focusView4.requestFocus();
                focusView5.requestFocus();
                focusView6.requestFocus();
            }else{
                transicion();
            }
        }catch (Exception e) {

        }
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_custom);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.title_activity_registro_usuario);

    }
    public void transicion(){
       startActivity(new Intent(this,MainActivity.class));
    }

}
