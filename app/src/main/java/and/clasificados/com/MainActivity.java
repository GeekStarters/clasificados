package and.clasificados.com;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import and.clasificados.com.actividades.Login;
import and.clasificados.com.actividades.Publicar;
import and.clasificados.com.fragmentos.Categorias;
import and.clasificados.com.fragmentos.Favoritos;
import and.clasificados.com.fragmentos.Inicio;
import and.clasificados.com.fragmentos.Mensajes;
import and.clasificados.com.fragmentos.MisPublicaciones;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    Button image_login;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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

        View header = navigationView.getHeaderView(0);
        image_login= (Button) header.findViewById(R.id.image_login);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
       // image_login=(Button)findViewById(R.id.image_login);
        image_login.setOnClickListener(this);
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    public void iniciar(View v) {
        //startActivity(new Intent(this, Login.class));
    }

    public void crear(View v) {
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
                fragmentoGenerico = new MisPublicaciones();
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://and.clasificados.com/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://and.clasificados.com/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_login:
                startActivity(new Intent(this, Login.class));

                break;
        }
    }
}
