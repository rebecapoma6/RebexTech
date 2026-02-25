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
import javax.servlet.http.Part;

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
        if (accion == null) accion = ""; // Evita errores si la acción llega nula
        
        DAOFactory fabrica = DAOFactory.getDAOFactory();
        HttpSession sesion = request.getSession();
        String urlDestino = "FrontController";

        switch (accion) {
            case "login":
                String email = request.getParameter("email");
                String pass = request.getParameter("password");
                Usuario user = fabrica.getUsuarioDAO().login(email, pass);
                
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

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
                    
                    response.getWriter().write("{\"status\": \"success\"}");
                } else {
                    response.getWriter().write("{\"status\": \"error\", \"mensaje\": \"Email o contraseña incorrectos\"}");
                }
                break;

            case "updateAvatar":
                Usuario userAvatar = (Usuario) sesion.getAttribute("usuarioSesion");
                Part part = request.getPart("avatar_file");
                String nombreOrig = part.getSubmittedFileName();
                String ext = nombreOrig.substring(nombreOrig.lastIndexOf("."));
                String nuevoNombre = "avatar_" + userAvatar.getIdusuario() + "_" + System.currentTimeMillis() + ext;

                String ruta = getServletContext().getRealPath("/IMAGENES/avatares");
                part.write(ruta + separator + nuevoNombre);

                boolean okAvatar = fabrica.getUsuarioDAO().actualizarAvatar(userAvatar.getIdusuario(), nuevoNombre);
                response.setContentType("application/json");
                if (okAvatar) {
                    userAvatar.setAvatar(nuevoNombre);
                    sesion.setAttribute("usuarioSesion", userAvatar);
                    response.getWriter().write("{\"status\": \"success\"}");
                } else {
                    response.getWriter().write("{\"status\": \"error\"}");
                }
                break;

            case "updateDatos":
                Usuario userDatos = (Usuario) sesion.getAttribute("usuarioSesion");
                userDatos.setNombre(request.getParameter("nombre"));
                userDatos.setApellidos(request.getParameter("apellidos"));
                userDatos.setDireccion(request.getParameter("direccion"));
                userDatos.setLocalidad(request.getParameter("localidad"));
                userDatos.setProvincia(request.getParameter("provincia"));
                userDatos.setCodigo_postal(request.getParameter("codigo_postal"));
                userDatos.setTelefono(request.getParameter("telefono"));

                boolean okDatos = fabrica.getUsuarioDAO().actualizarUsuario(userDatos);
                response.setContentType("application/json");
                if (okDatos) {
                    sesion.setAttribute("usuarioSesion", userDatos);
                    response.getWriter().write("{\"status\": \"success\"}");
                } else {
                    response.getWriter().write("{\"status\": \"error\", \"mensaje\": \"Error al guardar datos\"}");
                }
                break;

            case "updatePass":
                Usuario userPass = (Usuario) sesion.getAttribute("usuarioSesion");
                String actual = request.getParameter("password");
                String nueva = request.getParameter("newPass");

                Usuario check = fabrica.getUsuarioDAO().login(userPass.getEmail(), actual);
                response.setContentType("application/json");
                if (check != null) {
                    boolean okPass = fabrica.getUsuarioDAO().cambiarPassword(userPass.getIdusuario(), nueva);
                    if (okPass) {
                        response.getWriter().write("{\"status\": \"success\"}");
                    } else {
                        response.getWriter().write("{\"status\": \"error\", \"mensaje\": \"Error en DB\"}");
                    }
                } else {
                    response.getWriter().write("{\"status\": \"error\", \"mensaje\": \"Contraseña actual incorrecta\"}");
                }
                break;

            case "salir":
                if (sesion != null) {
                    sesion.invalidate();
                }
                response.sendRedirect(urlDestino);
                break;

            default:
                // Por si acaso llega algo raro, volvemos al inicio
                response.sendRedirect(urlDestino);
                break;
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
