package and.clasificados.com;

import android.content.Intent;
import android.view.View;

import com.blunderer.materialdesignlibrary.handlers.ActionBarDefaultHandler;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerAccountsHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerAccountsMenuHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerBottomHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerStyleHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerTopHandler;
import com.blunderer.materialdesignlibrary.models.Account;

import and.clasificados.com.Actividades.Login;
import and.clasificados.com.Fragmentos.Categorias;
import and.clasificados.com.Fragmentos.Favoritos;
import and.clasificados.com.Fragmentos.Inicio;
import and.clasificados.com.Fragmentos.Mensajes;
import and.clasificados.com.Fragmentos.MisPublicaciones;

public class MainActivity  extends com.blunderer.materialdesignlibrary.activities.NavigationDrawerActivity {


    @Override
    public NavigationDrawerStyleHandler getNavigationDrawerStyleHandler() {
        return null;
    }

    @Override
    public NavigationDrawerAccountsHandler getNavigationDrawerAccountsHandler() {
            return new NavigationDrawerAccountsHandler(this)
                    .addAccount("Clasificados.com" , "correo@email.com",
                            R.drawable.profile3, R.drawable.profile2_background);
        }


    @Override
    public NavigationDrawerAccountsMenuHandler getNavigationDrawerAccountsMenuHandler() {
        return new NavigationDrawerAccountsMenuHandler(this)
                .addItem(getString(R.string.login), R.drawable.usuario, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), Login.class));
                    }
                });
    }

    @Override
    public void onNavigationDrawerAccountChange(Account account) {
    }

    @Override
    public NavigationDrawerTopHandler getNavigationDrawerTopHandler() {
        return new NavigationDrawerTopHandler(this)
                .addItem(R.string.nav_inicio, new Inicio())
                .addItem(R.string.nav_mispublicaciones, R.drawable.my, new MisPublicaciones())
                .addItem(R.string.nav_favoritos, R.drawable.favoritos,new Favoritos())
                .addItem(R.string.nav_mensajes, R.drawable.mensajes,new Mensajes())
                .addItem(R.string.nav_categorias, R.drawable.categorias, new Categorias());
    }

    @Override
    public NavigationDrawerBottomHandler getNavigationDrawerBottomHandler() {
        return new NavigationDrawerBottomHandler(this)
                .addItem(R.string.action_settings, R.drawable.configuracion, null)
                .addItem(R.string.facebook,R.drawable.ic_facebook,null);
    }

    @Override
    public boolean overlayActionBar() {
        return false;
    }

    @Override
    public boolean replaceActionBarTitleByNavigationDrawerItemTitle() {
        return true;
    }

    @Override
    public int defaultNavigationDrawerItemSelectedPosition() {
        return 0;
    }

    @Override
    protected boolean enableActionBarShadow() {
        return false;
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return new ActionBarDefaultHandler(this);
    }
}
