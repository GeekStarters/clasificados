package and.clasificados.com.actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.viewpagerindicator.CirclePageIndicator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import and.clasificados.com.R;
import and.clasificados.com.auxiliares.PrefUtils;
import and.clasificados.com.auxiliares.SlidingImageAdapter;
import and.clasificados.com.exception.NetworkException;
import and.clasificados.com.exception.ParsingException;
import and.clasificados.com.exception.ServerException;
import and.clasificados.com.exception.TimeOutException;
import and.clasificados.com.modelo.Clasificado;
import and.clasificados.com.modelo.Usuario;
import and.clasificados.com.services.AppAsynchTask;

public class Single extends AppCompatActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<String> ImagesArray;
    ImageView contactar, ofertar,  tw,fb,wha, msg, sha;
    TextView reportar;
    String url = null;
    Activity context;
    TextView tit, pre, descr;
    GoogleMap mapa;
    Usuario login_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        agregarToolbar();
        context=this;
        login_user= PrefUtils.getCurrentUser(Single.this);
        Intent i = getIntent();
        url = i.getStringExtra("single");
        tit = (TextView) findViewById(R.id.texto_anuncio);
        descr= (TextView)findViewById(R.id.texto_descripcion);
        pre=(TextView)findViewById(R.id.texto_precio);
        mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        LatLng guate = new LatLng(13.74205, -90.1285);
        CameraPosition camPos = new CameraPosition.Builder()
                .target(guate)
                .zoom(5)
                .build();
        CameraUpdate camUpd1 = CameraUpdateFactory.newCameraPosition(camPos);
        mapa.moveCamera(camUpd1);
        new ObtenerSingle(context).execute(url);
        contactar=(ImageView)findViewById(R.id.contactar);
        ofertar=(ImageView)findViewById(R.id.ofertar);
        reportar=(TextView)findViewById(R.id.reportar);
        fb=(ImageView)findViewById(R.id.fb);
        tw=(ImageView)findViewById(R.id.tw);
        wha=(ImageView)findViewById(R.id.wha);
        msg=(ImageView)findViewById(R.id.msg);
        sha=(ImageView)findViewById(R.id.sha);
        View.OnClickListener onclick= new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                sendIntent.setType("text/plain");
                switch (v.getId()){
                    case R.id.contactar:
                        if(login_user!=null){
                            startActivity(new Intent(getApplicationContext(),Mensajes.class));
                        }else{
                            startActivity(new Intent(getApplicationContext(),Login.class));
                        }
                        break;
                    case R.id.ofertar:
                        if(login_user!=null){
                            showInputDialog();
                        }else{
                            startActivity(new Intent(getApplicationContext(),Login.class));
                        }
                        break;
                    case R.id.reportar:
                        Toast.makeText(getApplicationContext(),"Esto desplegara un alert para reportar el anuncio", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.fb:
                        Toast.makeText(getApplicationContext(),"Esto desplegara un intent a facebook", Toast.LENGTH_LONG).show();
                     break;
                    case R.id.tw:
                        Toast.makeText(getApplicationContext(),"Esto desplegara un intent a twitter", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.wha:
                        sendIntent.setPackage("com.whatsapp");
                        try{
                            startActivity(sendIntent);
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),getString(R.string.install_wha), Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.sha:
                        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                        break;
                    case R.id.msg:
                        sendIntent.setPackage("com.facebook.orca");
                        try{
                            startActivity(sendIntent);
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),getString(R.string.install_mesg), Toast.LENGTH_LONG).show();
                        }
                        break;

                }
            }
        };
        contactar.setOnClickListener(onclick);
        ofertar.setOnClickListener(onclick);
        tw.setOnClickListener(onclick);
        fb.setOnClickListener(onclick);
        wha.setOnClickListener(onclick);
        msg.setOnClickListener(onclick);
        sha.setOnClickListener(onclick);
        reportar.setOnClickListener(onclick);

    }

    protected void showInputDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(Single.this);
        View promptView = layoutInflater.inflate(R.layout.input_text_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Single.this);
        alertDialogBuilder.setView(promptView);
        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        final Button b=(Button) promptView.findViewById(R.id.button_dialog);
        alertDialogBuilder.setCancelable(true);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Oferta: " + editText.getText(), Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void init() {
        mPager = (ViewPager) findViewById(R.id.pager_imagen);
        mPager.setAdapter(new SlidingImageAdapter(Single.this,ImagesArray));
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }
            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_custom);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.title_activity_single);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_single, menu);
        return true;
    }

    private class ObtenerSingle extends AppAsynchTask<String,Integer,Boolean> {
        Clasificado c;
        Activity actividad;
        String precio=null, titulo=null, url_imagen=null, descripcion=null, vista=null;

        public ObtenerSingle(Activity activity) {
            super(activity);
            actividad=activity;
        }

        protected Boolean customDoInBackground(String... params)   throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException {
            boolean resul;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(params[0]);
            get.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject data  = respJSON.getJSONObject("data");
                JSONObject single = data.getJSONObject("single");
                titulo=single.getString("title");
                descripcion= single.getString("description");
                precio = single.getString("currencySymbol")+" "+single.getString("price");
                ImagesArray = new ArrayList<String>();
               JSONArray results = single.getJSONArray("images");
                for(int i=0; i<results.length(); i++)
                {
                    String ad = results.getString(i);
                    ImagesArray.add(ad);
                }
                resul = true;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul=false;
            }
            return resul;
        }

        protected void customOnPostExecute(final Boolean result) {
            if (result) {
                tit.setText(titulo);
                pre.setText(precio);
                descr.setText(descripcion);
                init();
            }
        }
    }

}
