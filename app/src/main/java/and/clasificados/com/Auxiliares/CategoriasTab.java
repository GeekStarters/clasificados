package and.clasificados.com.auxiliares;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.util.ArrayList;
import java.util.List;

import and.clasificados.com.Constants;
import and.clasificados.com.actividades.Single;
import and.clasificados.com.common.CircleTransformation;
import and.clasificados.com.exception.NetworkException;
import and.clasificados.com.exception.ParsingException;
import and.clasificados.com.exception.ServerException;
import and.clasificados.com.exception.TimeOutException;
import and.clasificados.com.modelo.Clasificado;
import and.clasificados.com.R;
import and.clasificados.com.services.AppAsynchTask;
import io.fabric.sdk.android.services.concurrency.AsyncTask;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class CategoriasTab extends Fragment {

    private static final String INDICE_SECCION
            = "and.clasificados.com.FragmentoCategoriasTab.extra.INDICE_SECCION";

    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorCategorias adaptador;
    private Context context;
    private List<Clasificado> resultado;

    public CategoriasTab() {
    }

    public static CategoriasTab nuevaInstancia(int indiceSeccion) {
        CategoriasTab fragment = new CategoriasTab();
        Bundle args = new Bundle();
        args.putInt(INDICE_SECCION, indiceSeccion);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_grupo_items, container, false);
        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);
        resultado = new ArrayList<>();
        int indiceSeccion = getArguments().getInt(INDICE_SECCION);
        if(indiceSeccion>3){
            String pasar = ""+indiceSeccion;
            LlenarLista llenar = new LlenarLista();
            llenar.execute(pasar);
        }else{
            switch (indiceSeccion) {
                case 4:
                    adaptador = new AdaptadorCategorias(Clasificado.EMPLEOS);
                    break;
                case 5:
                    adaptador = new AdaptadorCategorias(Clasificado.SERVICIOS);
                    break;
                case 6:
                    adaptador = new AdaptadorCategorias(Clasificado.MIOS);
                    break;
                case 7:
                    adaptador = new AdaptadorCategorias(Clasificado.FAVORITOS);
                    break;
            }
            adaptador.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), Single.class));
                }
            });
            reciclador.setAdapter(adaptador);
            reciclador.addItemDecoration(new and.clasificados.com.auxiliares.DecoracionLineaDivisoria(getActivity()));
        }
        return view;
    }

    private class LlenarLista extends AsyncTask<String,Integer,Boolean> {
        String precio=null, titulo=null, url_imagen=null, categoria=null;
        protected Boolean doInBackground(String... params) {
            boolean resul;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.last+params[0]);
            get.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject data  = respJSON.getJSONObject("data");
                JSONArray respu = data.getJSONArray("results");
                Clasificado c;

                for(int i=0; i<respu.length(); i++)
                {
                    c=new Clasificado();
                    JSONObject obj = respu.getJSONObject(i);
                    JSONObject info = obj.getJSONObject("info");
                    JSONArray imagen = info.getJSONArray("images");
                    titulo=info.getString("title");
                    precio = info.getString("currencySymbol")+" "+info.getString("precio");
                    url_imagen = imagen.getString(0);
                    categoria = info.getString("subCategoryName");
                    c.setCategoria(categoria);
                    c.setPrecio(precio);
                    c.setTextoAnuncio(titulo);
                    c.setUrl(url_imagen);
                    resultado.add(c);
                }
                resul = true;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul=false;
            }
            return resul;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                adaptador =  new AdaptadorCategorias(resultado);
                adaptador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), Single.class));
                    }
                });
                reciclador.setAdapter(adaptador);
                reciclador.addItemDecoration(new and.clasificados.com.auxiliares.DecoracionLineaDivisoria(getActivity()));
            }
        }
}
}