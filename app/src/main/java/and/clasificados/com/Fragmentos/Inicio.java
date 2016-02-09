package and.clasificados.com.fragmentos;

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
import android.widget.ImageView;
import android.widget.TextView;

import and.clasificados.com.R;
import and.clasificados.com.auxiliares.CategoriasTab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class Inicio extends Fragment {
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public Inicio() {
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
            setupTabIcons();
        }

        return view;
    }

        private void setupTabIcons() {
 
        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tabbar_view, null);
        tabOne.setText(getString(R.string.titulo_tab_vehiculos));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_tabbar_vehiculo, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);
 
        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tabbar_view, null);
        tabTwo.setText(getString(R.string.titulo_tab_inmuebles));
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_tabbar_edificio, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
 
        TextView tabThree = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tabbar_view, null);
        tabThree.setText(getString(R.string.titulo_tab_productos));
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_tabbar_producto, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
         
        TextView tabFour = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tabbar_view, null);
        tabFour.setText(getString(R.string.titulo_tab_empleos));
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_tabbar_empleo, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);

        TextView tabFive = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tabbar_view, null);
        tabFive.setText(getString(R.string.titulo_tab_servicios));
        tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_tabbar_servicio, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tabFive);
    }

    private void insertarTabs(ViewGroup container) {
        View padre = (View) container.getParent();
        appBarLayout = (AppBarLayout) padre.findViewById(R.id.appbar                                      );
        tabLayout = new TabLayout(getActivity());
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
        appBarLayout.addView(tabLayout);
    }

    private void poblarViewPager(ViewPager viewPager) {
        AdaptadorSecciones adapter = new AdaptadorSecciones(getFragmentManager());
        adapter.addFragment(CategoriasTab.nuevaInstancia(0), getString(R.string.titulo_tab_vehiculos));
        adapter.addFragment(CategoriasTab.nuevaInstancia(1), getString(R.string.titulo_tab_inmuebles));
        adapter.addFragment(CategoriasTab.nuevaInstancia(2), getString(R.string.titulo_tab_productos));
        adapter.addFragment(CategoriasTab.nuevaInstancia(3), getString(R.string.titulo_tab_empleos));
        adapter.addFragment(CategoriasTab.nuevaInstancia(4), getString(R.string.titulo_tab_servicios));
        viewPager.setAdapter(adapter);
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
}