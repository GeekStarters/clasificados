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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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
import and.clasificados.com.auxiliares.PrefUtils;
import and.clasificados.com.fragmentos.Inicio;
import and.clasificados.com.modelo.Usuario;
import and.clasificados.com.services.AppAsynchTask;
import and.clasificados.com.views.EditTextLight;
import io.fabric.sdk.android.services.concurrency.AsyncTask;


public class  Registro extends AppCompatActivity {

    Usuario user;
    Button registro;
    private LoginButton fb;
    private CallbackManager callbackManager;
    TextView terminos;
    EditTextLight nombre, apellido,usuario, pass1, pass2,email;
    private static final String TAG = Registro.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_registro);
        agregarToolbar();
        registro = (Button)findViewById(R.id.registro);
        fb=(LoginButton)findViewById(R.id.button_fb2);
        fb.setText(getString(R.string.login_fb));
        fb.setReadPermissions("email", "user_friends");
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
        fb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                Log.e("response: ", response + "");
                                try {
                                    user = new Usuario();
                                    user.facebookID = object.getString("id").toString();
                                    user.email = object.getString("email").toString();
                                    String nombre = object.getString("name").toString();
                                    String[] partes = nombre.split(" ");
                                    user.name = partes[0];
                                    user.last = partes[1];
                                    user.provider = "facebook";
                                    NuevoUsuario t = new NuevoUsuario();
                                    t.execute(user.provider,user.facebookID,user.email,user.name,user.last);
                                  //  PrefUtils.setCurrentUser(user, Rn.this);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                             //   Toast.makeText(Login.this,"welcome "+user.name,Toast.LENGTH_LONG).show();
                                finish();

                            }

                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println(exception.toString());
            }
        });
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
                tarea.execute("local",user,correo,nom,apel,contra,contra2);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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


                JSONObject map = new JSONObject();
                map.put("provider", params[0]);
                map.put("first_name", params[3]);
                map.put("last_name", params[4]);
                map.put("user_name", params[1]);
                map.put("email", params[2]);
                map.put("phone",null);
                if(params[0]=="facebook"){
                    map.put("password", null);
                    map.put("password_confirmation", null);
                    map.put("fb_user_id",params[1]);
                }else{
                    map.put("password", params[5]);
                    map.put("password_confirmation", params[6]);
                    map.put("fb_user_id",null);
                }

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
