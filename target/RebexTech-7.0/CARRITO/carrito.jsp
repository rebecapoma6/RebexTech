<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="estilo" value="${pageContext.request.contextPath}/CSS/style.css" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <jsp:include page="INCLUDE/metas.inc"/>
        <title>Carrito-RebexTech</title>
        <link rel="stylesheet" href="${estilo}">
    </head>
    <body>
        <jsp:include page="index.jsp" /> <div class="container my-5 pt-5">
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
                                <tr>
                                    <td class="small opacity-75" colspan="5">Tu carrito está vacío actualmente.</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="col-lg-4">
                    <div class="card border-0 shadow-sm p-4 hero-bg-dark text-white rounded-4">
                        <h4 class="fw-bold mb-4">Resumen</h4>
                        <div class="d-flex justify-content-between mb-2">
                            <span>Productos:</span>
                            <span>0.00€</span>
                        </div>
                        <div class="d-flex justify-content-between mb-4">
                            <span>Envío:</span>
                            <span class="text-success">Gratis</span>
                        </div>
                        <hr>
                        <div class="d-flex justify-content-between mb-4">
                            <span class="fs-5 fw-bold">TOTAL:</span>
                            <span class="fs-5 fw-bold text-morado-claro">0.00€</span>
                        </div>

                        <c:choose>
                            <c:when test="${empty sessionScope.usuarioSesion}">
                                <button class="btn btn-rebex w-100 py-3 fw-bold" data-bs-toggle="modal" data-bs-target="#loginModal">
                                    IDENTIFÍCATE PARA COMPRAR
                                </button>
                            </c:when>
                            <c:otherwise>
                                <a href="ControladorPedido" class="btn btn-rebex w-100 py-3 fw-bold">
                                    FINALIZAR COMPRA
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
