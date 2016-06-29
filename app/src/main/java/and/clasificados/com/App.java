package and.clasificados.com;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.layer.atlas.messagetypes.text.TextCellFactory;
import com.layer.atlas.messagetypes.threepartimage.ThreePartImageUtils;
import com.layer.atlas.provider.ParticipantProvider;
import com.layer.atlas.util.Util;
import com.layer.atlas.util.picasso.requesthandlers.MessagePartRequestHandler;

import and.clasificados.com.layer.atlas.MyAuthenticationProvider;
import and.clasificados.com.layer.atlas.MyParticipantProvider;
import and.clasificados.com.layer.util.AuthenticationProvider;
import and.clasificados.com.layer.util.Log;

import com.layer.sdk.LayerClient;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

/**
 * App provides static access to a LayerClient and other Atlas and Messenger context, including
 * AuthenticationProvider, ParticipantProvider, MyParticipant, and Picasso.
 *
 * App.Flavor allows build variants to target different environments, such as the Atlas Demo and the
 * open source Rails Identity Provider.  Switch flavors with the Android Studio `Build Variant` tab.
 * When using a flavor besides the Atlas Demo you must manually set your Layer App ID and GCM Sender
 * ID in that flavor's Flavor.java.
 *
 * @see LayerClient
 * @see ParticipantProvider
 * @see Picasso
 * @see AuthenticationProvider
 */
public class App extends Application {

 /* private static final String LAYER_APP_ID = "layer:///apps/staging/613d665c-d4c9-11e5-bfa3-44c9010027fe";
    private static final String GCM_SENDER_ID = "653137112103";*/
    private final static String LAYER_APP_ID = "layer:///apps/staging/0ac8fec8-f05b-11e5-b670-8407e8084abc";
    private final static String GCM_SENDER_ID = "748607264448";
    private static Application sInstance;
    private static LayerClient sLayerClient;
    private static ParticipantProvider sParticipantProvider;
    private static AuthenticationProvider sAuthProvider;
    private static Picasso sPicasso;


    //==============================================================================================
    // Application Overrides
    //==============================================================================================

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable verbose logging in debug builds
        com.layer.atlas.util.Log.setLoggingEnabled(true);
        AuthenticationProvider.Log.setAlwaysLoggable(true);
        LayerClient.setLoggingEnabled(this, true);
        LayerClient.applicationCreated(this);

        sInstance = this;
    }

    public static Application getInstance() {
        return sInstance;
    }


    //==============================================================================================
    // Identity Provider Methods
    //==============================================================================================

    /**
     * Routes the user to the proper Activity depending on their authenticated state.  Returns
     * `true` if the user has been routed to another Activity, or `false` otherwise.
     *
     * @param from Activity to route from.
     * @return `true` if the user has been routed to another Activity, or `false` otherwise.
     */
    public static boolean routeLogin(Activity from) {
        return getAuthenticationProvider().routeLogin(getLayerClient(), LAYER_APP_ID, from);
    }

    /**
     * Authenticates with the AuthenticationProvider and Layer, returning asynchronous results to
     * the provided callback.
     *
     * @param credentials Credentials associated with the current AuthenticationProvider.
     * @param callback    Callback to receive authentication results.
     */
    @SuppressWarnings("unchecked")
    public static void authenticate(Object credentials, AuthenticationProvider.Callback callback) {
        LayerClient client = getLayerClient();
        if (client == null) return;
        String layerAppId = LAYER_APP_ID;
        if (layerAppId == null) return;
        getAuthenticationProvider()
                .setCredentials(credentials)
                .setCallback(callback);
        client.authenticate();
    }

    /**
     * Deauthenticates with Layer and clears cached AuthenticationProvider credentials.
     *
     * @param callback Callback to receive deauthentication success and failure.
     */
    public static void deauthenticate(final Util.DeauthenticationCallback callback) {
        Util.deauthenticate(getLayerClient(), new Util.DeauthenticationCallback() {
            @Override
            @SuppressWarnings("unchecked")
            public void onDeauthenticationSuccess(LayerClient client) {
                getAuthenticationProvider().setCredentials(null);
                callback.onDeauthenticationSuccess(client);
            }

            @Override
            public void onDeauthenticationFailed(LayerClient client, String reason) {
                callback.onDeauthenticationFailed(client, reason);
            }
        });
    }


    //==============================================================================================
    // Getters / Setters
    //==============================================================================================

    /**
     * Gets or creates a LayerClient, using a default set of LayerClient.Options and flavor-specific
     * App ID and Options from the `generateLayerClient` method.  Returns `null` if the flavor was
     * unable to create a LayerClient (due to no App ID, etc.).
     *
     * @return New or existing LayerClient, or `null` if a LayerClient could not be constructed.

     */
    public static LayerClient getLayerClient() {
        if (sLayerClient == null) {
            LayerClient.Options options = new LayerClient.Options()
                    .historicSyncPolicy(LayerClient.Options.HistoricSyncPolicy.FROM_LAST_MESSAGE)
                    .autoDownloadMimeTypes(Arrays.asList(
                            TextCellFactory.MIME_TYPE,
                            ThreePartImageUtils.MIME_TYPE_INFO,
                            ThreePartImageUtils.MIME_TYPE_PREVIEW));

            sLayerClient = generateLayerClient(sInstance, options);
            if (sLayerClient == null) return null;
            sLayerClient.registerAuthenticationListener(getAuthenticationProvider());
        }
        return sLayerClient;
    }

    public static LayerClient generateLayerClient(Context context, LayerClient.Options options) {
        // If no App ID is set yet, return `null`; we'll launch the AppIdScanner to get one.
        String appId = LAYER_APP_ID;
        if (appId == null) return null;

        options.googleCloudMessagingSenderId(GCM_SENDER_ID);
        return LayerClient.newInstance(context, appId, options);
    }



    public static ParticipantProvider getParticipantProvider() {
        if (sParticipantProvider == null) {
            sParticipantProvider = generateParticipantProvider(sInstance, getAuthenticationProvider());
        }
        return sParticipantProvider;
    }

    public static AuthenticationProvider getAuthenticationProvider() {
        if (sAuthProvider == null) {
            sAuthProvider = generateAuthenticationProvider(sInstance);

            // If we have cached credentials, try authenticating with Layer
            LayerClient layerClient = getLayerClient();
            if (layerClient != null && sAuthProvider.hasCredentials()) layerClient.authenticate();
        }
        return sAuthProvider;
    }

    public static ParticipantProvider generateParticipantProvider(Context context, AuthenticationProvider authenticationProvider) {
        return new MyParticipantProvider(context).setLayerAppId(LAYER_APP_ID);
    }

    public static AuthenticationProvider generateAuthenticationProvider(Context context) {
        return new MyAuthenticationProvider(context);
    }

    public static Picasso getPicasso() {
        if (sPicasso == null) {
            // Picasso with custom RequestHandler for loading from Layer MessageParts.
            sPicasso = new Picasso.Builder(sInstance)
                    .addRequestHandler(new MessagePartRequestHandler(getLayerClient()))
                    .build();
        }
        return sPicasso;
    }

}
