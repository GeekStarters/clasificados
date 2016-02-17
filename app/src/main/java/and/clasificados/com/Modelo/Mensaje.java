package and.clasificados.com.modelo;

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
    private int idDrawable;

    public Mensaje(int idDrawable, String nombre, String mensaje) {
        this.nombre = nombre;
        this.mensaje = mensaje;
        this.idDrawable=idDrawable;
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

    public int getIdDrawable() {
        return idDrawable;
    }

}
