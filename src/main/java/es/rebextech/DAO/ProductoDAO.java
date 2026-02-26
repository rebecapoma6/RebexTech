package es.rebextech.DAO;

import es.rebextech.IDAO.IProductoDAO;
import es.rebextech.beans.LineaPedido;
import es.rebextech.beans.Pedido;
import es.rebextech.beans.Producto;
import es.rebextech.utils.Metodos;
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
    public List<Producto> getProductosCarrito(String[] ids) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
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

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql.toString());
            rs = ps.executeQuery();
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
        return lista;
    }

    @Override
    public List<Producto> getProductosAleatorios(int cantidad) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Producto> lista = new ArrayList<>();
        // Usamos ORDER BY RAND() para la aleatoriedad que buscas
        String sql = "SELECT * FROM productos ORDER BY RAND() LIMIT ?";

        try {

            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, cantidad);
            rs = ps.executeQuery();

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdproducto(rs.getShort("idproducto"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setMarca(rs.getString("marca"));
                p.setDescripcion(rs.getString("descripcion"));
                String imgBD = rs.getString("imagen");
                // Si la ruta no termina en .jpg, se lo concatenamos aquí
                if (imgBD != null && !imgBD.toLowerCase().endsWith(".jpg")) {
                    imgBD += ".jpg";
                }

                p.setImagen(imgBD); // Ahora p.imagen vale: "graficas/14259388993036489.jpg"
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cierre manual consistente con el resto del DAO
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
        return lista;
    }

    @Override
    public List<Producto> busquedalibreNavabar(String textoBusqueda) {
        Connection cnx = null;
        PreparedStatement ps = null;
        ResultSet resultado = null;
        List<Producto> listaProductos = new ArrayList<>();

        String filtro = "%" + textoBusqueda + "%";
        String sql = "SELECT * FROM productos WHERE nombre LIKE ? OR marca LIKE ? ";
        // Buscamos en nombre, marca o descripción.

        try {
            cnx = ConnectionFactory.getConnection();
            ps = cnx.prepareStatement(sql);
            ps.setString(1, filtro);
            ps.setString(2, filtro);

            resultado = ps.executeQuery();
            while (resultado.next()) {
                Producto producto = new Producto();
                producto.setIdproducto(resultado.getShort("idproducto"));
                producto.setNombre(resultado.getString("nombre"));
                producto.setPrecio(resultado.getDouble("precio"));
                producto.setMarca(resultado.getString("marca"));
                producto.setDescripcion(resultado.getString("descripcion"));
                String imagenBD = resultado.getString("imagen");
                if (imagenBD != null && !imagenBD.toLowerCase().endsWith(".jpg")) {
                    imagenBD += ".jpg";
                }
                producto.setImagen(imagenBD);

                listaProductos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Metodos.cerrarRecursos(cnx, ps, resultado);
        }
        return listaProductos;

    }

    @Override
    public List<Producto> filtroavanzadoCatalogo(String texto, String[] idsCategorias, double precioMin, double precioMax) {
        Connection conexion = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;
        List<Producto> listaProductos = new ArrayList<>();

        // 1. Empezamos con la base
        StringBuilder sql = new StringBuilder("SELECT * FROM productos WHERE precio BETWEEN ? AND ?");

        // 2. Filtro de Texto (Nombre o Marca)
        if (texto != null && !texto.isEmpty()) {
            sql.append(" AND (nombre LIKE ? OR marca LIKE ?)");
        }

        // 3. Filtro Múltiple de Categorías (La clave del éxito)
        // Si el array trae datos, construimos: AND idcategoria IN (?,?,?)
        if (idsCategorias != null && idsCategorias.length > 0) {
            sql.append(" AND idcategoria IN (");
            for (int i = 0; i < idsCategorias.length; i++) {
                sql.append("?"); // Ponemos un interrogante por cada categoría
                if (i < idsCategorias.length - 1) {
                    sql.append(",");
                }
            }
            sql.append(")");
        }

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql.toString());

            int indice = 1;
            // Seteamos precios siempre (son fijos)
            preparada.setDouble(indice++, precioMin);
            preparada.setDouble(indice++, precioMax);

            // Seteamos texto si existe
            if (texto != null && !texto.isEmpty()) {
                String filtro = "%" + texto + "%";
                preparada.setString(indice++, filtro);
                preparada.setString(indice++, filtro);
            }

            // Seteamos las categorías del array dinámicamente
            if (idsCategorias != null && idsCategorias.length > 0) {
                for (String id : idsCategorias) {
                    preparada.setInt(indice++, Integer.parseInt(id));
                }
            }

            resultado = preparada.executeQuery();
            while (resultado.next()) {
                Producto producto = new Producto();
                producto.setIdproducto(resultado.getShort("idproducto"));
                producto.setNombre(resultado.getString("nombre"));
                producto.setPrecio(resultado.getDouble("precio"));
                producto.setMarca(resultado.getString("marca"));
                producto.setDescripcion(resultado.getString("descripcion"));
                String imagenBD = resultado.getString("imagen");
                if (imagenBD != null && !imagenBD.toLowerCase().endsWith(".jpg")) {
                    imagenBD += ".jpg";
                }
                producto.setImagen(imagenBD);

                listaProductos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Metodos.cerrarRecursos(conexion, preparada, resultado);
        }
        return listaProductos;
    }

    @Override
    public List<LineaPedido> getProductosCarritoBD(short idUsuario) {
        Connection conexion = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;
        List<LineaPedido> listaCarrito = new ArrayList<>();

        // Usamos concatenación simple para la constante porque es estática y segura
        // O mejor aún, la comparamos en el WHERE para asegurar que solo traiga lo que está en carrito
        String sql = "SELECT p.*, lp.cantidad, lp.idlinea, lp.idpedido "
                + "FROM productos p "
                + "JOIN lineaspedidos lp ON p.idproducto = lp.idproducto "
                + "JOIN pedidos ped ON lp.idpedido = ped.idpedido "
                + "WHERE ped.idusuario = ? AND ped.estado = '" + Pedido.ESTADO_CARRITO + "'";

        try {
            conexion = ConnectionFactory.getConnection();
            preparada = conexion.prepareStatement(sql);
            preparada.setShort(1, idUsuario);
            resultado = preparada.executeQuery();

            while (resultado.next()) {
                // 1. Creamos el objeto Producto (lo que Micaela ve)
                Producto p = new Producto();
                p.setIdproducto(resultado.getShort("idproducto"));
                p.setNombre(resultado.getString("nombre"));
                p.setPrecio(resultado.getDouble("precio"));

                // Tratamiento de imagen que ya tenías
                String imagenBD = resultado.getString("imagen");
                if (imagenBD != null && !imagenBD.toLowerCase().endsWith(".jpg")) {
                    imagenBD += ".jpg";
                }
                p.setImagen(imagenBD);

                // 2. Creamos la LineaPedido (la estructura del carrito)
                LineaPedido lineaPd = new LineaPedido();
                lineaPd.setIdlinea(resultado.getShort("idlinea"));
                lineaPd.setIdpedido(resultado.getShort("idpedido"));
                lineaPd.setCantidad(resultado.getByte("cantidad"));

                // ¡La magia de la POO!
                lineaPd.setProducto(p);

                listaCarrito.add(lineaPd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Metodos.cerrarRecursos(conexion, preparada, resultado);
        }
        return listaCarrito;
    }

}
