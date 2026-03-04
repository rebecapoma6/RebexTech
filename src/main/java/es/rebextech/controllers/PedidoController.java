package es.rebextech.controllers;
import es.rebextech.IDAO.DAOFactory;
import es.rebextech.beans.LineaPedido;
import es.rebextech.beans.Pedido;
import es.rebextech.beans.Usuario;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet que actúa como controlador especializado para la gestión de pedidos en RebexTech.
 * Implementa la lógica de negocio para el ciclo final de compra (Checkout),
 * la recuperación del historial de pedidos del usuario y la visualización 
 * detallada de comprobantes/facturas electrónicas.
 * Se encarga de transformar el carrito actual en un registro persistente en la 
 * base de datos y de gestionar la transición de datos hacia las vistas de usuario.
 * @author Rebeca Poma
 */
public class PedidoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /***
     * 
     * Gestiona tres acciones principales:
     * <li><b>finalizarCompra:</b> Registra el pedido en la BD, vacía el carrito y genera la factura.</li>
     * <li><b>historialPedidos:</b> Recupera todos los pedidos realizados por el usuario logueado.</li>
     * <li><b>verDetallePedido:</b> Carga las líneas de detalle y la fecha original de un pedido específico.</li>
     * @param request objeto que contiene la petición del cliente
     * @param response objeto que contiene la respuesta del servlet
     * @throws ServletException si ocurre un error específico del servlet
     * @throws IOException si ocurre un error de entrada/salida
     *
     *
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        Usuario usuarioLogueado = (Usuario) sesion.getAttribute("usuarioSesion");
        DAOFactory fabrica = DAOFactory.getDAOFactory();
        String urlDestino = "index.jsp";

        
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = request.getParameter("accionCarrito");
        }

        if (accion != null && usuarioLogueado != null) {
            switch (accion) {

                // 1. LÓGICA DE PAGAR EL CARRITO
                case "finalizarCompra":
                    // Recuperamos lo que el usuario tiene en el carrito antes de borrar
                    List<LineaPedido> carritoFinal = fabrica.getProductoDAO().getProductosCarritoBD(usuarioLogueado.getIdusuario());

                    double totalFinal = 0;
                    for (LineaPedido lp : carritoFinal) {
                        totalFinal += lp.getSubtotal();
                    }

                    //El DAO ahora devuelve un short (el ID del pedido)
                    short idGenerado = fabrica.getPedidoDAO().finalizarPedido(usuarioLogueado.getIdusuario(), carritoFinal, totalFinal);

                    if (idGenerado > 0) {
                        // Pasamos datos a la factura (detallePedido.jsp)
                        request.setAttribute("idPedidoGenerado", idGenerado);
                        request.setAttribute("listaProductos", carritoFinal);
                        request.setAttribute("totalPrecio", totalFinal);
                        request.setAttribute("totalFinal", true);
                        request.setAttribute("fechaHoy", new Date());

                        // Limpiamos carrito de la BD y de la sesión
                        fabrica.getPedidoDAO().vaciarCarritoBD(usuarioLogueado.getIdusuario());
                        sesion.setAttribute("cantidadProductos", 0);

                        // Mostramos la factura directamente
                        request.getRequestDispatcher("/USUARIO/detallePedido.jsp").forward(request, response);
                        return;
                    }
                    break;

                // 2. LÓGICA DE VER TODOS LOS PEDIDOS (HISTORIAL)
                case "historialPedidos":
                    try {
                    List<Pedido> listaHistorial = fabrica.getPedidoDAO().getHistorialPedidos(usuarioLogueado.getIdusuario());
                    request.setAttribute("historialPedidos", listaHistorial);
                } catch (Exception e) {
                    System.out.println("Error al cargar historial: " + e.getMessage());
                }
                urlDestino = "/USUARIO/historialPedidos.jsp";
                break;

                // 3. LÓGICA DE VER EL DETALLE DE UN PEDIDO ANTIGUO
                case "verDetallePedido":
                    try {
                    int idPed = Integer.parseInt(request.getParameter("id"));
                    List<LineaPedido> detalleLineas = fabrica.getPedidoDAO().getLineasPorIdPedido(idPed);

                    double totalFactura = 0;
                    for (LineaPedido lp : detalleLineas) {
                        totalFactura += (lp.getProducto().getPrecio() * lp.getCantidad());
                    }

                    // Buscamos la fecha antigua en el historial
                    List<Pedido> historial = fabrica.getPedidoDAO().getHistorialPedidos(usuarioLogueado.getIdusuario());
                    for (Pedido p : historial) {
                        if (p.getIdpedido() == idPed) {
                            request.setAttribute("fechaPedido", p.getFecha());
                            break;
                        }
                    }

                    request.setAttribute("idPedidoGenerado", idPed);
                    request.setAttribute("listaProductos", detalleLineas);
                    request.setAttribute("totalPrecio", totalFactura);
                } catch (Exception e) {
                    System.out.println("Error al cargar detalle pedido: " + e.getMessage());
                }
                urlDestino = "/USUARIO/detallePedido.jsp";
                break;
            }
        } else {
           
            urlDestino = "index.jsp";
        }


        request.getRequestDispatcher(urlDestino).forward(request, response);

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
