package and.clasificados.com.modelo;

/**
 * Created by Gabriela Mejia on 24/2/2016.
 */
public class Zona {

    private String id;
    private String nombre;

    public Zona(){

    }

    public Zona(String id, String nombre) {
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
        return "Zona{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
