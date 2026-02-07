
package es.rebextech.DAO;

import es.rebextech.IDAO.ICategoriaDAO;
import es.rebextech.beans.Categoria;
import java.util.List;

/**
 *
 * @author User
 */
public class CategoriaDAO implements ICategoriaDAO {
    @Override
    public List<Categoria> getCategorias() {
        return null; // De momento lo dejamos así para que no de error
    }
}
