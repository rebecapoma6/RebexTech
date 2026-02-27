<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="urlBase" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">
<head>
    <jsp:include page="/INCLUDE/metas.inc"/>
    <title>Página no encontrada - RebexTech</title>
    <link rel="stylesheet" href="${urlBase}/CSS/style.css">
</head>
<body class="bg-light">


    <div class="container text-center my-5 py-5">
        <i class="bi bi-emoji-frown display-1 text-morado-rebex mb-3 d-block"></i>
        <h1 class="fw-bold display-4 text-dark">¡Ups! Error 404</h1>
        <p class="lead text-muted mb-4">Parece que el componente o la página que buscas se ha perdido en el ciberespacio.</p>
        
        <form action="${urlBase}/FrontController" method="POST" style="margin: 0;">
            <input type="hidden" name="accion" value="inicio">
            <button type="submit" class="btn btn-rebex btn-lg px-5 fw-bold shadow-sm">
                <i class="bi bi-house-door me-2"></i>Volver a la Tienda
            </button>
        </form>
    </div>

    <jsp:include page="/INCLUDE/pie.jsp" />


    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>