package and.clasificados.com.modelo;

import java.util.ArrayList;
import java.util.List;

import and.clasificados.com.R;

/**
 * Created by Gabriela Mejia on 1/2/2016.
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
    public static final List<Clasificado> FAVORITOS = new ArrayList<>();
    public static final List<Clasificado> MIOS = new ArrayList<>();

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

        PRODUCTOS.add(new Clasificado(2483.54,"Electrodomensticos", "Refrigerador", R.drawable.celular));
        PRODUCTOS.add(new Clasificado(3483.54, "Celulares","Celular", R.drawable.celular));
        PRODUCTOS.add(new Clasificado(2483.54,"Muebles", "Chaise Longue", R.drawable.celular));
        PRODUCTOS.add(new Clasificado(4483.54, "Joyeria","Collar de diamantes", R.drawable.celular));
        PRODUCTOS.add(new Clasificado(5483.54, "Otros","Laptop", R.drawable.celular));

        EMPLEOS.add(new Clasificado(3002.50, "Servicios", "Fontaneria", R.drawable.ingeniero));
        EMPLEOS.add(new Clasificado(1200.00, "Industria", "Ingeniero", R.drawable.ingeniero));
        EMPLEOS.add(new Clasificado(5025.50, "Industria", "Ingeniero", R.drawable.ingeniero));
        EMPLEOS.add(new Clasificado(2400.00, "Industria", "Ingeniero", R.drawable.ingeniero));
        EMPLEOS.add(new Clasificado(3004.00, "Industria", "Ingeniero", R.drawable.ingeniero));

        SERVICIOS.add(new Clasificado(2125.00,"Plomeria", "Fontaneria", R.drawable.electricista));
        SERVICIOS.add(new Clasificado(3125.00, "Electricidad","Fontaneria", R.drawable.electricista));
        SERVICIOS.add(new Clasificado(2125.00,"Restaurador", "Fontaneria", R.drawable.electricista));
        SERVICIOS.add(new Clasificado(4125.00, "Otro","Fontaneria", R.drawable.electricista));
        SERVICIOS.add(new Clasificado(5125.00, "Otros","Fontaneria", R.drawable.electricista));

        FAVORITOS.add(new Clasificado(5450.90, "Autos","Mitsubishi Lancer", R.drawable.carro));
        FAVORITOS.add(new Clasificado(4250.00, "Otros", "Mitsubishi Lancer", R.drawable.carro));
        FAVORITOS.add(new Clasificado(2410.00, "Servicios", "Fontaneria", R.drawable.ingeniero));
        FAVORITOS.add(new Clasificado(5000.00, "Otros","Laptop", R.drawable.celular));
        FAVORITOS.add(new Clasificado(5102.00, "Otros","Fontaneria", R.drawable.electricista));

        MIOS.add(new Clasificado(5050.90, "Autos", "Mitsubishi Lancer", R.drawable.carro));
        MIOS.add(new Clasificado(4050.00, "Alquiler", "Edifico", R.drawable.edificio));
        MIOS.add(new Clasificado(2010.00, "Servicios", "Fontaneria", R.drawable.ingeniero));
        MIOS.add(new Clasificado(5000.00, "Otros", "Laptop", R.drawable.celular));
        MIOS.add(new Clasificado(5002.00, "Otros", "Fontaneria", R.drawable.electricista));

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
