/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package es.rebextech.controllers;

import es.rebextech.IDAO.DAOFactory;
import es.rebextech.beans.Producto;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author User
 */
public class CatalogoController extends HttpServlet {


  
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOFactory fabrica = DAOFactory.getDAOFactory();
        
        // 1. Recoger parámetros
        String texto = request.getParameter("busqueda");
        String idCat = request.getParameter("idcategoria");
        String rango = request.getParameter("rangoPrecio");
        
        List<Producto> resultados;

        // LÓGICA: ¿Es una búsqueda avanzada (trae categoría o precio) o es del Navbar?
    if ((idCat != null && !idCat.isEmpty()) || (rango != null && !rango.isEmpty())) {
        
        double min = 0, max = 9999;
        if (rango != null && rango.contains("-")) {
            String[] partes = rango.split("-");
            min = Double.parseDouble(partes[0]);
            max = Double.parseDouble(partes[1]);
        }
        
        // Llamada al método EXPLÍCITO del catálogo
        resultados = fabrica.getProductoDAO().filtroavanzadoCatalogo(texto, idCat, min, max);
        
    } else {
        // Si viene del Navbar o solo con texto, usamos la búsqueda LIBRE
        // Esto es lo que hará que "Apple" encuentre el MacBook
        resultados = fabrica.getProductoDAO().busquedalibreNavabar(texto);
    }
        
        // 4. Guardar resultados para el JSP
        request.setAttribute("productosLanding", resultados);
        
        // 5. Ir a la vista
        request.getRequestDispatcher("/CATALOGO/catalogo.jsp").forward(request, response);
    
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
