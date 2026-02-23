package es.rebextech.controllers;

import es.rebextech.IDAO.DAOFactory;
import java.io.IOException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author User
 */
public class AjaxController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        JSONObject objeto = new JSONObject();
        DAOFactory fabrica = DAOFactory.getDAOFactory(); // Para usar tus DAOs

        // ==========================================
        // 1. ACCIÓN: VALIDAR NIF (Inciso D)
        // ==========================================
        if ("validarDni".equals(accion)) {
            String dniRaw = request.getParameter("dni");
            String soloNumeros = (dniRaw != null) ? dniRaw.replaceAll("[^0-9]", "") : "";

            try {
                if (soloNumeros.length() >= 7 && soloNumeros.length() <= 8) {
                    int numero = Integer.parseInt(soloNumeros);
                    String letras = "TRWAGMYFPDXBNJZSQVHLCKE";

                    int indice = numero % 23;
                    objeto.put("tipo", "success");
                    objeto.put("letra", String.valueOf(letras.charAt(indice)));
                } else {
                    objeto.put("tipo", "warning");
                    objeto.put("letra", "Debes ingresar 8 números");
                }
            } catch (NumberFormatException e) {
                objeto.put("tipo", "warning");
                objeto.put("letra", "Error en el formato del NIF");
            }
        } // ==========================================
        // 2. ACCIÓN: COMPROBAR EMAIL (Inciso A) - ¡AÑADE ESTO!
        // ==========================================
        else if ("comprobarEmail".equals(accion)) {
            String email = request.getParameter("email");
            // Usamos tu método que ya revisamos en el UsuarioDAO
            boolean ocupado = fabrica.getUsuarioDAO().existeEmail(email);

            if (ocupado) {
                objeto.put("status", "ocupado");
                objeto.put("mensaje", "Este correo ya está registrado");
            } else {
                objeto.put("status", "libre");
                objeto.put("mensaje", "Correo disponible");
            }
        }

        // Configuramos la respuesta una sola vez al final
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(objeto.toString());

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
