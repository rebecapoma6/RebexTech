package es.rebextech.controllers;

import es.rebextech.IDAO.DAOFactory;
import es.rebextech.beans.Producto;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author User
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
        String urlDestino = "index.jsp";
        String accionSolicitada = request.getParameter("accion");

        DAOFactory fabrica = DAOFactory.getDAOFactory();

        if ("buscar".equals(accionSolicitada)) {
            
            // Aquí llamarías a un método de búsqueda en tu DAO
            // List<Producto> resultados = fabrica.getProductoDAO().buscar(textoBusqueda);
            // request.setAttribute("productosLanding", resultados);
            urlDestino = "CatalogoController"; // O la página donde muestres resultados

            // 2. CASO LANDING (Si no hay acción o es "inicio")
        } else if (accionSolicitada == null || accionSolicitada.isEmpty() || "inicio".equals(accionSolicitada)) {

            // Pedimos los 12 productos aleatorios para la portada
            List<Producto> productosAlAzar = fabrica.getProductoDAO().getProductosAleatorios(12);

            // Los guardamos en el REQUEST (para que cambien con cada F5)
            request.setAttribute("productosLanding", productosAlAzar);
            urlDestino = "index.jsp";

        } else {
            // 3. Control de navegación para el resto de la tienda
            switch (accionSolicitada) {
                case "verCarrito":
                    urlDestino = "CarritoController";
                    break;
                case "login":
                case "salir":
                    urlDestino = "UsuarioController";
                    break;
                case "registro":
                    urlDestino = "RegistroController";
                    break;
//                case "salir":
//                    request.getSession().invalidate();
//                    urlDestino = "index.jsp";
//                    break;
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
