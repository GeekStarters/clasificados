package and.clasificados.com;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;


public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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