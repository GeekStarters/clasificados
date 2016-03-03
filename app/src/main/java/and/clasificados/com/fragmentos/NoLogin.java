package and.clasificados.com.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import and.clasificados.com.R;
import and.clasificados.com.actividades.Login;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class NoLogin extends Fragment {

    Button login;
    public NoLogin() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_nologin, container, false);
        login=(Button) view.findViewById(R.id.button4);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Login.class));
            }
        });
        return view;
    }
}