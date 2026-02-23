package es.rebextech.controllers;

import es.rebextech.IDAO.DAOFactory;
import es.rebextech.beans.Usuario;
import es.rebextech.utils.Metodos;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.commons.beanutils.BeanUtils;

@MultipartConfig(
        fileSizeThreshold = 1024 * 10,       // 10 KB
        maxFileSize = 1024 * 100,            // 100 KB (Restricción del profesor)
        maxRequestSize = 1024 * 500          // 500 KB total
)
public class RegistroController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. AVISAMOS QUE HABLAREMOS EN JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        DAOFactory fabrica = DAOFactory.getDAOFactory();
        Usuario nuevoUsuario = new Usuario();
        HttpSession ssRegistro = request.getSession();

        try {
            String contrasenia1 = request.getParameter("password");
            String contrasenia2 = request.getParameter("confirmarPassword");
            String email = request.getParameter("email");
            String nif = request.getParameter("nif");
            
            // 2. VALIDACIONES BÁSICAS
            if (email == null || email.trim().isEmpty() || nif == null || nif.trim().isEmpty()
                    || contrasenia1 == null || contrasenia1.trim().isEmpty() || contrasenia2 == null || contrasenia2.trim().isEmpty()) {
                response.getWriter().print("{\"status\":\"error\", \"mensaje\":\"Todos los campos son obligatorios.\"}");
                return;
            }
            
            if(!contrasenia1.equals(contrasenia2)){
                response.getWriter().print("{\"status\":\"error\", \"mensaje\":\"Las contraseñas no coinciden.\"}");
                return;                
            }
            // REVISAMOS EL NIF (Esto es lo que te faltaba para que sea perfecto)
            if (fabrica.getUsuarioDAO().existeNif(nif.toUpperCase().trim())) {
                response.getWriter().print("{\"status\":\"error\", \"mensaje\":\"Este NIF ya pertenece a otra cuenta.\"}");
                return;
            }
            
            if (fabrica.getUsuarioDAO().existeEmail(email)) {
                response.getWriter().print("{\"status\":\"error\", \"mensaje\":\"Este correo ya está registrado.\"}");
                return; // IMPORTANTE: Cortamos la ejecución aquí
            }

            // 3. POBLAR BEAN
            BeanUtils.populate(nuevoUsuario, request.getParameterMap());
            nuevoUsuario.setNombre(Metodos.capitalizar(nuevoUsuario.getNombre()));
            nuevoUsuario.setApellidos(Metodos.capitalizar(nuevoUsuario.getApellidos()));
            nuevoUsuario.setDireccion(Metodos.capitalizar(nuevoUsuario.getDireccion()));
            nuevoUsuario.setLocalidad(Metodos.capitalizar(nuevoUsuario.getLocalidad()));
            nuevoUsuario.setProvincia(Metodos.capitalizar(nuevoUsuario.getProvincia()));
            
            if (nuevoUsuario.getEmail() != null) {
                nuevoUsuario.setEmail(nuevoUsuario.getEmail().toLowerCase().trim());
            }
            if (nuevoUsuario.getNif() != null) {
                nuevoUsuario.setNif(nuevoUsuario.getNif().toUpperCase().trim());
            }

            // 4. GESTIÓN DEL AVATAR
            Part fotoPart = request.getPart("avatar_file");
            if (fotoPart != null && fotoPart.getSize() > 0) {
                String nombreArchivo = nuevoUsuario.getNif() + ".jpg";
                String rutaCarpeta = getServletContext().getRealPath("/IMAGENES/avatares");
                File carpeta = new File(rutaCarpeta);
                if (!carpeta.exists()) carpeta.mkdirs();
                fotoPart.write(rutaCarpeta + File.separator + nombreArchivo);
                nuevoUsuario.setAvatar(nombreArchivo);
            } else {
                nuevoUsuario.setAvatar("default.jpg");
            }

            // 5. REGISTRAR EN BD
            int idGenerado = fabrica.getUsuarioDAO().registrarUsuario(nuevoUsuario);

            if (idGenerado > 0) {
                // ¡AQUÍ ESTABA EL TRUCO! Le asignamos el ID generado al objeto en memoria
                nuevoUsuario.setIdusuario((short) idGenerado);

                // --- LÓGICA DE MIGRACIÓN DE CARRITO (Igual que en Login) ---
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
                    fabrica.getPedidoDAO().migrarCarritoDeCookieABaseDeDatos(idGenerado, contenidoCarrito);
                }

                // Borramos la cookie anónima
                Cookie cookieBorrar = new Cookie("carritoRebex", "");
                cookieBorrar.setPath("/");
                cookieBorrar.setMaxAge(0);
                response.addCookie(cookieBorrar);
                ssRegistro.removeAttribute("cantidadProductos");

                // Actualizamos último acceso (Indicación de tu profe)
                fabrica.getUsuarioDAO().actualizarUltimoAcceso(idGenerado);

                // Guardamos al usuario logueado
                ssRegistro.setAttribute("usuarioSesion", nuevoUsuario);
                ssRegistro.setAttribute("alerta", "¡Registro completado con éxito! Bienvenido.");
                ssRegistro.setAttribute("tipoAlerta", "success");
                
                // RESPUESTA DE ÉXITO A AJAX
                response.getWriter().print("{\"status\":\"success\"}");
            } else {
                response.getWriter().print("{\"status\":\"error\", \"mensaje\":\"No se pudo registrar, intente otra vez.\"}");
            }

        } catch (IllegalStateException e) {
            // Este catch específico salta si la imagen pesa más de 100KB
            response.getWriter().print("{\"status\":\"error\", \"mensaje\":\"La imagen es muy pesada. Máximo 100KB.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print("{\"status\":\"error\", \"mensaje\":\"Ocurrió un error inesperado en el servidor.\"}");
        }
    
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
