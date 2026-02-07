package es.rebextech.controllers;

import java.io.IOException;
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

        // 2. LÓGICA DE AGREGAR (Si accionCarrito == "agregar")
        if ("agregar".equals(accionCarrito) && idProducto != null) {
            if (sesion.getAttribute("usuarioSesion") == null) {
                // USUARIO ANÓNIMO: Guardamos en Cookie (PDF)
                Cookie cookieCarrito = new Cookie("carritoRebex", idProducto);
                cookieCarrito.setMaxAge(60 * 60 * 24 * 7); // 1 semana
                cookieCarrito.setPath("/");
                response.addCookie(cookieCarrito);
            } else {
                // USUARIO REGISTRADO: Aquí irá tu lógica de sesión más adelante
            }
        }

        // 3. LÓGICA DE VISUALIZACIÓN
        // Comprobamos si hay algo para mostrar "Carrito Vacío" correctamente
        boolean vacio = true;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("carritoRebex".equals(c.getName())) {
                    vacio = false;
                    request.setAttribute("itemsCarritoCookie", c.getValue());
                    break;
                }
            }
        }

        // Si hay sesión de usuario, también comprobamos el carrito de sesión
        if (sesion.getAttribute("carritoSesion") != null) {
            vacio = false;
        }

        request.setAttribute("carritoVacio", vacio);
        request.getRequestDispatcher(urlDestino).forward(request, response);

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
