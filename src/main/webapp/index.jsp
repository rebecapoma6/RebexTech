<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="imgBase" value="${pageContext.request.contextPath}/IMAGENES/productos" />
<c:set var="estilo" value="${pageContext.request.contextPath}/CSS/style.css" />


<!DOCTYPE html>
<html lang="es">
    <head>
        <jsp:include page="INCLUDE/metas.inc"/>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <title>RebexTech</title>
        <link rel="stylesheet" href="${estilo}">
    </head>


    <body>
        <jsp:include page="/INCLUDE/navbar.jsp" />

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
                                    <p class="text-primary fs-5 fw-bold mb-2">
                                        <fmt:formatNumber value="${produ.precio}" type="number" minFractionDigits="2" maxFractionDigits="2" />€
                                    </p>
                                    <div class="d-flex gap-2">
                                        <button type="button" class="btn btn-sm btn-outline-rebex w-100 fw-bold" 
                                                data-bs-toggle="modal" 
                                                data-bs-target="#modalDetalles${produ.idproducto}">
                                            DETALLES
                                        </button>

<!--                                        <a href="FrontController?accion=verCarrito&accionCarrito=agregar&idProducto=${produ.idproducto}" 
                                           class="btn btn-rebex rounded-3">
                                            <i class="bi bi-cart-plus-fill"></i>
                                        </a>-->
                                        <form action="FrontController" method="POST" style="display:inline;">
                                            <input type="hidden" name="accion" value="verCarrito">
                                            <input type="hidden" name="accionCarrito" value="agregar">
                                            <input type="hidden" name="idProducto" value="${produ.idproducto}">

                                            <button type="submit" class="btn btn-rebex rounded-3">
                                                <i class="bi bi-cart-plus-fill"></i>
                                            </button>
                                        </form>
                                    </div>
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

        <div class="modal fade" id="modalDetalles${produ.idproducto}" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg">
                    <div class="modal-header modal-header-rebex text-white">
                        <h5 class="modal-title fw-bold">${produ.nombre}</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="text-center mb-4">
                            <img src="${imgBase}/${produ.imagen}" class="img-fluid rounded" style="max-height: 250px;" alt="${produ.nombre}">
                        </div>
                        <h6 class="text-uppercase text-muted fw-bold small">${produ.marca}</h6>
                        <p class="text-dark">
                            ${produ.descripcion} <%-- Aquí cargamos la descripción de la DB --%>
                        </p>
                        <div class="d-flex justify-content-between align-items-center mt-4">
                            <span class="fs-4 fw-bold text-morado">
                                <fmt:formatNumber value="${produ.precio}" type="number" minFractionDigits="2" maxFractionDigits="2" />€
                            </span>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <jsp:include page="/INCLUDE/pie.jsp" />
        <jsp:include page="/INCLUDE/modal.jsp" />

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

        <script src="${pageContext.request.contextPath}/JS/validaciones.js"></script>
        <script src="${pageContext.request.contextPath}/JS/registro.js"></script>
    </body>
</html>