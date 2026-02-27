<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="urlBase" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">
<head>
    <jsp:include page="/INCLUDE/metas.inc"/>
    <title>Error de Servidor - RebexTech</title>
    <link rel="stylesheet" href="${urlBase}/CSS/style.css">
</head>
<body class="bg-light">



    <div class="container text-center my-5 py-5">
        <i class="bi bi-cone-striped display-1 text-warning mb-3 d-block"></i>
        <h1 class="fw-bold display-4 text-dark">¡Uy! Error 500</h1>
        <p class="lead text-muted mb-4">Nuestros servidores están teniendo un pequeño cortocircuito. Ya estamos trabajando para repararlo.</p>
        
        <form action="${urlBase}/FrontController" method="POST" style="margin: 0;">
            <input type="hidden" name="accion" value="inicio">
            <button type="submit" class="btn btn-rebex btn-lg px-5 fw-bold shadow-sm">
                <i class="bi bi-house-door me-2"></i>Volver al Inicio Seguro
            </button>
        </form>
    </div>

    <jsp:include page="/INCLUDE/pie.jsp" />
    <jsp:include page="/INCLUDE/modal.jsp" />

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${urlBase}/JS/validaciones.js"></script>
    <script src="${urlBase}/JS/login.js"></script>
</body>
</html>