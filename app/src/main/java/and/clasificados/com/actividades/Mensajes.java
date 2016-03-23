package and.clasificados.com.actividades;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import and.clasificados.com.Constants;
import and.clasificados.com.R;
import and.clasificados.com.auxiliares.AdaptadorMensaje;
import and.clasificados.com.auxiliares.PrefUtils;
import and.clasificados.com.exception.NetworkException;
import and.clasificados.com.exception.ParsingException;
import and.clasificados.com.exception.ServerException;
import and.clasificados.com.exception.TimeOutException;
import and.clasificados.com.modelo.Clasificado;
import and.clasificados.com.modelo.Mensaje;
import and.clasificados.com.modelo.Usuario;
import and.clasificados.com.services.AppAsynchTask;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class Mensajes extends AppCompatActivity {


    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorMensaje adaptador;
    private List<Mensaje> resultado;
    String auto;
    Activity context;
    Usuario login_user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);
        agregarToolbar();
        context = this;
        login_user = PrefUtils.getCurrentUser(Mensajes.this);
        auto = login_user.auto;
        reciclador = (RecyclerView) findViewById(R.id.reciclador_mensajes);
        layoutManager = new LinearLayoutManager(this);
        reciclador.setLayoutManager(layoutManager);
        LlenarLista llenar = new LlenarLista(context);
        llenar.execute(auto);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mensaje, menu);
        return true;
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_custom);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }


    private class LlenarLista extends AppAsynchTask<String, Integer, Boolean> {
        Mensaje c;
        String precio = null, titulo = null, url_imagen = null, categoria = null;
        Activity actividad;

        public LlenarLista(Activity activity) {
            super(activity);
            actividad = activity;
        }

        protected Boolean customDoInBackground(String... params) throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException {
            boolean resul;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.mensajes);
            get.setHeader("content-type", "application/json");
            get.setHeader("authorization", "Basic" + " " + params[0]);
            Log.i("direccion", get.toString());
            try {

               /* HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject data  = respJSON.getJSONObject("data");
                JSONArray results = data.getJSONArray("results");
                for(int i=0; i<results.length(); i++)
                {
                    JSONObject ad = results.getJSONObject(i);
                    System.out.println(ad.toString());
                    JSONObject info = ad.getJSONObject("info");
                    titulo=info.getString("title");
                    precio = info.getString("currencySymbol")+" "+info.getString("price");
                    categoria = info.getString("subCategoryName");
                    JSONArray imagen = info.getJSONArray("images");
                    url_imagen = imagen.getString(0);
                    c= new Clasificado(precio,categoria,titulo,url_imagen);
                    resultado.add(c);
                }*/
                resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;
        }

        protected void customOnPostExecute(Boolean result) {
            if (result) {
                adaptador = new AdaptadorMensaje(Mensaje.MENSAJES);
                adaptador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "Click en mensaje", Toast.LENGTH_SHORT).show();
                /*Intent i = new Intent(v.getContext(), Single.class);
                v.getContext().startActivity(i);*/
                    }
                });
                reciclador.setAdapter(adaptador);
                reciclador.addItemDecoration(new and.clasificados.com.auxiliares.DecoracionLineaDivisoria(getApplicationContext()));
            }
        }
    }

}