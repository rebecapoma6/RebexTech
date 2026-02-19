package es.rebextech.DAO;

import es.rebextech.IDAO.IUsuarioDAO;
import es.rebextech.beans.Usuario;
import es.rebextech.utils.Metodos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
                user.setApellidos(rs.getString("apellidos"));
                user.setEmail(rs.getString("email"));
                user.setTelefono(rs.getString("telefono"));
                user.setDireccion(rs.getString("direccion"));
                user.setLocalidad(rs.getString("localidad"));
                user.setProvincia(rs.getString("provincia"));
                user.setAvatar(rs.getString("avatar"));
                // ... añade los campos que necesites mostrar en el perfil ...
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
            }
            ConnectionFactory.closeConexion(con);
        }
        return user;
    }

    @Override
    public int registrarUsuario(Usuario usuario) {

        int idUsuarioGenerado = -1;
        String passCifrada = Metodos.encriptar(usuario.getPassword());
        Connection cx = null;
        PreparedStatement sp = null;

        String consultaSql = "INSERT INTO usuarios (nif, nombre, apellidos, email, password, "
                + "direccion, localidad, provincia, codigo_postal, telefono,avatar) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        try {
            cx = ConnectionFactory.getConnection();
            sp = cx.prepareStatement(consultaSql, Statement.RETURN_GENERATED_KEYS);
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
                // Recuperamos la clave generada automáticamente
                try (ResultSet resultadoId = sp.getGeneratedKeys()) {
                    if (resultadoId.next()) {
                        idUsuarioGenerado = resultadoId.getInt(1);
                    }
                }
            }

        } catch (SQLException error) {
            System.out.println("Error al registrar usuario: " + error.getMessage());

        } finally {
            // Cerramos la conexión llamando a tu fábrica para liberar el hilo
            ConnectionFactory.closeConexion(cx);
            // Cerramos la sentencia manualmente para mayor seguridad
            try {
                if (sp != null) {
                    sp.close();
                }
            } catch (SQLException e) {
            }
        }
        return idUsuarioGenerado;

    }

    @Override
    public boolean existeEmail(String email) {
        // Usado para validaciones antes de registrar
        return false;
    }

    @Override
    public boolean actualizarAvatar(int idUsuario, String nombreArchivo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean existeNif(String nif) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
