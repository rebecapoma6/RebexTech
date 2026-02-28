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
            List<Producto> productosAlAzar = fabrica.getProductoDAO().getProductosAleatorios(12);
            request.setAttribute("productosLanding", productosAlAzar);
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
                        // Traemos la lista del DAO filtrada por usuario y estado 'f'
                        List<Pedido> listaHistorial = fabrica.getPedidoDAO().getHistorialPedidos(usuarioLogueado.getIdusuario());               
                        request.setAttribute("historialPedidos", listaHistorial);
                        urlDestino = "/USUARIO/historialPedidos.jsp";
                    } else {
                        // Si no está logueado, lo mandamos al inicio
                        urlDestino = "index.jsp";
                    }
                    break;
                case "verDetallePedido":
                    if (usuarioLogueado != null) {
                        // 1. Atrapamos el ID del pedido que viene del botón
                        int idPed = Integer.parseInt(request.getParameter("id"));
                        
                        // 2. Buscamos todas las líneas (productos) de ese pedido
                        List<LineaPedido> detalleLineas = fabrica.getPedidoDAO().getLineasPorIdPedido(idPed);
                        
                        // 3. Calculamos el total sumando (precio * cantidad) de cada línea
                        double totalFactura = 0;
                        for (LineaPedido lp : detalleLineas) {
                            totalFactura += (lp.getProducto().getPrecio() * lp.getCantidad());
                        }
                        
                        // 4. Mandamos los datos a la vista detallePedido.jsp
                        request.setAttribute("idPedidoGenerado", idPed);
                        request.setAttribute("listaProductos", detalleLineas);
                        request.setAttribute("totalPrecio", totalFactura);                                            
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
