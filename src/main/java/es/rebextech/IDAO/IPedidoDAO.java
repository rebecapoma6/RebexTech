package es.rebextech.IDAO;
import es.rebextech.beans.LineaPedido;
import es.rebextech.beans.Pedido;
import java.util.List;

/**
 * Interfaz que define las operaciones de base de datos para la gestión 
 * tanto de los carritos de compra (pedidos en estado 'c') como de las facturas
 * finalizadas (pedidos en estado 'f').
 * * @author Rebeca
 */
public interface IPedidoDAO {
    
    /**
     *Convierte el carrito actual de un usuario en un pedido finalizado (compra real).
     **@param idUsuario El identificador del cliente que realiza la compra.
     *@param carrito La lista de productos (líneas) a comprar.
     *@param totalImporte El precio total a cobrar.
     *@return El ID del pedido generado (factura), o -1 si hubo un error.
     */
    public short finalizarPedido(int idUsuario, List<LineaPedido> carrito, double totalImporte);
    
    
    /**
     * Transfiere los productos que un usuario anónimo guardó en su Cookie
     * hacia su carrito en la base de datos al momento de iniciar sesión.
     **@param idDelUsuario Identificador del usuario que acaba de loguearse.
     *@param contenidoDeLaCookie Cadena de texto con los IDs de productos (ej: "4-4-15").
     */
    public void migrarCarritoDeCookieABaseDeDatos(int idDelUsuario, String contenidoDeLaCookie);
    
    
    /**
     * Añade un producto al carrito en la base de datos. Si el producto ya existe,
     * aumenta su cantidad en 1.
     **@param idDelUsuario Identificador del dueño del carrito.
     *@param idDelProducto Identificador del artículo a agregar.
     */
    public void agregarUnProductoAlCarritoBD(int idDelUsuario, int idDelProducto);
    
    
    /**
     * Cuenta el número total de artículos individuales dentro del carrito de un usuario.
     **@param idUsuario Identificador del usuario.
     *@return Cantidad total de productos en el carrito.
     */
    public int contarProductosCarrito(int idUsuario);
    
    
    /**
     * Elimina por completo un producto específico del carrito del usuario.
     *@param idUsuario Identificador del usuario.
     *@param idProducto Identificador del producto a eliminar.
     */
    public void eliminarProductoDelCarritoBD(int idUsuario, int idProducto);
    
    
    /**
     * Elimina todos los productos del carrito activo de un usuario (Estado 'c').
     *@param idUsuario Identificador del usuario cuyo carrito se va a vaciar.
     */
    public void vaciarCarritoBD(int idUsuario);
    
    
    /**
     * Recupera el listado de todas las compras (facturas) que un usuario 
     * ha completado exitosamente en el pasado.
     *@param idUsuario Identificador del cliente.
     *@return Lista de pedidos en estado 'f' (Finalizado).
     */
    public List<Pedido> getHistorialPedidos(int idUsuario);
    
    
    /**
     * Actualiza directamente la cantidad de un producto en el carrito (mediante botones +/-).
     *@param idUsuario Identificador del usuario.
     *@param idProducto Identificador del producto a modificar.
     *@param nuevaCantidad La cantidad exacta que se desea establecer.
     */
    public void actualizarCantidadProductoBD(int idUsuario, int idProducto, int nuevaCantidad);
    
    
    /**
     * Obtiene el detalle (las líneas) de los productos comprados en un pedido específico.
     * Ideal para mostrar el comprobante o factura de la compra.
     **@param idPedido Identificador de la factura/pedido.
     *@return Lista con el detalle de productos y cantidades de esa factura.
     */
    public List<LineaPedido> getLineasPorIdPedido(int idPedido);
  
}
