package es.rebextech.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        String urlDestino = "index.jsp";
        String accionSolicitada = request.getParameter("accion");

        if (accionSolicitada != null) {
            switch (accionSolicitada) {
                
                case "verCarrito":
                    // Acceso libre para todos (Anónimos y Registrados) como pide tu PDF
                    urlDestino = "CarritoController";
                    break;

                case "login":
                    urlDestino = "LoginController";
                    break;

                case "registro":
                    urlDestino = "RegistroController";
                    break;

                case "salir":
                    request.getSession().invalidate();
                    urlDestino = "index.jsp";
                    break;

                default:
                    urlDestino = "index.jsp";
                    break;
            }
        }

        // Enviamos la petición al destino final
        request.getRequestDispatcher(urlDestino).forward(request, response);
    
      
    }

   
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
