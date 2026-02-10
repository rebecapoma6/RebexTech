<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="imgLogo" value="${pageContext.request.contextPath}/IMAGENES" />

<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/index.jsp"> 
            <img src="${imgLogo}/LogoRebexTech.png" 
                 alt="RebexTech" 
                 class="logo-rebex">
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <form class="d-flex mx-auto w-50" action="${pageContext.request.contextPath}/FrontController" method="GET">
                <input type="hidden" name="accion" value="buscar">
                <input class="form-control me-2 form-control-rebex" type="search" name="busqueda" placeholder="Buscar componentes...">
                <button class="btn btn-rebex" type="submit"><i class="bi bi-search"></i></button>
            </form>

            <ul class="navbar-nav ms-auto align-items-center">
                <li class="nav-item me-3">
                    <a class="nav-link position-relative icono-carrito-rebex" href="${pageContext.request.contextPath}/FrontController?accion=verCarrito">
                        <i class="bi bi-cart-fill fs-4"></i>
                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger tamano-badge-carrito">
                            ${empty sessionScope.cantidadProductos ? 0 : sessionScope.cantidadProductos}
                        </span>
                    </a>
                </li>

                <c:choose>
                    <c:when test="${empty sessionScope.usuarioSesion}">
                        <li class="nav-item">
                            <a class="btn btn-rebex px-4" href="#" data-bs-toggle="modal" data-bs-target="#loginModal">
                                Registro / Iniciar Sesión
                            </a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle text-white fw-bold" href="#" id="menuUsuario" data-bs-toggle="dropdown">
                                Hola, ${sessionScope.usuarioSesion.nombre}
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end shadow border-0">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/perfil.jsp">Mi Perfil</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/FrontController?accion=verPedidos">Mis Pedidos</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/FrontController?accion=salir">Cerrar Sesión</a>
                                </li>
                            </ul>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>                    
        </div>
    </div>
</nav>