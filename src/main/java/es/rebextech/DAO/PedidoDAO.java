package es.rebextech.DAO;
import es.rebextech.IDAO.IPedidoDAO;
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
 * Implementación concreta en MySQL de la interfaz IPedidoDAO.
 * Utiliza transacciones manuales (commit/rollback) para garantizar la integridad 
 * de los datos al momento de transformar un carrito en una factura finalizada.
 * @author Rebeca
 */
public class PedidoDAO implements IPedidoDAO {

    @Override
    public short finalizarPedido(int idUsuario, List<LineaPedido> carrito, double totalImporte) {
        Connection con = null;
        PreparedStatement psPedido = null;
        PreparedStatement psLinea = null;
        ResultSet rs = null;
        short idPedidoGenerado = 0;

        // Calculamos el IVA basándonos en tu 21%
        double calculadoIva = totalImporte * 0.21;
        double importeSinIva = totalImporte - calculadoIva;

        // SQL para el Pedido
        String sqlPedido = "INSERT INTO pedidos (idusuario, fecha, importe, iva, estado) VALUES (?, NOW(), ?, ?, ?)";

        // SQL PARA LAS LÍNEAS (idpedido, idproducto, cantidad)
        String sqlLinea = "INSERT INTO lineaspedidos (idpedido, idproducto, cantidad) VALUES (?, ?, ?)";

        String sqlLastId = "SELECT LAST_INSERT_ID()";

        try {
            con = ConnectionFactory.getConnection();
            con.setAutoCommit(false); // Iniciar Transacción

            // 1. Insertar la cabecera del Pedido
            psPedido = con.prepareStatement(sqlPedido);
            psPedido.setInt(1, idUsuario);
            psPedido.setDouble(2, importeSinIva);
            psPedido.setDouble(3, calculadoIva);
            psPedido.setString(4, String.valueOf(Pedido.ESTADO_FINALIZADO));

            int filasPedido = psPedido.executeUpdate();

            if (filasPedido > 0) {
                psPedido = con.prepareStatement(sqlLastId);
                rs = psPedido.executeQuery();

                if (rs.next()) {
                    idPedidoGenerado = rs.getShort(1);

                    // 2. Insertar las líneas del pedido
                    psLinea = con.prepareStatement(sqlLinea);
                    for (LineaPedido lp : carrito) {
                        psLinea.setShort(1, idPedidoGenerado);
                        psLinea.setInt(2, lp.getProducto().getIdproducto());
                        psLinea.setInt(3, lp.getCantidad());                     
                        psLinea.addBatch();
                    }

                    psLinea.executeBatch();
                    con.commit();

                }
            }

        } catch (SQLException e) {
            System.err.println("Error al finalizar compra: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            idPedidoGenerado = 0;
        } finally {
            Metodos.cerrarRecursos(null, psLinea, null);
            Metodos.cerrarRecursos(con, psPedido, rs);
        }

        return idPedidoGenerado;
    }

    @Override
    public void migrarCarritoDeCookieABaseDeDatos(int idDelUsuario, String contenidoDeLaCookie) {

        Connection conexionABaseDeDatos = null;
        PreparedStatement psBuscar = null;
        PreparedStatement psCrear = null;
        PreparedStatement psInsertarLinea = null;
        PreparedStatement psBuscarLinea = null; // Para chequear si el producto ya está
        PreparedStatement psActualizarLinea = null; // Para sumar cantidad
        ResultSet rs = null;
        int idPedidoCarrito = 0;

        if (contenidoDeLaCookie == null || contenidoDeLaCookie.isEmpty()) {
            return;
        }

        try {
            conexionABaseDeDatos = ConnectionFactory.getConnection();
            // ¡MAGIA 1! Iniciamos la transacción manualmente
            conexionABaseDeDatos.setAutoCommit(false);

            // Buscamos carrito con estado 'c' usando tu constante segura
            String sqlBuscar = "SELECT idpedido FROM pedidos WHERE idusuario = ? AND estado = '" + Pedido.ESTADO_CARRITO + "'";
            psBuscar = conexionABaseDeDatos.prepareStatement(sqlBuscar);
            psBuscar.setInt(1, idDelUsuario);
            rs = psBuscar.executeQuery();

            if (rs.next()) {
                idPedidoCarrito = rs.getInt("idpedido");
            } else {
                // Creamos el pedido usando la constante
                String sqlCrear = "INSERT INTO pedidos (fecha, estado, idusuario, importe, iva) VALUES (NOW(), '" + Pedido.ESTADO_CARRITO + "', ?, 0, 0)";
                psCrear = conexionABaseDeDatos.prepareStatement(sqlCrear);
                psCrear.setInt(1, idDelUsuario);
                psCrear.executeUpdate();

                try (PreparedStatement psLastId = conexionABaseDeDatos.prepareStatement("SELECT LAST_INSERT_ID()"); ResultSet rsNuevo = psLastId.executeQuery()) {
                    if (rsNuevo.next()) {
                        idPedidoCarrito = rsNuevo.getInt(1);
                    }
                }
            }

            if (idPedidoCarrito > 0) {
                String[] idProductos = contenidoDeLaCookie.split("-");

                // SQLs para la lógica de duplicados
                String sqlCheck = "SELECT cantidad FROM lineaspedidos WHERE idpedido = ? AND idproducto = ?";
                String sqlIns = "INSERT INTO lineaspedidos (idpedido, idproducto, cantidad) VALUES (?, ?, 1)";
                String sqlUpd = "UPDATE lineaspedidos SET cantidad = cantidad + 1 WHERE idpedido = ? AND idproducto = ?";

                psBuscarLinea = conexionABaseDeDatos.prepareStatement(sqlCheck);
                psInsertarLinea = conexionABaseDeDatos.prepareStatement(sqlIns);
                psActualizarLinea = conexionABaseDeDatos.prepareStatement(sqlUpd);

                for (String idString : idProductos) {
                    if (!idString.trim().isEmpty()) {
                        int idProd = Integer.parseInt(idString);

                        // Chequeamos si ya existe
                        psBuscarLinea.setInt(1, idPedidoCarrito);
                        psBuscarLinea.setInt(2, idProd);
                        try (ResultSet rsCheck = psBuscarLinea.executeQuery()) {
                            if (rsCheck.next()) {
                                // Si existe, sumamos
                                psActualizarLinea.setInt(1, idPedidoCarrito);
                                psActualizarLinea.setInt(2, idProd);
                                psActualizarLinea.executeUpdate();
                            } else {
                                // Si no existe, insertamos
                                psInsertarLinea.setInt(1, idPedidoCarrito);
                                psInsertarLinea.setInt(2, idProd);
                                psInsertarLinea.executeUpdate();
                            }
                        }
                    }
                }
            }
            // ¡MAGIA 2! Si todo salió de lujo, guardamos los cambios en MySQL
            conexionABaseDeDatos.commit();

        } catch (SQLException e) {
            System.out.println("Ocurrió un error al intentar migrar el carrito: " + e.getMessage());
            e.printStackTrace();
            // ¡MAGIA 3! Si algo falló, deshacemos todo
            if (conexionABaseDeDatos != null) {
                try {
                    conexionABaseDeDatos.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // Cerramos todo con tus métodos
            Metodos.cerrarRecursos(null, psBuscarLinea, null);
            Metodos.cerrarRecursos(null, psActualizarLinea, null);
            Metodos.cerrarRecursos(null, psInsertarLinea, null);
            Metodos.cerrarRecursos(null, psCrear, null);
            Metodos.cerrarRecursos(conexionABaseDeDatos, psBuscar, rs);
        }

    }

    @Override
    public void agregarUnProductoAlCarritoBD(int idDelUsuario, int idDelProducto) {
        Connection con = null;
        PreparedStatement psBuscarPed = null;
        PreparedStatement psBuscarLinea = null;
        PreparedStatement psAccion = null;
        ResultSet rsPed = null;
        ResultSet rsLinea = null;
        int idPedido = 0;

        try {
            con = ConnectionFactory.getConnection();
            con.setAutoCommit(false);

            // 1. Buscar o crear el pedido en estado 'c'
            String sqlPed = "SELECT idpedido FROM pedidos WHERE idusuario = ? AND estado = '" + Pedido.ESTADO_CARRITO + "'";
            psBuscarPed = con.prepareStatement(sqlPed);
            psBuscarPed.setInt(1, idDelUsuario);
            rsPed = psBuscarPed.executeQuery();

            if (rsPed.next()) {
                idPedido = rsPed.getInt("idpedido");
            } else {
                String sqlCrear = "INSERT INTO pedidos (fecha, estado, idusuario, importe, iva) VALUES (NOW(), '" + Pedido.ESTADO_CARRITO + "', ?, 0, 0)";
                psAccion = con.prepareStatement(sqlCrear);
                psAccion.setInt(1, idDelUsuario);
                psAccion.executeUpdate();

                try (PreparedStatement psLastId = con.prepareStatement("SELECT LAST_INSERT_ID()"); ResultSet rsLastId = psLastId.executeQuery()) {
                    if (rsLastId.next()) {
                        idPedido = rsLastId.getInt(1);
                    }
                }

                Metodos.cerrarRecursos(null, psAccion, null);
            }

            // 2. ¿Ya existe ese producto en ese pedido?
            String sqlCheck = "SELECT cantidad FROM lineaspedidos WHERE idpedido = ? AND idproducto = ?";
            psBuscarLinea = con.prepareStatement(sqlCheck);
            psBuscarLinea.setInt(1, idPedido);
            psBuscarLinea.setInt(2, idDelProducto);
            rsLinea = psBuscarLinea.executeQuery();

            if (rsLinea.next()) {
                // ¡EXISTE! Update cantidad + 1
                String sqlUpd = "UPDATE lineaspedidos SET cantidad = cantidad + 1 WHERE idpedido = ? AND idproducto = ?";
                psAccion = con.prepareStatement(sqlUpd);
                psAccion.setInt(1, idPedido);
                psAccion.setInt(2, idDelProducto);
            } else {
                // ¡NO EXISTE! Insert nuevo
                String sqlIns = "INSERT INTO lineaspedidos (idpedido, idproducto, cantidad) VALUES (?, ?, 1)";
                psAccion = con.prepareStatement(sqlIns);
                psAccion.setInt(1, idPedido);
                psAccion.setInt(2, idDelProducto);
            }
            psAccion.executeUpdate();

            con.commit();
        } catch (SQLException e) {
            if (con != null) try {
                con.rollback();
            } catch (SQLException ex) {
            }
            e.printStackTrace();
        } finally {
            Metodos.cerrarRecursos(null, psBuscarLinea, rsLinea);
            Metodos.cerrarRecursos(con, psBuscarPed, rsPed);
            Metodos.cerrarRecursos(null, psAccion, null);
        }

    }

    @Override
    public int contarProductosCarrito(int idUsuario) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int cantidadTotal = 0;

        // Esta consulta es clave: une las tablas pedidos y lineaspedidos
        // Solo cuenta los productos del pedido que todavía es un carrito (estado 'c')
        String sql = "SELECT SUM(lp.cantidad) AS total FROM lineaspedidos lp "
                + "JOIN pedidos p ON lp.idpedido = p.idpedido "
                + "WHERE p.idusuario = ? AND p.estado = 'c'";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                // Si el resultado es nulo (carrito vacío), getInt devuelve 0 automáticamente
                cantidadTotal = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error en contarProductosCarrito: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Usamos tu método de Metodos para cerrar todo limpio
            Metodos.cerrarRecursos(con, ps, rs);
        }

        return cantidadTotal;
    }

    @Override
    public void eliminarProductoDelCarritoBD(int idUsuario, int idProducto) {
        Connection con = null;
        PreparedStatement ps = null;
        // Borramos la línea del producto que pertenece al pedido 'c' (carrito) de ese usuario
        String sql = "DELETE lp FROM lineaspedidos lp "
                + "JOIN pedidos p ON lp.idpedido = p.idpedido "
                + "WHERE p.idusuario = ? AND lp.idproducto = ? AND p.estado = 'c'";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idProducto);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Metodos.cerrarRecursos(con, ps, null);
        }
    }

    @Override
    public void vaciarCarritoBD(int idUsuario) {
        Connection con = null;
        PreparedStatement ps = null;
        // Borramos todas las líneas del pedido 'c' de ese usuario
        String sql = "DELETE lp FROM lineaspedidos lp "
                + "JOIN pedidos p ON lp.idpedido = p.idpedido "
                + "WHERE p.idusuario = ? AND p.estado = 'c'";
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
    public List<Pedido> getHistorialPedidos(int idUsuario) {
        List<Pedido> lista = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Filtramos estrictamente por estado 'f'
        String sql = "SELECT idpedido, fecha, importe, iva FROM pedidos "
                + "WHERE idusuario = ? AND estado = '" + Pedido.ESTADO_FINALIZADO + "' "
                + "ORDER BY fecha DESC";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            while (rs.next()) {
                Pedido p = new Pedido();
                p.setIdpedido(rs.getShort("idpedido"));
                p.setFecha(rs.getTimestamp("fecha")); // Traemos el objeto Date
                p.setImporte(rs.getDouble("importe"));
                p.setIva(rs.getDouble("iva"));
                p.setEstado(Pedido.ESTADO_FINALIZADO);
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Metodos.cerrarRecursos(con, ps, rs);
        }
        return lista;
    }

    @Override
    public void actualizarCantidadProductoBD(int idUsuario, int idProducto, int nuevaCantidad) {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "UPDATE lineaspedidos lp "
                + "JOIN pedidos p ON lp.idpedido = p.idpedido "
                + "SET lp.cantidad = ? "
                + "WHERE p.idusuario = ? AND lp.idproducto = ? AND p.estado = '" + Pedido.ESTADO_CARRITO + "'";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, idUsuario);
            ps.setInt(3, idProducto);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Metodos.cerrarRecursos(con, ps, null);
        }
    }

    @Override
    public List<LineaPedido> getLineasPorIdPedido(int idPedido) {
        List<LineaPedido> lista = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Unimos con productos para sacar el nombre, imagen y precio
        String sql = "SELECT lp.*, p.nombre, p.imagen, p.precio "
                + "FROM lineaspedidos lp "
                + "JOIN productos p ON lp.idproducto = p.idproducto "
                + "WHERE lp.idpedido = ?";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPedido);
            rs = ps.executeQuery();
            while (rs.next()) {
                LineaPedido lp = new LineaPedido();
                lp.setCantidad(rs.getByte("cantidad"));

                Producto p = new Producto();
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                String imgBD = rs.getString("imagen");
                if (imgBD != null && !imgBD.toLowerCase().endsWith(".jpg")) {
                    imgBD += ".jpg";
                }
                p.setImagen(imgBD); 

                lp.setProducto(p);
                lista.add(lp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Metodos.cerrarRecursos(con, ps, rs);
        }
        return lista;
    }

}
