package and.clasificados.com.modelo;

import java.util.ArrayList;

/**
 * Created by Gabriela Mejia on 24/2/2016.
 */
public class Modelo {

    private String id;
    private String nombre;

    public Modelo(){

    }


    public Modelo(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    @Override
    public String toString() {
        return "Categoria{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
