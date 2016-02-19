package and.clasificados.com.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import and.clasificados.com.R;
import and.clasificados.com.actividades.Login;
import and.clasificados.com.auxiliares.AdaptadorCategorias;
import and.clasificados.com.auxiliares.DecoracionLineaDivisoria;
import and.clasificados.com.modelo.Clasificado;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class NoLogin extends Fragment {

    public NoLogin() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_nologin, container, false);
        return view;
    }
}