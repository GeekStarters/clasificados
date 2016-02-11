package and.clasificados.com.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import and.clasificados.com.R;


public class  Registro extends Fragment {

    ImageView fb,registro;
    TextView terminos, condiciones;
    public Registro(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_registro, container, false);
        registro = (ImageView)view.findViewById(R.id.registro);
        fb=(ImageView)view.findViewById(R.id.button_fb2);
        terminos=(TextView)view.findViewById(R.id.terminos);
        condiciones=(TextView)view.findViewById(R.id.condiciones);
        View.OnClickListener onclick= new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId()){
                    case R.id.button_fb2:
                        Toast.makeText(getActivity(),"Iremos a Facebook", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.registro:
                        registro();
                        break;
                    case R.id.terminos:
                        Toast.makeText(getActivity(),"Saldra un alert con los terminos", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.condiciones:
                        Toast.makeText(getActivity(),"Saldra un alert con las condiciones", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        terminos.setOnClickListener(onclick);
        condiciones.setOnClickListener(onclick);
        registro.setOnClickListener(onclick);
        fb.setOnClickListener(onclick);
        return view;
    }

    public void registro(){
        Fragment fragmento = new Inicio();
        FragmentManager fragmentM = getFragmentManager();
        if (fragmento != null) {
            fragmentM
                    .beginTransaction()
                    .replace(R.id.main_content, fragmento)
                    .commit();
        }
    }

}
