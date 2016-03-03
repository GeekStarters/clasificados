package and.clasificados.com;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import and.clasificados.com.auxiliares.PrefUtils;
import and.clasificados.com.fragmentos.Inicio;
import and.clasificados.com.modelo.Usuario;

public class MainActivity extends AppCompatActivity {

    String auto=null;
    Usuario login_user=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        agregarToolbar();
        login_user=PrefUtils.getCurrentUser(MainActivity.this);
        Fragment fragmentoGenerico =new Inicio();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(login_user!=null){
            auto=login_user.auto;
        }else{
            auto="false";
        }
        Bundle bundle=new Bundle();
        bundle.putString("auto", auto);
        fragmentoGenerico.setArguments(bundle);
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_content, fragmentoGenerico)
                .commit();
       }



    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // ab.setDisplayHomeAsUpEnabled(true);
        }

    }

}
