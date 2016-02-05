package and.clasificados.com.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import and.clasificados.com.auxiliares.AdaptadorCategorias;
import and.clasificados.com.auxiliares.DecoracionLineaDivisoria;
import and.clasificados.com.modelo.Clasificado;
import and.clasificados.com.R;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class MisPublicaciones extends Fragment {

    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorCategorias adaptador;

    public MisPublicaciones() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_grupo_items, container, false);

        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);
        adaptador = new AdaptadorCategorias(Clasificado.FAVORITOS);
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Click en mis publicaciones", Toast.LENGTH_SHORT).show();
                /*Intent i = new Intent(v.getContext(), Single.class);
                v.getContext().startActivity(i);*/
            }
        });

        reciclador.setAdapter(adaptador);
        reciclador.addItemDecoration(new DecoracionLineaDivisoria(getActivity()));



        return view;
    }
}