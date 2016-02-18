package and.clasificados.com.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

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
import and.clasificados.com.views.EditTextLight;
import io.fabric.sdk.android.services.concurrency.AsyncTask;

public class Publicar extends AppCompatActivity {

    EditTextLight title,costo,descr;
    Button publicar;
    String auto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar);
        agregarToolbar();
        Intent i= getIntent();
        auto = i.getStringExtra("basic");
        title = (EditTextLight)findViewById(R.id.titulo);
        costo = (EditTextLight)findViewById(R.id.costo);
        descr = (EditTextLight)findViewById(R.id.descripcion);
        publicar = (Button) findViewById(R.id.button_publicar);
        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NuevoAnuncio a = new NuevoAnuncio();
                a.execute(auto);
            }
        });

    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_custom);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.title_activity_publicar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publicar, menu);
        return true;
    }

    private class NuevoAnuncio extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {
            boolean resul;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(Constants.nuevo_clasificado);
            post.setHeader("Authorization", "Basic"+params[0] );
            post.setHeader("content-type", "application/json");
            try
            {
                final String titulo = title.getText().toString();
              //  final String categoria = cat.getText().toString();
                final String precio = costo.getText().toString();
                final String descripcion= descr.getText().toString();

                JSONObject map = new JSONObject();
                map.put("title", titulo);
                map.put("description", descripcion);
                map.put("price", precio);
                map.put("category_id",69);
                map.put("product_id",173);
                map.put("currency_id",2);
                map.put("location_id","root");
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
                Log.e("ServicioRest", "Error!", ex);
                //Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }
    }

}
