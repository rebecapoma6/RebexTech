package es.rebextech.IDAO;
import es.rebextech.beans.Categoria;
import java.util.List;

/**
 *Interfaz que define el contrato para el acceso a los datos de las categorías.
 *Su función principal es obtener las familias de productos disponibles en la tienda.
 * 
 * @author Rebeca Poma
 */
public interface ICategoriaDAO {
    
    /**
     *Recupera todas las categorías registradas en la base de datos.
     *Generalmente se utiliza para cargar el menú de navegación al iniciar la aplicación.
     *@return Una lista (List) de objetos Categoria.
     */
    public List<Categoria> listarTodas();
    
  
}
