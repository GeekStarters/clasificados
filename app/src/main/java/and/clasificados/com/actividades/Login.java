package and.clasificados.com.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import and.clasificados.com.Constants;
import and.clasificados.com.MainActivity;
import and.clasificados.com.R;
import and.clasificados.com.views.EditTextLight;
import io.fabric.sdk.android.services.concurrency.AsyncTask;

public class Login extends AppCompatActivity{

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
                        tarea.execute();
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

                String accessToken = loginResult.getAccessToken().getToken();
                System.out.println("aqui " + accessToken);
                AccessToken accesstoken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(
                        accesstoken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,

                                    GraphResponse response) {

                                try {
                                    Log.v("LoginActivity", response.toString());
                                    String id = object.getString("id");

                                    System.out.println(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,first_name,gender,last_name");
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
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("usuario",user);
                i.putExtra("contra", pass);
                setResult(RESULT_OK, i);
                finish();
               // startActivity(i);
            }else{
               //
            }
        }
    }

}