package es.rebextech.DAOFactory;
import es.rebextech.DAO.CategoriaDAO;
import es.rebextech.DAO.PedidoDAO;
import es.rebextech.DAO.ProductoDAO;
import es.rebextech.DAO.UsuarioDAO;
import es.rebextech.IDAO.DAOFactory;
import es.rebextech.IDAO.ICategoriaDAO;
import es.rebextech.IDAO.IPedidoDAO;
import es.rebextech.IDAO.IProductoDAO;
import es.rebextech.IDAO.IUsuarioDAO;

/**
 * Fábrica concreta para el motor de base de datos MySQL.
 * Hereda de la clase abstracta DAOFactory y se encarga de instanciar 
 * y devolver los objetos DAO específicos creados para trabajar con MySQL.
 * @author Rebeca
 * 
 */
public class MySQLDAOFactory extends DAOFactory{
    @Override
    public IUsuarioDAO getUsuarioDAO() {
        return new UsuarioDAO();
    }

    @Override
    public IProductoDAO getProductoDAO() {
        return new ProductoDAO();
    }

    @Override
    public ICategoriaDAO getCategoriaDAO() {
        return new CategoriaDAO();
    }

    @Override
    public IPedidoDAO getPedidoDAO() {
        return new PedidoDAO();
    }
}
