package es.rebextech.IDAO;

import es.rebextech.beans.Usuario;

/**
 * Interfaz que define las operaciones relacionadas con la tabla 'usuarios'.
 * Gestiona el acceso, registro, perfiles y seguridad de los clientes de RebexTech.
 * * @author Rebeca
 */
public interface IUsuarioDAO {

    /**
     * Valida las credenciales de un cliente para darle acceso a la tienda.
     **@param email Correo electrónico ingresado en el formulario.
     *@param password Contraseña ingresada en texto plano (se encriptará internamente).
     *@return Objeto Usuario cargado si las credenciales son correctas, o null si fallan.
     */
    public Usuario login(String email, String password);

    
    /**
     * Inserta un nuevo cliente en el sistema.
     **@param usuario Objeto con todos los datos personales capturados desde el formulario.
     *@return El ID numérico autogenerado del nuevo usuario, o -1 si falla.
     */
    public int registrarUsuario(Usuario usuario);

    
    /**
     * Comprueba si un correo electrónico ya está registrado en el sistema.
     * Utilizado en validaciones AJAX.
     **@param email Correo electrónico a verificar.
     *@return true si el email ya existe en la BD, false si está libre.
     */
    public boolean existeEmail(String email);
    
    
    /**
     * Comprueba si un documento de identidad (NIF) ya pertenece a otro cliente.
     **@param nif Documento a verificar.
     *@return true si el NIF ya está en uso, false si está libre.
     */
    public boolean existeNif(String nif);

    
    /**
     * Modifica la ruta de la imagen de perfil del usuario.
     **@param idUsuario Identificador del usuario.
     *@param nombreArchivo Nombre del nuevo archivo de imagen subido.
     *@return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarAvatar(int idUsuario, String nombreArchivo);
    
    
    /**
     * Registra la fecha y hora exacta en la que un cliente inició sesión.
     **@param idUsuario Identificador del cliente.
     */
    public void actualizarUltimoAcceso(int idUsuario);
    
    
    /**
     * Modifica los datos personales del cliente (Dirección, teléfono, nombre, etc.).
     * Nota: Por reglas de negocio, no permite alterar ni el Email ni el NIF.
     **@param u Objeto Usuario con la información editada.
     *@return true si la actualización tuvo éxito, false en caso de error.
     */
    public boolean actualizarUsuario(Usuario u);
    
    
    /**
     * Cambia la contraseña del cliente por una nueva.
     **@param id Identificador del cliente.
     *@param nuevaPass Nueva contraseña en texto plano (se encriptará en el proceso).
     *@return true si se cambió correctamente, false en caso contrario.
     */
    public boolean cambiarPassword(int id, String nuevaPass);
}
