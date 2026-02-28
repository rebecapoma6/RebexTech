package es.rebextech.DAO;

import es.rebextech.IDAO.ICategoriaDAO;
import es.rebextech.beans.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación concreta en MySQL de la interfaz ICategoriaDAO. Extrae los
 * registros de la tabla 'categorias' para ser inyectados en el contexto global
 * de la aplicación.
 *
 * @author Rebeca
 *
 */
public class CategoriaDAO implements ICategoriaDAO {

    @Override
    public List<Categoria> listarTodas() {
        List<Categoria> listaCategorias = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement sentenciaPreparada = null;
        ResultSet resultado = null;

        String consultaSql = "SELECT idcategoria, nombre FROM categorias ORDER BY nombre ASC";

        try {
            // 1. Obtenemos conexión del Pool (ConnectionFactory)
            conexion = ConnectionFactory.getConnection();

            // 2. Preparamos y ejecutamos
            sentenciaPreparada = conexion.prepareStatement(consultaSql);
            resultado = sentenciaPreparada.executeQuery();

            // 3. Recorremos el ResultSet y llenamos la lista
            while (resultado.next()) {
                Categoria cat = new Categoria();
                cat.setIdcategoria(resultado.getByte("idcategoria"));
                cat.setNombre(resultado.getString("nombre"));

                listaCategorias.add(cat);
            }

        } catch (SQLException error) {
            // Imprimimos el error para depurar si falla la DB
            error.printStackTrace();
        } finally {
            // 4. CERRAMOS TODO MANUALMENTE (Lo que garantiza que no se queden hilos abiertos)
            // Usamos tu método de la fábrica para la conexión
            ConnectionFactory.closeConexion(conexion);

            // Cerramos ResultSet y Statement
            try {
                if (resultado != null) {
                    resultado.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (sentenciaPreparada != null) {
                    sentenciaPreparada.close();
                }
            } catch (SQLException e) {
            }
        }

        return listaCategorias;
    }

}
