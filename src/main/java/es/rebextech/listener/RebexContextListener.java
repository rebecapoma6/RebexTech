    
package es.rebextech.listener;

import es.rebextech.IDAO.DAOFactory;
import es.rebextech.beans.Categoria;
import es.rebextech.beans.Producto;
import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author User
 */
@javax.servlet.annotation.WebListener
public class RebexContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
       // 1. Obtenemos la fábrica (estilo profesor)
        DAOFactory fabric = DAOFactory.getDAOFactory();
        
        // 2. Cargamos categorías para el menú
        List<Categoria> listaCategorias = fabric.getCategoriaDAO().getCategorias();
        sce.getServletContext().setAttribute("todasCategorias", listaCategorias);
        
        // 3. Cargamos los 12 productos aleatorios para el diseño que quieres
        List<Producto> productosAzar = fabric.getProductoDAO().getProductosAleatorios(12);
        sce.getServletContext().setAttribute("productosLanding", productosAzar);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
       
    }
    
}
