package es.rebextech.IDAO;

import es.rebextech.beans.Producto;
import java.util.List;

/**
 *
 * @author User
 */
public interface IProductoDAO {

    public List<Producto> getProductos();

    public Producto getProductoById(short idproducto);
    
    public List<Producto> getProductosAleatorios(int cantidad);
}
