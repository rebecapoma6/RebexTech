package es.rebextech.controllers;

import es.rebextech.IDAO.DAOFactory;
import es.rebextech.beans.Usuario;
import es.rebextech.utils.Metodos;
import static java.io.File.separator;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@javax.servlet.annotation.MultipartConfig
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
        HttpSession sesion = request.getSession();
        // Redirigimos al FrontController para que cargue productos al volver
        String urlDestino = "FrontController";

        // --- CASO 1: LOGIN ---
        if ("login".equals(accion)) {
            String email = request.getParameter("email");
            String pass = request.getParameter("password");
            Usuario user = fabrica.getUsuarioDAO().login(email, pass);

            if (user != null) {
                Cookie[] cookies = request.getCookies();
                String contenidoCarrito = null;
                if (cookies != null) {
                    for (Cookie c : cookies) {
                        if ("carritoRebex".equals(c.getName())) {
                            contenidoCarrito = c.getValue();
                            break;
                        }
                    }
                }
                if (contenidoCarrito != null && !contenidoCarrito.isEmpty()) {
                    fabrica.getPedidoDAO().migrarCarritoDeCookieABaseDeDatos(user.getIdusuario(), contenidoCarrito);
                    Metodos.eliminarCookieCarrito(response);
                    sesion.removeAttribute("cantidadProductos");
                }
                fabrica.getUsuarioDAO().actualizarUltimoAcceso(user.getIdusuario());
                sesion.setAttribute("usuarioSesion", user);
                sesion.setAttribute("alerta", "¡Bienvenido de nuevo, " + user.getNombre() + "!");
                sesion.setAttribute("tipoAlerta", "success");
                
            } else {
                sesion.setAttribute("alerta", "Email o contraseña incorrectos");
                sesion.setAttribute("tipoAlerta", "danger");
            }
        } // --- CASO 2: ACTUALIZAR AVATAR (AJAX) ---
        else if ("updateAvatar".equals(accion)) {
            Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
            javax.servlet.http.Part part = request.getPart("avatar_file");
            String nombreOrig = part.getSubmittedFileName();
            String ext = nombreOrig.substring(nombreOrig.lastIndexOf("."));
            String nuevoNombre = "avatar_" + user.getIdusuario() + "_" + System.currentTimeMillis() + ext;

            String ruta = getServletContext().getRealPath("/IMAGENES/avatares");
            part.write(ruta + separator + nuevoNombre);

            boolean ok = fabrica.getUsuarioDAO().actualizarAvatar(user.getIdusuario(), nuevoNombre);
            response.setContentType("application/json");
            if (ok) {
                user.setAvatar(nuevoNombre);
                sesion.setAttribute("usuarioSesion", user);
                response.getWriter().write("{\"status\": \"success\"}");
            } else {
                response.getWriter().write("{\"status\": \"error\"}");
            }
            return; // IMPORTANTE: Corta aquí porque es AJAX
        } // --- CASO 3: ACTUALIZAR DATOS (AJAX) ---
        else if ("updateDatos".equals(accion)) {
            Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
            user.setNombre(request.getParameter("nombre"));
            user.setApellidos(request.getParameter("apellidos"));
            user.setDireccion(request.getParameter("direccion"));
            user.setLocalidad(request.getParameter("localidad"));
            user.setProvincia(request.getParameter("provincia"));
            user.setCodigo_postal(request.getParameter("codigo_postal"));
            user.setTelefono(request.getParameter("telefono"));

            boolean ok = fabrica.getUsuarioDAO().actualizarUsuario(user);
            response.setContentType("application/json");
            if (ok) {
                sesion.setAttribute("usuarioSesion", user);
                response.getWriter().write("{\"status\": \"success\"}");
            } else {
                response.getWriter().write("{\"status\": \"error\", \"mensaje\": \"Error al guardar datos\"}");
            }
            return; // Corta aquí
        } // --- CASO 4: ACTUALIZAR PASSWORD (AJAX) ---
        else if ("updatePass".equals(accion)) {
            Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
            String actual = request.getParameter("password");
            String nueva = request.getParameter("newPass");

            // Validamos contra la DB si la actual es correcta
            Usuario check = fabrica.getUsuarioDAO().login(user.getEmail(), actual);
            response.setContentType("application/json");
            if (check != null) {
                boolean ok = fabrica.getUsuarioDAO().cambiarPassword(user.getIdusuario(), nueva);
                if (ok) {
                    response.getWriter().write("{\"status\": \"success\"}");
                } else {
                    response.getWriter().write("{\"status\": \"error\", \"mensaje\": \"Error en DB\"}");
                }
            } else {
                response.getWriter().write("{\"status\": \"error\", \"mensaje\": \"Contraseña actual incorrecta\"}");
            }
            return; // Corta aquí
        } // --- CASO 5: SALIR ---
        else if ("salir".equals(accion)) {
            if (sesion != null) {
                sesion.invalidate();
            }
        }

        // REDIRECT (Solo llega aquí si es Login o Salir)
        response.sendRedirect(urlDestino);

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
