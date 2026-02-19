package es.rebextech.IDAO;

import es.rebextech.beans.Usuario;

/**
 *
 * @author User
 */
public interface IUsuarioDAO {

   // Para entrar a la tienda
    public Usuario login(String email, String password);

    // El método principal de registro (donde usarás el objeto poblado por BeanUtils)
    public int registrarUsuario(Usuario usuario);

    // Validaciones de seguridad (El PDF suele pedir evitar duplicados)
    public boolean existeEmail(String email);
    public boolean existeNif(String nif); // Muy útil porque el NIF es único

    // Opcional: Solo si vas a permitir cambiar la foto después del registro
    public boolean actualizarAvatar(int idUsuario, String nombreArchivo);
}
