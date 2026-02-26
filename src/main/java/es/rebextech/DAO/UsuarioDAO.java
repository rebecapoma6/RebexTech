package es.rebextech.DAO;

import es.rebextech.IDAO.IUsuarioDAO;
import es.rebextech.beans.Usuario;
import es.rebextech.utils.Metodos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author User
 */
public class UsuarioDAO implements IUsuarioDAO {

    @Override
    public Usuario login(String email, String password) {
        Usuario user = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Importante: Ciframos la clave que nos da el usuario para compararla con la de la BD
        String passCifrada = Metodos.encriptar(password);

        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ?";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, passCifrada);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = new Usuario();
                // Llenamos el objeto con lo que viene de la BD
                user.setIdusuario(rs.getShort("idusuario"));
                user.setNombre(rs.getString("nombre"));
                user.setNif(rs.getString("nif"));
                user.setApellidos(rs.getString("apellidos"));
                user.setEmail(rs.getString("email"));
                user.setTelefono(rs.getString("telefono"));
                user.setDireccion(rs.getString("direccion"));
                user.setLocalidad(rs.getString("localidad"));
                user.setProvincia(rs.getString("provincia"));
                user.setCodigo_postal(rs.getString("codigo_postal"));
                user.setAvatar(rs.getString("avatar"));
               
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // ¡Usamos tu método utilitario!
            Metodos.cerrarRecursos(con, ps, rs);
        }
        return user;
    }

    @Override
    public int registrarUsuario(Usuario usuario) {

        int idUsuarioGenerado = -1;
        String passCifrada = Metodos.encriptar(usuario.getPassword());
        Connection cx = null;
        PreparedStatement sp = null;
        ResultSet rs = null;
        
        String consultaSql = "INSERT INTO usuarios (nif, nombre, apellidos, email, password, "
                + "direccion, localidad, provincia, codigo_postal, telefono,avatar) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        
        String sqlLastId = "SELECT LAST_INSERT_ID()";
        try {
            cx = ConnectionFactory.getConnection();
            sp = cx.prepareStatement(consultaSql);
            
            sp.setString(1, usuario.getNif());
            sp.setString(2, usuario.getNombre());
            sp.setString(3, usuario.getApellidos());
            sp.setString(4, usuario.getEmail());
            sp.setString(5, passCifrada);
            sp.setString(6, usuario.getDireccion());
            sp.setString(7, usuario.getLocalidad());
            sp.setString(8, usuario.getProvincia());
            sp.setString(9, usuario.getCodigo_postal());
            sp.setString(10, usuario.getTelefono());
            sp.setString(11, usuario.getAvatar());

            int filasAfectadas = sp.executeUpdate();
            if (filasAfectadas > 0) {
                // Ejecutamos la consulta manual para obtener el ID
                sp = cx.prepareStatement(sqlLastId);
                rs = sp.executeQuery();
                if (rs.next()) {
                    idUsuarioGenerado = rs.getInt(1);
                }

            }

        } catch (SQLException error) {
            System.out.println("Error al registrar usuario: " + error.getMessage());

        } finally {
            // Mandamos null en el ResultSet porque ya se cerró arriba con el try-with-resources
            Metodos.cerrarRecursos(cx,sp,rs);
        }
        return idUsuarioGenerado;
    }
    
    
    

    @Override
    public boolean existeEmail(String email) {
        boolean existe = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT idusuario FROM usuarios WHERE email = ?";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                existe = true; // Si hay resultados, el email ya está en uso
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Metodos.cerrarRecursos(con, ps, rs);
        }
        return existe;
    }

    @Override
    public boolean actualizarAvatar(int idUsuario, String nombreArchivo) {
       boolean exito = false;
        Connection con = null;
        PreparedStatement ps = null;
        
        // La consulta para cambiar solo la ruta de la imagen
        String sql = "UPDATE usuarios SET avatar = ? WHERE idusuario = ?";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombreArchivo);
            ps.setInt(2, idUsuario);

            // Si se ha actualizado al menos una fila, devolvemos true
            if (ps.executeUpdate() > 0) {
                exito = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar avatar en DB: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Usamos tu método de utilidades para limpiar
            Metodos.cerrarRecursos(con, ps, null);
        }
        return exito;
    }
    
    
    

    @Override
    public boolean existeNif(String nif) {
        boolean existe = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT idusuario FROM usuarios WHERE nif = ?";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nif);
            rs = ps.executeQuery();

            if (rs.next()) {
                existe = true; // Si hay resultados, el NIF ya está en uso
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Metodos.cerrarRecursos(con, ps, rs);
        }
        return existe;
    }
    
    
    

    @Override
    public void actualizarUltimoAcceso(int idUsuario) {
        Connection con = null;
        PreparedStatement ps = null;
        // La consulta usa NOW() para la fecha y hora actual de MySQL
        String sql = "UPDATE usuarios SET ultimo_acceso = NOW() WHERE idusuario = ?";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Metodos.cerrarRecursos(con, ps, null);
        }
    }

    @Override
    public boolean actualizarUsuario(Usuario u) {
        boolean exito = false;
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "UPDATE usuarios SET nombre=?, apellidos=?, direccion=?, "
               + "localidad=?, provincia=?, codigo_postal=?, telefono=?"
               + "WHERE idusuario=?";

    try {
        con = ConnectionFactory.getConnection();
        ps = con.prepareStatement(sql);
        
        
        ps.setString(1, u.getNombre());
        ps.setString(2, u.getApellidos());
        ps.setString(3, u.getDireccion());
        ps.setString(4, u.getLocalidad());
        ps.setString(5, u.getProvincia());
        ps.setString(6, u.getCodigo_postal());
        ps.setString(7, u.getTelefono());
        ps.setInt(8, u.getIdusuario());

        if (ps.executeUpdate() > 0) {
            exito = true;
        }
    } catch (SQLException e) {
        System.err.println("Error al actualizar usuario: " + e.getMessage());
        e.printStackTrace();
    } finally {
        Metodos.cerrarRecursos(con, ps, null);
    }
    return exito;
    }

    @Override
    public boolean cambiarPassword(int id, String nuevaPass) {
        boolean exito = false;
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "UPDATE usuarios SET password=? WHERE idusuario=?";

        // ¡IMPORTANTE! Cifrar antes de guardar
        String passCifrada = Metodos.encriptar(nuevaPass);

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, passCifrada);
            ps.setInt(2, id);

            if (ps.executeUpdate() > 0) {
                exito = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Metodos.cerrarRecursos(con, ps, null);
        }
        return exito;
    }
}
