package es.rebextech.controllers;

import es.rebextech.IDAO.DAOFactory;
import es.rebextech.beans.Usuario;
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
        DAOFactory fabrica = DAOFactory.getDAOFactory();
        Usuario nuevoUsuario = new Usuario();
        String urlDestino = "FrontController?accion=inicio";
        HttpSession ssRegistro = request.getSession();

        try {
            // 1. POBLAR EL BEAN CON BEANUTILS
//            BeanUtils.populate(nuevoUsuario, request.getParameterMap());


            String contrasenia1 = request.getParameter("password");
            String contrasenia2 = request.getParameter("confirmarPassword");
            String email = request.getParameter("email");
            String nif = request.getParameter("nif");
            
            
            if(email == null || email.isEmpty() || nif==null || nif.isEmpty() || contrasenia1 == null || contrasenia2.isEmpty()){
                ssRegistro.setAttribute("alerta", "Todos los campos son obligatorios.");
                ssRegistro.setAttribute("tipoAlerta", "warning");
                response.sendRedirect(urlDestino);
                return;
            }
            
            if(!contrasenia1.equals(contrasenia2)){
                ssRegistro.setAttribute("alerta", "Las contraseñas no coinciden.");
                ssRegistro.setAttribute("tipoAlerta", "danger");
                response.sendRedirect(urlDestino);
                return;                
            }
            
            // C. VALIDACIÓN: EMAIL ÚNICO (Suponiendo que tienes este método en el DAO)
//            if (fabrica.getUsuarioDAO().existeEmail(email)) {
//                ssRegistro.setAttribute("alerta", "El correo electrónico ya está registrado.");
//                ssRegistro.setAttribute("tipoAlerta", "danger");
//                response.sendRedirect(urlDestino);
//                return;
//            }

            // 2. POBLAR EL BEAN (Si pasó las validaciones)
            BeanUtils.populate(nuevoUsuario, request.getParameterMap());

            // 3. GESTIONAMOS EL AVATAR
            Part fotoPart = request.getPart("avatar_file"); // Verifica que el name en el JSP coincida

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

            // 3. REGISTRAR EN BD
            int idGenerado = fabrica.getUsuarioDAO().registrarUsuario(nuevoUsuario);

            if (idGenerado > 0) {
              

                // 1. Limpiamos el contador visual de la sesión
                ssRegistro.removeAttribute("cantidadProductos");

                // 2. DESTRUIR LA COOKIE (Seguridad del PDF)
                Cookie cookieBorrar = new Cookie("carritoRebex", "");
                cookieBorrar.setPath("/"); // Esto es vital para que el navegador la encuentre
                cookieBorrar.setMaxAge(0); // Cero segundos = Borrado inmediato
                response.addCookie(cookieBorrar);

                // 3. Guardar usuario
                ssRegistro.setAttribute("usuarioSesion", nuevoUsuario);
                ssRegistro.setAttribute("alerta", "¡Registro completado con éxito! Bienvenido.");
                ssRegistro.setAttribute("tipoAlerta", "success");
            } else {
                ssRegistro.setAttribute("alerta", "No se pudo registrar, intente otra vez.");
                ssRegistro.setAttribute("tipoAlerta", "danger");
            }

        } catch (Exception e) {
            e.printStackTrace();
            ssRegistro.setAttribute("alerta", "No se ha cargado la img o el registro falló. Intente otra vez.");
            ssRegistro.setAttribute("tipoAlerta", "danger");
        }

        response.sendRedirect(urlDestino);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
