package es.rebextech.controllers;
import es.rebextech.IDAO.DAOFactory;
import es.rebextech.beans.LineaPedido;
import es.rebextech.beans.Pedido;
import es.rebextech.beans.Producto;
import es.rebextech.beans.Usuario;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet principal que actúa como enrutador básico de la aplicación.
 * Se encarga de gestionar la navegación general del usuario, redirigiendo 
 * las peticiones hacia las vistas públicas como el inicio (index) o el catálogo.
 * @author Rebeca
 * 
 */
public class FrontController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accionSolicitada = request.getParameter("accion");
        HttpSession sesion = request.getSession();
        String urlDestino = "index.jsp";
        DAOFactory fabrica = DAOFactory.getDAOFactory();
        Usuario usuarioLogueado = (Usuario) sesion.getAttribute("usuarioSesion");

        // Limpieza de URL  
        if (accionSolicitada == null && sesion.getAttribute("verCarritoDespues") != null) {
            accionSolicitada = "verCarrito";
            sesion.removeAttribute("verCarritoDespues");
        }

        if ("buscar".equals(accionSolicitada)) {
            urlDestino = "CatalogoController";

        } else if (accionSolicitada == null || accionSolicitada.isEmpty() || "inicio".equals(accionSolicitada)) {
       
            try {
                List<Producto> productosAlAzar = fabrica.getProductoDAO().getProductosAleatorios(12);
                request.setAttribute("productosLanding", productosAlAzar);
            } catch (Exception e) {
                System.out.println("Error al cargar productos de inicio: " + e.getMessage());
            }
            urlDestino = "index.jsp";

        } else {
            switch (accionSolicitada) {
                case "verCarrito":
                case "finalizarCompra":
                    urlDestino = "CarritoController";
                    break;
                case "login":
                case "perfil":
                case "salir":
                    urlDestino = "UsuarioController";
                    break;
                case "registro":
                    urlDestino = "RegistroController";
                    break;             
                case "historialPedidos":
                    if (usuarioLogueado != null) {
                        // --- TRY-CATCH PARA HISTORIAL ---
                        try {
                            List<Pedido> listaHistorial = fabrica.getPedidoDAO().getHistorialPedidos(usuarioLogueado.getIdusuario());                
                            request.setAttribute("historialPedidos", listaHistorial);
                        } catch (Exception e) {
                            System.out.println("Error al cargar historial: " + e.getMessage());
                        }
                        urlDestino = "/USUARIO/historialPedidos.jsp";
                    } else {
                        urlDestino = "index.jsp";
                    }
                    break;
                case "verDetallePedido":
                    if (usuarioLogueado != null) {
                        // --- TRY-CATCH PARA DETALLE ---
                        try {
                            int idPed = Integer.parseInt(request.getParameter("id"));
                            List<LineaPedido> detalleLineas = fabrica.getPedidoDAO().getLineasPorIdPedido(idPed);
                            
                            double totalFactura = 0;
                            for (LineaPedido lp : detalleLineas) {
                                totalFactura += (lp.getProducto().getPrecio() * lp.getCantidad());
                            }
                            
                            request.setAttribute("idPedidoGenerado", idPed);
                            request.setAttribute("listaProductos", detalleLineas);
                            request.setAttribute("totalPrecio", totalFactura);                                             
                        } catch (Exception e) {
                            System.out.println("Error al cargar detalle pedido: " + e.getMessage());
                        }
                        urlDestino = "/USUARIO/detallePedido.jsp";
                    } else {
                        urlDestino = "index.jsp";
                    }
                    break;
                    
                default:
                    urlDestino = "index.jsp";
                    break;
            }
        }

        // 4. Redirección final
        request.getRequestDispatcher(urlDestino).forward(request, response);

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
