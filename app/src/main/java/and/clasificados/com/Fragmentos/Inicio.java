package and.clasificados.com.fragmentos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import and.clasificados.com.Constants;
import and.clasificados.com.R;
import and.clasificados.com.actividades.Login;
import and.clasificados.com.actividades.Mensajes;
import and.clasificados.com.actividades.MiCuenta;
import and.clasificados.com.actividades.Publicar;
import and.clasificados.com.auxiliares.CategoriasTab;
import and.clasificados.com.exception.NetworkException;
import and.clasificados.com.exception.ParsingException;
import and.clasificados.com.exception.ServerException;
import and.clasificados.com.exception.TimeOutException;
import and.clasificados.com.modelo.Tab;
import and.clasificados.com.services.AppAsynchTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class Inicio extends Fragment {
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ArrayList<Tab> listaTabs;
    Activity context;
    ImageView plus;
    TextView mensajes, miCuenta;

    public Inicio() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_paginado, container, false);
        final String strtext = getArguments().getString("auto");
        context = getActivity();
        if (savedInstanceState == null) {
            listaTabs = new ArrayList<Tab>();
            insertarTabs(container);
            viewPager = (ViewPager) view.findViewById(R.id.pager);
            new ObtenerTabs(context).execute();

        }
        plus = (ImageView) view.findViewById(R.id.nuevo);
        miCuenta = (TextView)view.findViewById(R.id.miCuenta);
        mensajes = (TextView)view.findViewById(R.id.mensajes_button);
        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    }

                }
            }
        };
        plus.setOnClickListener(onclick);
        mensajes.setOnClickListener(onclick);
        miCuenta.setOnClickListener(onclick);
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

    private void setupTabIcons(ArrayList<Tab> lista) {
        for(int i=0;i<lista.size();i++){
            LayoutInflater inflador = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vista = inflador.inflate(R.layout.tabbar_view_new,null);
            TextView tabText = (TextView)vista.findViewById(R.id.tab);
            tabText.setText(lista.get(i).getNombre());
            ImageView tabIcon = (ImageView)vista.findViewById(R.id.img_tab);
            Picasso.with(getActivity())
                    .load(lista.get(i).getUrl_imagen())
                    .fit()
                    .into(tabIcon);
            tabLayout.getTabAt(i).setCustomView(vista);
        }
    }

    private void insertarTabs(ViewGroup container) {
        View padre = (View) container.getParent();
        appBarLayout = (AppBarLayout) padre.findViewById(R.id.appbar                                      );
        tabLayout = new TabLayout(getActivity());
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
        appBarLayout.addView(tabLayout);
    }

    private void poblarViewPager(ViewPager viewPager,ArrayList<Tab> lista) {
        AdaptadorSecciones adapter = new AdaptadorSecciones(getFragmentManager());
        for (int i=0;i<lista.size();i++){
            adapter.addFragment(CategoriasTab.nuevaInstancia(Integer.parseInt(lista.get(i).getId())), lista.get(i).getNombre());
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBarLayout.removeView(tabLayout);
    }



    /**
     * Un {@link FragmentStatePagerAdapter} que gestiona las secciones, fragmentos y
     * títulos de las pestañas
     */
    public class AdaptadorSecciones extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentos = new ArrayList<>();
        private final List<String> titulosFragmentos = new ArrayList<>();

        public AdaptadorSecciones(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentos.get(position);
        }

        @Override
        public int getCount() {
            return fragmentos.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentos.add(fragment);
            titulosFragmentos.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titulosFragmentos.get(position);
        }

    }


    private class ObtenerTabs extends AppAsynchTask<Void, Void,Boolean> {
    Activity actividad;
        public ObtenerTabs(Activity activity) {
            super(activity);
            actividad=activity;
        }

        @Override
        protected Boolean customDoInBackground(Void... arg0)   throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException{
            String id=null, nombre=null, icono=null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.sitios);
            get.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONArray results = respJSON.getJSONArray("data");
                Tab t;
                for(int i=0; i<results.length(); i++)
                {
                    JSONObject info= results.getJSONObject(i);
                    id=info.getString("id");
                    nombre = info.getString("name");
                    icono=info.getString("icon");
                    t= new Tab(id, nombre,icono);
                    Log.i("Tab", t.toString());
                    listaTabs.add(t);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void customOnPostExecute(Boolean result) {
            if(result){
                poblarViewPager(viewPager, listaTabs);
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons(listaTabs);
            }
        }
    }
}