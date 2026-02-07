package es.rebextech.IDAO;

import es.rebextech.beans.Usuario;

/**
 *
 * @author User
 */
public interface IUsuarioDAO {

    public Usuario login(String email, String password);

    public void registrarUsuario(Usuario usuario);

    // Este método lo usará el Servlet después de que el usuario envíe el formulario
    public boolean existeEmail(String email);
}
