
package es.rebextech.IDAO;

import es.rebextech.DAOFactory.MySQLDAOFactory;

/**
 *
 * @author User
 */
public abstract class DAOFactory {
    // Un método abstracto por cada tabla principal de tu base de datos
    public abstract IUsuarioDAO getUsuarioDAO();
    public abstract IProductoDAO getProductoDAO();
    public abstract ICategoriaDAO getCategoriaDAO();
    public abstract IPedidoDAO getPedidoDAO();

    /**
     * Devuelve la implementación concreta de la fábrica (en este caso MySQL).
     * @return Objeto de la fábrica MySQLDAOFactory
     */
    public static DAOFactory getDAOFactory() {
        return new MySQLDAOFactory();
    }
}
