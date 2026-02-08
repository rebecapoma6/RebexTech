<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="baseUrl" value="${pageContext.request.contextPath}" />
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
<!--                                                    <a href="${baseUrl}/FrontController?accion=verCarrito&accionCarrito=eliminar&idProducto=${p.idproducto}" 
                                                       class="btn btn-sm btn-outline-danger">
                                                        <i class="bi bi-trash"></i>
                                                    </a>-->
                                                <td>
                                                    <button type="button" class="btn btn-sm btn-outline-rebex border-0" 
                                                            data-bs-toggle="modal" 
                                                            data-bs-target="#confirmarEliminar${p.idproducto}">
                                                        <i class="bi bi-trash fs-5"></i>
                                                    </button>
                                                </td>
                                            </tr>
                                        <div class="modal fade" id="confirmarEliminar${p.idproducto}" tabindex="-1" aria-hidden="true">
                                            <div class="modal-dialog modal-dialog-centered">
                                                <div class="modal-content modal-content-rebex shadow-lg border-0">
                                                    <div class="modal-header modal-rebex-header">
                                                        <h5 class="modal-title small fw-bold text-uppercase tracking-wider">Quitar Producto</h5>
                                                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                                    </div>
                                                    <div class="modal-body py-5 text-center bg-white">
                                                        <p class="fs-5 mb-0">¿Seguro que quieres eliminar <br><strong class="text-morado-claro">${p.nombre}</strong>?</p>
                                                    </div>
                                                    <div class="modal-footer border-0 bg-light justify-content-center">
                                                        <button type="button" class="btn btn-sm btn-gris-rebex px-4" data-bs-dismiss="modal">CANCELAR</button>
                                                        <a href="${baseUrl}/FrontController?accion=verCarrito&accionCarrito=eliminar&idProducto=${p.idproducto}" 
                                                           class="btn btn-sm btn-rebex-confirm px-4 shadow-sm">ELIMINAR</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>

                            </tbody>
                        </table>
                        <button type="button" class="btn btn-rebex px-4 mt-3 text-uppercase shadow-sm" 
                                data-bs-toggle="modal" 
                                data-bs-target="#confirmarVaciar">
                            Vaciar Carrito
                        </button>

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
                                    PAGAR
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

        <div class="modal fade" id="confirmarVaciar" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content modal-content-rebex shadow-lg border-0">
                    <div class="modal-header modal-rebex-header">
                        <h5 class="modal-title small fw-bold text-uppercase tracking-wider">Vaciar todo el carrito</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body py-5 text-center bg-white">
                        <div class="mb-3">
                            <i class="bi bi-cart-x text-morado-claro display-2"></i>
                        </div>
                        <p class="fs-5">¿Estás seguro de que quieres borrar <br><strong>todos tus productos</strong>?</p>
                    </div>
                    <div class="modal-footer border-0 bg-light justify-content-center">
                        <button type="button" class="btn btn-sm btn-gris-rebex px-4 text-uppercase" data-bs-dismiss="modal">Mantener compra</button>
                        <a href="${baseUrl}/FrontController?accion=verCarrito&accionCarrito=vaciar" 
                           class="btn btn-sm btn-rebex-confirm px-4 shadow-sm text-uppercase">Sí, vaciar todo</a>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="/INCLUDE/pie.jsp" />
        <jsp:include page="/INCLUDE/modal.jsp" />

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
