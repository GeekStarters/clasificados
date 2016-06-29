package and.clasificados.com;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import and.clasificados.com.auxiliares.PrefUtils;
import and.clasificados.com.layer.atlas.MyAuthenticationProvider;
import and.clasificados.com.layer.util.AuthenticationProvider;
import io.fabric.sdk.android.Fabric;


public class Splash extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "LyvL1cg4UCAgHEDdevAOoi2Xv";
    private static final String TWITTER_SECRET = "tZrGmpHLx4pU0pwaj1w6mViUOdMyBo7A0Ak7fyD3FqJ79z0wUN";
/*
    public static final String LAYER_APP_ID = "layer:///apps/staging/613d665c-d4c9-11e5-bfa3-44c9010027fe";
    // 2. Optionally replace the Google Cloud Messaging Sender ID (in the Developer Console too)
    private static final String GCM_SENDER_ID = "000000000"; // Set your GCM Sender ID*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
        setContentView(R.layout.splash);
        new MiTareaAsincrona().execute();
    }

    private void tareaLarga() {
        try {
            Thread.sleep(200);
        } catch(InterruptedException e) {}
    }

    private class MiTareaAsincrona extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            for(int i=1; i<=10; i++) {
                tareaLarga();

                publishProgress(i*10);

                if(isCancelled())
                    break;
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Boolean result) {

            Intent menu = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(menu);
            finish();

        }
    }


}