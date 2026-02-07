<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
