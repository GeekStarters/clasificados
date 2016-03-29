package and.clasificados.com.actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.layer.sdk.LayerClient;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import and.clasificados.com.Constants;
import and.clasificados.com.R;
import and.clasificados.com.auxiliares.AdaptadorMensaje;
import and.clasificados.com.auxiliares.PrefUtils;
import and.clasificados.com.exception.NetworkException;
import and.clasificados.com.exception.ParsingException;
import and.clasificados.com.exception.ServerException;
import and.clasificados.com.exception.TimeOutException;
import and.clasificados.com.layer.ConversationViewController;
import and.clasificados.com.layer.MyAuthListener;
import and.clasificados.com.layer.MyConnectionListener;
import and.clasificados.com.modelo.Mensaje;
import and.clasificados.com.modelo.Usuario;
import and.clasificados.com.services.AppAsynchTask;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class Mensajes extends AppCompatActivity {


    //Replace this with your App ID from the Layer Developer page.
    //Go http://developer.layer.com, click on "Dashboard" and select "Keys"
    public static final String LAYER_APP_ID = "layer:///apps/staging/613d665c-d4c9-11e5-bfa3-44c9010027fe";

    //Optional: Enable Push Notifications
    // Layer uses Google Cloud Messaging for Push Notifications. Go to
    // https://developer.layer.com/docs/guides/android#push-notification
    // and follow the guide to configure a Google Project. If the default or
    // an invalid Project Number is used here, the Layer SDK will function, but
    // users will not receive Notifications when the app is closed or in the
    // background).
    public static final String GCM_PROJECT_NUMBER = "00000";


    //Global variables used to manage the Layer Client and the conversations in this app
    private LayerClient layerClient;
    private ConversationViewController conversationView;

    //Layer connection and authentication callback listeners
    private MyConnectionListener connectionListener;
    private MyAuthListener authenticationListener;

    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorMensaje adaptador;
    private List<Mensaje> resultado;
    String auto;
    Activity context;
    static Usuario login_user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);
        agregarToolbar();
        context = this;
        login_user = PrefUtils.getCurrentUser(Mensajes.this);
        auto = login_user.auto;
        reciclador = (RecyclerView) findViewById(R.id.reciclador_mensajes);
        layoutManager = new LinearLayoutManager(this);
        reciclador.setLayoutManager(layoutManager);
        LlenarLista llenar = new LlenarLista(context);
        llenar.execute(auto);
        //If we haven't created a LayerClient, show the loading splash screen
        /*if(layerClient == null)
            setContentView(R.layout.activity_loading);
*/

        //Create the callback listeners

        if(connectionListener == null)
            connectionListener = new MyConnectionListener(this);

        if(authenticationListener == null)
            authenticationListener = new MyAuthListener(this);
    }

    //onResume is called on App Start and when the app is brought to the foreground
    protected void onResume(){
        super.onResume();

        //Connect to Layer and Authenticate a user
        loadLayerClient();

        //Every time the app is brought to the foreground, register the typing indicator
        //if(layerClient != null && conversationView != null)
           // layerClient.registerTypingIndicator(conversationView);
    }

    //onPause is called when the app is sent to the background
    protected void onPause(){
        super.onPause();

        //When the app is moved to the background, unregister the typing indicator
       // if(layerClient != null && conversationView != null)
           // layerClient.unregisterTypingIndicator(conversationView);
    }

    //Checks to see if the SDK is connected to Layer and whether a user is authenticated
    //The respective callbacks are executed in MyConnectionListener and MyAuthListener
    private void loadLayerClient(){

        // Check if Sample App is using a valid app ID.
        if (isValidAppID()) {

            if(layerClient == null){

                //Used for debugging purposes ONLY. DO NOT include this option in Production Builds.
                //LayerClient.setLoggingEnabled(this.getApplicationContext(),true);

                // Initializes a LayerClient object with the Google Project Number
                LayerClient.Options options = new LayerClient.Options();

                //Sets the GCM sender id allowing for push notifications
                options.googleCloudMessagingSenderId(GCM_PROJECT_NUMBER);

                //By default, only unread messages are synced after a user is authenticated, but you
                // can change that behavior to all messages or just the last message in a conversation
                options.historicSyncPolicy(LayerClient.Options.HistoricSyncPolicy.ALL_MESSAGES);


                layerClient = LayerClient.newInstance(this, LAYER_APP_ID, options);

                //Register the connection and authentication listeners
                layerClient.registerConnectionListener(connectionListener);
                layerClient.registerAuthenticationListener(authenticationListener);
            }

            //Check the current state of the SDK. The client must be CONNECTED and the user must
            // be AUTHENTICATED in order to send and receive messages. Note: it is possible to be
            // authenticated, but not connected, and vice versa, so it is a best practice to check
            // both states when your app launches or comes to the foreground.
            if (!layerClient.isConnected()) {

                //If Layer is not connected, make sure we connect in order to send/receive messages.
                // MyConnectionListener.java handles the callbacks associated with Connection, and
                // will start the Authentication process once the connection is established
                layerClient.connect();

            } else if (!layerClient.isAuthenticated()) {

                //If the client is already connected, try to authenticate a user on this device.
                // MyAuthListener.java handles the callbacks associated with Authentication
                // and will start the Conversation View once the user is authenticated
                layerClient.authenticate();

            } else {

                // If the client is to Layer and the user is authenticated, start the Conversation
                // View. This will be called when the app moves from the background to the foreground,
                // for example.
                onUserAuthenticated();
            }
        }
    }

    //If you haven't replaced "LAYER_APP_ID" with your App ID, send a message
    private boolean isValidAppID() {
        if(LAYER_APP_ID.equalsIgnoreCase("LAYER_APP_ID")) {

            // Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Chain together various setter methods to set the dialog characteristics
            builder.setMessage("To correctly use this project you need to replace LAYER_APP_ID in MiMensaje.java (line 39) with your App ID from developer.layer.com.")
                    .setTitle(":-(");

            // Get the AlertDialog from create() and then show() it
            AlertDialog dialog = builder.create();
            dialog.show();

            return false;
        }

        return true;
    }

    //Layer is fairly flexible when it comes to User Management. You can use an existing system, or
    // create a new one, as long as all user ids are unique. For demonstration purposes, we are
    // making the assumption that this App will be run simultaneously on a Simulator and on a
    // Device, and assign the User ID based on the runtime environment.
    public static String getUserID(){
        return login_user.id;
    }

    //By default, create a conversationView between these 3 participants
    public static List<String> getAllParticipants(){
        return Arrays.asList("1273");
    }

    //Once the user has successfully authenticated, begin the conversationView
    public void onUserAuthenticated(){

        if(conversationView == null) {

            conversationView = new ConversationViewController(this, layerClient);

           /* if (layerClient != null) {
                layerClient.registerTypingIndicator(conversationView);
            }*/
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mensaje, menu);
        return true;
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
    }


    private class LlenarLista extends AppAsynchTask<String, Integer, Boolean> {
        Mensaje c;
        String precio = null, titulo = null, url_imagen = null, categoria = null;
        Activity actividad;

        public LlenarLista(Activity activity) {
            super(activity);
            actividad = activity;
        }

        protected Boolean customDoInBackground(String... params) throws NetworkException, ServerException, ParsingException,
                TimeOutException, IOException, JSONException {
            boolean resul;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.mensajes);
            get.setHeader("content-type", "application/json");
            get.setHeader("authorization", "Basic" + " " + params[0]);
            Log.i("direccion", get.toString());
            try {

               /* HttpResponse resp = httpClient.execute(get);
                JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
                JSONObject data  = respJSON.getJSONObject("data");
                JSONArray results = data.getJSONArray("results");
                for(int i=0; i<results.length(); i++)
                {
                    JSONObject ad = results.getJSONObject(i);
                    System.out.println(ad.toString());
                    JSONObject info = ad.getJSONObject("info");
                    titulo=info.getString("title");
                    precio = info.getString("currencySymbol")+" "+info.getString("price");
                    categoria = info.getString("subCategoryName");
                    JSONArray imagen = info.getJSONArray("images");
                    url_imagen = imagen.getString(0);
                    c= new Clasificado(precio,categoria,titulo,url_imagen);
                    resultado.add(c);
                }*/
                resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;
        }

        protected void customOnPostExecute(Boolean result) {
            if (result) {
                adaptador = new AdaptadorMensaje(Mensaje.MENSAJES);
                adaptador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                /* //       Toast.makeText(v.getContext(), "Click en mensaje", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(v.getContext(), MiMensaje.class);
                v.getContext().startActivity(i);*/
                    }
                });
                reciclador.setAdapter(adaptador);
                reciclador.addItemDecoration(new and.clasificados.com.auxiliares.DecoracionLineaDivisoria(getApplicationContext()));
            }
        }
    }

}