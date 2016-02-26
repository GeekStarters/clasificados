package and.clasificados.com.fragmentos;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
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
import and.clasificados.com.actividades.Publicar;
import and.clasificados.com.auxiliares.CategoriasTab;
import and.clasificados.com.exception.NetworkException;
import and.clasificados.com.exception.ParsingException;
import and.clasificados.com.exception.ServerException;
import and.clasificados.com.exception.TimeOutException;
import and.clasificados.com.modelo.Tab;
import and.clasificados.com.services.AppAsynchTask;
import io.fabric.sdk.android.services.concurrency.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class Inicio extends Fragment {
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    RelativeLayout filtro;
    ProgressDialog pDialog;
    ArrayList<Tab> listaTabs;

    public Inicio() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_paginado, container, false);
        final String strtext=getArguments().getString("auto");
        /*filtro = (RelativeLayout) view.findViewById(R.id.filtro_vehiculos);
        Button filtrar = (Button)filtro.findViewById(R.id.filtrado);
        filtrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ocultar(v);
            }
        });*/
        if (savedInstanceState == null) {
            listaTabs=new ArrayList<Tab>();
            insertarTabs(container);
            viewPager = (ViewPager) view.findViewById(R.id.pager);
            new ObtenerTabs().execute();

        }
        ImageView plus = (ImageView)view.findViewById(R.id.nuevo);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert strtext != null;
                if(strtext.equals("false")||strtext.isEmpty()){
                    transicion();
                }else {
                    Intent i = new Intent(getContext(), Publicar.class);
                    i.putExtra("basic",strtext);
                    startActivity(i);
                }
            }
        });

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

    private void animar(boolean mostrar)
    {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar)
        {
            //desde la esquina inferior derecha a la superior izquierda
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        }
        else
        {    //desde la esquina superior izquierda a la esquina inferior derecha
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
        //duración en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
        filtro.setLayoutAnimation(controller);
        filtro.startAnimation(animation);
    }

    public void ocultar(View button)
    {
        if (filtro.getVisibility() == View.VISIBLE)
        {
            animar(false);
            filtro.setVisibility(View.INVISIBLE);
        }

    }

    public void mostrar(View button) {
        if (filtro.getVisibility() == View.INVISIBLE) {
            animar(false);
            filtro.setVisibility(View.VISIBLE);
        }
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


    private class ObtenerTabs extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
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
                    listaTabs.add(t);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            poblarViewPager(viewPager, listaTabs);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons(listaTabs);
        }
    }

}