package and.clasificados.com.actividades;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;


import and.clasificados.com.auxiliares.AdaptadorMensaje;
import and.clasificados.com.auxiliares.DecoracionLineaDivisoria;
import and.clasificados.com.modelo.Mensaje;
import and.clasificados.com.R;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class Mensajes extends AppCompatActivity {


    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorMensaje adaptador;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmento_grupo_items);

        reciclador = (RecyclerView) findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(this);
        reciclador.setLayoutManager(layoutManager);
        adaptador = new AdaptadorMensaje(Mensaje.MENSAJES);
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Click en mensaje", Toast.LENGTH_SHORT).show();
                /*Intent i = new Intent(v.getContext(), Single.class);
                v.getContext().startActivity(i);*/
            }
        });

        reciclador.setAdapter(adaptador);
        reciclador.addItemDecoration(new DecoracionLineaDivisoria(this));

    }

}