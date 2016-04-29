package and.clasificados.com.modelo;

/**
 * Created by Gabriela Mejia on 24/2/2016.
 */
public class Localidad {

    private String id;
    private String nombre;
    private Double longitud;
    private Double latitud;
    public Localidad(){

    }

    public Localidad(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Localidad(String id, String nombre, Double longitud, Double latitud) {
        this.id = id;
        this.nombre = nombre;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public Double getLatitud() {

        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
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
        return "Localidad{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
