package and.clasificados.com.actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.avast.android.dialogs.fragment.ListDialogFragment;
import com.avast.android.dialogs.iface.IListDialogListener;
import com.squareup.picasso.Picasso;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import and.clasificados.com.Constants;
import and.clasificados.com.MainActivity;
import and.clasificados.com.R;
import and.clasificados.com.auxiliares.PrefUtils;
import and.clasificados.com.common.RoundedTransformation;
import and.clasificados.com.exception.NetworkException;
import and.clasificados.com.exception.ParsingException;
import and.clasificados.com.exception.ServerException;
import and.clasificados.com.exception.TimeOutException;
import and.clasificados.com.modelo.Categoria;
import and.clasificados.com.modelo.Localidad;
import and.clasificados.com.modelo.Moneda;
import and.clasificados.com.modelo.Municipio;
import and.clasificados.com.modelo.SubCategoria;
import and.clasificados.com.modelo.Usuario;
import and.clasificados.com.modelo.Zona;
import and.clasificados.com.services.AppAsynchTask;
import and.clasificados.com.views.EditTextLight;

public class Publicar extends AppCompatActivity implements IListDialogListener,AdapterView.OnItemSelectedListener {

    RelativeLayout divZona;
    EditTextLight title,costo,descr;
    ImageView tomarFoto,galeria1,galeria2,galeria3,galeria4,galeria5,galeria6, vaciar, desdeGal;
    Button publicar, moneda;
    String auto, categoria, subcategoria, idCurrency, idProducto="173",idLocacion="root";
    ProgressDialog pDialog;
    Spinner spinnerCat, spinnerSub,spinnerZona,spinnerLoc,spinnerMun;
    GridLayout grid;
    Activity context;
    private ArrayList<Categoria> categoriasLista;
    private ArrayList<SubCategoria> subCategoriasLista;
    private ArrayList<Moneda> monedaLista;
    private ArrayList<Localidad> localidadesLista;
    private ArrayList<Municipio> municipiosLista;
    private ArrayList<Zona> zonasLista;
    private ArrayList<String> encoded, name;
    Uri file;
    File f;
    String nombre=null;
    boolean esInmueble=false;
    TextView contador;
    int num=0;
    final int GALERIA = 655;
    final int FOTOGRAFIA = 654;
    private static final int REQUEST_LIST_SIMPLE= 11;
    Usuario login_user;
    private static final int REQUEST_CODE = 0x11;
    String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar);
        agregarToolbar();
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE); // without sdk version check
        login_user= PrefUtils.getCurrentUser(Publicar.this);
        auto = login_user.auto;
        context=this;
        spinnerCat = (Spinner) findViewById(R.id.spinner_categoria);
        spinnerSub = (Spinner) findViewById(R.id.spinner_subcat);
        spinnerLoc = (Spinner) findViewById(R.id.spinner_location);
        spinnerMun = (Spinner) findViewById(R.id.spinner_municipio);
        spinnerZona = (Spinner) findViewById(R.id.spinner_zona);
        title = (EditTextLight)findViewById(R.id.titulo);
        costo = (EditTextLight)findViewById(R.id.costo);
        descr = (EditTextLight)findViewById(R.id.descripcion);
        tomarFoto = (ImageView) findViewById(R.id.camera);
        desdeGal=(ImageView)findViewById(R.id.gallery);
        vaciar=(ImageView)findViewById(R.id.delete);
        galeria1 = (ImageView) findViewById(R.id.anuncio1);
        galeria2 = (ImageView) findViewById(R.id.anuncio2);
        galeria3 = (ImageView) findViewById(R.id.anuncio3);
        galeria4 = (ImageView) findViewById(R.id.anuncio4);
        galeria5 = (ImageView) findViewById(R.id.anuncio5);
        galeria6 = (ImageView) findViewById(R.id.anuncio6);
        grid = (GridLayout)findViewById(R.id.galeria_imagenes);
        contador=(TextView)findViewById(R.id.numfotografias);
        divZona = (RelativeLayout)findViewById(R.id.contentTwo);
        llenarGaleria(savedInstanceState);
        monedaLista = new ArrayList<Moneda>();
        moneda=(Button)findViewById(R.id.currency);
        new ObtenerCategorias(context).execute();
       // new ObtenerLocalidades(context).execute();
        new ObtenerMoneda(context).execute();
        publicar = (Button) findViewById(R.id.button_publicar);
        View.OnClickListener onclick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                switch (v.getId()){
                    case R.id.currency:
                        ListDialogFragment
                                .createBuilder(getApplicationContext(), getSupportFragmentManager())
                                .setTitle(getString(R.string.select_moneda))
                                .setItems(obtenerMoneda(monedaLista))
                                .setRequestCode(REQUEST_LIST_SIMPLE)
                                .show();
                        break;
                    case R.id.button_publicar:
                        NuevoAnuncio a = new NuevoAnuncio(context);
                        a.execute(auto);
                        break;
                    case R.id.camera:
                       if(num>=6){
                           Toast.makeText(getApplicationContext(),"Solo puede agregar 6 fotografias",Toast.LENGTH_LONG).show();
                       }else{
                           getFileUri();
                           intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
                           startActivityForResult(intent,FOTOGRAFIA);
                       }
                        break;
                    case R.id.gallery:
                        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        startActivityForResult(intent,GALERIA);
                        break;
                    case R.id.delete:
                        grid.setVisibility(View.INVISIBLE);
                        num=0;
                        contador.setText(getString(R.string.fotografia));
                        break;

                }
            }
        };
        name = new ArrayList<>();
        categoriasLista = new ArrayList<Categoria>();
        subCategoriasLista = new ArrayList<SubCategoria>();
        localidadesLista = new ArrayList<Localidad>();
        municipiosLista = new ArrayList<Municipio>();
        zonasLista = new ArrayList<Zona>();
        municipiosLista.add(new Municipio(null,"Municipios","NO"));
        localidadesLista.add(new Localidad("1","Toda Guatemala"));
        zonasLista.add(new Zona(null,"Zonas"));
        categoriasLista.add(new Categoria(null,"Categoria"));
        subCategoriasLista.add(new SubCategoria(null,"Subcategoria",null));
        vaciar.setOnClickListener(onclick);
        desdeGal.setOnClickListener(onclick);
        moneda.setOnClickListener(onclick);
        publicar.setOnClickListener(onclick);
        tomarFoto.setOnClickListener(onclick);
        spinnerCat.setOnItemSelectedListener(this);
        spinnerSub.setOnItemSelectedListener(this);
        spinnerZona.setOnItemSelectedListener(this);
        spinnerMun.setOnItemSelectedListener(this);
        spinnerLoc.setOnItemSelectedListener(this);
    }

    private void getFileUri() {
        nombre = String.valueOf(Calendar.getInstance().getTimeInMillis())+".jpg";
        f=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+File.separator+nombre);
        file=Uri.fromFile(f);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // save file
            } else {
                Toast.makeText(getApplicationContext(), "PERMISSION_DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void llenarGaleria(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.getString("Foto") != null) {
                switch (num){
                    case 0:
                        galeria1.setImageURI(Uri.parse(savedInstanceState
                                .getString("Foto")));
                        file = Uri.parse(savedInstanceState.getString("Foto"));
                        break;
                    case 1:
                        galeria2.setImageURI(Uri.parse(savedInstanceState
                                .getString("Foto")));
                        file = Uri.parse(savedInstanceState.getString("Foto"));
                        break;
                    case 2:
                        galeria3.setImageURI(Uri.parse(savedInstanceState
                                .getString("Foto")));
                        file = Uri.parse(savedInstanceState.getString("Foto"));
                        break;
                    case 3:
                        galeria4.setImageURI(Uri.parse(savedInstanceState
                                .getString("Foto")));
                        file = Uri.parse(savedInstanceState.getString("Foto"));
                        break;
                    case 4:
                        galeria5.setImageURI(Uri.parse(savedInstanceState
                                .getString("Foto")));
                        file = Uri.parse(savedInstanceState.getString("Foto"));
                        break;
                    case 5:
                        galeria6.setImageURI(Uri.parse(savedInstanceState
                                .getString("Foto")));
                        file = Uri.parse(savedInstanceState.getString("Foto"));
                        break;
                }
            }
        }
    }

    public void onSaveInstanceState(Bundle bundle){
        if (file!=null){
            bundle.putString("Foto", file.toString());
        }
        super.onSaveInstanceState(bundle);
    }

    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }


        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent intent) {
        if (RequestCode==FOTOGRAFIA &&ResultCode == RESULT_OK){
            name.add(file.getPath());
                switch (num){
                    case 0:
                        galeria1.setImageBitmap(decodeSampledBitmapFromFile(file.getPath(), 680, 480));
                        num=num+1;
                        contador.setText(num+" "+getString(R.string.fotografia));
                        break;
                    case 1:
                        galeria2.setImageBitmap(decodeSampledBitmapFromFile(file.getPath(), 680, 480));
                        num=num+1;
                        contador.setText(num+" "+getString(R.string.fotografias));
                        break;
                    case 2:
                        galeria3.setImageBitmap(decodeSampledBitmapFromFile(file.getPath(),680,480));
                        num=num+1;
                        contador.setText(num+" "+getString(R.string.fotografias));
                        break;
                    case 3:
                        galeria4.setImageBitmap(decodeSampledBitmapFromFile(file.getPath(), 680, 480));
                        num=num+1;
                        contador.setText(num+" "+getString(R.string.fotografias));
                        break;
                    case 4:
                        galeria5.setImageBitmap(decodeSampledBitmapFromFile(file.getPath(), 680, 480));
                        num=num+1;
                        contador.setText(num+" "+getString(R.string.fotografias));
                        break;
                    case 5:
                        galeria6.setImageBitmap(decodeSampledBitmapFromFile(file.getPath(),680,480));
                        num=num+1;
                        contador.setText(num+" "+getString(R.string.fotografias));
                        break;
                }
            } else if(RequestCode==GALERIA && ResultCode == RESULT_OK){
                Uri selectedImage = intent.getData();
                name.add(selectedImage.getPath());
            switch (num){
                        case 0:
                            Picasso.with(getApplicationContext())
                                    .load(selectedImage)
                                    .transform(new RoundedTransformation(15,0))
                                    .fit()
                                    .into(galeria1);
                            num=num+1;
                            contador.setText(num+" "+getString(R.string.fotografia));
                            break;
                        case 1:
                            Picasso.with(getApplicationContext())
                                    .load(selectedImage)
                                    .transform(new RoundedTransformation(15,0))
                                    .fit()
                                    .into(galeria2);
                            num=num+1;
                            contador.setText(num+" "+getString(R.string.fotografias));
                            break;
                        case 2:
                            Picasso.with(getApplicationContext())
                                    .load(selectedImage)
                                    .transform(new RoundedTransformation(15,0))
                                    .fit()
                                    .into(galeria3);
                            num=num+1;
                            contador.setText(num+" "+getString(R.string.fotografias));
                            break;
                        case 3:
                            Picasso.with(getApplicationContext())
                                    .load(selectedImage)
                                    .transform(new RoundedTransformation(15,0))
                                    .fit()
                                    .into(galeria4);
                            num=num+1;
                            contador.setText(num+" "+getString(R.string.fotografias));
                            break;
                        case 4:
                            Picasso.with(getApplicationContext())
                                    .load(selectedImage)
                                    .transform(new RoundedTransformation(15,0))
                                    .fit()
                                    .into(galeria5);
                            num=num+1;
                            contador.setText(num+" "+getString(R.string.fotografias));
                            break;
                        case 5:
                            Picasso.with(getApplicationContext())
                                    .load(selectedImage)
                                    .transform(new RoundedTransformation(15,0))
                                    .fit()
                                    .into(galeria6);
                            num=num+1;
                            contador.setText(num+" "+getString(R.string.fotografias));
                            break;
                    }
            } else{
                Toast.makeText(getApplicationContext(),"fotografia No tomada", Toast.LENGTH_SHORT).show();
            }
    }

    private String[] obtenerMoneda(ArrayList<Moneda> lista){
        ArrayList<String> datos = new ArrayList<String>();
        for (int i = 0; i < lista.size(); i++) {
            datos.add(lista.get(i).getSimbolo());
        }
        String[] campos = datos.toArray(new String[datos.size()]);
        return campos;
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_custom);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.title_activity_publicar);
    }

    @Override
    public void onListItemSelected(CharSequence value, int number, int requestCode) {
        if (requestCode == REQUEST_LIST_SIMPLE) {
            idCurrency = monedaLista.get(number).getId();
            moneda.setText(value);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publicar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clean:
                grid.setVisibility(View.INVISIBLE);
                num=0;
                contador.setText(getString(R.string.fotografia));
                title.setText("");
                descr.setText("");
                costo.setText("");
                moneda.setText("Q");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
        switch (parent.getId()) {
            case R.id.spinner_categoria:
                categoria = categoriasLista.get(position).getId();
                String nombre = parent.getItemAtPosition(position).toString();
                if(nombre.equals("Inmuebles")){
                    esInmueble=true;
                }
                subCategoriasLista.clear();
                subCategoriasLista.add(new SubCategoria(null, "Subcategoria", null));
                ObtenerSubCategorias sub = new ObtenerSubCategorias(context);
                sub.execute(nombre);
                break;
            case R.id.spinner_subcat:
                subcategoria = subCategoriasLista.get(position).getId();
                break;/*
            case R.id.spinner_location:
                if(localidadesLista.get(position).getId().equals("1")){
                    idLocacion = localidadesLista.get(position).getId();
                }else{
                    idLocacion=localidadesLista.get(position).getId();
                    /*ObtenerMunicipio mun = new ObtenerMunicipio();
                    mun.execute(localidadesLista.get(position).getId());*/
                    /*poblarSpinnerMunicipios();
                }
                break;
            case R.id.spinner_municipio:
                if(esInmueble){
                    divZona.setVisibility(View.VISIBLE);
                    poblarSpinnerZona();
                    /*if(municipiosLista.get(position).getZona().equals("NO")){
                        idLocacion = municipiosLista.get(position).getId();
                    }else{
                       ObtenerZona zo=new ObtenerZon();
                        zo.execute(municipiosLista.get(position).getZona());
                       spinnerZona.setEnabled(true);
                    }*/
               /* }else{
                    divZona.setVisibility(View.INVISIBLE);
                   // idLocacion = municipiosLista.get(position).getId();
                }
                break;
            case R.id.spinner_zona:
               // idLocacion=zonasLista.get(position).getId();
                break;*/
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class NuevoAnuncio extends AppAsynchTask<String,Integer,Boolean> {
        String idAd=null;
        Activity actividad;

        public NuevoAnuncio(Activity activity) {
            super(activity);
            actividad=activity;
        }

        protected Boolean customDoInBackground(String... params)  throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException{
            boolean resul;
            String message;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(Constants.nuevo_clasificado);
            post.setHeader("authorization", "Basic"+ " " +params[0] );
            post.setHeader("content-type", "application/json");
            try
            {
                final String titulo = title.getText().toString();
                final String precio = costo.getText().toString();
                final String descripcion= descr.getText().toString();
                JSONObject map = new JSONObject();
                map.put("title", titulo);
                map.put("description", descripcion);
                map.put("price", precio);
                map.put("category_id",Integer.parseInt(subcategoria));
                map.put("currency_id",Integer.parseInt(idCurrency));
                map.put("location_id","root");
                map.put("product_id",Integer.parseInt(idProducto));
                message = map.toString();
                post.setHeader("Content-type", "application/json");
                post.setHeader("authorization", "Basic" + " " + params[0]);
                post.setEntity(new StringEntity(message, "UTF8"));
                HttpResponse resp = httpClient.execute(post);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                String aux = respJSON.get("errors").toString();
                JSONObject data= respJSON.getJSONObject("data");
                if(aux.equals("[]")){
                    idAd=data.getString("id");
                    Log.i("Objeto",idAd);
                    resul=true;
                }else{
                    resul=false;
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void customOnPostExecute(Boolean result) {

            if (result)
            {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                showShareDialog();
            }else{
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ObtenerCategorias extends AppAsynchTask<Void, Void, Boolean>{

        Activity actividad;
        public ObtenerCategorias(Activity activity) {
            super(activity);
            actividad=activity;
        }

        @Override
        protected Boolean customDoInBackground(Void... arg0)  throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException {

           boolean resul=false; String id=null, nombre=null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.categories);
            get.setHeader("content-type", "application/json");
                try {
                    HttpResponse resp = httpClient.execute(get);
                    JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                    JSONArray results = respJSON.getJSONArray("data");
                    Categoria c;
                    for(int i=0; i<results.length(); i++) {
                        JSONObject info = results.getJSONObject(i);
                        id = info.getString("id");
                        nombre = info.getString("name");
                        c = new Categoria(id, nombre);
                        categoriasLista.add(c);
                    }
                    resul=true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    resul=false;
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    resul=false;
                } catch (IOException e) {
                    e.printStackTrace();
                    resul=false;
                }

            return resul;
        }

        @Override
        protected void customOnPostExecute(Boolean result) {
           if(result){
               poblarSpinnerCategorias();
           }
        }
    }

    private class ObtenerLocalidades extends AppAsynchTask<Void, Void, Void>{

        Activity actividad;
        public ObtenerLocalidades(Activity activity) {
            super(activity);
            actividad=activity;
        }

        @Override
        protected Void customDoInBackground(Void... arg0)  throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException {
            String id=null, nombre=null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.localidades);
            get.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject data = respJSON.getJSONObject("data");
                JSONArray results = data.getJSONArray("locations");
                Localidad c;
                for(int i=0; i<results.length(); i++)
                {
                    JSONObject info= results.getJSONObject(i);
                    id=info.getString("id");
                    nombre = info.getString("name");
                    c= new Localidad(id, nombre);
                    localidadesLista.add(c);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void customOnPostExecute(Void result) {
            poblarSpinnerLocalidades();
        }
    }

    private class ObtenerSubCategorias extends AppAsynchTask<String, Void, Boolean>{

        Activity actividad;
        public ObtenerSubCategorias(Activity activity) {
            super(activity);
            actividad=activity;
        }

        @Override
        protected Boolean customDoInBackground(String... params)  throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException{
            String sub_id=null, sub_nombre=null, parent_id=null;
            Boolean resul;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.categories);
            get.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONArray results = respJSON.getJSONArray("data");
                SubCategoria sc;
                for(int i=0; i<results.length(); i++)
                {
                    JSONObject childs= results.getJSONObject(i);
                    JSONArray sub=childs.getJSONArray("childs");
                    for (int j=0; j<sub.length(); j++){
                        JSONObject info= sub.getJSONObject(j);
                        sub_id=info.getString("child_id");
                        sub_nombre = info.getString("child_name");
                        parent_id = info.getString("parent_name");
                        sc= new SubCategoria(sub_id,sub_nombre,parent_id);
                        Log.i("Subcategoria "+j,sc.toString());
                        if(sc.getNameParent().equals(params[0])) {
                            subCategoriasLista.add(sc);
                        }
                    }
                }
                resul=true;
            } catch (JSONException e) {
                e.printStackTrace();
                resul=false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                resul=false;
            } catch (IOException e) {
                e.printStackTrace();
                resul=false;
            }

            return resul;
        }

        @Override
        protected void customOnPostExecute(Boolean result) {
            if(result){
                poblarSpinnerSubCategorias();
            }
        }
    }

    private class ObtenerMoneda extends AppAsynchTask<Void, Void, Boolean>{

        Activity actividad;
        public ObtenerMoneda(Activity activity) {
            super(activity);
            actividad=activity;
        }

        @Override
        protected Boolean customDoInBackground(Void... arg0)  throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException {
            String id=null, nombre=null,simb=null;
            Boolean resul = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.moneda);
            get.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONArray results = respJSON.getJSONArray("data");
                Moneda c;
                for(int i=0; i<results.length(); i++)
                {
                    JSONObject info= results.getJSONObject(i);
                    id=info.getString("id");
                    nombre = info.getString("name");
                    simb=info.getString("symbol");
                    c= new Moneda(id, nombre, simb);
                    monedaLista.add(c);
                    resul=true;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                resul=false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                resul=false;
            } catch (IOException e) {
                e.printStackTrace();
                resul=false;
            }

            return resul;
        }

        @Override
        protected void customOnPostExecute(Boolean result) {
            if (result) {
                obtenerMoneda(monedaLista);
            }
        }
    }

    private void poblarSpinnerCategorias() {
        List<String> campos = new ArrayList<String>();
        for (int i = 0; i < categoriasLista.size(); i++) {
            Log.i("Categoria",categoriasLista.get(i).toString());
            campos.add(categoriasLista.get(i).getNombre());
            Log.i("Campo",campos.get(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,R.layout.my_simple_spinner_item, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerCat.setAdapter(spinnerAdapter);
    }

    private void poblarSpinnerLocalidades() {
        List<String> campos = new ArrayList<String>();
        for (int i = 0; i < localidadesLista.size(); i++) {
            campos.add(localidadesLista.get(i).getNombre());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,R.layout.my_simple_spinner_item, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerLoc.setAdapter(spinnerAdapter);
    }

    private void poblarSpinnerSubCategorias() {
        List<String> campos = new ArrayList<String>();
        for (int i = 0; i < subCategoriasLista.size(); i++) {
            campos.add(subCategoriasLista.get(i).getNombre());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,R.layout.my_simple_spinner_item, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerSub.setAdapter(spinnerAdapter);
    }

    private void poblarSpinnerMunicipios() {
        List<String> campos = new ArrayList<String>();
        for (int i = 0; i < municipiosLista.size(); i++) {
            campos.add(municipiosLista.get(i).getNombre());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,R.layout.my_simple_spinner_item, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerMun.setAdapter(spinnerAdapter);
    }

    private void poblarSpinnerZona() {
        List<String> campos = new ArrayList<String>();
        for (int i = 0; i < zonasLista.size(); i++) {
            campos.add(zonasLista.get(i).getNombre());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,R.layout.my_simple_spinner_item, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerZona.setAdapter(spinnerAdapter);
    }

    protected void showShareDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(Publicar.this);
        View promptView = layoutInflater.inflate(R.layout.share_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Publicar.this);
        alertDialogBuilder.setView(promptView);
        final CheckBox check = (CheckBox)promptView.findViewById(R.id.checkbox_dialog);
        final Button b=(Button) promptView.findViewById(R.id.button_dialog);
        alertDialogBuilder.setCancelable(true);
        View.OnClickListener oncl=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Se publico y fuimos a fb", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Solo se publico", Toast.LENGTH_LONG).show();
                }
            }
        };
        b.setOnClickListener(oncl);
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void encodedImage(){
        encoded = new ArrayList<>();
        for (int i=0;i<name.size();i++){
            Bitmap bm = BitmapFactory.decodeFile(name.get(i));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, output); //bm is the bitmap object
            byte[] bytes = output.toByteArray();
            String encode = Base64.encodeToString(bytes,0);
            encoded.add(encode);
        }
    }

    private class SubirFoto extends AppAsynchTask<String,Void,Boolean>{

        Activity actividad;
        public SubirFoto(Activity activity) {
            super(activity);
            actividad=activity;
        }

        @Override
        protected Boolean customDoInBackground(String... params)
                throws NetworkException, ServerException, ParsingException, TimeOutException, IOException, JSONException {
            Boolean resul;
            encodedImage();
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost post = new HttpPost(Constants.upload_foto);
                post.setHeader("authorization", "Basic" + " " + params[0]);
                post.setHeader("Content-Type", "multipart/form-data");
                HttpEntity builder = MultipartEntityBuilder.create()
                        .addTextBody("ad_id", params[1])
                        .addTextBody("priority", "1")
                        .addTextBody("category_id",params[2])
                        .build();

                post.setEntity(builder);
                HttpResponse resp = httpClient.execute(post);
                HttpEntity responseEntity = resp.getEntity();




                resul=true;
            }catch (Exception ex){
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;

        }

        @Override
        protected void customOnPostExecute(Boolean result) {

        }

    }

}
