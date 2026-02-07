
package es.rebextech.DAO;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author User
 */
public class ConnectionFactory {

    private static final String DATA_SOURCE = "java:comp/env/jdbc/RebexTechDS";

    /**
     * Obtiene una conexión del Pool configurado en el servidor.
     * @return Connection o null si hay error
     */
    public static Connection getConnection() {
        Connection conexion = null;
        try {
            Context contextoInicial = new InitialContext();
            DataSource ds = (DataSource) contextoInicial.lookup(DATA_SOURCE);
            conexion = ds.getConnection();
        } catch (NamingException | SQLException ex) {
            // Es vital loguear el error para depurar si el recurso JNDI falla
            ex.printStackTrace();
        }
        return conexion;
    }

    /**
     * Devuelve la conexión al Pool para que pueda ser reutilizada.
     * @param conexion La conexión a cerrar
     */
    public static void closeConexion(Connection conexion) {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close(); 
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
