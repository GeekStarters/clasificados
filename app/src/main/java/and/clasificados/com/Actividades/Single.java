package and.clasificados.com.actividades;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import and.clasificados.com.R;
import and.clasificados.com.auxiliares.SlidingImageAdapter;

public class Single extends AppCompatActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.anuncio1,R.drawable.anuncio2,R.drawable.anuncio3,R.drawable.anuncio4};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    ImageView contactar, ofertar,  tw,fb,wha, msg, sha;
    TextView reportar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        agregarToolbar();
        init();
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
                switch (v.getId()){
                    case R.id.contactar:
                        Toast.makeText(getApplicationContext(),"Iremos a mensajes", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.ofertar:
                        Toast.makeText(getApplicationContext(),"Esto desplegara un alert para introducir oferta", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(),"Esto desplegara un intent a whatsapp", Toast.LENGTH_LONG).show();

                        break;
                    case R.id.sha:
                        Toast.makeText(getApplicationContext(),"Esto desplegara un intent para compartir por otro medio, bluetooth, etc", Toast.LENGTH_LONG).show();

                        break;
                    case R.id.msg:
                        Toast.makeText(getApplicationContext(),"Esto desplegara un alert para compartir por messenger fb", Toast.LENGTH_LONG).show();
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

    private void init() {
        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        mPager = (ViewPager) findViewById(R.id.pager_imagen);
        mPager.setAdapter(new SlidingImageAdapter(Single.this,ImagesArray));
        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
        NUM_PAGES =IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

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

}
