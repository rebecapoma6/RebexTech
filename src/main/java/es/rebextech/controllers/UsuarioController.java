package es.rebextech.controllers;

import es.rebextech.IDAO.DAOFactory;
import es.rebextech.beans.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UsuarioController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        DAOFactory fabrica = DAOFactory.getDAOFactory();
        // Redirigimos al FrontController para que cargue productos al volver
        String urlDestino = "FrontController?accion=inicio";

        if ("login".equals(accion)) {
            String email = request.getParameter("email");
            String pass = request.getParameter("password");

            // 1. LLAMAMOS AL DAO
            Usuario user = fabrica.getUsuarioDAO().login(email, pass);

            if (user != null) {
                HttpSession sesion = request.getSession();

                // 1. Limpiamos el contador visual de la sesión
                sesion.removeAttribute("cantidadProductos");

                // 2. DESTRUIR LA COOKIE (Seguridad del PDF)
                Cookie cookieBorrar = new Cookie("carritoRebex", "");
                cookieBorrar.setPath("/"); // Esto es vital para que el navegador la encuentre
                cookieBorrar.setMaxAge(0); // Cero segundos = Borrado inmediato
                response.addCookie(cookieBorrar);

                // 3. Guardar usuario
                sesion.setAttribute("usuarioSesion", user);
                urlDestino += "&login=success";
            } else {
                // FALLO: Credenciales incorrectas
                urlDestino += "&error=login_fallido";
            }
        } else if ("salir".equals(accion)) {
            // CERRAR SESIÓN
            HttpSession sesion = request.getSession(false);
            if (sesion != null) {
                sesion.invalidate();
            }
            // Al salir no hace falta añadir parámetros, volvemos al inicio limpio
        }

        // UN SOLO REDIRECT AL FINAL
        response.sendRedirect(urlDestino);

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
