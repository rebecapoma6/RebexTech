
package es.rebextech.DAO;

import es.rebextech.IDAO.IUsuarioDAO;
import es.rebextech.beans.Usuario;

/**
 *
 * @author User
 */
public class UsuarioDAO implements IUsuarioDAO {
    @Override
    public Usuario login(String email, String password) {
        // Aquí irá la lógica SQL con MD5 más adelante
        return null;
    }

    @Override
    public void registrarUsuario(Usuario usuario) {
        // Aquí irá el INSERT a la tabla usuarios
    }

    @Override
    public boolean existeEmail(String email) {
        // Usado para validaciones antes de registrar
        return false;
    }
}
