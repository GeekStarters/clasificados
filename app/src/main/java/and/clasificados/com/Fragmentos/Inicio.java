package and.clasificados.com.Fragmentos;

import com.blunderer.materialdesignlibrary.handlers.ViewPagerHandler;

import and.clasificados.com.Auxiliares.CategoriasTab;
import and.clasificados.com.R;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class Inicio  extends com.blunderer.materialdesignlibrary.fragments.ViewPagerWithTabsFragment {

    @Override
    public ViewPagerHandler getViewPagerHandler() {
        return new ViewPagerHandler(getActivity())
                .addPage(R.string.titulo_tab_vehiculos,
                        CategoriasTab.newInstance(0))
                .addPage(R.string.titulo_tab_inmuebles,
                        CategoriasTab.newInstance(1))
                .addPage(R.string.titulo_tab_productos,
                        CategoriasTab.newInstance(2))
                .addPage(R.string.titulo_tab_empleos,
                        CategoriasTab.newInstance(3))
                .addPage(R.string.titulo_tab_servicios,
                        CategoriasTab.newInstance(4));
    }

    @Override
    public boolean expandTabs() {
        return false;
    }

    @Override
    public int defaultViewPagerPageSelectedPosition() {
        return 0;
    }



}