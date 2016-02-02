package and.clasificados.com.Modelo;

import java.util.ArrayList;
import java.util.List;

import and.clasificados.com.R;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class Mensaje {
    private int imagen;
    private String nombre;
    private String mensaje;

    public Mensaje(int imagen, String nombre, String mensaje) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.mensaje = mensaje;
    }

    public static final List<Mensaje> MENSAJES= new ArrayList<Mensaje>();

    static {
        MENSAJES.add(new Mensaje(R.drawable.profile2, "Lorem Ipsum","Lorem ipsum sit dolor lorem ipsum"));
        MENSAJES.add(new Mensaje(R.drawable.profile2, "Lorem Ipsum","Lorem ipsum sit dolor lorem ipsum"));
        MENSAJES.add(new Mensaje(R.drawable.profile2, "Lorem Ipsum","Lorem ipsum sit dolor lorem ipsum"));
        MENSAJES.add(new Mensaje(R.drawable.profile2, "Lorem Ipsum","Lorem ipsum sit dolor lorem ipsum"));
        MENSAJES.add(new Mensaje(R.drawable.profile2, "Lorem Ipsum","Lorem ipsum sit dolor lorem ipsum"));
    }


    public int getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMensaje() {
        return mensaje;
    }

}
