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
public class CatalogoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOFactory fabrica = DAOFactory.getDAOFactory();

        // 1. Recoger parámetros
        String texto = request.getParameter("busqueda");
        String[] idsCats = request.getParameterValues("idcategoria");
        String rango = request.getParameter("rangoPrecio");

        // LÓGICA: ¿Es una búsqueda avanzada (trae categoría o precio) o es del Navbar?
        double min = 0, max = 9999;
        if (rango != null && rango.contains("-")) {
            String[] partes = rango.split("-");
            min = Double.parseDouble(partes[0]);
            max = Double.parseDouble(partes[1]);
        }
        List<Producto> resultados;

        // Si el usuario ha tocado categorías, precio o búsqueda avanzada
        if ((idsCats != null && idsCats.length > 0) || (rango != null && !rango.isEmpty()) || (texto != null && !texto.isEmpty())) {
            // Usamos el nuevo método que construimos con el "IN (?,?,?)"
            resultados = fabrica.getProductoDAO().filtroavanzadoCatalogo(texto, idsCats, min, max);
        } else {
            // Si entra al catálogo limpio, le mostramos todo o aleatorios
            resultados = fabrica.getProductoDAO().getProductosAleatorios(12);
        }

        // 3. ENVIAR A LA VISTA
        request.setAttribute("productosLanding", resultados);
        request.getRequestDispatcher("/CATALOGO/catalogo.jsp").forward(request, response);

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
