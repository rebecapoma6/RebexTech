package es.rebextech.DAO;

import es.rebextech.IDAO.IProductoDAO;
import es.rebextech.beans.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class ProductoDAO implements IProductoDAO {

    @Override
    public List<Producto> getProductos() {
        return null;
    }

    @Override
    public List<Producto> getProductosCarrito(String[] ids) {
        List<Producto> lista = new ArrayList<>();
        if (ids == null || ids.length == 0) {
            return lista;
        }

        // Creamos una consulta dinámica: SELECT * FROM productos WHERE idproducto IN (14, 36, 25)
        StringBuilder sql = new StringBuilder("SELECT * FROM productos WHERE idproducto IN (");
        for (int i = 0; i < ids.length; i++) {
            sql.append(ids[i]).append(i < ids.length - 1 ? "," : "");
        }
        sql.append(")");

        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString()); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdproducto(rs.getShort("idproducto"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));

                String imgBD = rs.getString("imagen");
                if (imgBD != null && !imgBD.toLowerCase().endsWith(".jpg")) {
                    imgBD += ".jpg";
                }
                p.setImagen(imgBD);
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Producto> getProductosAleatorios(int cantidad) {
        List<Producto> lista = new ArrayList<>();
        // Usamos ORDER BY RAND() para la aleatoriedad que buscas
        String sql = "SELECT * FROM productos ORDER BY RAND() LIMIT ?";

        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cantidad);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setIdproducto(rs.getShort("idproducto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setMarca(rs.getString("marca"));

                    String imgBD = rs.getString("imagen");
                    // Si la ruta no termina en .jpg, se lo concatenamos aquí
                    if (imgBD != null && !imgBD.toLowerCase().endsWith(".jpg")) {
                        imgBD += ".jpg";
                    }

                    p.setImagen(imgBD); // Ahora p.imagen vale: "graficas/14259388993036489.jpg"
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
