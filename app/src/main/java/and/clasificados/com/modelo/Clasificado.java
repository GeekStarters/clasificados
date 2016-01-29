package and.clasificados.com.modelo;

import java.util.ArrayList;
import java.util.List;

import and.clasificados.com.R;

/**
 * Created by Gabriela Mejia on 26/1/2016.
 */
public class Clasificado {
    private double precio;
    private String textoAnuncio;
    private String categoria;
    private int idDrawable;

    public Clasificado(double precio, String categoria, String anuncio, int idDrawable) {
        this.categoria=categoria;
        this.precio = precio;
        this.textoAnuncio = anuncio;
        this.idDrawable = idDrawable;
    }

    public static final List<Clasificado> VEHICULOS = new ArrayList<Clasificado>();
    public static final List<Clasificado> INMUEBLES = new ArrayList<>();
    public static final List<Clasificado> PRODUCTOS = new ArrayList<>();
    public static final List<Clasificado> EMPLEOS = new ArrayList<>();
    public static final List<Clasificado> SERVICIOS = new ArrayList<>();

    static {
        VEHICULOS.add(new Clasificado(5450.90, "Autos","Mitsubishi Lancer", R.drawable.carro));
        VEHICULOS.add(new Clasificado(4250.00, "Otros", "Mitsubishi Lancer", R.drawable.carro));
        VEHICULOS.add(new Clasificado(1200.50,"Moto", "Mitsubishi Lancer", R.drawable.carro));
        VEHICULOS.add(new Clasificado(9800.00, "Moto","Mitsubishi Lancer", R.drawable.carro));
        VEHICULOS.add(new Clasificado(3400.00,"Otros", "Mitsubishi Lancer", R.drawable.carro));

        INMUEBLES.add(new Clasificado(5000.00,"En Venta","Edificio", R.drawable.edificio));
        INMUEBLES.add(new Clasificado(3205.00, "Alquiler","Edificio", R.drawable.edificio));
        INMUEBLES.add(new Clasificado(1205.50, "En Venta","Edificioo", R.drawable.edificio));
        INMUEBLES.add(new Clasificado(9000.00,"Alquiler", "Edificio", R.drawable.edificio));
        INMUEBLES.add(new Clasificado(3500.00, "En  Venta", "Edificio", R.drawable.edificio));

        PRODUCTOS.add(new Clasificado(2,"Electrodomensticos", "Postre De Vainilla", R.drawable.celular));
        PRODUCTOS.add(new Clasificado(3, "Celulares","Flan Celestial", R.drawable.celular));
        PRODUCTOS.add(new Clasificado(2.5f,"Muebles", "Cupcake Festival", R.drawable.celular));
        PRODUCTOS.add(new Clasificado(4, "Joyeria","Pastel De Fresa", R.drawable.celular));
        PRODUCTOS.add(new Clasificado(5, "Otros","Muffin Amoroso", R.drawable.celular));

        EMPLEOS.add(new Clasificado(3, "Servicios", "Taza de Café", R.drawable.ingeniero));
        EMPLEOS.add(new Clasificado(12, "Industria", "Coctel Tronchatoro", R.drawable.ingeniero));
        EMPLEOS.add(new Clasificado(5, "", "Jugo Natural", R.drawable.ingeniero));
        EMPLEOS.add(new Clasificado(24, "Industria", "Coctel Jordano", R.drawable.ingeniero));
        EMPLEOS.add(new Clasificado(30, "", "Botella Vino Tinto Darius", R.drawable.ingeniero));

        SERVICIOS.add(new Clasificado(2,"Plomeeria", "Postre De Vainilla", R.drawable.electricista));
        SERVICIOS.add(new Clasificado(3, "Celulares","Flan Celestial", R.drawable.electricista));
        SERVICIOS.add(new Clasificado(2.5f,"Muebles", "Cupcake Festival", R.drawable.electricista));
        SERVICIOS.add(new Clasificado(4, "Joyeria","Pastel De Fresa", R.drawable.electricista));
        SERVICIOS.add(new Clasificado(5, "Otros","Muffin Amoroso", R.drawable.electricista));


    }

    public double getPrecio() {
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

