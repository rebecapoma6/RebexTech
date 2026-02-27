<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="baseUrl" value="${pageContext.request.contextPath}" />
<c:set var="imgBase" value="${pageContext.request.contextPath}/IMAGENES/productos" />
<c:set var="estilo" value="${pageContext.request.contextPath}/CSS/style.css" />

<!DOCTYPE html>
<html lang="es">
<head>
    <jsp:include page="/INCLUDE/metas.inc"/>
    <title>Comprobante de Compra - RebexTech</title>
    <link rel="stylesheet" href="${estilo}">
    <style>
        .ticket-bg { background-color: #f8f9fa; border-radius: 20px; }
        .factura-header { border-bottom: 2px dashed #dee2e6; }
        @media print {
            .no-print { display: none; }
            .ticket-bg { background-color: white; }
        }
    </style>
</head>
<body>
    <jsp:include page="/INCLUDE/navbar.jsp" />

    <div class="container my-5 pt-4">
    <div class="row justify-content-center">
        <div class="col-lg-10">
            
            <c:if test="${not empty totalFinal}">
                <div class="alert alert-success border-0 shadow-sm mb-4 text-center rounded-4 p-4">
                    <i class="bi bi-check-circle-fill display-4 text-success mb-2 d-block"></i>
                    <h3 class="fw-bold">¡Pago Realizado con Éxito!</h3>
                    <p class="mb-0">Gracias por tu compra, <strong>${sessionScope.usuarioSesion.nombre}</strong>. Aquí tienes tu factura.</p>
                </div>
            </c:if>

            <div class="factura-container">
                <div class="header-factura">
                    <div class="row align-items-center">
                        <div class="col-md-6">
                            <h1 class="fw-bold text-morado-rebex mb-0" style="letter-spacing: -1px;">REBEXTECH</h1>
                            <p class="text-muted mb-0 fw-bold small text-uppercase">Soluciones Hardware de Alto Rendimiento</p>
                        </div>
                        <div class="col-md-6 text-md-end mt-3 mt-md-0">
                            <h4 class="fw-bold text-dark mb-2">FACTURA ELECTRÓNICA</h4>
                            <span class="badge-pedido">Pedido #<c:out value="${idPedidoGenerado != null ? idPedidoGenerado : 'N/A'}" /></span>
                        </div>
                    </div>
                </div>

                <div class="card-body p-4 p-md-5">
                    <div class="row mb-5">
                        <div class="col-sm-6">
                            <h6 class="text-uppercase text-muted fw-bold small mb-3">Datos del Cliente</h6>
                            <p class="fs-5 fw-bold mb-0">${sessionScope.usuarioSesion.nombre} ${sessionScope.usuarioSesion.apellidos}</p>
                            <p class="text-muted small mb-0"><i class="bi bi-envelope me-2"></i>${sessionScope.usuarioSesion.email}</p>
                            <p class="text-muted small"><i class="bi bi-geo-alt me-2"></i>${sessionScope.usuarioSesion.direccion}</p>
                        </div>
                        <div class="col-sm-6 text-md-end">
                            <h6 class="text-uppercase text-muted fw-bold small mb-3">Fecha de Pago</h6>
                            <p class="fs-5 fw-bold text-dark">
                                <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd-MM-yyyy" />
                            </p>
                        </div>
                    </div>

                    <div class="table-responsive mb-4">
                        <table class="table table-hover align-middle tabla-factura">
                            <thead>
                                <tr>
                                    <th class="py-3 ps-4">Producto</th>
                                    <th class="py-3 text-center">Cant.</th>
                                    <th class="py-3 text-end">Precio</th>
                                    <th class="py-3 text-end pe-4">Subtotal</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${listaProductos}">
                                    <tr class="border-bottom">
                                        <td class="ps-4">
                                            <div class="d-flex align-items-center py-2">
                                                <img src="${imgBase}/${item.producto.imagen}" width="45" class="rounded shadow-sm me-3 border">
                                                <span class="fw-bold text-dark">${item.producto.nombre}</span>
                                            </div>
                                        </td>
                                        <td class="text-center fw-bold">${item.cantidad}</td>
                                        <td class="text-end">
                                            <fmt:formatNumber value="${item.producto.precio}" minFractionDigits="2" />€
                                        </td>
                                        <td class="text-end pe-4 fw-bold text-morado-rebex">
                                            <fmt:formatNumber value="${item.producto.precio * item.cantidad}" minFractionDigits="2" />€
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="row justify-content-end">
                        <div class="col-md-5">
                            <div class="bg-total-factura p-4">
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="text-muted">Base Imponible:</span>
                                    <span class="fw-bold"><fmt:formatNumber value="${totalPrecio / 1.21}" minFractionDigits="2" />€</span>
                                </div>
                                <div class="d-flex justify-content-between mb-3">
                                    <span class="text-muted">IVA (21%):</span>
                                    <span class="fw-bold"><fmt:formatNumber value="${totalPrecio - (totalPrecio / 1.21)}" minFractionDigits="2" />€</span>
                                </div>
                                <div class="d-flex justify-content-between border-top pt-3">
                                    <span class="h5 fw-bold text-dark">TOTAL:</span>
                                    <span class="h4 fw-bold text-morado-rebex"><fmt:formatNumber value="${totalPrecio}" minFractionDigits="2" />€</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="card-footer bg-light p-4 no-print text-center border-0">
<!--                    <button onclick="window.print()" class="btn btn-outline-dark px-4 fw-bold me-2">
                        <i class="bi bi-printer me-2"></i> IMPRIMIR
                    </button>-->
                    <a href="${baseUrl}/FrontController?accion=inicio" class="btn btn-rebex px-5">
                        VOLVER A LA TIENDA
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

    <jsp:include page="/INCLUDE/pie.jsp" />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>