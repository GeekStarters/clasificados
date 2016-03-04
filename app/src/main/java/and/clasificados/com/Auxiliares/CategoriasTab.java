package and.clasificados.com.auxiliares;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avast.android.dialogs.fragment.ListDialogFragment;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.IListDialogListener;
import com.avast.android.dialogs.iface.ISimpleDialogCancelListener;
import com.avast.android.dialogs.iface.ISimpleDialogListener;

import org.apache.http.HttpResponse;
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
import java.util.ArrayList;
import java.util.List;

import and.clasificados.com.Constants;
import and.clasificados.com.R;
import and.clasificados.com.actividades.Modificar;
import and.clasificados.com.actividades.Single;
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
public class CategoriasTab extends Fragment implements IListDialogListener,ISimpleDialogCancelListener, ISimpleDialogListener{

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
        int indiceSeccion = getArguments().getInt(INDICE_SECCION);
        String pasar = ""+indiceSeccion;
        LlenarLista llenar = new LlenarLista(context);
        llenar.execute(pasar);
        return view;
    }

    @Override
    public void onListItemSelected(CharSequence value, int number, int requestCode) {
        if(value.equals("Ver")){
            String  single = resultado.get(requestCode).getSingle();
            Intent o=new Intent(context,Single.class);
            o.putExtra("single",single);
            startActivity(o);
        }else if(value.equals("Modificar")){
            String  slugo = resultado.get(requestCode).getSlug();
            Intent o = new Intent(context,Modificar.class);
            o.putExtra("slug",slugo);
            startActivity(o);
        }else if(value.equals("Eliminar")){
            SimpleDialogFragment.createBuilder(getActivity(), getFragmentManager())
                    .setTitle("Eliminar")
                    .setMessage("¿Desea eliminar el clasificado? Esta accion no podra ser deshecha")
                    .setPositiveButtonText("Love")
                    .setNegativeButtonText("Hate")
                    .setRequestCode(1993)
                    .show();
             slug = resultado.get(requestCode).getSlug();

        }

    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        if (requestCode == 1993) {
           if(login_user!=null){
               new EliminarClasificado(context).execute(login_user.auto,slug);
           }else{
               transicion();
           }
        }
    }

    private void transicion() {
        Fragment fragmentoGenerico = new NoLogin();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fav_content, fragmentoGenerico)
                .commit();
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        if (requestCode == 1993) {

        }
    }

    @Override
    public void onCancelled(int requestCode) {
        if (requestCode == 1993) {

        }
    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {

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
            HttpPost post = new HttpPost(Constants.nuevo_clasificado);
            post.setHeader("authorization", "Basic"+ " " +params[0] );
            post.setHeader("content-type", "application/json");
            try
            {
                JSONObject map = new JSONObject();
                map.put("slug", params[1]);
                StringEntity entity = new StringEntity(map.toString());
                post.setEntity(entity);
                HttpResponse resp = httpClient.execute(post);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject data  = respJSON.getJSONObject("errors");
                if(data.toString().equals("[]")) {
                    mensaje = "El anuncio solicitado no existe";
                }else {
                    String result = data.getString("status");
                    if (result.equals("removed")) {
                        mensaje = "Anuncio eliminado con exito";
                    }
                }
                resul=true;
            } catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul=false;
            }
            return resul;
        }


        protected void customOnPostExecute(final Boolean result) {
            if (result) {
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
            HttpGet get = new HttpGet(Constants.last+params[0]);
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
                    url_imagen = imagen.getString(0);
                    String slug = info.getString("slug");
                    vista = ad.getString("singleApiURL");
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
                    public void onItemClick(View v, int position) {
                        ListDialogFragment
                                .createBuilder(getActivity(), getFragmentManager())
                                .setTitle(getString(R.string.selecc))
                                .setItems(new String[]{"Ver", "Modificar", "Eliminar"})
                                .setRequestCode(position)
                                .show();

                    }
                });
                reciclador.setAdapter(adaptador);
                reciclador.addItemDecoration(new and.clasificados.com.auxiliares.DecoracionLineaDivisoria(getActivity()));
            }
        }
}


}