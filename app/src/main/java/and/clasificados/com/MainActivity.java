package and.clasificados.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import and.clasificados.com.actividades.Login;
import and.clasificados.com.actividades.Publicar;
import and.clasificados.com.fragmentos.Categorias;
import and.clasificados.com.fragmentos.Favoritos;
import and.clasificados.com.fragmentos.Inicio;
import and.clasificados.com.fragmentos.Mensajes;
import and.clasificados.com.fragmentos.MisPublicaciones;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        agregarToolbar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            prepararDrawer(navigationView);
            seleccionarItem(navigationView.getMenu().getItem(0));
        }
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
           // ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    public void iniciar(View v){
        startActivity(new Intent(this,Login.class));
    }

    public void crear(View v){
        startActivity(new Intent(this, Publicar.class));
    }

    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        seleccionarItem(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

    }


    private void seleccionarItem(MenuItem itemDrawer) {
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (itemDrawer.getItemId()) {
            case R.id.item_inicio:
                fragmentoGenerico = new Inicio();
                break;
            case R.id.item_publicaciones:
                fragmentoGenerico =new MisPublicaciones();
                break;
            case R.id.item_favoritos:
                fragmentoGenerico = new Favoritos();
                break;
            case R.id.item_mensajes:
                fragmentoGenerico = new Mensajes();
                break;
            case R.id.item_categorias:
                fragmentoGenerico = new Categorias();
                break;
            case R.id.item_configuracion:
                // startActivity(new Intent(this, ActividadConfiguracion.class));
                break;
            case R.id.item_sharefb:
                //     startActivity(new Intent(this, ActividadConfiguracion.class));
                break;
        }
        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_content, fragmentoGenerico)
                    .commit();
        }

        setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
