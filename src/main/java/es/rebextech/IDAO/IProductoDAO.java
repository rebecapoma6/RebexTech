package es.rebextech.IDAO;

import es.rebextech.beans.Producto;
import java.util.List;

/**
 *
 * @author User
 */
public interface IProductoDAO {



    public List<Producto> getProductosCarrito(String[] ids);
    
    public List<Producto> getProductosAleatorios(int cantidad);
    
    public List<Producto> busquedalibreNavabar(String textoBusqueda);
    
    public List<Producto> filtroavanzadoCatalogo(String texto, String idCategoria, double precioMin, double precioMax);
    
//    public List<Producto> getBusquedaAvanzada(String texto, String idCat, double min, double max);
    
}
