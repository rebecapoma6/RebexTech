<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="urlBase" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">
<head>
    <jsp:include page="/INCLUDE/metas.inc"/>
    <title>Mis Pedidos - RebexTech</title>
    <link rel="stylesheet" href="${urlBase}/CSS/style.css">
</head>
<body class="bg-light">

    <jsp:include page="/INCLUDE/navbar.jsp" />

    <div class="container my-5 pt-5">
        <h2 class="fw-bold mb-4 section-title-rebex text-uppercase">Mi Historial de Pedidos</h2>

        <div class="card border-0 shadow-sm p-4 rounded-4 bg-white">
           <c:choose>
                <%-- 1. CORRECCIÓN: Ahora evalúa la variable "historialPedidos" correcta y a prueba de fallos --%>
                <c:when test="${historialPedidos != null && historialPedidos.size() > 0}">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle text-center">
                            <thead class="table-light">
                                <tr>
                                    <th>Nº Pedido</th>
                                    <th>Fecha de Compra</th>
                                    <th>Total Importe</th>
                                    <th>Estado</th>
                                    <th>Acción</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="p" items="${historialPedidos}">
                                    <tr>
                                        <td class="fw-bold text-morado-rebex">#${p.idpedido}</td>
                                        
                                        <%-- Formato de fecha exacto: dd-mm-yyyy --%>
                                        <td>
                                            <fmt:formatDate value="${p.fecha}" pattern="dd-MM-yyyy" />
                                        </td>
                                        
                                        <td class="fw-bold">
                                            <fmt:formatNumber value="${p.importe}" minFractionDigits="2" maxFractionDigits="2" />€
                                        </td>
                                        
                                        <td>
                                            <span class="badge bg-success px-3 py-2 rounded-pill shadow-sm">
                                                <i class="bi bi-check-circle me-1"></i> Finalizado
                                            </span>
                                        </td>
                                        
                                        <td>
                                            <%-- 2. CORRECCIÓN: URL Limpia usando un Formulario POST --%>
                                            <form action="${urlBase}/FrontController" method="POST" style="margin: 0;">
                                                <input type="hidden" name="accion" value="verDetallePedido">
                                                <input type="hidden" name="id" value="${p.idpedido}">
                                                <button type="submit" class="btn btn-sm btn-outline-rebex fw-bold px-3">
                                                    <i class="bi bi-receipt"></i> Ver Detalles
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                
                <%-- SI EL HISTORIAL ESTÁ VACÍO --%>
                <c:otherwise>
                    <div class="text-center py-5">
                        <i class="bi bi-box-seam text-muted opacity-50 display-1"></i>
                        <h4 class="mt-4 fw-bold text-secondary">Aún no tienes pedidos</h4>
                        <p class="text-muted mb-4">Parece que todavía no has realizado ninguna compra en RebexTech.</p>
                        <form action="${urlBase}/FrontController" method="POST" style="margin: 0;">
                            <button type="submit" class="btn btn-rebex px-4 py-2 fw-bold shadow-sm">
                                <i class="bi bi-cart me-2"></i>¡Ir de Compras!
                            </button>
                        </form>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <jsp:include page="/INCLUDE/pie.jsp" />
    <jsp:include page="/INCLUDE/modal.jsp" />

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${urlBase}/JS/validaciones.js"></script>
    <script src="${urlBase}/JS/login.js"></script>
</body>
</html>