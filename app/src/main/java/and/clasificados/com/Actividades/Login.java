package and.clasificados.com.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blunderer.materialdesignlibrary.handlers.ActionBarDefaultHandler;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;

import and.clasificados.com.MainActivity;
import and.clasificados.com.R;

public class Login extends com.blunderer.materialdesignlibrary.activities.Activity {

    ImageView login;
    TextView  registro;
    EditText user, password;
    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContentView();
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
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return new ActionBarDefaultHandler(this);
    }

    @Override
    protected boolean enableActionBarShadow() {
        return false;
    }
}