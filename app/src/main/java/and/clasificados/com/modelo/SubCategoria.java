package and.clasificados.com.modelo;

/**
 * Created by Gabriela Mejia on 24/2/2016.
 */
public class SubCategoria {

    private String id;
    private String nombre;
    private String slug;
    private String useSlug;
    private String nameParent;

    public SubCategoria() {
    }

    public SubCategoria (String id, String nombre, String nameParent) {
        this.nameParent = nameParent;
        this.nombre = nombre;
        this.id = id;
    }

    public SubCategoria(String id, String nombre, String slug, String useSlug) {
        this.id = id;
        this.nombre = nombre;
        this.slug = slug;
        this.useSlug = useSlug;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUseSlug() {
        return useSlug;
    }

    public void setUseSlug(String useSlug) {
        this.useSlug = useSlug;
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

    public String getNameParent() {
        return nameParent;
    }

    public void setNameParent(String nameParent) {
        this.nameParent = nameParent;
    }

    @Override
    public String toString() {
        return "SubCategoria{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", nameParent='" + nameParent + '\'' +
                '}';
    }
}
