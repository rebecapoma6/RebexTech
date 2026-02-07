package es.rebextech.controllers;

import es.rebextech.DAO.ProductoDAO;
import es.rebextech.beans.Producto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author User
 */
public class CarritoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession();
        String accionCarrito = request.getParameter("accionCarrito");
        String idProducto = request.getParameter("idProducto");
        String urlDestino = "/CARRITO/carrito.jsp";

        // 1. RECUPERAR DATOS ACTUALES DE LA COOKIE
        String datosCarrito = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("carritoRebex".equals(c.getName())) {
                    datosCarrito = c.getValue();
                    break;
                }
            }
        }

        // 2. LÓGICA DE AGREGAR
        if ("agregar".equals(accionCarrito) && idProducto != null) {
            if (sesion.getAttribute("usuarioSesion") == null) {
                // Actualizamos la cadena local antes de guardarla
                if (datosCarrito.isEmpty()) {
                    datosCarrito = idProducto;
                } else {
                    datosCarrito += "-" + idProducto;
                }

                Cookie cookieCarrito = new Cookie("carritoRebex", datosCarrito);
                cookieCarrito.setMaxAge(60 * 60 * 24 * 7);
                cookieCarrito.setPath("/");
                response.addCookie(cookieCarrito);

                int totalItems = datosCarrito.split("-").length;
                sesion.setAttribute("cantidadProductos", totalItems);
            }
            // Nota: Aquí podrías añadir la lógica para usuarios logueados (BD directa)
        }

        // 3. CARGA DE DATOS PARA LA VISTA (Independientemente de si agregamos o solo miramos)
        if (!datosCarrito.isEmpty()) {
            String[] ids = datosCarrito.split("-");
            ProductoDAO pDAO = new ProductoDAO();

            List<Producto> listaProductos = pDAO.getProductosCarrito(ids);

            double total = 0;
            for (Producto p : listaProductos) {
                total += p.getPrecio();
            }

            request.setAttribute("listaProductos", listaProductos);
            request.setAttribute("totalPrecio", total);
        }

        // 4. LÓGICA DE VISUALIZACIÓN
        boolean vacio = datosCarrito.isEmpty() && sesion.getAttribute("carritoSesion") == null;
        request.setAttribute("itemsCarritoCookie", datosCarrito);
        request.setAttribute("carritoVacio", vacio);

        request.getRequestDispatcher(urlDestino).forward(request, response);

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
