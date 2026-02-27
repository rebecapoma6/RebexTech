package es.rebextech.controllers;

import es.rebextech.DAO.ProductoDAO;
import es.rebextech.IDAO.DAOFactory;
import es.rebextech.beans.LineaPedido;
import es.rebextech.beans.Producto;
import es.rebextech.beans.Usuario;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
        String datosCarrito = "";

        Usuario usuarioLogueado = (Usuario) sesion.getAttribute("usuarioSesion");

        // 1. Recuperar cookie si NO hay usuario logueado
        if (usuarioLogueado == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("carritoRebex".equals(c.getName())) {
                        datosCarrito = c.getValue();
                        break;
                    }
                }
            }
        }

        // 2. ACCIONES DEL CARRITO (Organizadas en Switch)
        if (accionCarrito != null) {
            DAOFactory fabrica = DAOFactory.getDAOFactory();
            switch (accionCarrito) {
                case "agregar":
                    if (idProducto != null) {
                        if (usuarioLogueado == null) {
                            datosCarrito = datosCarrito.isEmpty() ? idProducto : datosCarrito + "-" + idProducto;
                        } else {
                            fabrica.getPedidoDAO()
                                    .agregarUnProductoAlCarritoBD(usuarioLogueado.getIdusuario(), Integer.parseInt(idProducto));
                        }
                    }
                    break;

                case "eliminar":
                    if (idProducto != null) {
                        if (usuarioLogueado == null) {
                            String[] ids = datosCarrito.split("-");
                            StringBuilder nuevaCadena = new StringBuilder();
                            for (String id : ids) {
                                if (!id.equals(idProducto)) {
                                    if (nuevaCadena.length() > 0) {
                                        nuevaCadena.append("-");
                                    }
                                    nuevaCadena.append(id);
                                }
                            }
                            datosCarrito = nuevaCadena.toString();
                        } else {
                            fabrica.getPedidoDAO()
                                    .eliminarProductoDelCarritoBD(usuarioLogueado.getIdusuario(), Integer.parseInt(idProducto));
                        }
                    }
                    break;

                case "vaciar":
                    if (usuarioLogueado == null) {
                        datosCarrito = "";
                    } else {
                        fabrica.getPedidoDAO().vaciarCarritoBD(usuarioLogueado.getIdusuario());
                    }
                    break;

                case "actualizarCantidadCarrito":

                    int idProd = Integer.parseInt(request.getParameter("id"));
                    int nuevaCant = Integer.parseInt(request.getParameter("cantidad"));
                    double nuevoSubtotal = 0;
                    double nuevoTotal = 0;
                    int totalItemsGlobal = 0;

                    if (usuarioLogueado != null) {
                        // CASO LOGUEADO: Base de Datos
                        fabrica.getPedidoDAO().actualizarCantidadProductoBD(usuarioLogueado.getIdusuario(), idProd, nuevaCant);
                        List<LineaPedido> listaLineas = fabrica.getProductoDAO().getProductosCarritoBD(usuarioLogueado.getIdusuario());
                        for (LineaPedido lp : listaLineas) {
                            if (lp.getProducto().getIdproducto() == idProd) {
                                nuevoSubtotal = lp.getSubtotal();
                            }
                            nuevoTotal += lp.getSubtotal();
                            totalItemsGlobal += lp.getCantidad();
                        }
                    } else {
                        // CASO ANÓNIMO: Reconstruir Cookie
                        String[] idsActuales = datosCarrito.split("-");
                        StringBuilder nuevaCadenaCookie = new StringBuilder();
                        // Agregar los demás productos
                        for (String id : idsActuales) {
                            if (!id.equals(String.valueOf(idProd)) && !id.isEmpty()) {
                                if (nuevaCadenaCookie.length() > 0) {
                                    nuevaCadenaCookie.append("-");
                                }
                                nuevaCadenaCookie.append(id);
                            }
                        }
                        // Agregar el actual N veces
                        for (int i = 0; i < nuevaCant; i++) {
                            if (nuevaCadenaCookie.length() > 0) {
                                nuevaCadenaCookie.append("-");
                            }
                            nuevaCadenaCookie.append(idProd);
                        }
                        datosCarrito = nuevaCadenaCookie.toString();

                        // Calcular totales para la respuesta
                        List<Producto> listaProdsCoke = fabrica.getProductoDAO().getProductosCarrito(datosCarrito.split("-"));
                        for (Producto p : listaProdsCoke) {
                            if (p.getIdproducto() == idProd) {
                                nuevoSubtotal = p.getPrecio() * nuevaCant;
                                nuevoTotal += nuevoSubtotal;
                                totalItemsGlobal += nuevaCant;
                            } else {
                                nuevoTotal += p.getPrecio(); // Asumiendo 1 de cada uno de los otros
                                totalItemsGlobal += 1;
                            }
                        }
                        Cookie c = new Cookie("carritoRebex", datosCarrito);
                        c.setPath("/");
                        c.setMaxAge(60 * 60 * 24 * 2);
                        response.addCookie(c);
                    }

                    // Respuesta JSON
                    sesion.setAttribute("cantidadProductos", totalItemsGlobal);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    String json = "{\"status\":\"success\","
                            + "\"nuevoSubtotal\":\"" + String.format("%.2f", nuevoSubtotal).replace(",", ".") + "\","
                            + "\"nuevoTotal\":\"" + String.format("%.2f", nuevoTotal).replace(",", ".") + "\","
                            + "\"nuevoContador\":\"" + totalItemsGlobal + "\"}";
                    response.getWriter().print(json);
                    return;

                case "finalizarCompra":
                    if (usuarioLogueado != null) {

                        // Recuperamos lo que el usuario tiene en el carrito antes de borrar
                        List<LineaPedido> carritoFinal = fabrica.getProductoDAO().getProductosCarritoBD(usuarioLogueado.getIdusuario());

                        double totalFinal = 0;
                        for (LineaPedido lp : carritoFinal) {
                            totalFinal += lp.getSubtotal();
                        }

                        //El DAO ahora devuelve un short (el ID del pedido)
                        short idGenerado = fabrica.getPedidoDAO().finalizarPedido(usuarioLogueado.getIdusuario(), carritoFinal, totalFinal);

                        if (idGenerado > 0) {
                            // Pasamos datos a la factura (detallePedido.jsp)
                            request.setAttribute("idPedidoGenerado", idGenerado);
                            request.setAttribute("listaProductos", carritoFinal);
                            request.setAttribute("totalPrecio", totalFinal);

                            // Limpiamos carrito de la BD y de la sesión
                            fabrica.getPedidoDAO().vaciarCarritoBD(usuarioLogueado.getIdusuario());
                            sesion.setAttribute("cantidadProductos", 0);
                            System.out.println("DEBUG: Redirigiendo a detallePedido.jsp");

                            // Mostramos la factura
                            request.getRequestDispatcher("/USUARIO/detallePedido.jsp").forward(request, response);
                            return;
                        }
                    }
                    break;
            }
        }

        // 3. VISUALIZACIÓN DEL CARRITO (Carga de datos para el JSP)
        DAOFactory fabrica = DAOFactory.getDAOFactory();
        if (usuarioLogueado
                != null) {

            List<LineaPedido> listaLineas = fabrica.getProductoDAO().getProductosCarritoBD(usuarioLogueado.getIdusuario());
            double total = 0;
            int totalItemsBD = 0;
            for (LineaPedido lp : listaLineas) {
                total += lp.getSubtotal();
                totalItemsBD += lp.getCantidad();
            }
            sesion.setAttribute("cantidadProductos", totalItemsBD);
            request.setAttribute("listaProductos", listaLineas);
            request.setAttribute("totalPrecio", total);
            request.setAttribute("carritoVacio", listaLineas.isEmpty());
        } else {
            Cookie cookieCarrito = new Cookie("carritoRebex", datosCarrito);
            cookieCarrito.setPath("/");
            if (datosCarrito.isEmpty()) {
                cookieCarrito.setMaxAge(0);
                sesion.setAttribute("cantidadProductos", 0);
                request.setAttribute("carritoVacio", true);
            } else {
                cookieCarrito.setMaxAge(60 * 60 * 24 * 2);
                String[] ids = datosCarrito.split("-");
                sesion.setAttribute("cantidadProductos", ids.length);
                ProductoDAO pDAO = new ProductoDAO();
                List<Producto> listaProductos = pDAO.getProductosCarrito(ids);
                List<LineaPedido> listaParaJSP = new ArrayList<>();
                double total = 0;
                for (Producto p : listaProductos) {
                    LineaPedido lp = new LineaPedido();
                    lp.setProducto(p);
                    lp.setCantidad((byte) 1);
                    listaParaJSP.add(lp);
                    total += p.getPrecio();
                }
                request.setAttribute("listaProductos", listaParaJSP);
                request.setAttribute("totalPrecio", total);
                request.setAttribute("carritoVacio", false);
            }
            response.addCookie(cookieCarrito);
        }

        // 4. REDIRECCIÓN FINAL
        if (accionCarrito
                != null && !"actualizarCantidadCarrito".equals(accionCarrito)) {
            sesion.setAttribute("verCarritoDespues", true);
            response.sendRedirect("FrontController");
        } else {
            request.getRequestDispatcher(urlDestino).forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "CarritoController";
    }
}
