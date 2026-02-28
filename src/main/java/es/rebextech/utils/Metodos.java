package es.rebextech.utils;
import es.rebextech.DAO.ConnectionFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Clase de utilidades que agrupa herramientas genéricas del sistema.
 * Proporciona métodos estáticos (accesibles desde cualquier parte sin instanciar)
 * para realizar tareas comunes y repetitivas, tales como la encriptación 
 * de contraseñas mediante el algoritmo MD5 y el cierre seguro de los recursos 
 * de la base de datos (Connection, PreparedStatement, ResultSet).
 * @author Rebeca Poma
 * 
 */
public class Metodos {

    public static String encriptar(String cadena) {
        if (cadena == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(cadena.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // En caso de error, devolvemos null para no comprometer la seguridad
            return null;
        }
    }

    public static String capitalizar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return texto;
        }

        String[] palabras = texto.trim().split("\\s+");
        StringBuilder resultado = new StringBuilder();

        for (String palabra : palabras) {
            if (palabra.length() > 0) {
                resultado.append(palabra.substring(0, 1).toUpperCase())
                        .append(palabra.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return resultado.toString().trim();
    }

    public static void configurarAlerta(HttpSession session, String mensajeAlerta, String tipoAlerta) {
        session.setAttribute("alerta", mensajeAlerta);
        session.setAttribute("tipoAlerta", tipoAlerta);
    }

    public static void eliminarCookieCarrito(HttpServletResponse response) {
        Cookie cookieBorrar = new Cookie("carritoRebex", "");
        cookieBorrar.setPath("/");
        cookieBorrar.setMaxAge(0);
        response.addCookie(cookieBorrar);
    }

    public static boolean esCPValido(String cp) {
        // Explicación: 5 dígitos, los dos primeros del 01 al 52
        return cp != null && cp.matches("^(0[1-9]|[1-4][0-9]|5[0-2])[0-9]{3}$");
    }

    public static boolean esTelefonoValido(String tlf) {
        // Explicación: 9 dígitos, empieza por 6, 7, 8 o 9
        return tlf != null && tlf.matches("^[6789][0-9]{8}$");
    }

    public static boolean esCampoVacio(String valorCampo) {
        return (valorCampo == null || valorCampo.trim().isEmpty());
    }

    public static void cerrarRecursos(Connection con, PreparedStatement ps, ResultSet rs) {
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
}
