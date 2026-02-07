
package es.rebextech.DAO;

import es.rebextech.IDAO.IPedidoDAO;
import es.rebextech.beans.LineaPedido;
import es.rebextech.beans.Pedido;
import java.util.List;

/**
 *
 * @author User
 */
public class PedidoDAO implements IPedidoDAO {
    @Override
    public void finalizarPedido(Pedido pedido, List<LineaPedido> lineas) {
        // Aquí implementaremos la Transacción (commit/rollback)
        // para guardar el pedido y sus líneas juntas
    }
}
