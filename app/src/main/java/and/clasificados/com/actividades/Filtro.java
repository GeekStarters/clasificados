package and.clasificados.com.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import and.clasificados.com.Constants;
import and.clasificados.com.R;
import and.clasificados.com.auxiliares.AdaptadorCategoria;
import and.clasificados.com.auxiliares.AdaptadorMensaje;
import and.clasificados.com.auxiliares.Footer;
import and.clasificados.com.auxiliares.Item;
import and.clasificados.com.auxiliares.PrefUtils;
import and.clasificados.com.auxiliares.RecyclerViewOnItemClickListener;
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
public class Filtro extends AppCompatActivity {

    private static final int SINGLE = 2404 ;
    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorMensaje adaptador;
    private List<Clasificado> clasificados;
    private List<Item> resultado;
    String auto,titulo,par;
    Activity context;
    Usuario login_user;
    private boolean hasMore;
    private ImageView plus, footerL,footerR;
    private String url_page, nextLink;
    private TextView mensajes, miCuenta;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);
        Intent i = getIntent();
        hasMore = true;
        titulo = i.getStringExtra("title");
        par=i.getStringExtra("lista");
        agregarToolbar(titulo);
        context = this;
        login_user = PrefUtils.getCurrentUser(Filtro.this);
        if(login_user!=null){
            auto = login_user.auto;
        }else{
            auto=null;
        }
        //Footer
        plus = (ImageView) findViewById(R.id.nuevo);
        miCuenta = (TextView)findViewById(R.id.miCuenta);
        mensajes = (TextView)findViewById(R.id.mensajes_button);
        footerL=(ImageView)findViewById(R.id.footer_left);
        footerR=(ImageView)findViewById(R.id.footer_right);
        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(context, Filtro.class);
                if(auto==null){
                    switch (v.getId()) {
                        case R.id.nuevo:
                            startActivity(new Intent(context, Login.class));
                            break;
                        case R.id.miCuenta:
                            startActivity(new Intent(context,Login.class));
                            break;
                        case R.id.mensajes_button:
                            startActivity(new Intent(context,Login.class));
                            break;
                        case R.id.footer_left:
                            startActivity(new Intent(context,Login.class));
                            break;
                        case R.id.footer_right:
                            startActivity(new Intent(context,Login.class));
                            break;
                    }
                }else{
                    switch (v.getId()) {
                        case R.id.nuevo:
                            startActivity(new Intent(context, Publicar.class));
                            break;
                        case R.id.miCuenta:
                            startActivity(new Intent(context,MiCuenta.class));
                            break;
                        case R.id.mensajes_button:
                            startActivity(new Intent(context,Mensajes.class));
                            break;
                        case R.id.footer_left:
                            startActivity(new Intent(context,MiCuenta.class));
                            break;
                        case R.id.footer_right:
                            startActivity(new Intent(context, Mensajes.class));
                            break;
                    }
                }
            }
        };
        plus.setOnClickListener(onclick);
        footerL.setOnClickListener(onclick);
        footerR.setOnClickListener(onclick);
        mensajes.setOnClickListener(onclick);
        miCuenta.setOnClickListener(onclick);
        reciclador = (RecyclerView) findViewById(R.id.reciclador_filtro);
        layoutManager = new LinearLayoutManager(this);
        reciclador.setLayoutManager(layoutManager);
        LlenarLista llenar = new LlenarLista(context);
        llenar.execute(par);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filtro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent= new Intent();
               //intent.putExtra("posicion", posicion);
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void agregarToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_custom);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(title);
    }

    private class LlenarLista extends AppAsynchTask<String,Integer,Boolean> {
        Clasificado c;

        String precio=null, titulo=null, url_imagen=null, categoria=null, vista=null;

        public LlenarLista(Activity activity) {
            super(activity);
        }

        protected Boolean customDoInBackground(String... params)   throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException{
            boolean resul;
            HttpClient httpClient = new DefaultHttpClient();
            url_page=Constants.last+params[0];
            HttpGet get = new HttpGet(Constants.last+params[0]);
            get.setHeader("content-type", "application/json");
            try
            {
                clasificados = new ArrayList<>();
                resultado = new ArrayList<>();
                HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject data  = respJSON.getJSONObject("data");
                JSONArray results = data.getJSONArray("results");
                JSONObject page=data.getJSONObject("paging");
                String auxi=page.getString("getNextLink");
                String[] split=auxi.split("=");
                nextLink=split[1];
                hasMore=true;
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
                    vista = ad.getString("singleApiURL");
                    String slug=info.getString("slug");
                    c= new Clasificado(precio,categoria,titulo,url_imagen,vista, slug);
                    resultado.add(c);
                    clasificados.add(c);
                }
                resul = true;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul=false;
            }
            return resul;
        }

        protected void customOnPostExecute(final Boolean result) {
            if (result) {
                reciclador.setAdapter(new AdaptadorCategoria(resultado, new RecyclerViewOnItemClickListener() {
                    @Override
                    public void onClick(View v, int position) {
                        String single = clasificados.get(position).getSingle();
                        String pos = "" + position;
                        Intent o = new Intent(context, Single.class);
                        o.putExtra("single", single);
                        o.putExtra("posicion", pos);
                        startActivityForResult(o, SINGLE);

                    }
                }));
                reciclador.addItemDecoration(new and.clasificados.com.auxiliares.DecoracionLineaDivisoria(getApplicationContext()));
                reciclador.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (hasMore && !(hasFooter())) {
                            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                            //position starts at 0
                            if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 2) {
                                NuevaLista asyncTask = new NuevaLista();
                                asyncTask.execute(nextLink);
                            }
                        }
                    }
                });
            }
        }
    }

    private boolean hasFooter() {
        return resultado.get(resultado.size() - 1) instanceof Footer;
    }

    private class NuevaLista extends AsyncTask<String, Void, Boolean> {
        List<Item> resi;
        List<Clasificado> residuo;
        Clasificado c;
        String precio=null, titulo=null, url_imagen=null, categoria=null, vista=null;

        @Override
        protected void onPreExecute() {
            resultado.add(new Footer());
            reciclador.getAdapter().notifyItemInserted(resultado.size() - 1);
        }

        @Override
        protected Boolean doInBackground(String ... params) {
            boolean resul;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(url_page+"?p="+params[0]);
            System.out.println("url "+url_page+"?p="+params[0]);
            get.setHeader("content-type", "application/json");
            try
            {
                resi = new ArrayList<>();
                residuo = new ArrayList<>();
                HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject data  = respJSON.getJSONObject("data");
                try{
                    JSONArray results = data.getJSONArray("results");
                    try{
                        JSONObject page=data.getJSONObject("paging");
                        String auxi=page.getString("getNextLink");
                        String[] split=auxi.split("=");
                        nextLink=split[1];

                        hasMore=true;
                    }catch (Exception ex){
                        Log.e("Paginas", "Error!", ex);
                        hasMore=false;
                    }
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
                        vista = ad.getString("singleApiURL");
                        String slug=info.getString("slug");
                        c= new Clasificado(precio,categoria,titulo,url_imagen,vista, slug);
                        resi.add(c);
                        residuo.add(c);
                    }
                    resul = true;
                }catch (Exception er){
                    Log.e("Paginas", "Error!", er);
                    hasMore=false;
                    resul=false;
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul=false;
            }
            return resul;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                int size = resultado.size();
                resultado.remove(size - 1);//removes footer
                resultado.addAll(resi);
                clasificados.addAll(residuo);
                reciclador.getAdapter().notifyItemRangeChanged(size - 1, resultado.size() - size);
            }else {
                int size = resultado.size();
                resultado.remove(size - 1);//removes footer
            }

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == SINGLE) {
            if(data!=null) {
                String value = data.getStringExtra("posicion");
                int p=Integer.parseInt(value);
                layoutManager.scrollToPositionWithOffset(p,30);
            }
        }
    }
}