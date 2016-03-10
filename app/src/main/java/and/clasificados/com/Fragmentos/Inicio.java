package and.clasificados.com.fragmentos;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import and.clasificados.com.actividades.Login;
import and.clasificados.com.actividades.Mensajes;
import and.clasificados.com.actividades.MiCuenta;
import and.clasificados.com.actividades.Publicar;
import and.clasificados.com.actividades.Single;
import and.clasificados.com.auxiliares.AdaptadorCategorias;
import and.clasificados.com.exception.NetworkException;
import and.clasificados.com.exception.ParsingException;
import and.clasificados.com.exception.ServerException;
import and.clasificados.com.exception.TimeOutException;
import and.clasificados.com.modelo.Clasificado;
import and.clasificados.com.services.AppAsynchTask;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class Inicio extends Fragment {
    ImageView plus, vehiculo, producto,edificio, footerL,footerR;
    TextView mensajes, miCuenta;
    Button fV, fP, fI;
    private RecyclerView reciclador;
    private RelativeLayout filtro_v, filtro_p, filtro_i, filtros_super;
    private LinearLayoutManager layoutManager;
    private AdaptadorCategorias adaptador;
    private Activity context;
    private List<Clasificado> resultado;

    public Inicio() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragmento_inicio, container, false);
        final String strtext = getArguments().getString("auto");
        context = getActivity();
        fV = (Button)view.findViewById(R.id.f_v);
        fP=(Button)view.findViewById(R.id.f_p);
        fI=(Button)view.findViewById(R.id.f_i);
        vehiculo = (ImageView)view.findViewById(R.id.img_tab);
        producto = (ImageView)view.findViewById(R.id.img_tab3);
        edificio=(ImageView)view.findViewById(R.id.img_tab2);
        footerL=(ImageView)view.findViewById(R.id.footer_left);
        footerR=(ImageView)view.findViewById(R.id.footer_right);
        filtros_super= (RelativeLayout)view.findViewById(R.id.filtros);
        filtro_v=(RelativeLayout)view.findViewById(R.id.filtro_vehiculos);
        filtro_p=(RelativeLayout)view.findViewById(R.id.filtro_productos);
        filtro_i=(RelativeLayout)view.findViewById(R.id.filtro_inmuebles);
        reciclador = (RecyclerView) view.findViewById(R.id.reciclador_principal);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);
        filtro_i.setVisibility(View.GONE);
        filtro_p.setVisibility(View.GONE);
        filtro_v.setVisibility(View.GONE);
        filtros_super.setVisibility(View.GONE);
        LlenarLista llenar = new LlenarLista(context);
        llenar.execute("1");
        plus = (ImageView) view.findViewById(R.id.nuevo);
        miCuenta = (TextView)view.findViewById(R.id.miCuenta);
        mensajes = (TextView)view.findViewById(R.id.mensajes_button);
        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.img_tab:
                        filtros_super.setVisibility(View.VISIBLE);
                        filtro_i.setVisibility(View.GONE);
                        filtro_p.setVisibility(View.GONE);
                        filtro_v.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Desplegara un filtro de vehiculos", Toast.LENGTH_LONG).show();
                        new LlenarLista(context).execute("1");
                        break;
                    case R.id.img_tab2:
                        filtros_super.setVisibility(View.VISIBLE);
                        filtro_i.setVisibility(View.VISIBLE);
                        filtro_p.setVisibility(View.GONE);
                        filtro_v.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Desplegara un filtro de inmuebles", Toast.LENGTH_LONG).show();
                        new LlenarLista(context).execute("3");
                        break;
                    case R.id.img_tab3:
                        filtros_super.setVisibility(View.VISIBLE);
                        filtro_i.setVisibility(View.GONE);
                        filtro_p.setVisibility(View.VISIBLE);
                        filtro_v.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Desplegara un filtro de productos", Toast.LENGTH_LONG).show();
                        new LlenarLista(context).execute("2");
                        break;
                }
                if(strtext.equals("false")||strtext.isEmpty()){
                    switch (v.getId()) {
                        case R.id.nuevo:
                            transicion();
                            break;
                        case R.id.miCuenta:
                            startActivity(new Intent(getContext(),Login.class));
                            break;
                        case R.id.mensajes_button:
                            transicion();
                            break;
                        case R.id.footer_left:
                            startActivity(new Intent(getContext(),Login.class));
                            break;
                        case R.id.footer_right:
                            transicion();
                            break;
                        case R.id.f_v:
                            filtro_i.setVisibility(View.GONE);
                            filtro_p.setVisibility(View.GONE);
                            filtro_v.setVisibility(View.GONE);
                            filtros_super.setVisibility(View.GONE);
                            break;
                        case R.id.f_i:
                            filtro_i.setVisibility(View.GONE);
                            filtro_p.setVisibility(View.GONE);
                            filtro_v.setVisibility(View.GONE);
                            filtros_super.setVisibility(View.GONE);
                            break;
                        case R.id.f_p:
                            filtro_i.setVisibility(View.GONE);
                            filtro_p.setVisibility(View.GONE);
                            filtro_v.setVisibility(View.GONE);
                            filtros_super.setVisibility(View.GONE);
                            break;
                    }
                }else{
                    switch (v.getId()) {
                        case R.id.nuevo:
                                startActivity(new Intent(getContext(), Publicar.class));
                            break;
                        case R.id.miCuenta:
                                startActivity(new Intent(getContext(),MiCuenta.class));
                            break;
                        case R.id.mensajes_button:
                            startActivity(new Intent(getContext(),Mensajes.class));
                            break;
                        case R.id.footer_left:
                            startActivity(new Intent(getContext(),MiCuenta.class));
                            break;
                        case R.id.footer_right:
                            startActivity(new Intent(getContext(), Mensajes.class));
                            break;
                        case R.id.f_v:
                            filtro_i.setVisibility(View.GONE);
                            filtro_p.setVisibility(View.GONE);
                            filtro_v.setVisibility(View.GONE);
                            filtros_super.setVisibility(View.GONE);
                            break;
                        case R.id.f_i:
                            filtro_i.setVisibility(View.GONE);
                            filtro_p.setVisibility(View.GONE);
                            filtro_v.setVisibility(View.GONE);
                            filtros_super.setVisibility(View.GONE);
                            break;
                        case R.id.f_p:
                            filtro_i.setVisibility(View.GONE);
                            filtro_p.setVisibility(View.GONE);
                            filtro_v.setVisibility(View.GONE);
                            filtros_super.setVisibility(View.GONE);
                            break;
                    }
                }
            }
        };
        plus.setOnClickListener(onclick);
        mensajes.setOnClickListener(onclick);
        miCuenta.setOnClickListener(onclick);
        vehiculo.setOnClickListener(onclick);
        producto.setOnClickListener(onclick);
        edificio.setOnClickListener(onclick);
        fI.setOnClickListener(onclick);
        fV.setOnClickListener(onclick);
        fP.setOnClickListener(onclick);

        return view;
    }

    private void transicion() {
        Fragment fragmentoGenerico = new NoLogin();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_content, fragmentoGenerico)
                .commit();
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
                resultado = new ArrayList<>();
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
                    vista = ad.getString("singleApiURL");
                    String slug=info.getString("slug");
                    c= new Clasificado(precio,categoria,titulo,url_imagen,vista, slug);
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
                        String  single = resultado.get(position).getSingle();
                        Intent o=new Intent(context,Single.class);
                        o.putExtra("single",single);
                        startActivity(o);
                    }
                });
                reciclador.setAdapter(adaptador);
                reciclador.addItemDecoration(new and.clasificados.com.auxiliares.DecoracionLineaDivisoria(getActivity()));
            }
        }
    }
}