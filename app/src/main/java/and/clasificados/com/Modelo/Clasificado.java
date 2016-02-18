package and.clasificados.com.modelo;

import java.util.ArrayList;
import java.util.List;

import and.clasificados.com.R;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class Clasificado {
    private String precio;
    private String textoAnuncio;
    private String categoria;
    private int idDrawable;
    private String url;

    public Clasificado() {
    }

    public Clasificado(String precio, String categoria, String anuncio, int idDrawable) {
        this.categoria=categoria;
        this.precio = precio;
        this.textoAnuncio = anuncio;
        this.idDrawable = idDrawable;
    }

    public Clasificado(String precio, String categoria, String anuncio, String url) {
        this.categoria=categoria;
        this.precio = precio;
        this.textoAnuncio = anuncio;
        this.url = url;
    }


    public static final List<Clasificado> EMPLEOS = new ArrayList<Clasificado>();
    public static final List<Clasificado> SERVICIOS = new ArrayList<>();
    public static final List<Clasificado> FAVORITOS = new ArrayList<>();
    public static final List<Clasificado> MIOS = new ArrayList<>();

    static {

        EMPLEOS.add(new Clasificado("Q. 3002.50", "Servicios", "Fontaneria", R.drawable.ingeniero));
        EMPLEOS.add(new Clasificado("Q. 1200.00", "Industria", "Ingeniero", R.drawable.ingeniero));
        EMPLEOS.add(new Clasificado("Q. 5025.50", "Industria", "Ingeniero", R.drawable.ingeniero));
        EMPLEOS.add(new Clasificado("Q. 2400.00", "Industria", "Ingeniero", R.drawable.ingeniero));
        EMPLEOS.add(new Clasificado("Q. 3004.00", "Industria", "Ingeniero", R.drawable.ingeniero));

        SERVICIOS.add(new Clasificado("Q. 2125.00","Plomeria", "Fontaneria", R.drawable.electricista));
        SERVICIOS.add(new Clasificado("Q. 3125.0", "Electricidad","Fontaneria", R.drawable.electricista));
        SERVICIOS.add(new Clasificado("Q. 2125.00","Restaurador", "Fontaneria", R.drawable.electricista));
        SERVICIOS.add(new Clasificado("Q. 4125.00", "Otro","Fontaneria", R.drawable.electricista));
        SERVICIOS.add(new Clasificado("Q. 5125.00", "Otros","Fontaneria", R.drawable.electricista));

        FAVORITOS.add(new Clasificado("Q."+5450.90, "Autos","Mitsubishi Lancer", R.drawable.carro));
        FAVORITOS.add(new Clasificado("Q."+4250.00, "Otros", "Mitsubishi Lancer", R.drawable.carro));
        FAVORITOS.add(new Clasificado("Q."+2410.00, "Servicios", "Fontaneria", R.drawable.ingeniero));
        FAVORITOS.add(new Clasificado("Q."+5000.00, "Otros","Laptop", R.drawable.celular));
        FAVORITOS.add(new Clasificado("Q."+5102.00, "Otros","Fontaneria", R.drawable.electricista));

        MIOS.add(new Clasificado("Q."+5050.90, "Autos", "Mitsubishi Lancer", R.drawable.carro));
        MIOS.add(new Clasificado("Q."+4050.00, "Alquiler", "Edifico", R.drawable.edificio));
        MIOS.add(new Clasificado("Q."+2010.00, "Servicios", "Fontaneria", R.drawable.ingeniero));
        MIOS.add(new Clasificado("Q."+5000.00, "Otros", "Laptop", R.drawable.celular));
        MIOS.add(new Clasificado("Q."+5002.00, "Otros", "Fontaneria", R.drawable.electricista));

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
}
