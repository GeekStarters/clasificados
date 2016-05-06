package and.clasificados.com.modelo;

import java.util.ArrayList;

/**
 * Created by Gabriela Mejia on 24/2/2016.
 */
public class Categoria {

    private String id;
    private String nombre;
    private String slug;
    private ArrayList<SubCategoria> sub;

    public Categoria(){

    }

    public Categoria(String id, String nombre, ArrayList<SubCategoria> sub) {
        this.id = id;
        this.nombre = nombre;
        this.sub = sub;
    }

    public Categoria(String id, String nombre, String slug, ArrayList<SubCategoria> sub) {
        this.id = id;
        this.nombre = nombre;
        this.slug = slug;
        this.sub = sub;
    }

    public Categoria(String id, String nombre) {
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

    public ArrayList<SubCategoria> getSub() {
        return sub;
    }

    public void setSub(ArrayList<SubCategoria> sub) {
        this.sub = sub;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
