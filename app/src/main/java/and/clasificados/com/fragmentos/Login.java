package and.clasificados.com.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import and.clasificados.com.MainActivity;
import and.clasificados.com.R;
import and.clasificados.com.modelo.Usuario;
import and.clasificados.com.views.EditTextLight;

public class Login extends Fragment {

    Button registro;
    ImageView sesion, fb;
    TextView olvide;
    EditTextLight usuario, contra;


    public Login() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);
        registro = (Button) view.findViewById(R.id.button_registro);
        sesion = (ImageView) view.findViewById(R.id.button_session);
        fb = (ImageView) view.findViewById(R.id.button_fb);
        olvide = (TextView) view.findViewById(R.id.olvide);
        usuario = (EditTextLight) view.findViewById(R.id.user);
        contra = (EditTextLight) view.findViewById(R.id.pass);
        contra.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL) {
                    login();
                    return true;
                }
                return false;
            }
        });

        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_fb:
                        Toast.makeText(getActivity(), "Iremos a Facebook", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.button_registro:
                        registro();
                        break;
                    case R.id.button_session:
                        login();
                        break;
                    case R.id.olvide:
                        Toast.makeText(getActivity(), "Iremos a cambiar contraseña", Toast.LENGTH_LONG).show();
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

    public void registro() {
        Fragment fragmento = new Registro();
        FragmentManager fragmentM = getFragmentManager();
        fragmentM.beginTransaction().replace(R.id.main_content, fragmento).commit();
    }

    public void login() {
        usuario.setError(null);
        contra.setError(null);
        String idUser = usuario.getText().toString();
        String password = contra.getText().toString();

        boolean cancel = false;
        View focusView = null;
        try {
            //  Usuario usuario = new Usuario();
            if (!password.equals(idUser)) {
                contra.setError(getString(R.string.error_incorrect_password));
                focusView = contra;
                cancel = true;
            }
            if (cancel) {
                focusView.requestFocus();
            } else {
                Fragment fragmento = new Inicio();
                FragmentManager fragmentM = getFragmentManager();
                fragmentM.beginTransaction().replace(R.id.main_content, fragmento).commit();
            }
        } catch (Exception e) {
            usuario.setError(getString(R.string.error_invalid_email));
            focusView = usuario;
        }
    }
}