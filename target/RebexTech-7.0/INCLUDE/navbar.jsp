<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="imgLogo" value="${pageContext.request.contextPath}/IMAGENES" />
<c:set var="url" value="${pageContext.request.contextPath}" />

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
                    <ul class="dropdown-menu shadow border-0">
                        <c:forEach var="cat" items="${categoriasGlobales}">
                            <li>
                                <form action="${url}/FrontController" method="POST" style="margin: 0;">
                                    <input type="hidden" name="accion" value="buscar">
                                    <input type="hidden" name="idcategoria" value="${cat.idcategoria}">
                                    <button type="submit" class="dropdown-item" style="border:none; background:none; width:100%; text-align:left;">
                                        ${cat.nombre}
                                    </button>
                                </form>
                            </li>
                        </c:forEach>
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
                    <a class="nav-link position-relative icono-carrito-rebex" href="${url}/FrontController?accion=verCarrito">
                        <i class="bi bi-cart-fill fs-4 text-white"></i>
                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger tamano-badge-carrito">
                            ${empty sessionScope.cantidadProductos ? 0 : sessionScope.cantidadProductos}
                        </span>
                    </a>
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
                                <li><a class="dropdown-item" href="${url}/perfil.jsp">Mi Perfil</a></li>
                                <li><a class="dropdown-item" href="${url}/FrontController?accion=verPedidos">Mis Pedidos</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item text-danger" href="${url}/FrontController?accion=salir">Cerrar Sesión</a>
                                </li>
                            </ul>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>