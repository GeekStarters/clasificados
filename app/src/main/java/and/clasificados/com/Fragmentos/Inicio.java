package and.clasificados.com.fragmentos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import and.clasificados.com.Constants;
import and.clasificados.com.R;
import and.clasificados.com.actividades.Filtro;
import and.clasificados.com.actividades.Login;
import and.clasificados.com.actividades.Mensajes;
import and.clasificados.com.actividades.MiCuenta;
import and.clasificados.com.actividades.Publicar;
import and.clasificados.com.actividades.Single;
import and.clasificados.com.auxiliares.AdaptadorCategoria;
import and.clasificados.com.auxiliares.AdaptadorCategorias;
import and.clasificados.com.auxiliares.Footer;
import and.clasificados.com.auxiliares.Item;
import and.clasificados.com.auxiliares.RecyclerViewOnItemClickListener;
import and.clasificados.com.exception.NetworkException;
import and.clasificados.com.exception.ParsingException;
import and.clasificados.com.exception.ServerException;
import and.clasificados.com.exception.TimeOutException;
import and.clasificados.com.modelo.Categoria;
import and.clasificados.com.modelo.Clasificado;
import and.clasificados.com.modelo.Localidad;
import and.clasificados.com.modelo.Marca;
import and.clasificados.com.modelo.Mensaje;
import and.clasificados.com.modelo.Modelo;
import and.clasificados.com.modelo.Municipio;
import and.clasificados.com.modelo.SubCategoria;
import and.clasificados.com.modelo.Vehiculo;
import and.clasificados.com.modelo.Zona;
import and.clasificados.com.services.AppAsynchTask;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class Inicio extends Fragment {
    private static final int SINGLE = 2404 ;
    private static final int FILTRO = 3452;
    ImageView plus, vehiculo, producto,edificio, footerL,footerR;
    TextView mensajes, miCuenta, btn1,btn2,btn3, btn4,btn5,btn6;
    Button fV, fP, fI;
    private boolean hasMore, click, click2,click3;
    Spinner spinnerCat, spinnerSub,spinnerZona,spinnerLoc,spinnerMun, spinnerMarca,spinnerModelo,spinnerTipoA, spinnerTipoI;
    private RecyclerView reciclador;
    private RelativeLayout filtros_super, rel1,rel2,rel3, rel4,rel5,rel6, filtro_v, filtro_p, filtro_i;
    private LinearLayoutManager layoutManager;
    private Activity context;
    private List<Item> resultado;
    private List<Municipio> listaMunicipio;
    private List<Clasificado> clasificados;
    private List<Localidad> listaLocaciones;
    private List<Categoria> categoriasLista, categoriasVehiculo, categoriasInmueble;
    private Vehiculo vehiculoLista;
    private ArrayList<Marca> marcas;
    private static int current_page = 1;
    private int ival = 1;
    private String url_page;
    private int loadLimit = 10;
    String nextLink;

    public Inicio() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragmento_inicio, container, false);
        final String strtext = getArguments().getString("auto");
        context = getActivity();
        click=false;
        click2=false;
        click3=false;
        hasMore = true;
        //Fltros 1
        llenarListasSpinner();
        fV = (Button)view.findViewById(R.id.f_v);
        fP=(Button)view.findViewById(R.id.f_p);
        fI=(Button)view.findViewById(R.id.f_i);
        //Spinners
        spinnerMarca=(Spinner)view.findViewById(R.id.spinner_marca);
        spinnerModelo=(Spinner)view.findViewById(R.id.spinner_modelo);
        spinnerTipoA=(Spinner)view.findViewById(R.id.spinner_tipo);
        spinnerTipoI=(Spinner)view.findViewById(R.id.spinner3);
        spinnerLoc=(Spinner)view.findViewById(R.id.spinner4);
        spinnerMun=(Spinner)view.findViewById(R.id.spinner5);
        spinnerZona=(Spinner)view.findViewById(R.id.spinner6);
        spinnerCat = (Spinner)view.findViewById(R.id.spinnercatfiltro);
        spinnerSub = (Spinner)view.findViewById(R.id.spinnersubfiltro);
        //Tabs
        vehiculo = (ImageView)view.findViewById(R.id.img_tab);
        producto = (ImageView)view.findViewById(R.id.img_tab3);
        edificio=(ImageView)view.findViewById(R.id.img_tab2);
        //Footer
        footerL=(ImageView)view.findViewById(R.id.footer_left);
        footerR=(ImageView)view.findViewById(R.id.footer_right);
        //Filtros 2
        filtros_super= (RelativeLayout)view.findViewById(R.id.filtros);
        filtro_v=(RelativeLayout)view.findViewById(R.id.filtro_vehiculos);
        filtro_p=(RelativeLayout)view.findViewById(R.id.filtro_productos);
        filtro_i=(RelativeLayout)view.findViewById(R.id.filtro_inmuebles);
        btn1 = (TextView)view.findViewById(R.id.tab_marca);
        btn2 = (TextView)view.findViewById(R.id.tab_modelo);
        btn3 = (TextView)view.findViewById(R.id.tab_nuevo);
        rel1=(RelativeLayout)view.findViewById(R.id.tabs_marca);
        rel2=(RelativeLayout)view.findViewById(R.id.tabs_modelo);
        rel3=(RelativeLayout)view.findViewById(R.id.tabs_nuevo);
        btn4 = (TextView)view.findViewById(R.id.tab_alquiler);
        btn5 = (TextView)view.findViewById(R.id.tab_proy);
        btn6 = (TextView)view.findViewById(R.id.tab_venta);
        rel4=(RelativeLayout)view.findViewById(R.id.tabs_alquiler);
        rel5=(RelativeLayout)view.findViewById(R.id.tabs_proy);
        rel6=(RelativeLayout)view.findViewById(R.id.tabs_venta);

        reciclador = (RecyclerView) view.findViewById(R.id.reciclador_principal);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);
        filtro_i.setVisibility(View.GONE);
        filtro_p.setVisibility(View.GONE);
        filtro_v.setVisibility(View.GONE);
        filtros_super.setVisibility(View.GONE);
        LlenarLista llenar = new LlenarLista(context);
        llenar.execute("vehiculos");
        plus = (ImageView) view.findViewById(R.id.nuevo);
        miCuenta = (TextView)view.findViewById(R.id.miCuenta);
        mensajes = (TextView)view.findViewById(R.id.mensajes_button);
        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(context, Filtro.class);
                switch (v.getId()){
                    case R.id.img_tab:
                        if(click){
                            filtros_super.setVisibility(View.GONE);
                            filtro_v.setVisibility(View.GONE);
                            click=false;
                        }else{
                            filtros_super.setVisibility(View.VISIBLE);
                            filtro_v.setVisibility(View.VISIBLE);
                            click=true;
                        }
                        filtro_i.setVisibility(View.GONE);
                        filtro_p.setVisibility(View.GONE);
                        break;
                    case R.id.img_tab2:
                        if(click2){
                            filtros_super.setVisibility(View.GONE);
                            filtro_i.setVisibility(View.GONE);
                            click2=false;
                        }else{
                            filtros_super.setVisibility(View.VISIBLE);
                            filtro_i.setVisibility(View.VISIBLE);
                            click2=true;
                        }
                        filtro_p.setVisibility(View.GONE);
                        filtro_v.setVisibility(View.GONE);
                        break;
                    case R.id.img_tab3:
                        if(click3){
                            filtros_super.setVisibility(View.GONE);
                            filtro_p.setVisibility(View.GONE);
                            click3=false;
                        }else{
                            filtros_super.setVisibility(View.VISIBLE);
                            filtro_p.setVisibility(View.VISIBLE);
                            click3=true;
                        }
                        filtro_i.setVisibility(View.GONE);
                        filtro_v.setVisibility(View.GONE);
                        break;
                    case R.id.f_v:
                        filtro_i.setVisibility(View.GONE);
                        filtro_p.setVisibility(View.GONE);
                        filtro_v.setVisibility(View.GONE);
                        filtros_super.setVisibility(View.GONE);
                        a.putExtra("lista", "vehiculos");
                        a.putExtra("title", "Ultimos Vehiculos");
                        context.startActivityForResult(a, FILTRO);
                        break;
                    case R.id.f_i:
                        filtro_i.setVisibility(View.GONE);
                        filtro_p.setVisibility(View.GONE);
                        filtro_v.setVisibility(View.GONE);
                        filtros_super.setVisibility(View.GONE);
                        a.putExtra("lista", "inmuebles");
                        a.putExtra("title", "Ultimos Inmuebles");
                        context.startActivityForResult(a, FILTRO);
                        break;
                    case R.id.f_p:
                        filtro_i.setVisibility(View.GONE);
                        filtro_p.setVisibility(View.GONE);
                        filtro_v.setVisibility(View.GONE);
                        filtros_super.setVisibility(View.GONE);
                        a.putExtra("lista", "productos");
                        a.putExtra("title", "Ultimos Productos");
                        context.startActivityForResult(a, FILTRO);
                        break;
                    case R.id.tab_nuevo:
                        rel1.setBackground(getResources().getDrawable(R.drawable.radius_left_tabs_no));
                        rel2.setBackground(getResources().getDrawable(R.drawable.radius_tabs));
                        rel3.setBackground(getResources().getDrawable(R.drawable.radius_tabs_right_no));
                        btn1.setBackgroundColor(getResources().getColor(R.color.white));
                        btn2.setBackgroundColor(getResources().getColor(R.color.white));
                        btn3.setBackgroundColor(getResources().getColor(R.color.tab_blue));
                        btn1.setTextColor(getResources().getColor(R.color.tab_blue));
                        btn2.setTextColor(getResources().getColor(R.color.tab_blue));
                        btn3.setTextColor(getResources().getColor(R.color.white));
                        new ObtenerFiltroVehiculo(context).execute(2);
                        break;
                    case R.id.tab_marca:
                        rel1.setBackground(getResources().getDrawable(R.drawable.radius_left_tabs_selected_2));
                        rel2.setBackground(getResources().getDrawable(R.drawable.radius_tabs));
                        rel3.setBackground(getResources().getDrawable(R.drawable.radius_tabs_right));
                        btn3.setBackgroundColor(getResources().getColor(R.color.white));
                        btn2.setBackgroundColor(getResources().getColor(R.color.white));
                        btn1.setBackgroundColor(getResources().getColor(R.color.tab_blue));
                        btn3.setTextColor(getResources().getColor(R.color.tab_blue));
                        btn2.setTextColor(getResources().getColor(R.color.tab_blue));
                        btn1.setTextColor(getResources().getColor(R.color.white));
                        new ObtenerFiltroVehiculo(context).execute(0);
                        break;
                    case R.id.tab_modelo:
                        rel1.setBackground(getResources().getDrawable(R.drawable.radius_left_tabs_no));
                        rel2.setBackground(getResources().getDrawable(R.drawable.radius_tabs_no));
                        rel3.setBackground(getResources().getDrawable(R.drawable.radius_tabs_right));
                        btn1.setBackgroundColor(getResources().getColor(R.color.white));
                        btn3.setBackgroundColor(getResources().getColor(R.color.white));
                        btn2.setBackgroundColor(getResources().getColor(R.color.tab_blue));
                        btn1.setTextColor(getResources().getColor(R.color.tab_blue));
                        btn3.setTextColor(getResources().getColor(R.color.tab_blue));
                        btn2.setTextColor(getResources().getColor(R.color.white));
                        new ObtenerFiltroVehiculo(context).execute(1);
                        break;
                    case R.id.tab_alquiler:
                        rel4.setBackground(getResources().getDrawable(R.drawable.radius_left_tabs_selected_2));
                        rel5.setBackground(getResources().getDrawable(R.drawable.radius_tabs));
                        rel6.setBackground(getResources().getDrawable(R.drawable.radius_tabs_right));
                        btn6.setBackgroundColor(getResources().getColor(R.color.white));
                        btn5.setBackgroundColor(getResources().getColor(R.color.white));
                        btn4.setBackgroundColor(getResources().getColor(R.color.tab_blue));
                        btn6.setTextColor(getResources().getColor(R.color.tab_blue));
                        btn5.setTextColor(getResources().getColor(R.color.tab_blue));
                        btn4.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case R.id.tab_proy:
                        rel4.setBackground(getResources().getDrawable(R.drawable.radius_left_tabs_no));
                        rel5.setBackground(getResources().getDrawable(R.drawable.radius_tabs_no));
                        rel6.setBackground(getResources().getDrawable(R.drawable.radius_tabs_right));
                        btn4.setBackgroundColor(getResources().getColor(R.color.white));
                        btn6.setBackgroundColor(getResources().getColor(R.color.white));
                        btn5.setBackgroundColor(getResources().getColor(R.color.tab_blue));
                        btn4.setTextColor(getResources().getColor(R.color.tab_blue));
                        btn6.setTextColor(getResources().getColor(R.color.tab_blue));
                        btn5.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case R.id.tab_venta:
                        rel4.setBackground(getResources().getDrawable(R.drawable.radius_left_tabs_no));
                        rel5.setBackground(getResources().getDrawable(R.drawable.radius_tabs));
                        rel6.setBackground(getResources().getDrawable(R.drawable.radius_tabs_right_no));
                        btn4.setBackgroundColor(getResources().getColor(R.color.white));
                        btn5.setBackgroundColor(getResources().getColor(R.color.white));
                        btn6.setBackgroundColor(getResources().getColor(R.color.tab_blue));
                        btn4.setTextColor(getResources().getColor(R.color.tab_blue));
                        btn5.setTextColor(getResources().getColor(R.color.tab_blue));
                        btn6.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case R.id.filtros:
                        filtros_super.setVisibility(View.GONE);
                        click=false;
                        click2=false;
                        click3=false;
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
                    }
                }
            }
        };
        plus.setOnClickListener(onclick);
        footerL.setOnClickListener(onclick);
        footerR.setOnClickListener(onclick);
        mensajes.setOnClickListener(onclick);
        miCuenta.setOnClickListener(onclick);
        vehiculo.setOnClickListener(onclick);
        producto.setOnClickListener(onclick);
        edificio.setOnClickListener(onclick);
        fI.setOnClickListener(onclick);
        fV.setOnClickListener(onclick);
        fP.setOnClickListener(onclick);
        btn1.setOnClickListener(onclick);
        btn2.setOnClickListener(onclick);
        btn3.setOnClickListener(onclick);
        btn4.setOnClickListener(onclick);
        btn5.setOnClickListener(onclick);
        btn6.setOnClickListener(onclick);
        filtros_super.setOnClickListener(onclick);
        AdapterView.OnItemSelectedListener onselected = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
                switch (parent.getId()) {
                    case R.id.spinnercatfiltro:
                        ArrayList<SubCategoria> aux = categoriasLista.get(position).getSub();
                        poblarSpinnerSubCategorias(aux);
                        break;
                    case R.id.spinner_marca:
                        ArrayList<Modelo> model = marcas.get(position).getModelo();
                        poblarSpinnerModelos(model);
                        break;
                    case R.id.spinner4:
                        int identificadorA = Integer.parseInt(listaLocaciones.get(position).getId());
                        int identificadorB = Integer.parseInt(listaLocaciones.get(position+1).getId());
                        new ObtenerMunicipalidades(context).execute(identificadorA, identificadorB);
                        break;
                    case R.id.spinner5:
                        ArrayList<Zona> auxiliar = listaMunicipio.get(position).getZonas();
                        poblarSpinnerZonas(auxiliar);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinnerMarca.setOnItemSelectedListener(onselected);
        spinnerCat.setOnItemSelectedListener(onselected);
        spinnerLoc.setOnItemSelectedListener(onselected);
        spinnerMun.setOnItemSelectedListener(onselected);
        return view;
    }

    private void llenarListasSpinner() {
        new ObtenerCategoria(context).execute("products");
        new ObtenerFiltroVehiculo(context).execute(0);
        new ObtenerCategoriasVehiculos(context).execute();
        new ObtenerCategoriasInmuebles(context).execute();
        new ObtenerLocaciones(context).execute();
    }

    private void transicion() {
        Fragment fragmentoGenerico = new NoLogin();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_content, fragmentoGenerico)
                .commit();
    }

    private class ObtenerMunicipalidades extends AppAsynchTask<Integer, Void, Boolean>{

        Activity actividad;
        public ObtenerMunicipalidades(Activity activity) {
            super(activity);
            actividad=activity;
        }

        @Override
        protected Boolean customDoInBackground(Integer... params)  throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException {

            boolean resul=false; String id=null, nombre=null, idZona=null, nameZona=null;
            Double longitud,latitud, longitudZona,latitudZona;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.categories_filtro+"realstates");
            get.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject data = respJSON.getJSONObject("data");
                JSONObject aux = data.getJSONObject("municipalities");
                if(params[0]>=2){
                    JSONObject idMun = aux.getJSONObject(""+params[0]);
                    JSONObject results = idMun.getJSONObject("municipalities");
                    Municipio c;
                    ArrayList<Zona> zonita = new ArrayList<>();
                    zonita.add(new Zona(null,"Zona",null,null));
                    listaMunicipio=new ArrayList<>();
                    listaMunicipio.add(new Municipio(null,"Municipio",null,null,zonita));
                    for(int i=params[0]+1; i<params[1]; i++) {
                        JSONObject info = results.getJSONObject(String.valueOf(i));
                        id = info.getString("id");
                        nombre = info.getString("name");
                        longitud=Double.parseDouble(info.getString("longitude"));
                        latitud=Double.parseDouble(info.getString("latitude"));
                        ArrayList<Zona> zonaMun = new ArrayList<>();
                        zonaMun.add(new Zona(null,"Zona",null, null));
                        String auxiliar= info.get("zones").toString();
                        if(!auxiliar.equals("[]")){
                            JSONObject zones = info.getJSONObject("zones");
                            Zona z;
                            Iterator<String> iter = zones.keys();
                            while (iter.hasNext()) {
                                String key = iter.next();
                                try {
                                    JSONObject dataZone = zones.getJSONObject(key);
                                    idZona=dataZone.getString("id");
                                    nameZona=dataZone.getString("name");
                                    longitudZona=Double.parseDouble(dataZone.getString("longitude"));
                                    latitudZona=Double.parseDouble(dataZone.getString("latitude"));
                                    z=new Zona(idZona,nameZona,longitudZona,latitudZona);
                                    zonaMun.add(z);
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                            }
                        }
                        c = new Municipio(id, nombre, longitud, latitud,zonaMun);
                        listaMunicipio.add(c);
                    }
                }else{
                    ArrayList<Zona> zonita = new ArrayList<>();
                    zonita.add(new Zona(null,"Zona",null,null));
                    listaMunicipio=new ArrayList<>();
                    listaMunicipio.add(new Municipio(null,"Municipio",null,null,zonita));
                }
                resul=true;
            } catch (JSONException e) {
                e.printStackTrace();
                resul=false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                resul=false;
            } catch (IOException e) {
                e.printStackTrace();
                resul=false;
            }

            return resul;
        }

        @Override
        protected void customOnPostExecute(Boolean result) {
            if(result){
                poblarSpinnerMunicipios();
            }
        }
    }

    private void poblarSpinnerMunicipios() {
        List<String> campos = new ArrayList<String>();
        for (int i = 0; i < listaMunicipio.size(); i++) {
            Log.i("Categoria",listaMunicipio.get(i).toString());
            campos.add(listaMunicipio.get(i).getNombre());
            Log.i("Campo",campos.get(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,R.layout.my_simple_spinner_item2, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerMun.setAdapter(spinnerAdapter);
    }

    private void poblarSpinnerZonas(ArrayList<Zona> aux) {
        List<String> campos = new ArrayList<String>();
        for (int i = 0; i < aux.size(); i++) {
            Log.i("Categoria",aux.get(i).toString());
            campos.add(aux.get(i).getNombre());
            Log.i("Campo",campos.get(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,R.layout.my_simple_spinner_item2, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerZona.setAdapter(spinnerAdapter);
    }

    private class ObtenerLocaciones extends AppAsynchTask<String, Void, Boolean>{

        Activity actividad;
        public ObtenerLocaciones(Activity activity) {
            super(activity);
            actividad=activity;
        }

        @Override
        protected Boolean customDoInBackground(String... params)  throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException {

            boolean resul=false; String id=null, nombre=null;
            Double longitud,latitud;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.categories_filtro+"realstates");
            get.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject data = respJSON.getJSONObject("data");
                JSONArray results = data.getJSONArray("locations");
                Localidad c;
                listaLocaciones=new ArrayList<>();
                listaLocaciones.add(new Localidad("0","Departamento",null,null));
                for(int i=0; i<results.length(); i++) {
                    JSONObject info = results.getJSONObject(i);
                    id = info.getString("id");
                    nombre = info.getString("name");
                    longitud=Double.parseDouble(info.getString("longitude"));
                    latitud=Double.parseDouble(info.getString("latitude"));
                    c = new Localidad(id, nombre, longitud, latitud);
                    listaLocaciones.add(c);
                }
                resul=true;
            } catch (JSONException e) {
                e.printStackTrace();
                resul=false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                resul=false;
            } catch (IOException e) {
                e.printStackTrace();
                resul=false;
            }

            return resul;
        }

        @Override
        protected void customOnPostExecute(Boolean result) {
            if(result){
                poblarSpinnerLocaciones();
            }
        }
    }

    private void poblarSpinnerLocaciones() {
        List<String> campos = new ArrayList<String>();
        for (int i = 0; i < listaLocaciones.size(); i++) {
            Log.i("Categoria",listaLocaciones.get(i).toString());
            campos.add(listaLocaciones.get(i).getNombre());
            Log.i("Campo",campos.get(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,R.layout.my_simple_spinner_item2, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerLoc.setAdapter(spinnerAdapter);
    }


    private class ObtenerCategoriasInmuebles extends AppAsynchTask<String, Void, Boolean>{

        Activity actividad;
        public ObtenerCategoriasInmuebles(Activity activity) {
            super(activity);
            actividad=activity;
        }

        @Override
        protected Boolean customDoInBackground(String... params)  throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException {

            boolean resul=false; String id=null, nombre=null, slug=null, idSub=null, nameSub=null,slugSub=null, useSlug=null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.categories_filtro+"realstates");
            get.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject data = respJSON.getJSONObject("data");
                JSONArray results = data.getJSONArray("products");
                Categoria c;
                categoriasInmueble=new ArrayList<>();
                categoriasInmueble.add(new Categoria(null,"Tipo de Inmueble"));
                for(int i=0; i<results.length(); i++) {
                    JSONObject info = results.getJSONObject(i);
                    id = info.getString("id");
                    nombre = info.getString("name");
                    c = new Categoria(id, nombre);
                    categoriasInmueble.add(c);
                }
                resul=true;
            } catch (JSONException e) {
                e.printStackTrace();
                resul=false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                resul=false;
            } catch (IOException e) {
                e.printStackTrace();
                resul=false;
            }

            return resul;
        }

        @Override
        protected void customOnPostExecute(Boolean result) {
            if(result){
                poblarSpinnerCategoriasInmuebles();
            }
        }
    }

    private void poblarSpinnerCategoriasInmuebles() {
        List<String> campos = new ArrayList<String>();
        for (int i = 0; i < categoriasInmueble.size(); i++) {
            Log.i("Categoria",categoriasInmueble.get(i).toString());
            campos.add(categoriasInmueble.get(i).getNombre());
            Log.i("Campo",campos.get(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,R.layout.my_simple_spinner_item2, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerTipoI.setAdapter(spinnerAdapter);
    }

    private class ObtenerCategoriasVehiculos extends AppAsynchTask<String, Void, Boolean>{

        Activity actividad;
        public ObtenerCategoriasVehiculos(Activity activity) {
            super(activity);
            actividad=activity;
        }

        @Override
        protected Boolean customDoInBackground(String... params)  throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException {

            boolean resul=false; String id=null, nombre=null, slug=null, idSub=null, nameSub=null,slugSub=null, useSlug=null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.categories_filtro+"vehicles");
            get.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject data = respJSON.getJSONObject("data");
                JSONArray results = data.getJSONArray("categories");
                Categoria c;
                categoriasVehiculo=new ArrayList<>();
                categoriasVehiculo.add(new Categoria(null,"Estado"));
                for(int i=0; i<results.length(); i++) {
                    JSONObject info = results.getJSONObject(i);
                    id = info.getString("child_id");
                    nombre = info.getString("child_name");
                    c = new Categoria(id, nombre);
                    categoriasVehiculo.add(c);
                }
                resul=true;
            } catch (JSONException e) {
                e.printStackTrace();
                resul=false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                resul=false;
            } catch (IOException e) {
                e.printStackTrace();
                resul=false;
            }

            return resul;
        }

        @Override
        protected void customOnPostExecute(Boolean result) {
            if(result){
                poblarSpinnerCategoriasVehiculos();
            }
        }
    }

    private void poblarSpinnerCategoriasVehiculos() {
        List<String> campos = new ArrayList<String>();
        for (int i = 0; i < categoriasVehiculo.size(); i++) {
            Log.i("Categoria",categoriasVehiculo.get(i).toString());
            campos.add(categoriasVehiculo.get(i).getNombre());
            Log.i("Campo",campos.get(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,R.layout.my_simple_spinner_item2, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerTipoA.setAdapter(spinnerAdapter);
    }

    private class ObtenerCategoria extends AppAsynchTask<String, Void, Boolean>{

        Activity actividad;
        public ObtenerCategoria(Activity activity) {
            super(activity);
            actividad=activity;
        }

        @Override
        protected Boolean customDoInBackground(String... params)  throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException {

            boolean resul=false; String id=null, nombre=null, slug=null, idSub=null, nameSub=null,slugSub=null, useSlug=null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.categories_filtro+params[0]);
            get.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject data = respJSON.getJSONObject("data");
                JSONArray results = data.getJSONArray("categories");
                Categoria c;
                ArrayList<SubCategoria> subi = new ArrayList<>();
                subi.add(new SubCategoria(null,"Subcategoria",null,null));
                categoriasLista=new ArrayList<>();
                categoriasLista.add(new Categoria(null,"Categoria",null,subi));
                for(int i=0; i<results.length(); i++) {
                    JSONObject info = results.getJSONObject(i);
                    id = info.getString("id");
                    nombre = info.getString("name");
                    slug = info.getString("slug");
                    JSONArray sub = info.getJSONArray("sub_categories");
                    SubCategoria sc;
                    ArrayList<SubCategoria> subArray=new ArrayList<>();
                    subArray.add(new SubCategoria(null,"Subcategoria",null,null));
                    for (int j=0; j<sub.length();j++){
                        JSONObject dato = sub.getJSONObject(j);
                        idSub = dato.getString("id");
                        nameSub=dato.getString("name");
                        slugSub=dato.getString("slug");
                        useSlug=dato.getString("useSlug");
                        sc = new SubCategoria(idSub,nameSub,slugSub, useSlug);
                        subArray.add(sc);
                    }
                    c = new Categoria(id, nombre, slug,subArray);
                    categoriasLista.add(c);
                }
                resul=true;
            } catch (JSONException e) {
                e.printStackTrace();
                resul=false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                resul=false;
            } catch (IOException e) {
                e.printStackTrace();
                resul=false;
            }

            return resul;
        }

        @Override
        protected void customOnPostExecute(Boolean result) {
            if(result){
                poblarSpinnerCategorias();
            }
        }
    }

    private void poblarSpinnerCategorias() {
        List<String> campos = new ArrayList<String>();
        for (int i = 0; i < categoriasLista.size(); i++) {
            Log.i("Categoria",categoriasLista.get(i).toString());
            campos.add(categoriasLista.get(i).getNombre());
            Log.i("Campo",campos.get(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,R.layout.my_simple_spinner_item2, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerCat.setAdapter(spinnerAdapter);
    }

    private void poblarSpinnerSubCategorias(ArrayList<SubCategoria> aux) {
        List<String> campos = new ArrayList<String>();
        for (int i = 0; i < aux.size(); i++) {
            Log.i("Categoria",aux.get(i).toString());
            campos.add(aux.get(i).getNombre());
            Log.i("Campo",campos.get(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,R.layout.my_simple_spinner_item2, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerSub.setAdapter(spinnerAdapter);
    }


    private class ObtenerFiltroVehiculo extends AppAsynchTask<Integer, Void, Boolean>{

        Activity actividad;
        public ObtenerFiltroVehiculo(Activity activity) {
            super(activity);
            actividad=activity;
        }

        @Override
        protected Boolean customDoInBackground(Integer... params)  throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException {

            boolean resul=false; String id=null, nombre=null, idMar=null, nameMar=null, idMod=null, nameMod=null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.categories_filtro+"vehicles");
            get.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject data = respJSON.getJSONObject("data");
                JSONArray results = data.getJSONArray("products");
                JSONObject info = results.getJSONObject(params[0]);
                id = info.getString("id");
                nombre = info.getString("name");
                JSONArray mar = info.getJSONArray("brands");
                Marca marV;
                Modelo mod;
                marcas=new ArrayList<>();
                ArrayList<Modelo> maux = new ArrayList<>();
                maux.add(new Modelo(null, "Modelo"));
                marcas.add(new Marca(null,"Marca",maux));
                    for (int j=0; j<mar.length();j++){
                        JSONObject bran = mar.getJSONObject(j);
                        idMar = bran.getString("id");
                        nameMar=bran.getString("name");
                        JSONArray mode= bran.getJSONArray("models");
                        ArrayList<Modelo> modelosLista = new ArrayList<>();
                        modelosLista.add(new Modelo(null, "Modelo"));
                        if(mode.length()>0){
                            for (int i=0; i<mode.length(); i++){
                                JSONObject modelito = mode.getJSONObject(i);
                                idMod = modelito.getString("id");
                                nameMod = modelito.getString("name");
                                mod = new Modelo(idMod, nameMod);
                                modelosLista.add(mod);
                            }
                        }
                        marV = new Marca(idMar,nameMar,modelosLista);
                        marcas.add(marV);
                    }
                vehiculoLista = new Vehiculo(id, nombre, marcas);
                resul=true;
            } catch (JSONException e) {
                e.printStackTrace();
                resul=false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                resul=false;
            } catch (IOException e) {
                e.printStackTrace();
                resul=false;
            }

            return resul;
        }

        @Override
        protected void customOnPostExecute(Boolean result) {
            if(result){
                poblarSpinnerVehiculos();
            }
        }
    }

    private void poblarSpinnerVehiculos() {
        List<String> campos = new ArrayList<String>();
        for (int i = 0; i < marcas.size(); i++) {
            Log.i("Categoria",marcas.get(i).toString());
            campos.add(marcas.get(i).getNombre());
            Log.i("Campo",campos.get(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,R.layout.my_simple_spinner_item2, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerMarca.setAdapter(spinnerAdapter);
    }

    private void poblarSpinnerModelos(ArrayList<Modelo> aux) {
        List<String> campos = new ArrayList<String>();
        if(aux.size()>0){
        for (int i = 0; i < aux.size(); i++) {
            Log.i("Categoria", aux.get(i).toString());
            campos.add(aux.get(i).getNombre());
            Log.i("Campo", campos.get(i));
        }
        }else{
            campos.add(aux.get(0).getNombre());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context, R.layout.my_simple_spinner_item2, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerModelo.setAdapter(spinnerAdapter);
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
                nextLink=page.getString("getNextLink");
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
                            String  single = clasificados.get(position).getSingle();
                            String pos=""+position;
                            Intent o=new Intent(context,Single.class);
                            o.putExtra("single", single);
                            o.putExtra("posicion",pos);
                            context.startActivityForResult(o, SINGLE);

                    }
                }));
                reciclador.addItemDecoration(new and.clasificados.com.auxiliares.DecoracionLineaDivisoria(getActivity()));
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
            HttpGet get = new HttpGet(params[0]);
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
                    nextLink=page.getString("getNextLink");
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
        if(resultCode == context.RESULT_OK && requestCode == SINGLE) {
            if(data!=null) {
                String value = data.getStringExtra("posicion");
                int p=Integer.parseInt(value);
                layoutManager.scrollToPositionWithOffset(p,30);
            }
        }
        if(resultCode == context.RESULT_OK && requestCode == FILTRO) {
            if(data!=null) {
                //No se que hara xd

                /*String value = data.getStringExtra("posicion");
                int p=Integer.parseInt(value);
                layoutManager.scrollToPositionWithOffset(p,30);*/
            }
        }
    }
}