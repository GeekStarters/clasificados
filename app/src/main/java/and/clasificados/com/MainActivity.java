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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import and.clasificados.com.actividades.Login;
import and.clasificados.com.actividades.Publicar;
import and.clasificados.com.auxiliares.PrefUtils;
import and.clasificados.com.common.CircleTransformation;
import and.clasificados.com.fragmentos.Categorias;
import and.clasificados.com.actividades.Mis;
import and.clasificados.com.fragmentos.Inicio;
import and.clasificados.com.fragmentos.Mensajes;
import and.clasificados.com.fragmentos.MisPublicaciones;
import and.clasificados.com.fragmentos.NoLogin;
import and.clasificados.com.modelo.Usuario;
import io.fabric.sdk.android.services.concurrency.AsyncTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    Button image_login;
    ImageView picture;
    TextView user;
    String auto=null;
    NavigationView navigationView;
    Usuario login_user;
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
           navigationView = (NavigationView) findViewById(R.id.nav_view);
           View header = navigationView.getHeaderView(0);
           image_login= (Button) header.findViewById(R.id.image_login);
           picture = (ImageView) header.findViewById(R.id.profile);
           user = (TextView) header.findViewById(R.id.username);
           login_user=PrefUtils.getCurrentUser(MainActivity.this);
           if(login_user!=null){
               if(login_user.provider.equals("local")){
                   Picasso.with(getApplicationContext())
                           .load(R.drawable.profile3)              //aqui debe ir la url
                           .transform(new CircleTransformation())
                           .into(picture);
               }else{
                   Picasso.with(getApplicationContext())
                           .load("https://graph.facebook.com/"+login_user.facebookID + "/picture?type=large")
                           .transform(new CircleTransformation())
                           .into(picture);
               }
               image_login.setVisibility(View.INVISIBLE);
               picture.setVisibility(View.VISIBLE);
               user.setVisibility(View.VISIBLE);
               user.setText(login_user.name + " " + login_user.last);
               setNavigation(navigationView, true);
           }else {
               auto="none";
               image_login.setVisibility(View.VISIBLE);
               picture.setVisibility(View.INVISIBLE);
               user.setVisibility(View.INVISIBLE);
               setNavigation(navigationView, false);
           }

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

    private void prepararDrawer_nologin(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        selectItem(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

    }

    private void selectItem(MenuItem itemDrawer) {
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (itemDrawer.getItemId()) {
            case R.id.item_inicio:
                fragmentoGenerico = new Inicio();
                auto="false";
                Bundle bundle=new Bundle();
                bundle.putString("auto", auto);
                fragmentoGenerico.setArguments(bundle);
                break;
            case R.id.item_publicaciones:
                fragmentoGenerico = new NoLogin();
                break;
            case R.id.item_favoritos:
                fragmentoGenerico = new NoLogin();
                break;
            case R.id.item_mensajes:
                fragmentoGenerico = new NoLogin();
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

    private void seleccionarItem(MenuItem itemDrawer) {
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (itemDrawer.getItemId()) {
            case R.id.item_inicio:
                fragmentoGenerico = new Inicio();
                Bundle bundle=new Bundle();
                bundle.putString("auto", login_user.auto);
                fragmentoGenerico.setArguments(bundle);
                break;
            case R.id.item_publicaciones:
                fragmentoGenerico = new MisPublicaciones();
                break;
            case R.id.item_favoritos:
                startActivity(new Intent(this,Mis.class));
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
                Intent i = new Intent(this, Login.class);
                startActivity(i);
                break;
        }
    }

    public void setNavigation(NavigationView navigation, boolean b) {
        if (navigation != null) {
            if(b){
                prepararDrawer(navigation);
                seleccionarItem(navigation.getMenu().getItem(0));
            }else{
                prepararDrawer_nologin(navigation);
                selectItem(navigation.getMenu().getItem(0));
            }

        }
    }

}
