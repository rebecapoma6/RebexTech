package es.rebextech.controllers;

import es.rebextech.IDAO.DAOFactory;
import es.rebextech.beans.Producto;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

        String accionSolicitada = request.getParameter("accion");
        HttpSession sesion = request.getSession();
        String urlDestino = "index.jsp";
        DAOFactory fabrica = DAOFactory.getDAOFactory();

        // ============================================================
        // EL TRUCO PARA LA URL LIMPIA (Solo añade estas 4 líneas)
        // ============================================================
        if (accionSolicitada == null && sesion.getAttribute("verCarritoDespues") != null) {
            accionSolicitada = "verCarrito";
            sesion.removeAttribute("verCarritoDespues");
        }
        // ============================================================

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
