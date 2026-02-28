package es.rebextech.IDAO;
import es.rebextech.DAOFactory.MySQLDAOFactory;

/**
 * Clase abstracta que implementa el patrón de diseño Factory (Fábrica).
 * Su función es independizar el código de la aplicación de la base de datos,
 * permitiendo obtener instancias de los DAOs sin importar si la base por debajo
 * es MySQL, Oracle o cualquier otra tecnología en el futuro.
 * * @author Rebeca
 * @author User
 */
public abstract class DAOFactory {
    
    
    /** @return Implementación concreta para manejar usuarios */
    public abstract IUsuarioDAO getUsuarioDAO();
    
    
    /** @return Implementación concreta para manejar el catálogo de artículos */
    public abstract IProductoDAO getProductoDAO();
    
    
    /** @return Implementación concreta para obtener las familias de productos */
    public abstract ICategoriaDAO getCategoriaDAO();
    
    
    /** @return Implementación concreta para gestionar carritos y facturas */
    public abstract IPedidoDAO getPedidoDAO();
    

    /**
     * Método estático principal que sirve como punto de entrada a la fábrica.
     * Actualmente devuelve la implementación configurada para trabajar con MySQL.
     * @return Objeto de la fábrica concreta MySQLDAOFactory
     */
    public static DAOFactory getDAOFactory() {
        return new MySQLDAOFactory();
    }
}
