<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="imgLogo" value="${pageContext.request.contextPath}/IMAGENES" />
<c:set var="url" value="${pageContext.request.contextPath}" />


<%--<c:if test="${not empty sessionScope.alerta}">
    <div class="alert alert-${sessionScope.tipoAlerta} alert-dismissible fade show text-center m-0" role="alert" style="border-radius: 0;">
        <strong>${sessionScope.alerta}</strong>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="alerta" scope="session"/>
    <c:remove var="tipoAlerta" scope="session"/>
</c:if>--%>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow">
    <div class="container">
        <a class="navbar-brand" href="${url}/FrontController">
            <img src="${imgLogo}/LogoRebexTech.png" 
                 alt="RebexTech" 
                 class="logo-rebex">
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle text-white fw-bold" href="#" id="catMenu" data-bs-toggle="dropdown">
                        <i class="bi bi-list me-1"></i> CATEGORÍAS
                    </a>
                    <ul class="dropdown-menu shadow border-0 p-3" style="min-width: 250px;">
                        <form action="${url}/FrontController" method="POST" style="margin: 0;">
                            <input type="hidden" name="accion" value="buscar">

                            <h6 class="dropdown-header ps-0 text-dark fw-bold">SELECCIONA CATEGORÍAS</h6>

                            <c:forEach var="cat" items="${categoriasGlobales}">
                                <li class="mb-2">
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="idcategoria" 
                                               value="${cat.idcategoria}" id="navCat-${cat.idcategoria}">
                                        <label class="form-check-label small" for="navCat-${cat.idcategoria}">
                                            ${cat.nombre}
                                        </label>
                                    </div>
                                </li>
                            </c:forEach>

                            <li><hr class="dropdown-divider"></li>

                            <li class="mt-2">
                                <button type="submit" class="btn btn-rebex btn-sm w-100 fw-bold">
                                    FILTRAR CATEGORÍAS
                                </button>
                            </li>
                        </form>
                    </ul>
                </li>
            </ul>

            <form class="d-flex mx-auto w-50" action="${url}/FrontController" method="POST">
                <input type="hidden" name="accion" value="buscar">
                <input class="form-control me-2 form-control-rebex" type="search" name="busqueda" placeholder="¿Qué componente buscas?">
                <button class="btn btn-rebex" type="submit"><i class="bi bi-search"></i></button>
            </form>

            <ul class="navbar-nav ms-auto align-items-center">
                <li class="nav-item me-3">
                    <form action="${url}/FrontController" method="POST" id="formIconoCarrito" style="display:inline;">
                        <input type="hidden" name="accion" value="verCarrito">
                        <a class="nav-link position-relative icono-carrito-rebex" href="javascript:void(0)" onclick="document.getElementById('formIconoCarrito').submit();">
                            <i class="bi bi-cart-fill fs-4 text-white"></i>
                            <span id="cantidadProductosNavbar" class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger tamano-badge-carrito">
                                ${empty sessionScope.cantidadProductos ? 0 : sessionScope.cantidadProductos}
                            </span>
                        </a>
                    </form>
                </li>

                <c:choose>
                    <c:when test="${empty sessionScope.usuarioSesion}">
                        <li class="nav-item">
                            <a class="btn btn-rebex px-4" href="#" data-bs-toggle="modal" data-bs-target="#loginModal">
                                Registro / Acceder
                            </a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle text-white fw-bold" href="#" id="menuUsuario" data-bs-toggle="dropdown">
                                <img src="${url}/IMAGENES/avatares/${sessionScope.usuarioSesion.avatar}" 
                                     class="rounded-circle me-1" 
                                     style="width: 24px; height: 24px; object-fit: cover;">
                                Hola, ${sessionScope.usuarioSesion.nombre}
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end shadow border-0">
                                <li><a class="dropdown-item" href="${url}/USUARIO/perfil.jsp">Mi Perfil</a></li>
                                <li>
                                    <form action="${url}/FrontController" method="POST" style="margin:0;">
                                        <input type="hidden" name="accion" value="historialPedidos">
                                        <button type="submit" class="dropdown-item">Mis Pedidos</button>
                                    </form>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <form action="${url}/FrontController" method="POST" style="margin:0;">
                                        <input type="hidden" name="accion" value="salir">
                                        <button type="submit" class="dropdown-item text-danger">Cerrar Sesión</button>
                                    </form>
                                </li>
                            </ul>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>