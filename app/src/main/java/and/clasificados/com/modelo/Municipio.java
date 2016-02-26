package and.clasificados.com.modelo;

/**
 * Created by Gabriela Mejia on 24/2/2016.
 */
public class Municipio {

    private String id;
    private String nombre;
    private String zona;

    public Municipio() {
    }

    public Municipio(String id, String nombre, String zona) {
        this.zona = zona;
        this.nombre = nombre;
        this.id = id;
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

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    @Override
    public String toString() {
        return "SubCategoria{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", nameParent='" + zona + '\'' +
                '}';
    }
}
