package es.rebextech.IDAO;

import es.rebextech.beans.LineaPedido;
import es.rebextech.beans.Producto;
import java.util.List;

/**
 *Interfaz que define las reglas para la consulta y manipulación del catálogo 
 *de artículos de la tienda (Tabla 'productos').
 **@author Rebeca
 */
public interface IProductoDAO {

    /**
     * Obtiene la información completa de los productos basándose en una lista de IDs.
     * Utilizado principalmente para recuperar los productos guardados en la Cookie (Anónimos).
     **@param ids Array de Strings que contiene los identificadores numéricos de los productos.
     *@return Lista de objetos Producto con sus datos cargados.
     */
    public List<Producto> getProductosCarrito(String[] ids);
    
    
    /**
     * Recupera un número específico de productos seleccionados de forma aleatoria.
     * Ideal para el escaparate o página principal de la tienda.
     **@param cantidad El número de productos que se desea mostrar.
     *@return Lista de productos aleatorios.
     */
    public List<Producto> getProductosAleatorios(int cantidad);
    
    
    /**
     * Realiza una búsqueda flexible en el catálogo coincidiendo el texto ingresado 
     * con el nombre o la marca del producto.
     **@param textoBusqueda Palabra o frase introducida por el usuario.
     *@return Lista de productos que coinciden con el criterio de búsqueda.
     */
    public List<Producto> busquedalibreNavabar(String textoBusqueda);
    
    
    /**
     * Realiza una búsqueda compleja en el catálogo combinando varios filtros a la vez.
     **@param texto Filtro opcional por nombre o marca.
     *@param idsCategorias Array con los IDs de las categorías seleccionadas.
     *@param precioMin Precio mínimo del rango de búsqueda.
     *@param precioMax Precio máximo del rango de búsqueda.
     *@return Lista de productos que cumplen estrictamente todos los filtros aplicados.
     */
    public List<Producto> filtroavanzadoCatalogo(String texto, String[] idsCategorias, double precioMin, double precioMax);
    
    
    /**
     * Extrae los productos y sus cantidades que un usuario registrado 
     * tiene guardados actualmente en su carrito de compras en la base de datos.
     **@param idUsuario Identificador del dueño del carrito.
     *@return Lista de LineaPedido (que contiene tanto el Producto como la Cantidad).
     */
    public List<LineaPedido> getProductosCarritoBD(short idUsuario);
    
}
