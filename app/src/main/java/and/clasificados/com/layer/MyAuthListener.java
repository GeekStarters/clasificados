package and.clasificados.com.layer;

import android.os.AsyncTask;

import com.layer.sdk.LayerClient;
import com.layer.sdk.exceptions.LayerException;
import com.layer.sdk.listeners.LayerAuthenticationListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Gabriela Mejia on 21/3/2016.
 */
public class MyAuthListener implements LayerAuthenticationListener {

    @Override
    public void onAuthenticated(LayerClient client, String arg1) {
        System.out.println("Authentication successful");
    }

    /*
  * 1. Implement `onAuthenticationChallenge` in your Authentication Listener to acquire a nonce
  */
    @Override
    public void onAuthenticationChallenge(final LayerClient layerClient, final String nonce) {

        //You can use any identifier you wish to track users, as long as the value is unique
        //This identifier will be used to add a user to a conversation in order to send them messages
        final String mUserId = "USER_ID_HERE";

  /*
   * 2. Acquire an identity token from the Layer Identity Service
   */
        (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    HttpPost post = new HttpPost("https://layer-identity-provider.herokuapp.com/identity_tokens");
                    post.setHeader("Content-Type", "application/json");
                    post.setHeader("Accept", "application/json");

                    JSONObject json = new JSONObject()
                            .put("app_id", layerClient.getAppId())
                            .put("user_id", mUserId)
                            .put("nonce", nonce );
                    post.setEntity(new StringEntity(json.toString()));

                    HttpResponse response = (new DefaultHttpClient()).execute(post);
                    String eit = (new JSONObject(EntityUtils.toString(response.getEntity())))
                            .optString("identity_token");

            /*
             * 3. Submit identity token to Layer for validation
             */
                    layerClient.answerAuthenticationChallenge(eit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).execute();
    }

    @Override
    public void onAuthenticationError(LayerClient layerClient, LayerException e) {
        // TODO Auto-generated method stub
        System.out.println("There was an error authenticating");
    }

    // Called after the user has been deauthenticated
    public void onDeauthenticated(LayerClient client) {
        // Handle the case where the user deauthenticated (return to your App's login
        // screen, for example)
        System.out.println("User is deauthenticated");
    }
}

