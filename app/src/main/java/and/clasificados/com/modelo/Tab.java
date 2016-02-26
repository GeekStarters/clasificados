package and.clasificados.com.modelo;

/**
 * Created by Gabriela Mejia on 24/2/2016.
 */
public class Tab {

    private String id;
    private String nombre;
    private String url_imagen;

    public Tab(){

    }

    public Tab(String id, String nombre, String url_imagen) {
        this.id = id;
        this.nombre = nombre;
        this.url_imagen = url_imagen;
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

    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }

    @Override
    public String toString() {
        return "Tab{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", url_imagen='" + url_imagen + '\'' +
                '}';
    }
}
