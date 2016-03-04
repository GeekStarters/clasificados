package and.clasificados.com.modelo;

import java.util.ArrayList;
import java.util.List;

import and.clasificados.com.R;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class Mensaje {
    private int imagen;
    private String nombre_item;
    private String mensaje;
    private int idDrawable;

    public Mensaje(int idDrawable, String nombre_item, String mensaje) {
        this.nombre_item = nombre_item;
        this.mensaje = mensaje;
        this.idDrawable=idDrawable;
    }

    public static final List<Mensaje> MENSAJES= new ArrayList<Mensaje>();

    static {
        MENSAJES.add(new Mensaje(R.drawable.profile2, "Mazda 3","Lorem ipsum sit dolor lorem ipsum"));
        MENSAJES.add(new Mensaje(R.drawable.profile2, "iPhone 3","Lorem ipsum sit dolor lorem ipsum"));
        MENSAJES.add(new Mensaje(R.drawable.profile2, "Alquiler Casa","Lorem ipsum sit dolor lorem ipsum"));
        MENSAJES.add(new Mensaje(R.drawable.profile2, "Laptop","Lorem ipsum sit dolor lorem ipsum"));
        MENSAJES.add(new Mensaje(R.drawable.profile2, "Lorem Ipsum","Lorem ipsum sit dolor lorem ipsum"));
    }


    public int getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre_item;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getIdDrawable() {
        return idDrawable;
    }

}
