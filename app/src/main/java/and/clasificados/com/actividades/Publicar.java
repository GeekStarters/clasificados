package and.clasificados.com.actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
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
import and.clasificados.com.modelo.Modelo;
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
    Button publicar;
    TextView moneda;
    String auto, categoria, subcategoria, idCurrency="1", idProducto="173",idLocacion="root";
    Spinner spinnerCat, spinnerSub,spinnerZona,spinnerLoc,spinnerMun;
    //GridLayout grid;
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
    CheckBox f1, f2, f3, f4, f5, f6;
    final int GALERIA = 655;
    final int FOTOGRAFIA = 654;
    private static final int REQUEST_LIST_SIMPLE= 11;
    Usuario login_user;
    private static final int REQUEST_CODE = 0x11;
    String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE"};
    private List<Municipio> listaMunicipio;
    private List<Localidad> listaLocaciones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar);
        agregarToolbar();
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE); // without sdk version check
        login_user= PrefUtils.getCurrentUser(Publicar.this);
        auto = login_user.auto;
        context=this;
        //CheckBox
        f1 = (CheckBox)findViewById(R.id.checkBox1);
        f2 = (CheckBox)findViewById(R.id.checkBox2);
        f3 = (CheckBox)findViewById(R.id.checkBox3);
        f4 = (CheckBox)findViewById(R.id.checkBox4);
        f5 = (CheckBox)findViewById(R.id.checkBox5);
        f6 = (CheckBox)findViewById(R.id.checkBox6);
        //Spinnners
        spinnerCat = (Spinner) findViewById(R.id.spinner_categoria);
        spinnerSub = (Spinner) findViewById(R.id.spinner_subcat);
        spinnerLoc = (Spinner) findViewById(R.id.spinner_location);
        spinnerMun = (Spinner) findViewById(R.id.spinner_municipio);
        spinnerZona = (Spinner) findViewById(R.id.spinner_zona);
        title = (EditTextLight)findViewById(R.id.titulo);
        f1.setVisibility(View.GONE);
        f2.setVisibility(View.GONE);
        f3.setVisibility(View.GONE);
        f4.setVisibility(View.GONE);
        f5.setVisibility(View.GONE);
        f6.setVisibility(View.GONE);
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
        contador=(TextView)findViewById(R.id.numfotografias);
        divZona = (RelativeLayout)findViewById(R.id.contentTwo);
        llenarGaleria(savedInstanceState);
        monedaLista = new ArrayList<Moneda>();
        moneda=(TextView)findViewById(R.id.currency);
        new ObtenerCategorias(context).execute();
        new ObtenerLocaciones(context).execute();
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
                        View focusView=null; View focusView1=null;
                        String t = title.getText().toString();
                        String p = costo.getText().toString();
                        boolean bloqueo=true;
                        if(t.isEmpty()) {
                            title.setError("Debe asignar un titulo");
                            focusView = title;
                            focusView.requestFocus();
                            bloqueo=false;
                        }else if(p.isEmpty()) {
                            costo.setError("Debe asignar un costo.");
                            focusView1=costo;
                            focusView1.requestFocus();
                            bloqueo=false;
                        }else if(spinnerCat.getSelectedItemPosition()==0){
                            showAlertDialogC();
                            bloqueo=false;
                        }else if (spinnerSub.getSelectedItemPosition()==0) {
                            showAlertDialogS();
                            bloqueo = false;
                        }
                        if(bloqueo){
                            NuevoAnuncio a = new NuevoAnuncio(context);
                            a.execute(auto);
                        }
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
                        deleteImages();
        //                grid.setVisibility(View.GONE);
                       /* galeria1.setImageResource(R.drawable.blanco);
                        galeria2.setImageResource(R.drawable.blanco);
                        galeria3.setImageResource(R.drawable.blanco);
                        galeria4.setImageResource(R.drawable.blanco);
                        galeria5.setImageResource(R.drawable.blanco);
                        galeria6.setImageResource(R.drawable.blanco);
                        name.clear();
                        num=0;*/
                      //  contador.setText(getString(R.string.fotografia));
                        break;

                }
            }
        };
        name = new ArrayList<>();
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

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
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

    private void deleteImages(){
        List<String> aux=new ArrayList<>();
        if(!name.isEmpty()||num==0){
            if(f1.isChecked() || f2.isChecked() || f3.isChecked() ||f4.isChecked()||f5.isChecked()||f6.isChecked()){
                switch (num){
                    case 1:
                        if(!f1.isChecked()) {
                            aux.add(name.get(0));
                        }
                        break;
                    case 2:
                        if(!f1.isChecked()) {
                            aux.add(name.get(0));
                        }
                        if (!f2.isChecked()) {
                            aux.add(name.get(1));
                        }
                        break;
                    case 3:
                        if(!f1.isChecked()) {
                            aux.add(name.get(0));
                        }
                        if (!f2.isChecked()) {
                            aux.add(name.get(1));
                        }
                        if (!f3.isChecked()) {
                            aux.add(name.get(2));
                        }
                        break;
                    case 4:
                        if(!f1.isChecked()) {
                            aux.add(name.get(0));
                        }
                        if (!f2.isChecked()) {
                            aux.add(name.get(1));
                        }
                        if (!f3.isChecked()) {
                            aux.add(name.get(2));
                        }
                        if (!f4.isChecked()) {
                            aux.add(name.get(3));
                        }
                        break;
                    case 5:
                        if(!f1.isChecked()) {
                            aux.add(name.get(0));
                        }
                        if (!f2.isChecked()) {
                            aux.add(name.get(1));
                        }
                        if (!f3.isChecked()) {
                            aux.add(name.get(2));
                        }
                        if (!f4.isChecked()) {
                            aux.add(name.get(3));
                        }
                        if (!f5.isChecked()) {
                            aux.add(name.get(4));
                        }
                        break;
                    case 6:
                        if(!f1.isChecked()) {
                            aux.add(name.get(0));
                        }
                        if (!f2.isChecked()) {
                            aux.add(name.get(1));
                        }
                        if (!f3.isChecked()) {
                            aux.add(name.get(2));
                        }
                        if (!f4.isChecked()) {
                            aux.add(name.get(3));
                        }
                        if (!f5.isChecked()) {
                            aux.add(name.get(4));
                        }
                        if (!f6.isChecked()) {
                            aux.add(name.get(5));
                        }
                        break;
                }
                name.clear();
                name.addAll(aux);
                loadImages(name);
            }else{
                showAlertDialogCheck();
            }
        }else{
            showAlertDialogImage();
        }
    }

    private void loadImages(ArrayList name){
        num=name.size();
        if(num!=0){
            contador.setText(num+" "+getString(R.string.fotografias));
        }else{
            contador.setText(getString(R.string.fotografia));
        }
        switch(num){
            case 0:
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria1);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria2);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria3);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria4);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria5);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria6);
                f1.setVisibility(View.GONE);
                f2.setVisibility(View.GONE);
                f3.setVisibility(View.GONE);
                f4.setVisibility(View.GONE);
                f5.setVisibility(View.GONE);
                f6.setVisibility(View.GONE);
                f1.setChecked(false);
                f2.setChecked(false);
                f3.setChecked(false);
                f4.setChecked(false);
                f5.setChecked(false);
                f6.setChecked(false);
                break;
            case 1:
                File file=new File(name.get(0).toString());
                Uri uri=Uri.fromFile(file);
                Picasso.with(getApplicationContext())
                        .load(uri)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria1);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria2);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria3);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria4);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria5);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria6);
                f2.setVisibility(View.GONE);
                f3.setVisibility(View.GONE);
                f4.setVisibility(View.GONE);
                f5.setVisibility(View.GONE);
                f6.setVisibility(View.GONE);
                f1.setChecked(false);
                f2.setChecked(false);
                f3.setChecked(false);
                f4.setChecked(false);
                f5.setChecked(false);
                f6.setChecked(false);
                break;
            case 2:
                File fileA=new File(name.get(0).toString());
                Uri uriA=Uri.fromFile(fileA);
                Picasso.with(getApplicationContext())
                        .load(uriA)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria1);
                File fileB=new File(name.get(1).toString());
                Uri uriB=Uri.fromFile(fileB);
                Picasso.with(getApplicationContext())
                        .load(uriB)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria2);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria3);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria4);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria5);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria6);
                f3.setVisibility(View.GONE);
                f4.setVisibility(View.GONE);
                f5.setVisibility(View.GONE);
                f6.setVisibility(View.GONE);
                f1.setChecked(false);
                f2.setChecked(false);
                f3.setChecked(false);
                f4.setChecked(false);
                f5.setChecked(false);
                f6.setChecked(false);
                break;
            case 3:
                File fileC=new File(name.get(0).toString());
                Uri uriC=Uri.fromFile(fileC);
                Picasso.with(getApplicationContext())
                        .load(uriC)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria1);
                File fileD=new File(name.get(1).toString());
                Uri uriD=Uri.fromFile(fileD);
                Picasso.with(getApplicationContext())
                        .load(uriD)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria2);
                File fileE=new File(name.get(2).toString());
                Uri uriE=Uri.fromFile(fileE);
                Picasso.with(getApplicationContext())
                        .load(uriE)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria3);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria4);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria5);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria6);
                f4.setVisibility(View.GONE);
                f5.setVisibility(View.GONE);
                f6.setVisibility(View.GONE);
                f1.setChecked(false);
                f2.setChecked(false);
                f3.setChecked(false);
                f4.setChecked(false);
                f5.setChecked(false);
                f6.setChecked(false);
                break;
            case 4:
                File fileF=new File(name.get(0).toString());
                Uri uriF=Uri.fromFile(fileF);
                Picasso.with(getApplicationContext())
                        .load(uriF)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria1);
                File fileG=new File(name.get(1).toString());
                Uri uriG=Uri.fromFile(fileG);
                Picasso.with(getApplicationContext())
                        .load(uriG)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria2);
                File fileH=new File(name.get(2).toString());
                Uri uriH=Uri.fromFile(fileH);
                Picasso.with(getApplicationContext())
                        .load(uriH)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria3);
                File fileI=new File(name.get(3).toString());
                Uri uriI=Uri.fromFile(fileI);
                Picasso.with(getApplicationContext())
                        .load(uriI)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria4);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria5);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria6);
                f5.setVisibility(View.GONE);
                f6.setVisibility(View.GONE);
                f1.setChecked(false);
                f2.setChecked(false);
                f3.setChecked(false);
                f4.setChecked(false);
                f5.setChecked(false);
                f6.setChecked(false);
                break;
            case 5:
                File fileJ=new File(name.get(0).toString());
                Uri uriJ=Uri.fromFile(fileJ);
                Picasso.with(getApplicationContext())
                        .load(uriJ)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria1);
                File fileK=new File(name.get(1).toString());
                Uri uriK=Uri.fromFile(fileK);
                Picasso.with(getApplicationContext())
                        .load(uriK)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria2);
                File fileL=new File(name.get(2).toString());
                Uri uriL=Uri.fromFile(fileL);
                Picasso.with(getApplicationContext())
                        .load(uriL)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria3);
                File fileM=new File(name.get(3).toString());
                Uri uriM=Uri.fromFile(fileM);
                Picasso.with(getApplicationContext())
                        .load(uriM)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria4);
                File fileN=new File(name.get(4).toString());
                Uri uriN=Uri.fromFile(fileN);
                Picasso.with(getApplicationContext())
                        .load(uriN)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria5);
                Picasso.with(getApplicationContext())
                        .load(R.drawable.blanco)
                        .transform(new RoundedTransformation(15, 0))
                        .fit()
                        .into(galeria6);
                f6.setVisibility(View.GONE);
                f1.setChecked(false);
                f2.setChecked(false);
                f3.setChecked(false);
                f4.setChecked(false);
                f5.setChecked(false);
                f6.setChecked(false);
                break;
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

    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent intent) {
        if (RequestCode==FOTOGRAFIA &&ResultCode == RESULT_OK){
            Uri takedImage = intent.getData();
            File f=new File(takedImage.getPath());
            name.add(getRealPathFromURI(context,takedImage));
           // name.add(file.getPath());
                switch (num){
                    case 0:
          //              grid.setVisibility(View.VISIBLE);
                        Picasso.with(getApplicationContext())
                                .load(takedImage)
                                .transform(new RoundedTransformation(15,0))
                                .fit()
                                .into(galeria1);
                        num=num+1;
                        f1.setVisibility(View.VISIBLE);
                        contador.setText(num+" "+getString(R.string.fotografia));
                        break;
                    case 1:
                        Picasso.with(getApplicationContext())
                                .load(takedImage)
                                .transform(new RoundedTransformation(15,0))
                                .fit()
                                .into(galeria2);
                        num=num+1;
                        f2.setVisibility(View.VISIBLE);
                        contador.setText(num+" "+getString(R.string.fotografias));
                        break;
                    case 2:
                        Picasso.with(getApplicationContext())
                                .load(takedImage)
                                .transform(new RoundedTransformation(15,0))
                                .fit()
                                .into(galeria3);
                        num=num+1;
                        f3.setVisibility(View.VISIBLE);
                        contador.setText(num+" "+getString(R.string.fotografias));
                        break;
                    case 3:
                        Picasso.with(getApplicationContext())
                                .load(takedImage)
                                .transform(new RoundedTransformation(15,0))
                                .fit()
                                .into(galeria4);
                        num=num+1;
                        contador.setText(num+" "+getString(R.string.fotografias));
                        f4.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        Picasso.with(getApplicationContext())
                                .load(takedImage)
                                .transform(new RoundedTransformation(15,0))
                                .fit()
                                .into(galeria5);
                        num=num+1;
                        f5.setVisibility(View.VISIBLE);
                        contador.setText(num+" "+getString(R.string.fotografias));
                        break;
                    case 5:
                        Picasso.with(getApplicationContext())
                                .load(takedImage)
                                .transform(new RoundedTransformation(15,0))
                                .fit()
                                .into(galeria6);
                        num=num+1;
                        f6.setVisibility(View.VISIBLE);
                        contador.setText(num+" "+getString(R.string.fotografias));
                        break;
                }
            } else if(RequestCode==GALERIA && ResultCode == RESULT_OK){
                Uri selectedImage = intent.getData();
                File f=new File(selectedImage.getPath());
                name.add(getRealPathFromURI(context,selectedImage));
            switch (num){
                        case 0:
            //                grid.setVisibility(View.VISIBLE);
                            Picasso.with(getApplicationContext())
                                    .load(selectedImage)
                                    .transform(new RoundedTransformation(15,0))
                                    .fit()
                                    .into(galeria1);
                            num=num+1;
                            f1.setVisibility(View.VISIBLE);
                            contador.setText(num+" "+getString(R.string.fotografia));
                            break;
                        case 1:
                            Picasso.with(getApplicationContext())
                                    .load(selectedImage)
                                    .transform(new RoundedTransformation(15,0))
                                    .fit()
                                    .into(galeria2);
                            num=num+1;
                            f2.setVisibility(View.VISIBLE);
                            contador.setText(num+" "+getString(R.string.fotografias));
                            break;
                        case 2:
                            Picasso.with(getApplicationContext())
                                    .load(selectedImage)
                                    .transform(new RoundedTransformation(15,0))
                                    .fit()
                                    .into(galeria3);
                            num=num+1;
                            f3.setVisibility(View.VISIBLE);
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
                            f4.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            Picasso.with(getApplicationContext())
                                    .load(selectedImage)
                                    .transform(new RoundedTransformation(15,0))
                                    .fit()
                                    .into(galeria5);
                            num=num+1;
                            f5.setVisibility(View.VISIBLE);
                            contador.setText(num+" "+getString(R.string.fotografias));
                            break;
                        case 5:
                            Picasso.with(getApplicationContext())
                                    .load(selectedImage)
                                    .transform(new RoundedTransformation(15,0))
                                    .fit()
                                    .into(galeria6);
                            num=num+1;
                            f6.setVisibility(View.VISIBLE);
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
        getSupportActionBar().setElevation(0);
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
                num=0;
                contador.setText(getString(R.string.fotografia));
                title.setText("");
                descr.setText("");
                costo.setText("");
                moneda.setText("Q.");
                idCurrency="1";
              //  grid.setVisibility(View.GONE);
                galeria1.setImageResource(R.drawable.blanco);
                galeria2.setImageResource(R.drawable.blanco);
                galeria3.setImageResource(R.drawable.blanco);
                galeria4.setImageResource(R.drawable.blanco);
                galeria5.setImageResource(R.drawable.blanco);
                galeria6.setImageResource(R.drawable.blanco);
                spinnerCat.setSelection(0);
                spinnerSub.setSelection(0);
                name.clear();
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
                subCategoriasLista = categoriasLista.get(position).getSub();
                poblarSpinnerSubCategorias(subCategoriasLista);
                break;
            case R.id.spinner_subcat:
                subcategoria = subCategoriasLista.get(position).getId();
                break;
            case R.id.spinner_location:
                if(position!=0){
                    idLocacion= listaLocaciones.get(position).getId();
                }
                int identificadorA = Integer.parseInt(listaLocaciones.get(position).getId());
                int identificadorB = Integer.parseInt(listaLocaciones.get(position+1).getId());
                new ObtenerMunicipalidades(context).execute(identificadorA, identificadorB);
                break;
            case R.id.spinner_municipio:
                if(position!=0){
                    idLocacion=listaMunicipio.get(position).getId();
                }
                zonasLista = listaMunicipio.get(position).getZonas();
                poblarSpinnerZonas(zonasLista);
                break;
            case R.id.spinner_zona:
                if(position!=0){
                    idLocacion=zonasLista.get(position).getId();
                }
                break;
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
            boolean resul = false;
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
                    map.put("location_id",idLocacion);
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
                } catch(Exception ex)
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
                //showShareDialog();
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

           boolean resul=false; String id=null, nombre=null, idSub=null, nameSub=null, slugSub=null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.categories);
            get.setHeader("content-type", "application/json");
                try {
                    HttpResponse resp = httpClient.execute(get);
                    JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                    JSONArray data = respJSON.getJSONArray("data");
                    Categoria c;
                    ArrayList<SubCategoria> subia = new ArrayList<>();
                    subia.add(new SubCategoria(null,"Subcategoria",null));
                    categoriasLista=new ArrayList<>();
                    categoriasLista.add(new Categoria(null,"Categoria",subia));
                    for(int i=0; i<data.length(); i++) {
                        JSONObject info = data.getJSONObject(i);
                        id = info.getString("id");
                        nombre = info.getString("name");
                        JSONArray sub = info.getJSONArray("childs");
                        SubCategoria sc;
                        ArrayList<SubCategoria> subArray=new ArrayList<>();
                        subArray.add(new SubCategoria(null,"Subcategoria",null,null));
                        for (int j=0; j<sub.length();j++){
                            JSONObject dato = sub.getJSONObject(j);
                            idSub = dato.getString("child_id");
                            nameSub=dato.getString("child_name");
                            slugSub=dato.getString("child_slug");
                            sc = new SubCategoria(idSub,nameSub,slugSub);
                            subArray.add(sc);
                        }
                        c = new Categoria(id, nombre,subArray);
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
               Log.e("Entro a result", "ENTRO");
           }else {
               Log.e("Entro a result", "ALGO PASO");
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

    private void poblarSpinnerSubCategorias(ArrayList<SubCategoria> aux) {
        List<String> campos = new ArrayList<String>();
        subCategoriasLista = aux;
        for (int i = 0; i < aux.size(); i++) {
            Log.i("Categoria",aux.get(i).toString());
            campos.add(aux.get(i).getNombre());
            Log.i("Campo",campos.get(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,R.layout.my_simple_spinner_item, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerSub.setAdapter(spinnerAdapter);
    }

    protected void showAlertDialogC() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(getString(R.string.select_cat));
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                getString(R.string.aceptar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    protected void showAlertDialogS() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(getString(R.string.select_sub));
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                getString(R.string.aceptar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    protected void showAlertDialogCheck() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(getString(R.string.sele_del));
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                getString(R.string.aceptar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    protected void showAlertDialogImage() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(getString(R.string.no_foto));
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                getString(R.string.aceptar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
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

    private class ObtenerMunicipalidades extends AppAsynchTask<Integer, Void, Boolean>{

        Activity actividad;
        public ObtenerMunicipalidades(Activity activity) {
            super(activity);
            actividad=activity;
        }

        @Override
        protected Boolean customDoInBackground(Integer... params)  throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException {

            boolean resul=false; String id=null, nombre=null, idZona=null, nameZona=null;
            Double longitud,latitud, longitudZona,latitudZona;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.categories_filtro+"realstates");
            get.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject data = respJSON.getJSONObject("data");
                JSONObject aux = data.getJSONObject("municipalities");
                if(params[0]>=2){
                    JSONObject idMun = aux.getJSONObject(""+params[0]);
                    JSONObject results = idMun.getJSONObject("municipalities");
                    Municipio c;
                    ArrayList<Zona> zonita = new ArrayList<>();
                    zonita.add(new Zona(null,"Zona",null,null));
                    listaMunicipio=new ArrayList<>();
                    listaMunicipio.add(new Municipio(null,"Municipio",null,null,zonita));
                    for(int i=params[0]+1; i<params[1]; i++) {
                        JSONObject info = results.getJSONObject(String.valueOf(i));
                        id = info.getString("id");
                        nombre = info.getString("name");
                        longitud=Double.parseDouble(info.getString("longitude"));
                        latitud=Double.parseDouble(info.getString("latitude"));
                        ArrayList<Zona> zonaMun = new ArrayList<>();
                        zonaMun.add(new Zona(null,"Zona",null, null));
                        String auxiliar= info.get("zones").toString();
                        if(!auxiliar.equals("[]")){
                            JSONObject zones = info.getJSONObject("zones");
                            Zona z;
                            Iterator<String> iter = zones.keys();
                            while (iter.hasNext()) {
                                String key = iter.next();
                                try {
                                    JSONObject dataZone = zones.getJSONObject(key);
                                    idZona=dataZone.getString("id");
                                    nameZona=dataZone.getString("name");
                                    longitudZona=Double.parseDouble(dataZone.getString("longitude"));
                                    latitudZona=Double.parseDouble(dataZone.getString("latitude"));
                                    z=new Zona(idZona,nameZona,longitudZona,latitudZona);
                                    zonaMun.add(z);
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                            }
                        }
                        c = new Municipio(id, nombre, longitud, latitud,zonaMun);
                        listaMunicipio.add(c);
                    }
                }else{
                    ArrayList<Zona> zonita = new ArrayList<>();
                    zonita.add(new Zona(null,"Zona",null,null));
                    listaMunicipio=new ArrayList<>();
                    listaMunicipio.add(new Municipio(null,"Municipio",null,null,zonita));
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
                poblarSpinnerMunicipios();
            }
        }
    }

    private void poblarSpinnerMunicipios() {
        List<String> campos = new ArrayList<String>();
        for (int i = 0; i < listaMunicipio.size(); i++) {
            Log.i("Categoria",listaMunicipio.get(i).toString());
            campos.add(listaMunicipio.get(i).getNombre());
            Log.i("Campo",campos.get(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,R.layout.my_simple_spinner_item, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerMun.setAdapter(spinnerAdapter);
    }

    private void poblarSpinnerZonas(ArrayList<Zona> aux) {
        List<String> campos = new ArrayList<String>();
        for (int i = 0; i < aux.size(); i++) {
            Log.i("Categoria",aux.get(i).toString());
            campos.add(aux.get(i).getNombre());
            Log.i("Campo",campos.get(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,R.layout.my_simple_spinner_item, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerZona.setAdapter(spinnerAdapter);
    }

    private class ObtenerLocaciones extends AppAsynchTask<String, Void, Boolean>{

        Activity actividad;
        public ObtenerLocaciones(Activity activity) {
            super(activity);
            actividad=activity;
        }

        @Override
        protected Boolean customDoInBackground(String... params)  throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException {

            boolean resul=false; String id=null, nombre=null;
            Double longitud,latitud;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.categories_filtro+"realstates");
            get.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject data = respJSON.getJSONObject("data");
                JSONArray results = data.getJSONArray("locations");
                Localidad c;
                listaLocaciones=new ArrayList<>();
                listaLocaciones.add(new Localidad("0","Departamento",null,null));
                for(int i=0; i<results.length(); i++) {
                    JSONObject info = results.getJSONObject(i);
                    id = info.getString("id");
                    nombre = info.getString("name");
                    longitud=Double.parseDouble(info.getString("longitude"));
                    latitud=Double.parseDouble(info.getString("latitude"));
                    c = new Localidad(id, nombre, longitud, latitud);
                    listaLocaciones.add(c);
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
                poblarSpinnerLocaciones();
            }
        }
    }

    private void poblarSpinnerLocaciones() {
        List<String> campos = new ArrayList<String>();
        for (int i = 0; i < listaLocaciones.size(); i++) {
            Log.i("Categoria",listaLocaciones.get(i).toString());
            campos.add(listaLocaciones.get(i).getNombre());
            Log.i("Campo",campos.get(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,R.layout.my_simple_spinner_item, campos);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinnerLoc.setAdapter(spinnerAdapter);
    }

}
