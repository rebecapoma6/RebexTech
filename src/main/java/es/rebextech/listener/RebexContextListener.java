package es.rebextech.listener;
import es.rebextech.IDAO.DAOFactory;
import es.rebextech.beans.Categoria;
import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Clase que escucha los eventos del ciclo de vida de la aplicación web.
 * Se ejecuta automáticamente al arrancar el servidor Tomcat.
 * Su función principal es consultar la base de datos una única vez al inicio
 * para cargar el listado de categorías y guardarlo en el contexto global (Application Scope),
 * poniéndolo a disposición de todos los usuarios de forma instantánea.
 * @author Rebeca Poma
 * 
 */
@javax.servlet.annotation.WebListener
public class RebexContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
       
        DAOFactory fabric = DAOFactory.getDAOFactory();

        List<Categoria> listaCategorias = fabric.getCategoriaDAO().listarTodas();
        sce.getServletContext().setAttribute("categoriasGlobales", listaCategorias);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
