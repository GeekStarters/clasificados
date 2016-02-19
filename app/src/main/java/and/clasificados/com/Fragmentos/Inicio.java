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
import and.clasificados.com.services.AppAsynchTask;

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
    Activity context;
    RelativeLayout filtro;

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
            insertarTabs(container);
            viewPager = (ViewPager) view.findViewById(R.id.pager);
            poblarViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            context=getActivity();
            setupTabIcons();
            //new DataCategory(context).execute();

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


    private void setupTabIcons() {

            LayoutInflater inflator1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vOne = inflator1.inflate(R.layout.tabbar_view_new, null);
            TextView tabOne = (TextView)vOne.findViewById(R.id.tab);
            tabOne.setText(getString(R.string.titulo_tab_vehiculos));
            ImageView img_tab1 = (ImageView)vOne.findViewById(R.id.img_tab);
            Picasso.with(getActivity())
                    .load(R.drawable.icon_tabbar_vehiculo)
                    .fit()
                    .into(img_tab1);
            tabLayout.getTabAt(0).setCustomView(vOne);

            LayoutInflater inflator2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vTwo = inflator2.inflate(R.layout.tabbar_view_new, null);
            TextView tabTwo = (TextView)vTwo.findViewById(R.id.tab);
            tabTwo.setText(getString(R.string.titulo_tab_inmuebles));
            ImageView img_tab2 = (ImageView)vTwo.findViewById(R.id.img_tab);
            Picasso.with(getActivity())
                    .load(R.drawable.icon_tabbar_edificio)
                    .fit()
                    .into(img_tab2);
            tabLayout.getTabAt(2).setCustomView(vTwo);

            LayoutInflater inflator3 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vThree = inflator3.inflate(R.layout.tabbar_view_new, null);
            TextView tabThree = (TextView)vThree.findViewById(R.id.tab);
            tabThree.setText(getString(R.string.titulo_tab_productos));
            ImageView img_tab3 = (ImageView)vThree.findViewById(R.id.img_tab);
            Picasso.with(getActivity())
                    .load(R.drawable.icon_tabbar_producto)
                    .fit()
                    .into(img_tab3);
            tabLayout.getTabAt(1).setCustomView(vThree);

            LayoutInflater inflator4 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vFour = inflator4.inflate(R.layout.tabbar_view_new, null);
            TextView tabFour = (TextView)vFour.findViewById(R.id.tab);
            tabFour.setText(getString(R.string.titulo_tab_empleos));
            ImageView img_tab4 = (ImageView)vFour.findViewById(R.id.img_tab);
            Picasso.with(getActivity())
                    .load(R.drawable.icon_tabbar_empleo)
                    .fit()
                    .into(img_tab4);
            tabLayout.getTabAt(3).setCustomView(vFour);

            LayoutInflater inflator5 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vFive = inflator5.inflate(R.layout.tabbar_view_new, null);
            TextView tabFive = (TextView)vFive.findViewById(R.id.tab);
            tabFive.setText(getString(R.string.titulo_tab_servicios));
            ImageView img_tab5 = (ImageView)vFive.findViewById(R.id.img_tab);
            Picasso.with(getActivity())
                    .load(R.drawable.icon_tabbar_servicio)
                    .fit()
                    .into(img_tab5);
            tabLayout.getTabAt(4).setCustomView(vFive);

    }

    private void insertarTabs(ViewGroup container) {
        View padre = (View) container.getParent();
        appBarLayout = (AppBarLayout) padre.findViewById(R.id.appbar                                      );
        tabLayout = new TabLayout(getActivity());
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
        appBarLayout.addView(tabLayout);

    }

    private void poblarViewPager(ViewPager viewPager) {
        AdaptadorSecciones adapter = new AdaptadorSecciones(getFragmentManager());
        adapter.addFragment(CategoriasTab.nuevaInstancia(1), getString(R.string.titulo_tab_vehiculos));
        adapter.addFragment(CategoriasTab.nuevaInstancia(2), getString(R.string.titulo_tab_productos));
        adapter.addFragment(CategoriasTab.nuevaInstancia(3), getString(R.string.titulo_tab_inmuebles));
        adapter.addFragment(CategoriasTab.nuevaInstancia(1), getString(R.string.titulo_tab_empleos));
        adapter.addFragment(CategoriasTab.nuevaInstancia(1), getString(R.string.titulo_tab_servicios));
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


    public class DataCategory extends AppAsynchTask<Void, String, String> {
        Activity actividad;
        String respuestaWS=null;


        public DataCategory(Activity activity) {
            super(activity);
            // TODO Auto-generated constructor stub
            actividad=activity;
        }

        @Override
        protected String customDoInBackground(Void... params)
                throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException {

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(Constants.categories);


            try {

                // Execute HTTP Post Request
                //httppost.setHeader("Authorization",Constants.md5(Constants.KEY_HEADER));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                InputStream instream = entity.getContent();
                String result = Constants.convertStreamToString(instream);
                System.out.println("result "+result);
                /*JSONObject myObject = new JSONObject(result);
                JSONObject myObjectItems = new JSONObject(myObject.getString("response"));
                JSONArray myObjectitems  = new JSONArray(myObjectItems.getString("items"));
                total_resultado=myObjectItems.getInt("items_total");
                for(int i = 0; i < myObjectitems.length(); i++){
                    JSONObject c = myObjectitems.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("section", c.getString("section"));
                    map.put("type", c.getString("type"));
                    map.put("object_id", c.getString("object_id"));
                    map.put("title", c.getString("title"));
                    map.put("image", c.getString("image"));
                    map.put("category_name", c.getString("category_name"));
                    map.put("entry_creation_date", c.getString("entry_creation_date"));
                    map.put("entry_start_date", c.getString("entry_start_date"));

                }*/

                respuestaWS="si";


            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }/*catch (JSONException e) {
                e.printStackTrace();
            }*/


            return respuestaWS;

        }

        @Override
        protected void customOnPostExecute(String result){


        }


    }

}