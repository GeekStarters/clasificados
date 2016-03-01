package and.clasificados.com.modelo;

/**
 * Created by Gabriela Mejia on 24/2/2016.
 */
public class Moneda {

    private String id;
    private String nombre;
    private String simbolo;

    public Moneda(){

    }

    public Moneda(String id, String nombre, String simbolo) {
        this.id = id;
        this.nombre = nombre;
        this.simbolo=simbolo;
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

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    @Override
    public String toString() {
        return "Moneda{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", simbolo='" + simbolo + '\'' +
                '}';
    }
}
