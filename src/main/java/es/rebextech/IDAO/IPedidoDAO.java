
package es.rebextech.IDAO;

import es.rebextech.beans.LineaPedido;
import es.rebextech.beans.Pedido;
import java.util.List;

/**
 *
 * @author User
 */
public interface IPedidoDAO {
    public short finalizarPedido(int idUsuario, List<LineaPedido> carrito, double totalImporte);
    public void migrarCarritoDeCookieABaseDeDatos(int idDelUsuario, String contenidoDeLaCookie);
    // Método para agregar un solo producto al carrito de un usuario logueado
    public void agregarUnProductoAlCarritoBD(int idDelUsuario, int idDelProducto);
    public int contarProductosCarrito(int idUsuario);
    public void eliminarProductoDelCarritoBD(int idUsuario, int idProducto);
    public void vaciarCarritoBD(int idUsuario);
    public List<Pedido> getPedidosPorUsuario(int idUsuario);
    public void actualizarCantidadProductoBD(int idUsuario, int idProducto, int nuevaCantidad);
    public List<LineaPedido> getLineasPorIdPedido(int idPedido);
}
