package and.clasificados.com.actividades;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import and.clasificados.com.Constants;
import and.clasificados.com.MainActivity;
import and.clasificados.com.R;
import and.clasificados.com.auxiliares.PrefUtils;
import and.clasificados.com.modelo.Usuario;
import and.clasificados.com.views.EditTextLight;
import io.fabric.sdk.android.services.concurrency.AsyncTask;

public class Login extends AppCompatActivity{
    Usuario user;
    private LoginButton fb;
    private CallbackManager callbackManager;
    Button registro, sesion;
    TextView olvide;
    EditTextLight usuario, contra;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        agregarToolbar();
        if(PrefUtils.getCurrentUser(Login.this) != null){
            Intent homeIntent = new Intent(Login.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
        }
        registro = (Button) findViewById(R.id.button_registro);
        sesion = (Button) findViewById(R.id.button_session);
        fb = (LoginButton) findViewById(R.id.button_fb);
        fb.setText(getString(R.string.login_fb));
        fb.setReadPermissions("email", "user_friends");
        olvide = (TextView) findViewById(R.id.olvide);
        usuario = (EditTextLight) findViewById(R.id.user);
        contra = (EditTextLight) findViewById(R.id.pass);
        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_registro:
                        registro();
                        break;
                    case R.id.button_session:
                        AutenticarUsuario tarea = new AutenticarUsuario();
                        tarea.execute("local");
                        break;
                    case R.id.olvide:
                        Toast.makeText(getApplicationContext(), "Iremos a cambiar contraseña", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };


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
                                    user.provider="facebook";
                                    user.facebookID = object.getString("id").toString();
                                    user.email = object.getString("email").toString();
                                    AutenticarUsuario t = new AutenticarUsuario();
                                    t.execute(user.provider, user.facebookID, user.email);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

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
        registro.setOnClickListener(onclick);
        sesion.setOnClickListener(onclick);
        olvide.setOnClickListener(onclick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
        String resultado="none";
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
                map.put("provider", params[0]);
                if(params[0]=="local"){
                    map.put("access", acceso);
                    map.put("password", clave);
                    map.put("fb_user_id", null);
                }else{
                    map.put("access", params[2]);
                    map.put("password", null);
                    map.put("fb_user_id", params[1]);
                }

                StringEntity entity = new StringEntity(map.toString());
                post.setEntity(entity);
                HttpResponse resp = httpClient.execute(post);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                String aux = respJSON.get("errors").toString();
                if(aux.equals("[]")){
                    resul=true;
                    JSONObject data  = respJSON.getJSONObject("data");
                    user = new Usuario();
                    user.provider=params[0];
                    user.name=data.getString("first_name");
                    user.last=data.getString("last_name");
                    user.pic=respJSON.getString("imagesDomain")+data.getString("picture_profile");
                    user.auto= data.getString("basic_authentication");
                    user.email=data.getString("email");
                    user.facebookID=data.getString("fb_user_id");
                    user.token=data.getString("token");
                    PrefUtils.setCurrentUser(user, Login.this);
                }else{
                    resultado = aux.substring(13, aux.length() - 12);
                    user=null;
                    resul=false;
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(getApplicationContext(),getString(R.string.bienvenido),Toast.LENGTH_LONG).show();
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }else{
                Toast.makeText(getApplicationContext(),resultado,Toast.LENGTH_LONG).show();
            }
        }
    }

}