
package es.rebextech.IDAO;

import es.rebextech.beans.LineaPedido;
import es.rebextech.beans.Pedido;
import java.util.List;

/**
 *
 * @author User
 */
public interface IPedidoDAO {
    public void finalizarPedido(Pedido pedido, List<LineaPedido> lineas);
}
