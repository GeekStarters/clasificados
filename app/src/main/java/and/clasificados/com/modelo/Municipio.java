package and.clasificados.com.modelo;

import java.util.ArrayList;

/**
 * Created by Gabriela Mejia on 24/2/2016.
 */
public class Municipio {

    private String id;
    private String nombre;
    private Double longitud;
    private Double latitud;
    private ArrayList<Zona> zonas;
    private String zona;

    public Municipio() {
    }

    public Municipio(String id, String nombre, Double longitud, Double latitud, ArrayList<Zona> zonas) {
        this.id = id;
        this.nombre = nombre;
        this.longitud = longitud;
        this.latitud = latitud;
        this.zonas = zonas;
    }

    public Municipio(String id, String nombre, String zona) {
        this.zona = zona;
        this.nombre = nombre;
        this.id = id;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public ArrayList<Zona> getZonas() {
        return zonas;
    }

    public void setZonas(ArrayList<Zona> zonas) {
        this.zonas = zonas;
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
