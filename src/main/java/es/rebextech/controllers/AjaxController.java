package es.rebextech.controllers;

import es.rebextech.IDAO.DAOFactory;
import es.rebextech.beans.LineaPedido;
import es.rebextech.beans.Producto;
import es.rebextech.beans.Usuario;
import java.io.IOException;
import java.util.List;
import org.json.JSONObject;

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
public class AjaxController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        String accion = request.getParameter("accion");
        JSONObject objeto = new JSONObject();
        DAOFactory fabrica = DAOFactory.getDAOFactory();

        if (accion != null) {
            switch (accion) {
                case "validarDni":
                    String dniRaw = request.getParameter("nif");
                    String soloNumeros = (dniRaw != null) ? dniRaw.replaceAll("[^0-9]", "") : "";
                    try {
                        if (soloNumeros.length() == 8) {
                            int numero = Integer.parseInt(soloNumeros);
                            String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
                            int indice = numero % 23;
                            objeto.put("tipo", "success");
                            objeto.put("letra", String.valueOf(letras.charAt(indice)));
                        } else {
                            objeto.put("tipo", "warning");
                            objeto.put("letra", "Faltan dígitos");
                        }
                    } catch (NumberFormatException e) {
                        objeto.put("tipo", "warning");
                        objeto.put("letra", "Error de formato");
                    }
                    break;

                case "comprobarEmail":
                    String email = request.getParameter("email");
                    boolean ocupado = fabrica.getUsuarioDAO().existeEmail(email);
                    if (ocupado) {
                        objeto.put("status", "ocupado");
                        objeto.put("mensaje", "Este correo ya está registrado");
                    } else {
                        objeto.put("status", "libre");
                        objeto.put("mensaje", "Correo disponible");
                    }
                    break;

                case "actualizarCantidadCarrito":
                    int idProd = Integer.parseInt(request.getParameter("id"));
                    int nuevaCant = Integer.parseInt(request.getParameter("cantidad"));
                    double nuevoSubtotal = 0;
                    double nuevoTotal = 0;
                    int totalItemsGlobal = 0;

                    Usuario usuarioLog = (Usuario) sesion.getAttribute("usuarioSesion");

                    if (usuarioLog != null) {
                        // ==========================================
                        // 1. CASO USUARIO LOGUEADO (Base de Datos)
                        // ==========================================
                        fabrica.getPedidoDAO().actualizarCantidadProductoBD(usuarioLog.getIdusuario(), idProd, nuevaCant);

                        List<LineaPedido> lista = fabrica.getProductoDAO().getProductosCarritoBD(usuarioLog.getIdusuario());
                        for (LineaPedido lp : lista) {
                            if (lp.getProducto().getIdproducto() == idProd) {
                                nuevoSubtotal = lp.getSubtotal();
                            }
                            nuevoTotal += lp.getSubtotal();
                            totalItemsGlobal += lp.getCantidad();
                        }

                    } else {
                        // ==========================================
                        // 2. CASO USUARIO ANÓNIMO (Cookies)
                        // ==========================================
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

                        // Reconstruimos la cadena de la cookie
                        // Reconstruimos la cadena de la cookie
                        String[] idsActuales = datosCarrito.split("-");
                        StringBuilder nuevaCadenaCookie = new StringBuilder();

                        // Agregamos los DEMÁS productos (los que NO son el que estamos modificando)
                        for (String idStr : idsActuales) {
                            if (!idStr.isEmpty() && !idStr.equals(String.valueOf(idProd))) {
                                if (nuevaCadenaCookie.length() > 0) nuevaCadenaCookie.append("-");
                                nuevaCadenaCookie.append(idStr);
                            }
                        }

                        // Agregamos el producto modificado "nuevaCant" veces
                        for (int i = 0; i < nuevaCant; i++) {
                            if (nuevaCadenaCookie.length() > 0) nuevaCadenaCookie.append("-");
                            nuevaCadenaCookie.append(idProd);
                        }
                        datosCarrito = nuevaCadenaCookie.toString();
                        String[] idsFinales = datosCarrito.split("-");

                        // Calculamos totales consultando los precios al DAO
                        List<Producto> listaProdsCoke = fabrica.getProductoDAO().getProductosCarrito(idsFinales);
                        for (Producto p : listaProdsCoke) {
                            // Contamos cuantas veces aparece este producto en el nuevo carrito
                            int cantidadReal = 0;
                            for (String idF : idsFinales) {
                                if (!idF.isEmpty() && Integer.parseInt(idF) == p.getIdproducto()) {
                                    cantidadReal++;
                                }
                            }

                            if (p.getIdproducto() == idProd) {
                                nuevoSubtotal = p.getPrecio() * nuevaCant;
                            }
                            nuevoTotal += (p.getPrecio() * cantidadReal); // AHORA SÍ SUMA LO REAL
                        }

                        // Contamos los items totales para el Navbar
                        for (String idF : idsFinales) {
                            if (!idF.isEmpty()) totalItemsGlobal++;
                        }

                        // Guardamos la nueva cookie
                        Cookie c = new Cookie("carritoRebex", datosCarrito);
                        c.setPath("/");
                        c.setMaxAge(60 * 60 * 24 * 2);
                        response.addCookie(c);
                    }

                    // ==========================================
                    // 3. RESPUESTA JSON FINAL (Para ambos casos)
                    // ==========================================
                    sesion.setAttribute("cantidadProductos", totalItemsGlobal);
                    objeto.put("status", "success");
                    objeto.put("nuevoSubtotal", String.format("%.2f", nuevoSubtotal).replace(",", "."));
                    objeto.put("nuevoTotal", String.format("%.2f", nuevoTotal).replace(",", "."));
                    objeto.put("nuevoContador", totalItemsGlobal);

                    break;

            }
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(objeto.toString());

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
