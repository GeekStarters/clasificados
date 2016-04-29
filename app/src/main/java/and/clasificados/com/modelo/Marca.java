package and.clasificados.com.modelo;

import java.util.ArrayList;

/**
 * Created by Gabriela Mejia on 24/2/2016.
 */
public class Marca {

    private String id;
    private String nombre;
    private String slug;
    private ArrayList<Modelo> modelo;

    public Marca(){

    }

    public Marca(String id, String nombre, ArrayList<Modelo> modelo) {
        this.id = id;
        this.nombre = nombre;
        this.modelo = modelo;
    }

    public Marca(String id, String nombre) {
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ArrayList<Modelo> getModelo() {
        return modelo;
    }

    public void setSub(ArrayList<Modelo> marca) {
        this.modelo = marca;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
