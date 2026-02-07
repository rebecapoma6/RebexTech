<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="estilo" value="${pageContext.request.contextPath}/CSS/style.css" />


<!DOCTYPE html>
<html lang="es">
<head>
    <jsp:include page="INCLUDE/metas.inc"/>
    
    <title>RebexTech</title>

    <link rel="stylesheet" href="${estilo}">
</head>


    <body>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow">
        <div class="container">
            <a class="navbar-brand" href="index.jsp"> 
                <img src="IMAGENES/LogoRebexTech.png" alt="RebexTech" class="logo-rebex">
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <form class="d-flex mx-auto w-50" action="CatalogoController" method="GET">
                    <input class="form-control me-2 form-control-rebex" type="search" name="busqueda" placeholder="Buscar componentes...">
                    <button class="btn btn-rebex" type="submit"><i class="bi bi-search"></i></button>
                </form>
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="btn btn-rebex px-4" href="#" data-bs-toggle="modal" data-bs-target="#loginModal">Registro / Iniciar Sesión</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div id="heroRebex" class="carousel slide" data-bs-ride="carousel">
        <div class="carousel-inner">
            <div class="carousel-item active hero-height hero-bg-dark">
                <div class="container h-100">
                    <div class="row h-100 align-items-center">
                        <div class="col-md-6 text-white">
                            <h1 class="display-3 fw-bold">REBEXTECH</h1>
                            <p class="fs-4">Potencia sin límites para tu Setup.</p>
                            <a href="#ventas" class="btn btn-rebex btn-lg px-5">Ver Ofertas</a>
                        </div>
                        <div class="col-md-6 text-center">
                            <img src="IMAGENES/productos/graficas/14259396172475971.jpg" 
                                 class="img-fluid floating-img hero-img-size" style="width: 350px;height: 300px" alt="Producto Destacado">
                        </div>
                    </div>
                </div>
            </div>

            <div class="carousel-item hero-height hero-bg-gradient">
                <div class="container h-100 d-flex align-items-center justify-content-between">
                    <div class="text-white">
                        <h2 class="display-4 fw-bold">NUEVA GENERACIÓN</h2>
                        <p class="fs-5">Procesadores Intel y AMD al mejor precio.</p>
                    </div>
                    <img src="IMAGENES/productos/intelsocket/14258596456403026.jpg" 
                         class="img-fluid hero-img-size hero-img-shadow" style="width: 350px;height: 300px" alt="CPU">
                </div>
            </div>
        </div>
    </div>

    <section id="ventas" class="container my-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold m-0 section-title-rebex">LOS MÁS VENDIDOS</h2>
        </div>
        <div class="row g-4">
            <c:forEach var="p" items="${applicationScope.productosLanding}">
                <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                    <div class="card h-100 border-0 shadow-sm card-producto">
                        <div class="p-4 bg-light text-center">
                            <img src="${pageContext.request.contextPath}/IMAGENES/productos/${p.imagen}" alt="${p.nombre}" class="img-fluid img-producto-landing">
                        </div>
                        <div class="card-body d-flex flex-column pt-3">
                            <small class="text-uppercase text-muted fw-bold">${p.marca}</small>
                            <h5 class="card-title h6 fw-bold text-dark">${p.nombre}</h5>
                            <div class="mt-auto d-flex justify-content-between align-items-end">
                                <p class="text-primary fs-5 fw-bold mb-0">${p.precio}€</p>
                                <a href="CarritoController?id=${p.idproducto}" class="btn btn-rebex rounded-3"><i class="bi bi-cart-plus-fill"></i></a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </section>

    <section id="sobre-nosotros" class="py-5 bg-light">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-lg-6 position-relative">
                    <img src="IMAGENES/marcas/equipoRebex.jpg" alt="Equipo" class="img-fluid rounded-4 shadow-lg img-nosotros">
                    <div class="decoracion-morada d-none d-md-block"></div>
                </div>
                <div class="col-lg-6 ps-lg-5">
                    <h6 class="text-uppercase fw-bold text-morado">¿Quiénes somos?</h6>
                    <h2 class="display-5 fw-bold mb-4">Pasión por el Hardware de Alto Rendimiento</h2>
                    <p class="lead text-muted">En <strong>RebexTech</strong>, no solo vendemos componentes; construimos los sueños de cada gamer.</p>
                    <p>Nacimos en Mérida con un objetivo claro: ofrecer el hardware más puntero del mercado con un asesoramiento técnico real.</p>
                    <div class="row mt-4">
                        <div class="col-sm-6 mb-3">
                            <div class="d-flex align-items-center">
                                <i class="bi bi-check-circle-fill fs-3 me-3 text-morado"></i>
                                <span class="fw-bold">Garantía Oficial</span>
                            </div>
                        </div>
                        <div class="col-sm-6 mb-3">
                            <div class="d-flex align-items-center">
                                <i class="bi bi-truck fs-3 me-3 text-morado"></i>
                                <span class="fw-bold">Envío en 24/48h</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section class="logo-slider">
        <div class="logo-slide-track">
            <c:forEach var="i" begin="1" end="2">
                <div class="slide"><img src="IMAGENES/marcas/intel.png" alt="Intel"></div>
                <div class="slide"><img src="IMAGENES/marcas/amd.png" alt="AMD"></div>
                <div class="slide"><img src="IMAGENES/marcas/nvidia.png" alt="NVIDIA"></div>
                <div class="slide"><img src="IMAGENES/marcas/sata.png" alt="SATA"></div>
                <div class="slide"><img src="IMAGENES/marcas/ssd.png" alt="SSD"></div>
                <div class="slide"><img src="IMAGENES/marcas/radeon.png" alt="Radeon"></div>
            </c:forEach>
        </div>
    </section>

    <footer class="footer-rebex text-white py-5">
        <div class="container">
            <div class="row">
                <div class="col-md-4 mb-4">
                    <h5 class="fw-bold mb-3">REBEXTECH</h5>
                    <p class="small opacity-75">Tu tienda de confianza para el montaje de PCs personalizados.</p>
                </div>
                <div class="col-md-4 mb-4 text-center">
                    <h6 class="fw-bold mb-3">Enlaces Rápidos</h6>
                    <ul class="list-unstyled small">
                        <li><a href="#" class="text-white-50 text-decoration-none">Inicio</a></li>
                        <li><a href="#ventas" class="text-white-50 text-decoration-none">Lo más vendido</a></li>
                        <li><a href="#sobre-nosotros" class="text-white-50 text-decoration-none">Nosotros</a></li>
                    </ul>
                </div>
                <div class="col-md-4 mb-4 text-md-end">
                    <h6 class="fw-bold mb-3">Contacto</h6>
                    <p class="small mb-1"><i class="bi bi-geo-alt me-2"></i> Mérida, España</p>
                    <p class="small mb-1"><i class="bi bi-envelope me-2"></i> info@rebextech.es</p>
                    <div class="mt-3">
                        <a href="#" class="text-white me-3"><i class="bi bi-instagram"></i></a>
                        <a href="#" class="text-white me-3"><i class="bi bi-twitter-x"></i></a>
                    </div>
                </div>
            </div>
            <hr class="my-4 opacity-25">
            <div class="text-center small opacity-50">&copy; 2026 RebexTech. Todos los derechos reservados.</div>
        </div>
    </footer>

    <div class="modal fade" id="loginModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg">
                <div class="modal-header text-white modal-header-rebex">
                    <h5 class="modal-title fw-bold">Acceso Clientes</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body p-4">
                    <form action="LoginController" method="POST">
                        <div class="mb-3">
                            <label class="form-label fw-bold">Email</label>
                            <input type="email" name="email" class="form-control form-control-rebex" required>
                        </div>
                        <div class="mb-4">
                            <label class="form-label fw-bold">Contraseña</label>
                            <input type="password" name="password" class="form-control form-control-rebex" required>
                        </div>
                        <button type="submit" class="btn btn-rebex w-100 py-2 fw-bold">ENTRAR</button>
                    </form>
                </div>
                <div class="modal-footer justify-content-center border-0">
                    <p class="small">¿No tienes cuenta? 
                        <a href="#" class="text-morado fw-bold text-decoration-none" 
                           data-bs-toggle="modal" data-bs-target="#registroModal">Regístrate aquí</a>
                    </p>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="registroModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-xl modal-dialog-centered">
            <div class="modal-content border-0 overflow-hidden shadow-lg">
                <div class="row g-0">
                    <div class="col-lg-4 d-none d-lg-flex flex-column justify-content-center align-items-center text-white p-5 sidebar-registro-gradient">
                        <img src="IMAGENES/LogoRebexTech.png" alt="Logo" class="logo-sidebar-size">
                        <h3 class="mt-4 fw-bold">¡Únete a la élite!</h3>
                    </div>
                    <div class="col-lg-8 bg-white p-4 p-md-5">
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <h4 class="fw-bold mb-0">Formulario de Registro</h4>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <form action="RegistroController" method="POST">
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="form-label small fw-bold">Email</label>
                                    <input type="email" name="email" class="form-control form-control-rebex" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label small fw-bold">Contraseña</label>
                                    <input type="password" name="password" class="form-control form-control-rebex" required>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold">NIF</label>
                                    <input type="text" name="nif" maxlength="9" class="form-control form-control-rebex" required>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold">Nombre</label>
                                    <input type="text" name="nombre" class="form-control form-control-rebex" required>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold">Apellidos</label>
                                    <input type="text" name="apellidos" class="form-control form-control-rebex" required>
                                </div>
                            </div>
                            <div class="row mb-4">
                                <div class="col-md-6">
                                    <label class="form-label small fw-bold">Dirección</label>
                                    <input type="text" name="direccion" class="form-control form-control-rebex" required>
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label small fw-bold">C. Postal</label>
                                    <input type="text" name="codigo_postal" maxlength="5" class="form-control form-control-rebex" required>
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label small fw-bold">Teléfono</label>
                                    <input type="text" name="telefono" maxlength="9" class="form-control form-control-rebex">
                                </div>
                            </div>
                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-rebex btn-lg fw-bold">CREAR CUENTA</button>
                                <button type="button" class="btn btn-link text-muted small" 
                                        data-bs-toggle="modal" data-bs-target="#loginModal">¿Ya tienes cuenta? Inicia sesión</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>