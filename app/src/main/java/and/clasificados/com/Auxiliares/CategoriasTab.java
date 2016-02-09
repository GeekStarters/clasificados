package and.clasificados.com.auxiliares;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import and.clasificados.com.actividades.Single;
import and.clasificados.com.modelo.Clasificado;
import and.clasificados.com.R;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class CategoriasTab extends Fragment {

    private static final String INDICE_SECCION
            = "and.clasificados.com.FragmentoCategoriasTab.extra.INDICE_SECCION";

    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorCategorias adaptador;
    private Context context;

    public CategoriasTab() {
    }

    public static CategoriasTab nuevaInstancia(int indiceSeccion) {
        CategoriasTab fragment = new CategoriasTab();
        Bundle args = new Bundle();
        args.putInt(INDICE_SECCION, indiceSeccion);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_grupo_items, container, false);
        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);

        int indiceSeccion = getArguments().getInt(INDICE_SECCION);

        switch (indiceSeccion) {
            case 0:
                adaptador = new AdaptadorCategorias(Clasificado.VEHICULOS);
                break;
            case 1:
                adaptador = new AdaptadorCategorias(Clasificado.INMUEBLES);
                break;
            case 2:
                adaptador = new AdaptadorCategorias(Clasificado.PRODUCTOS);
                break;
            case 3:
                adaptador = new AdaptadorCategorias(Clasificado.EMPLEOS);
                break;
            case 4:
                adaptador = new AdaptadorCategorias(Clasificado.SERVICIOS);
                break;
        }

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Single.class));
            }
        });

        reciclador.setAdapter(adaptador);
        reciclador.addItemDecoration(new and.clasificados.com.auxiliares.DecoracionLineaDivisoria(getActivity()));
        return view;
    }
}