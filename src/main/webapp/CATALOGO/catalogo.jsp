<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="baseUrl" value="${pageContext.request.contextPath}" />
<c:set var="imgBase" value="${pageContext.request.contextPath}/IMAGENES/productos" />
<c:set var="estilo" value="${pageContext.request.contextPath}/CSS/style.css" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <jsp:include page="/INCLUDE/metas.inc"/>
        <jsp:include page="/INCLUDE/modal.jsp" />
        <title>Catalogo-RebexTech</title>
        <link rel="stylesheet" href="${estilo}">
    </head>
    <body>
        <jsp:include page="/INCLUDE/navbar.jsp" />
        <div class="container mt-4">
            <div class="card border-0 shadow-sm p-4 bg-white rounded-4 mb-5">
                <h5 class="text-dark fw-bold mb-3"><i class="bi bi-sliders2-vertical me-2"></i>Refinar Búsqueda</h5>
                <form action="${baseUrl}/FrontController" method="POST" class="row g-3 align-items-end">
                    <input type="hidden" name="accion" value="buscar">

                    <div class="col-md-4">
                        <label class="form-label small fw-bold text-muted">Marca</label>
                        <div class="input-group">
                            <span class="input-group-text bg-light border-0"><i class="bi bi-search"></i></span>
                            <input type="text" name="busqueda" class="form-control border-0 bg-light" placeholder="Ej: Acer, RTX, Teclado..." value="${param.busqueda}">
                        </div>
                    </div>

                            <div class="col-md-3">
    <label class="form-label small fw-bold text-muted mb-2">Categorías</label>
    <div class="dropdown">
        <button class="btn bg-light w-100 text-start d-flex justify-content-between align-items-center border-0 py-2" 
                type="button" id="multiSelectCat" data-bs-toggle="dropdown" aria-expanded="false" 
                style="border-radius: 8px; color: #6c757d;">
            <span>Seleccionar...</span>
            <i class="bi bi-chevron-down small"></i>
        </button>
        
        <ul class="dropdown-menu shadow border-0 p-3 w-100 scroll-personalizado" 
            aria-labelledby="multiSelectCat" 
            style="max-height: 200px; overflow-y: auto;">
            
            <c:forEach var="cat" items="${categoriasGlobales}">
                <li class="mb-2">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="idcategoria" 
                               value="${cat.idcategoria}" id="catCheck-${cat.idcategoria}"
                               <c:if test="${not empty paramValues.idcategoria}">
                                   <c:forEach var="selectedId" items="${paramValues.idcategoria}">
                                       <c:if test="${selectedId == cat.idcategoria}">checked</c:if>
                                   </c:forEach>
                               </c:if>>
                        <label class="form-check-label small" for="catCheck-${cat.idcategoria}">
                            ${cat.nombre}
                        </label>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>

                    <div class="col-md-3">
                        <label class="form-label small fw-bold text-muted">Rango de Precio</label>
                        <select name="rangoPrecio" class="form-select border-0 bg-light">
                            <option value="">Cualquier Precio</option>
                            <option value="0-100" ${param.rangoPrecio == '0-100' ? 'selected' : ''}>Hasta 100€</option>
                            <option value="100-500" ${param.rangoPrecio == '100-500' ? 'selected' : ''}>100€ - 500€</option>
                            <option value="500-1000" ${param.rangoPrecio == '500-1000' ? 'selected' : ''}>500€ - 1000€</option>
                            <option value="1000-9999" ${param.rangoPrecio == '1000-9999' ? 'selected' : ''}>+1000€</option>
                        </select>
                    </div>

                    <div class="col-md-2">
                        <button type="submit" class="btn btn-rebex w-100 fw-bold shadow-sm">
                            APLICAR
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <div class="row g-4">
            <c:choose>
                <c:when test="${empty productosLanding}">
                    <div class="col-12 text-center py-5">
                        <i class="bi bi-emoji-frown fs-1 text-muted"></i>
                        <p class="fs-4 text-muted mt-3">No hay productos que coincidan con esos filtros.</p>
                        <a href="${baseUrl}/FrontController?accion=inicio" class="btn btn-rebex">Limpiar filtros</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="produ" items="${productosLanding}">
                        <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                            <div class="card h-100 border-0 shadow-sm card-producto">
                                <div class="p-4 bg-light text-center">
                                    <img src="${imgBase}/${produ.imagen}" alt="${produ.nombre}" class="img-fluid img-producto-landing">
                                </div>
                                <div class="card-body d-flex flex-column pt-3">
                                    <small class="text-uppercase text-muted fw-bold">${produ.marca}</small>
                                    <h5 class="card-title h6 fw-bold text-dark">${produ.nombre}</h5>
                                    <div class="mt-auto">
                                        <p class="text-primary fs-5 fw-bold mb-2">${produ.precio}€</p>
                                        <div class="d-flex gap-2">
                                            <form action="${baseUrl}/FrontController" method="POST">
                                                <input type="hidden" name="accion" value="verCarrito">
                                                <input type="hidden" name="accionCarrito" value="agregar">
                                                <input type="hidden" name="idProducto" value="${produ.idproducto}">

                                                <button type="submit" class="btn btn-rebex w-100 rounded-3">
                                                    <i class="bi bi-cart-plus-fill me-1"></i> AÑADIR
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

        <jsp:include page="/INCLUDE/pie.jsp" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${baseUrl}/JS/validaciones.js"></script>

    </body>
</html>
