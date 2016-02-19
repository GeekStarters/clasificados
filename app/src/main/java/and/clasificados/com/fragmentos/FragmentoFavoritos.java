package and.clasificados.com.fragmentos;

import android.app.Activity;
import android.content.Context;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import and.clasificados.com.Constants;
import and.clasificados.com.R;
import and.clasificados.com.auxiliares.CategoriasTab;
import and.clasificados.com.exception.NetworkException;
import and.clasificados.com.exception.ParsingException;
import and.clasificados.com.exception.ServerException;
import and.clasificados.com.exception.TimeOutException;
import and.clasificados.com.services.AppAsynchTask;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class FragmentoFavoritos extends Fragment {
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Activity context;

    public FragmentoFavoritos() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_paginado, container, false);
        if (savedInstanceState == null) {
            insertarTabs(container);
            viewPager = (ViewPager) view.findViewById(R.id.pager);
            poblarViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            context=getActivity();
            setupTabIcons();
            //new DataCategory(context).execute();

        }

        return view;
    }


    private void setupTabIcons() {

            LayoutInflater inflator1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vOne = inflator1.inflate(R.layout.tabbar_view, null);
            TextView tabOne = (TextView) vOne.findViewById(R.id.tab);
            tabOne.setText(getString(R.string.nav_publicaciones));
            tabLayout.getTabAt(0).setCustomView(vOne);

            LayoutInflater inflator2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vTwo = inflator2.inflate(R.layout.tabbar_view_icon, null);
            TextView tabTwo = (TextView)vTwo.findViewById(R.id.tab);
            tabTwo.setText(getString(R.string.nav_favoritos));
            ImageView img_tab2 = (ImageView)vTwo.findViewById(R.id.img_tab);
            Picasso.with(getActivity())
                    .load(R.drawable.favoritos_small)
                    .fit()
                    .into(img_tab2);
            tabLayout.getTabAt(1).setCustomView(vTwo);
    }

    private void insertarTabs(ViewGroup container) {
        final View padre = (View) container.getParent();
        appBarLayout = (AppBarLayout) padre.findViewById(R.id.appbar);
        tabLayout = new TabLayout(getActivity());
        appBarLayout.addView(tabLayout);
    }

    private void poblarViewPager(ViewPager viewPager) {
        AdaptadorSecciones adapter = new AdaptadorSecciones(getFragmentManager());
        adapter.addFragment(CategoriasTab.nuevaInstancia(2), getString(R.string.nav_publicaciones));
        adapter.addFragment(CategoriasTab.nuevaInstancia(3), getString(R.string.nav_favoritos));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

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