package and.clasificados.com.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import and.clasificados.com.R;

public class Login extends Fragment {

    Button registro;
    ImageView sesion,fb;
    TextView olvide;

    public Login(){

    }
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.activity_login, container, false);
         registro = (Button)view.findViewById(R.id.button_registro);
         sesion=(ImageView)view.findViewById(R.id.button_session);
         fb=(ImageView)view.findViewById(R.id.button_fb);
         olvide=(TextView)view.findViewById(R.id.olvide);
         View.OnClickListener onclick= new View.OnClickListener()
         {
             @Override
             public void onClick(View v)
             {
                 switch (v.getId()){
                     case R.id.button_fb:
                         Toast.makeText(getActivity(),"Iremos a Facebook", Toast.LENGTH_LONG).show();
                         break;
                     case R.id.button_registro:
                         registro();
                         break;
                     case R.id.button_session:
                         Toast.makeText(getActivity(),"Cambiaremos el nav y bla bla", Toast.LENGTH_LONG).show();
                         break;
                     case R.id.olvide:
                         Toast.makeText(getActivity(),"Iremos a cambiar contraseña", Toast.LENGTH_LONG).show();
                         break;
                 }
             }
         };
         registro.setOnClickListener(onclick);
         fb.setOnClickListener(onclick);
         sesion.setOnClickListener(onclick);
         olvide.setOnClickListener(onclick);
        return view;
    }

    public void registro(){
        Fragment fragmento = new Registro();
        FragmentManager fragmentM = getFragmentManager();
        if (fragmento != null) {
            fragmentM
                    .beginTransaction()
                    .replace(R.id.main_content, fragmento)
                    .commit();
        }
    }

}