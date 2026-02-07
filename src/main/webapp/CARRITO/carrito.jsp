<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="imgBase" value="${pageContext.request.contextPath}/IMAGENES/productos" />
<c:set var="estilo" value="${pageContext.request.contextPath}/CSS/style.css" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <jsp:include page="/INCLUDE/metas.inc"/>
        <title>Carrito-RebexTech</title>
        <link rel="stylesheet" href="${estilo}">
    </head>
    <body>
        <jsp:include page="/INCLUDE/navbar.jsp" />

        <div class="container my-5 pt-5">
            <h2 class="fw-bold mb-4 section-title-rebex">TU CARRITO DE COMPRAS</h2>

            <div class="row">
                <div class="col-lg-8">
                    <div class="card border-0 shadow-sm p-3">
                        <table class="table align-middle">
                            <thead>
                                <tr>
                                    <th>Producto</th>
                                    <th>Precio</th>
                                    <th>Cantidad</th>
                                    <th>Subtotal</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <%-- Si el controlador nos dice que está vacío, mostramos el aviso --%>
                                    <c:when test="${empty itemsCarritoCookie && empty sessionScope.carritoSesion}">
                                        <tr>
                                            <td class="small opacity-75 text-center" colspan="5">
                                                Tu carrito está vacío actualmente.
                                            </td>
                                        </tr>
                                    </c:when>

                                    <%-- Si NO está vacío, mostramos los productos (de momento los IDs de la cookie) --%>
                                    <c:otherwise>
                                        <c:forEach var="p" items="${listaProductos}">
                                            <tr>
                                                <td>
                                                    <div class="d-flex align-items-center">
                                                        <%-- Mostramos la imagen y el nombre real --%>
                                                        <img src="${imgBase}/${p.imagen}" width="50" class="me-3">
                                                        <span class="fw-bold">${p.nombre}</span>
                                                    </div>
                                                </td>
                                                <td>${p.precio}€</td>
                                                <td>1</td>
                                                <td>${p.precio}€</td>
                                                <td>
                                                    <a href="CarritoController?accion=eliminar&id=${p.idproducto}" class="text-danger">
                                                        <i class="bi bi-trash"></i>
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="col-lg-4">
                    <div class="card border-0 shadow-sm p-4 hero-bg-dark text-white rounded-4">
                        <h4 class="fw-bold mb-4">Resumen</h4>
                        <div class="d-flex justify-content-between mb-2">
                            <span>Productos:</span>
                            <span>${empty totalPrecio ? '0.00' : totalPrecio}€</span>
                        </div>
                        <div class="d-flex justify-content-between mb-4">
                            <span>Envío:</span>
                            <span class="text-success">Gratis</span>
                        </div>
                        <hr>
                        <div class="d-flex justify-content-between mb-4">
                            <span class="fs-5 fw-bold">TOTAL:</span>
                            <span class="fs-5 fw-bold text-morado-claro">${empty totalPrecio ? '0.00' : totalPrecio}€</span>
                        </div>

                        <c:choose>
                            <c:when test="${empty sessionScope.usuarioSesion}">
                                <%-- Si es anónimo, el botón abre el modal de login que está en index.jsp --%>
                                <button class="btn btn-rebex w-100 py-3 fw-bold" data-bs-toggle="modal" data-bs-target="#loginModal">
                                    IDENTÍFICATE PARA COMPRAR
                                </button>
                            </c:when>
                            <c:otherwise>
                                <%-- Si está registrado, va al FrontController para procesar el pago --%>
                                <a href="FrontController?accion=finalizarCompra" class="btn btn-rebex w-100 py-3 fw-bold">
                                    FINALIZAR COMPRA
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="/INCLUDE/pie.jsp" />
        <jsp:include page="/INCLUDE/modal.jsp" />

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
