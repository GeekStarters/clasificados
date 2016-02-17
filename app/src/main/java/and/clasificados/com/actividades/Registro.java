package and.clasificados.com.actividades;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import and.clasificados.com.Constants;
import and.clasificados.com.MainActivity;
import and.clasificados.com.R;
import and.clasificados.com.fragmentos.Inicio;
import and.clasificados.com.modelo.Usuario;
import and.clasificados.com.services.AppAsynchTask;
import and.clasificados.com.views.EditTextLight;
import io.fabric.sdk.android.services.concurrency.AsyncTask;


public class  Registro extends AppCompatActivity {

    Button registro;
    ImageView fb;
    TextView terminos;
    EditTextLight nombre, apellido,usuario, pass1, pass2,email;
    private static final String TAG = Registro.class.getSimpleName();

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
        View focusView=null; View focusView1=null; View focusView2=null;View focusView3=null; View focusView4=null;
        try {
            if (user.isEmpty()) {
                usuario.setError(getString(R.string.no_vacios));
                focusView1 = usuario;
                bloqueo = true;
            } else {
                    if (correo.isEmpty()) {
                        email.setError(getString(R.string.no_vacios));
                        focusView2 = email;
                        bloqueo = true;
                    }else {
                            if (contra.isEmpty()) {
                                pass1.setError(getString(R.string.no_vacios));
                                focusView3 = pass1;
                                bloqueo = true;
                            } else {
                                if (contra2.isEmpty()) {
                                    pass2.setError(getString(R.string.no_vacios));
                                    focusView4 = pass1;
                                    bloqueo = true;
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
            }else{
                NuevoUsuario tarea = new NuevoUsuario();
                tarea.execute();
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
       startActivity(new Intent(this, Login.class));
    }


    private class NuevoUsuario extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {
            boolean resul;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(Constants.registro);
            post.setHeader("content-type", "application/json");
            try
            {
                final String first = nombre.getText().toString();
                final String last = apellido.getText().toString();
                final String user = usuario.getText().toString();
                final String correo= email.getText().toString();
                final String contra = pass1.getText().toString();
                final String contra2 = pass2.getText().toString();

                JSONObject map = new JSONObject();
                map.put("provider", "local");
                map.put("first_name", first);
                map.put("last_name", last);
                map.put("user_name", user);
                map.put("email", correo);
                map.put("password", contra);
                map.put("password_confirmation", contra2);
                map.put("fb_user_id",null);
                map.put("phone",null);
                StringEntity entity = new StringEntity(map.toString());
                post.setEntity(entity);
                HttpResponse resp = httpClient.execute(post);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                String aux = respJSON.get("errors").toString();
                if(aux.equals("[]")){
                    resul=true;
                }else{
                   /* String error1=null,error2=null, resultado=null;
                    String[] parts = aux.split(",");
                    error1 = parts[0];
                    error2 = parts[2];
                    if(!error2.isEmpty()){
                        resultado = error1.substring(13,error1.length()-1) + " ó " + error2.substring(12,error2.length()-1);
                    }else{
                        resultado = error1.substring(13,error1.length()-1);
                    }*/
                    resul=false;
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                //Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                transicion();
            }
        }
    }

}
