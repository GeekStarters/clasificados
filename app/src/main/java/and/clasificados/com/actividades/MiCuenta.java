package and.clasificados.com.actividades;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

import and.clasificados.com.Constants;
import and.clasificados.com.R;
import and.clasificados.com.auxiliares.PrefUtils;
import and.clasificados.com.common.CircleTransformation;
import and.clasificados.com.exception.NetworkException;
import and.clasificados.com.exception.ParsingException;
import and.clasificados.com.exception.ServerException;
import and.clasificados.com.exception.TimeOutException;
import and.clasificados.com.fragmentos.Cuenta;
import and.clasificados.com.modelo.Usuario;
import and.clasificados.com.services.AppAsynchTask;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class MiCuenta extends AppCompatActivity {
    Usuario login_user;
    ImageView picture;
    TextView user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmento_favoritos);
        agregarToolbar();
        picture = (ImageView) findViewById(R.id.profile);
        user = (TextView) findViewById(R.id.username);
        login_user = PrefUtils.getCurrentUser(MiCuenta.this);
        if (login_user != null) {
            if (login_user.provider.equals("local")) {
                Picasso.with(getApplicationContext())
                        .load(R.drawable.profile3)              //aqui debe ir la url
                        .transform(new CircleTransformation())
                        .into(picture);
            } else {
                System.out.println("https://graph.facebook.com/" + login_user.facebookID + "/picture?type=large");
                Picasso.with(getApplicationContext())
                        .load("https://graph.facebook.com/" + login_user.facebookID + "/picture?type=large")
                        .transform(new CircleTransformation())
                        .into(picture);
            }
            picture.setVisibility(View.VISIBLE);
            user.setVisibility(View.VISIBLE);
            user.setText(login_user.name + " " + login_user.last);
        }
        Fragment fragmentoGenerico = new Cuenta();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fav_content, fragmentoGenerico)
                .commit();
        setTitle("");
    }


    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_custom);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        setTitle("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favoritos, menu);
        return true;
    }


    public class DataCategory extends AppAsynchTask<Void, String, String> {
        Activity actividad;
        String respuestaWS = null;


        public DataCategory(Activity activity) {
            super(activity);
            // TODO Auto-generated constructor stub
            actividad = activity;
        }

        @Override
        protected String customDoInBackground(Void... params)
                throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException {

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(Constants.categories);


            try {

                // Execute HTTP Post Request
                //httppost.setHeader("Authorization",Constants.md5(Constants.KEY_HEADER));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                InputStream instream = entity.getContent();
                String result = Constants.convertStreamToString(instream);
                System.out.println("result " + result);
                /*JSONObject myObject = new JSONObject(result);
                JSONObject myObjectItems = new JSONObject(myObject.getString("response"));
                JSONArray myObjectitems  = new JSONArray(myObjectItems.getString("items"));
                total_resultado=myObjectItems.getInt("items_total");
                for(int i = 0; i < myObjectitems.length(); i++){
                    JSONObject c = myObjectitems.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("section", c.getString("section"));
                    map.put("type", c.getString("type"));
                    map.put("object_id", c.getString("object_id"));
                    map.put("title", c.getString("title"));
                    map.put("image", c.getString("image"));
                    map.put("category_name", c.getString("category_name"));
                    map.put("entry_creation_date", c.getString("entry_creation_date"));
                    map.put("entry_start_date", c.getString("entry_start_date"));

                }*/

                respuestaWS = "si";


            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }/*catch (JSONException e) {
                e.printStackTrace();
            }*/


            return respuestaWS;

        }

        @Override
        protected void customOnPostExecute(String result) {


        }


    }

}