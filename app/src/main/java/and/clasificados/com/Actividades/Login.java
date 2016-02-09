package and.clasificados.com.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import and.clasificados.com.MainActivity;
import and.clasificados.com.R;

public class Login extends AppCompatActivity {

    ImageView login;
    TextView  registro;
    EditText user, password;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=(ImageView) findViewById(R.id.button_session);
        registro=(TextView)findViewById(R.id.registro);
        user=(EditText) findViewById(R.id.user);
        password=(EditText)findViewById(R.id.contra);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                if(String.valueOf(user.getText()).isEmpty() || String.valueOf(password.getText()).isEmpty()){
                    i.putExtra("SESION", false);
                }else {
                    i.putExtra("SESION", true);
                }
                startActivity(i);
            }
        });
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Registro.class);
                startActivity(i);
            }
        });

        agregarToolbar();
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

}