package and.clasificados.com.auxiliares;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
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
import and.clasificados.com.actividades.MiCuenta;
import and.clasificados.com.actividades.Modificar;
import and.clasificados.com.actividades.Single;
import and.clasificados.com.auxiliares.PopupMenu.PopupMenuCompat;
import and.clasificados.com.exception.NetworkException;
import and.clasificados.com.exception.ParsingException;
import and.clasificados.com.exception.ServerException;
import and.clasificados.com.exception.TimeOutException;
import and.clasificados.com.fragmentos.NoLogin;
import and.clasificados.com.modelo.Clasificado;
import and.clasificados.com.modelo.Usuario;
import and.clasificados.com.services.AppAsynchTask;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class CategoriasTab extends Fragment{

    private static final String INDICE_SECCION
            = "and.clasificados.com.FragmentoCategoriasTab.extra.INDICE_SECCION";

    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorCategorias adaptador;
    private Activity context;
    private List<Clasificado> resultado;
    private String slug=null;
    private Usuario login_user=null;

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
        context=getActivity();
        View view = inflater.inflate(R.layout.fragmento_grupo_items, container, false);
        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);
        login_user=PrefUtils.getCurrentUser(context);
        resultado = new ArrayList<>();
        String pasar=null;
        if(login_user!=null){
            pasar = login_user.auto;
        }else{
            int indiceSeccion = getArguments().getInt(INDICE_SECCION);
            pasar = ""+indiceSeccion;
        }
        LlenarLista llenar = new LlenarLista(context);
        llenar.execute(pasar);
        return view;
    }



    private void transicion() {
        Fragment fragmentoGenerico = new NoLogin();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fav_content, fragmentoGenerico)
                .commit();
    }

    private void transicionCuenta() {
        startActivity(new Intent(context, MiCuenta.class));
    }

    private class EliminarClasificado extends AppAsynchTask<String,Integer,Boolean> {
        Clasificado c;
        String mensaje=null, titulo=null, url_imagen=null, categoria=null, vista=null;

        public EliminarClasificado(Activity activity) {
            super(activity);
        }

        protected Boolean customDoInBackground(String... params)   throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException{
            boolean resul;
            HttpClient httpClient = new DefaultHttpClient();
            HttpDelete post = new HttpDelete(Constants.nuevo_clasificado+"/"+params[1]);
            post.setHeader("authorization", "Basic"+ " " +params[0] );
            post.setHeader("content-type", "application/json");
            try
            {

                HttpResponse resp = httpClient.execute(post);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                String aux = respJSON.get("errors").toString();
                if(aux.toString().equals("[]")) {
                    JSONObject data  = respJSON.getJSONObject("data");
                    String result = data.getString("status");
                    if (result.equals("removed")) {
                        mensaje = "Anuncio eliminado con exito";
                    }
                    resul=true;
                }else {
                    resul=false;
                    mensaje = "El anuncio solicitado no existe";
                }
            } catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul=false;
            }
            return resul;
        }


        protected void customOnPostExecute(final Boolean result) {
            if (result) {
                transicionCuenta();
            }else{
                Toast.makeText(getActivity(),mensaje,Toast.LENGTH_LONG).show();
            }
        }
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
            HttpGet get = new HttpGet(Constants.mis_p);
            get.setHeader("authorization", "Basic"+ " " +params[0] );
            get.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(get);
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
                    if(imagen.length()>0){
                        url_imagen = imagen.getString(0);
                    }else {
                        url_imagen=null;
                    }
                    String slug = info.getString("slug");
                 //   vista = ad.getString("singleApiURL");
                    c= new Clasificado(precio,categoria,titulo,url_imagen,vista,slug);
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


        protected void customOnPostExecute(final Boolean result) {
            if (result) {
                adaptador =  new AdaptadorCategorias(resultado);
                adaptador.setOnItemClickListener(new AdaptadorCategorias.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, final int position) {
                        PopupMenuCompat menu = PopupMenuCompat.newInstance(context, v);
                        menu.inflate( R.menu.menu_popup);
                        menu.setOnMenuItemClickListener( new PopupMenuCompat.OnMenuItemClickListener()
                        {
                            @Override
                            public boolean onMenuItemClick( MenuItem item )
                            {
                                switch (item.getItemId()){
                                    case R.id.ver:
                                        String  single = resultado.get(position).getSingle();
                                        Intent o=new Intent(context,Single.class);
                                        o.putExtra("single",single);
                                        startActivity(o);
                                        break;
                                    case R.id.modificar:
                                        String  slugo = resultado.get(position).getSlug();
                                        Intent s = new Intent(context,Modificar.class);
                                        s.putExtra("slug", slugo);
                                        startActivity(s);
                                        break;
                                    case R.id.eliminar:
                                        showAlertDialogEliminar();
                                        slug = resultado.get(position).getSlug();
                                        break;
                                }
                                return true;
                            }
                        } );
                        menu.show();
                    }
                });
                reciclador.setAdapter(adaptador);
                reciclador.addItemDecoration(new and.clasificados.com.auxiliares.DecoracionLineaDivisoria(getActivity()));
            }
        }
}
    protected void showAlertDialogEliminar() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Eliminar");
        alertDialogBuilder.setMessage("¿Desea eliminar el clasificado? Esta accion no podra ser deshecha");
        alertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(login_user!=null){
                    new EliminarClasificado(context).execute(login_user.auto,slug);
                }else{
                    transicion();
                }
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               transicionCuenta();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


}