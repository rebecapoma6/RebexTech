package es.rebextech.IDAO;

import es.rebextech.beans.Producto;
import java.util.List;

/**
 *
 * @author User
 */
public interface IProductoDAO {

    public List<Producto> getProductos();

    public List<Producto> getProductosCarrito(String[] ids);
    
    public List<Producto> getProductosAleatorios(int cantidad);
}
