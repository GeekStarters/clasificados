package and.clasificados.com.modelo;

import java.util.ArrayList;
import java.util.List;

import and.clasificados.com.R;
import and.clasificados.com.auxiliares.Item;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class Clasificado extends Item{
    private String precio;
    private String textoAnuncio;
    private String categoria;
    private int idDrawable;
    private String url;
    private String single;
    private String slug;

    public Clasificado() {
    }

    public Clasificado(String precio, String categoria, String anuncio, int idDrawable) {
        this.categoria=categoria;
        this.precio = precio;
        this.textoAnuncio = anuncio;
        this.idDrawable = idDrawable;
    }

    public Clasificado(String precio, String categoria, String anuncio, String url, String single, String slug){
        this.categoria=categoria;
        this.precio = precio;
        this.textoAnuncio = anuncio;
        this.url = url;
        this.single=single;
        this.slug=slug;
    }

    public String getSingle() {
        return single;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public void setTextoAnuncio(String textoAnuncio) {
        this.textoAnuncio = textoAnuncio;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getPrecio() {
        return precio;
    }

    public String getTextoAnuncio() {
        return textoAnuncio;
    }

    public int getIdDrawable() {
        return idDrawable;
    }

    public String getCategoria() {
        return categoria;
    }

    @Override
    public String toString() {
        return "Clasificado{" +
                "precio='" + precio + '\'' +
                ", textoAnuncio='" + textoAnuncio + '\'' +
                ", categoria='" + categoria + '\'' +
                ", url='" + url + '\'' +
                ", single='" + single + '\'' +
                '}';
    }
}
