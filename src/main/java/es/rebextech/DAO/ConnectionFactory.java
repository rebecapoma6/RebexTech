package es.rebextech.DAO;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Clase utilitaria (Singleton/Factory) que administra el acceso a la base de
 * datos. Utiliza un Pool de Conexiones (DBCP) configurado mediante JNDI en el
 * archivo context.xml. Esto mejora el rendimiento del servidor al reutilizar
 * conexiones activas.
 *
 * @author Rebeca Poma
 */
public class ConnectionFactory {

    private static final String DATA_SOURCE = "java:comp/env/jdbc/RebexTechDS";

    public static Connection getConnection() {
        Connection conexion = null;
        try {
            Context contextoInicial = new InitialContext();
            DataSource ds = (DataSource) contextoInicial.lookup(DATA_SOURCE);
            conexion = ds.getConnection();
        } catch (NamingException | SQLException ex) {
            // ESTO HARÁ QUE EL ERROR SALGA EN TU PÁGINA SI TIENES UN TRY-CATCH ARRIBA
            throw new RuntimeException("REBEX_ERROR_CONEXION: " + ex.getMessage());
        }
        return conexion;
    }

    /**
     * Devuelve la conexión al Pool para que pueda ser reutilizada.
     *
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
