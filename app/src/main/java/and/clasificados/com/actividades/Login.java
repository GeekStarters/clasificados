package and.clasificados.com.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import and.clasificados.com.Constants;
import and.clasificados.com.MainActivity;
import and.clasificados.com.R;
import and.clasificados.com.actividades.Registro;
import and.clasificados.com.fragmentos.Inicio;
import and.clasificados.com.views.EditTextLight;
import io.fabric.sdk.android.services.concurrency.AsyncTask;

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
                        AutenticarUsuario tarea = new AutenticarUsuario();
                        tarea.execute();
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

    private class AutenticarUsuario extends AsyncTask<String,Integer,Boolean> {
        String user="none",pass="none";
        protected Boolean doInBackground(String... params) {
            boolean resul;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(Constants.autenticar);
            post.setHeader("content-type", "application/json");
            try
            {
                final String acceso = usuario.getText().toString();
                final String clave= contra.getText().toString();
                JSONObject map = new JSONObject();
                map.put("provider", "local");
                map.put("access", acceso);
                map.put("password", clave);
                map.put("fb_user_id", null);
                StringEntity entity = new StringEntity(map.toString());
                post.setEntity(entity);
                HttpResponse resp = httpClient.execute(post);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                String aux = respJSON.get("errors").toString();
                if(aux.equals("[]")){
                    resul=true;
                    user=acceso;
                    pass=clave;
                }else{
                    String resultado = aux.substring(13, aux.length() - 12);
                    Toast.makeText(getApplicationContext(),resultado,Toast.LENGTH_LONG).show();
                    user="none";
                    resul=false;
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
             //   Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                resul = false;
            }
            return resul;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
           //     Toast.makeText(getApplicationContext(), "Exito", Toast.LENGTH_LONG).show();
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("usuario",user);
                i.putExtra("contra",pass);
                startActivity(i);
            }
        }
    }

}