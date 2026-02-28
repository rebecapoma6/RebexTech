package es.rebextech.beans;
import java.io.Serializable;

/**
 * Clase Bean (Categoria) que define una familia o categoría de productos.
 * Mapea la tabla 'categorias' y se carga en el contexto global de la aplicación 
 * al iniciar el servidor para estar disponible en los menús de navegación.
 * * @author Rebeca
 */
public class Categoria implements Serializable {

    private byte idcategoria; // TINYINT (1 byte)
    private String nombre;
    private String imagen;

    public Categoria() {
    }

    public byte getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(byte idcategoria) {
        this.idcategoria = idcategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
