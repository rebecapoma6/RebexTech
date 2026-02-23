<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="url" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <jsp:include page="/INCLUDE/metas.inc"/>
        <title>Mi Perfil - RebexTech</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${url}/CSS/style.css">
    </head>
    <body class="bg-light">
        <jsp:include page="/INCLUDE/navbar.jsp" />

        <div class="container my-5 pt-5">
            <div class="row">
                <div class="col-lg-4 mb-4">
                    <div class="card border-0 shadow-sm text-center p-4 rounded-4">
                        <div class="position-relative d-inline-block mx-auto mb-3">
                            <img id="avatarPreview" src="${url}/IMAGENES/avatares/${sessionScope.usuarioSesion.avatar}" 
                                 class="rounded-circle img-thumbnail shadow-sm" 
                                 style="width: 150px; height: 150px; object-fit: cover;">

                            <label for="inputAvatar" 
                                   class="btn btn-morado-rebex btn-sm position-absolute bottom-0 end-0 rounded-circle shadow d-flex align-items-center justify-content-center"
                                   style="width: 38px; height: 38px; padding: 0; background-color: #6f42c1; border: 2px solid white; cursor: pointer;">
                                <i class="bi bi-camera-fill text-white" style="font-size: 1.1rem;"></i>
                            </label>

                            <input type="file" id="inputAvatar" style="display:none" accept="image/*">
                        </div>
                        <h4 class="fw-bold">${sessionScope.usuarioSesion.nombre} ${sessionScope.usuarioSesion.apellidos}</h4>
                        <p class="text-muted small">${sessionScope.usuarioSesion.email}</p>
                        <hr>
                        <div class="nav flex-column nav-pills border-0" id="v-pills-tab" role="tablist">
                            <button class="nav-link active text-start mb-2" data-bs-toggle="pill" data-bs-target="#datos">
                                <i class="bi bi-person-lines-fill me-2"></i> Mis Datos
                            </button>
                            <button class="nav-link text-start mb-2" data-bs-toggle="pill" data-bs-target="#seguridad">
                                <i class="bi bi-shield-lock-fill me-2"></i> Seguridad
                            </button>
                        </div>
                    </div>
                </div>

                <div class="col-lg-8">
                    <div class="card border-0 shadow-sm p-4 rounded-4 bg-white">
                        <div class="tab-content" id="v-pills-tabContent">

                            <div class="tab-pane fade show active" id="datos">
                                <h5 class="fw-bold mb-4 section-title-rebex">INFORMACIÓN PERSONAL</h5>
                                <form id="formUpdateDatos">
                                    <div class="row g-3">
                                        <div class="col-md-6">
                                            <label class="form-label small fw-bold">Email (No editable)</label>
                                            <input type="text" class="form-control bg-light" value="${sessionScope.usuarioSesion.email}" readonly>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label small fw-bold">NIF</label>
                                            <input type="text" name="nif" class="form-control form-control-rebex" 
                                                   value="${sessionScope.usuarioSesion.nif}" readonly>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label small fw-bold">Nombre</label>
                                            <input type="text" name="nombre" class="form-control form-control-rebex" value="${sessionScope.usuarioSesion.nombre}">
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label small fw-bold">Apellidos</label>
                                            <input type="text" name="apellidos" class="form-control form-control-rebex" value="${sessionScope.usuarioSesion.apellidos}">
                                        </div>
                                        <div class="col-12">
                                            <label class="form-label small fw-bold">Dirección</label>
                                            <input type="text" name="direccion" class="form-control form-control-rebex" value="${sessionScope.usuarioSesion.direccion}">
                                        </div>
                                        <div class="row g-3 mt-2">
                                            <div class="col-md-4">
                                                <label class="form-label small fw-bold">Localidad</label>
                                                <input type="text" name="localidad" class="form-control form-control-rebex" value="${sessionScope.usuarioSesion.localidad}">
                                            </div>
                                            <div class="col-md-4">
                                                <label class="form-label small fw-bold">Provincia</label>
                                                <input type="text" name="provincia" class="form-control form-control-rebex" value="${sessionScope.usuarioSesion.provincia}">
                                            </div>
                                            <div class="col-md-4">
                                                <label class="form-label small fw-bold">Código Postal</label>
                                                <input type="text" name="codigo_postal" class="form-control form-control-rebex" value="${sessionScope.usuarioSesion.codigo_postal}">
                                            </div>
                                            <div class="col-md-12">
                                                <label class="form-label small fw-bold">Teléfono</label>
                                                <input type="text" name="telefono" class="form-control form-control-rebex" value="${sessionScope.usuarioSesion.telefono}">
                                            </div>
                                        </div>
                                    </div>
                                    <button type="submit" class="btn btn-rebex mt-4 px-4">GUARDAR CAMBIOS</button>
                                </form>
                            </div>

                            <div class="tab-pane fade" id="seguridad">
                                <h5 class="fw-bold mb-4 section-title-rebex">CAMBIAR CONTRASEÑA</h5>
                                <form id="formUpdatePass">
                                    <div class="mb-3">
                                        <label class="form-label small fw-bold">Contraseña Actual</label>
                                        <input type="password" name="password" class="form-control form-control-rebex">
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label small fw-bold">Nueva Contraseña</label>
                                        <input type="password" name="newPass" class="form-control form-control-rebex">
                                    </div>
                                    <div class="mb-4">
                                        <label class="form-label small fw-bold">Confirmar Nueva Contraseña</label>
                                        <input type="password" name="confirmPass" class="form-control form-control-rebex">
                                    </div>
                                    <button type="submit" class="btn btn-rebex px-4">ACTUALIZAR CONTRASEÑA</button>
                                </form>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="/INCLUDE/pie.jsp" />
        <jsp:include page="/INCLUDE/modal.jsp" />

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${url}/JS/perfil.js"></script> </body>
</html>