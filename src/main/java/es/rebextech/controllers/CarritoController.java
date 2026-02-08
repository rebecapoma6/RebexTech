package es.rebextech.controllers;

import es.rebextech.DAO.ProductoDAO;
import es.rebextech.beans.Producto;
import java.io.IOException;
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

        // 2. GESTIÓN DEL CARRITO 
        // A. Acción: Añadir producto
        if ("agregar".equals(accionCarrito) && idProducto != null) {
            if (sesion.getAttribute("usuarioSesion") == null) {
                if (datosCarrito.isEmpty()) {
                    datosCarrito = idProducto;
                } else {
                    datosCarrito += "-" + idProducto;
                }
            }
        } // B. Acción: Eliminar un producto específico
        else if ("eliminar".equals(accionCarrito) && idProducto != null) {
            String[] ids = datosCarrito.split("-");
            StringBuilder nuevaCadena = new StringBuilder();
            for (String id : ids) {
                if (!id.equals(idProducto)) {
                    if (nuevaCadena.length() > 0) {
                        nuevaCadena.append("-");
                    }
                    nuevaCadena.append(id);
                }
            }
            datosCarrito = nuevaCadena.toString();
        } // C. Acción: Eliminar el carro entero 
        else if ("vaciar".equals(accionCarrito)) {
            datosCarrito = "";
        }

        // 3. ACTUALIZAR COOKIE Y CONTADOR 
        Cookie cookieCarrito = new Cookie("carritoRebex", datosCarrito);
        cookieCarrito.setPath("/");

        if (datosCarrito.isEmpty()) {
            cookieCarrito.setMaxAge(0); // Borramos la cookie del navegador
            sesion.setAttribute("cantidadProductos", 0);
        } else {
            // El carrito perdurará durante dos días
            cookieCarrito.setMaxAge(60 * 60 * 24 * 2);
            int totalItems = datosCarrito.split("-").length;
            sesion.setAttribute("cantidadProductos", totalItems);
        }
        response.addCookie(cookieCarrito);

        // 4. CARGA DE DATOS PARA LA VISTA 
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

        // 5. LÓGICA DE VISUALIZACIÓN
        request.setAttribute("itemsCarritoCookie", datosCarrito);
        request.setAttribute("carritoVacio", datosCarrito.isEmpty());
        request.getRequestDispatcher(urlDestino).forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
