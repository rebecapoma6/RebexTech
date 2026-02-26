<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="urlBase" value="${pageContext.request.contextPath}" />


<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<div class="modal fade" id="loginModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg">
            <div class="modal-header text-white modal-header-rebex">
                <h5 class="modal-title fw-bold">Acceso Clientes</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body p-4">
                <form id="formLogin" action="${urlBase}/UsuarioController" method="POST">
                    <input type="hidden" name="accion" value="login">
                    <div class="mb-3">
                        <label class="form-label fw-bold">Email</label>
                        <input type="email" name="email" class="form-control form-control-rebex">
                    </div>
                    <div class="mb-4">
                        <label class="form-label fw-bold">Contraseña</label>
                        <input type="password" name="password" class="form-control form-control-rebex">
                    </div>
                    <button type="submit" class="btn btn-rebex w-100 py-2 fw-bold">ENTRAR</button>
                </form>
            </div>
            <div class="modal-footer justify-content-center border-0">
                <p class="small">¿No tienes cuenta? 
                    <a href="#" class="text-morado fw-bold text-decoration-none" 
                       data-bs-dismiss="modal" 
                       data-bs-toggle="modal" 
                       data-bs-target="#registroModal">Regístrate aquí</a>
                </p>
            </div>
        </div>
    </div>
</div>



                            <!--REGISTRO DE NUEVO CLIENTE-->




    <div class="modal fade" id="registroModal" tabindex="-1" aria-labelledby="registroModalLabel">
        <div class="modal-dialog modal-xl modal-dialog-centered">
            <div class="modal-content border-0 overflow-hidden shadow-lg">
                <div class="row g-0">
                    <div class="col-lg-4 d-none d-lg-flex flex-column justify-content-center align-items-center text-white p-5 sidebar-registro-gradient">
                        <img src="${urlBase}/IMAGENES/LogoRebexTech.png" alt="Logo" class="logo-sidebar-size">
                    <h3 class="mt-4 fw-bold text-center">¡Únete a la élite de RebexTech!</h3>
                </div>

                <div class="col-lg-8 bg-white p-4 p-md-5">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h4 class="fw-bold mb-0">Crea tu Cuenta de Cliente</h4>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>

                    <div id="cajaErrores"></div>

                    <form id="formRegistro" enctype="multipart/form-data" onsubmit="return false;">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Email</label>
                                <input type="email" name="email" id="emailInput" class="form-control form-control-rebex">
                            </div>
                            <div class="col-md-3">
                                <label class="form-label small fw-bold">Contraseña</label>
                                <input type="password" name="password" class="form-control form-control-rebex">
                            </div>
                            <div class="col-md-3">
                                <label class="form-label small fw-bold">Confirmar Contraseña</label>
                                <input type="password" name="confirmarPassword" class="form-control form-control-rebex">
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-4">
                                <label class="form-label small fw-bold">NIF</label>
                                <div class="input-group">
                                    <input type="text" id="nifInput" maxlength="8" class="form-control form-control-rebex">

                                    <input type="text" id="letraNif" class="form-control fw-bold text-center bg-light" style="max-width: 50px;" readonly >

                                    <input type="hidden" name="nif" id="nifFinal">
                                </div>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label small fw-bold">Nombre</label>
                                <input type="text" name="nombre" class="form-control form-control-rebex">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label small fw-bold">Apellidos</label>
                                <input type="text" name="apellidos" class="form-control form-control-rebex">
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Dirección</label>
                                <input type="text" name="direccion" class="form-control form-control-rebex">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Localidad</label>
                                <input type="text" name="localidad" class="form-control form-control-rebex">
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-4">
                                <label class="form-label small fw-bold">Provincia</label>
                                <input type="text" name="provincia" class="form-control form-control-rebex">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label small fw-bold">C. Postal</label>
                                <input type="text" name="codigo_postal" maxlength="5" class="form-control form-control-rebex">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label small fw-bold">Teléfono</label>
                                <input type="text" name="telefono" maxlength="9" class="form-control form-control-rebex">
                            </div>
                        </div>

                        <div class="mb-4">
                            <label class="form-label small fw-bold text-morado">Sube tu Imagen de Perfil (Avatar)</label>
                            <input type="file" name="avatar_file" class="form-control border-rebex" accept="image/*">
                            <div class="form-text">Máximo 100KB (Solo JPG, PNG o GIF).</div>
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
<script src="${pageContext.request.contextPath}/JS/login.js"></script>
