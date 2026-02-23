package es.rebextech.IDAO;

import es.rebextech.beans.LineaPedido;
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
    
    // Cambiamos String idCategoria por un Array de String o una Lista
    public List<Producto> filtroavanzadoCatalogo(String texto, String[] idsCategorias, double precioMin, double precioMax);
    
    public List<LineaPedido> getProductosCarritoBD(short idUsuario);
    
}
