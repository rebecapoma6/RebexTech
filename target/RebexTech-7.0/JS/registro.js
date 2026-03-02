document.addEventListener("DOMContentLoaded", () => {

    // ------------------------------
    // 1. Lógica del Registro
    // ------------------------------
    const inicializarRegistro = () => {
        const formRegistro = document.getElementById('formRegistro');
        const cajaErrores = document.getElementById('cajaErrores');

        if (!formRegistro) return;

        formRegistro.addEventListener('submit', async (e) => {
            e.preventDefault();
            const datosFormulario = new FormData(formRegistro);

            try {
                const respuesta = await fetch(URL_REGISTRO, {
                    method: 'POST',
                    body: datosFormulario
                });

                const resultado = await respuesta.json();

                if (resultado.status === 'error') {
                    mostrarError(cajaErrores, resultado.mensaje);
                } else if (resultado.status === 'success') {
                    cerrarModalRegistro();
                    mostrarExitoYRedirigir();
                }
            } catch (error) {
                mostrarErrorServidor();
            }
        });
    };

    // ------------------------------
    // 2. Lógica del Avatar PREVIO EN EL REGISTRO
    // ------------------------------
    const inicializarAvatarRegistro = () => {
        const inputAvatarRegistro = document.getElementById('inputAvatarRegistro');
        const previewLogoRegistro = document.getElementById('previewLogoRegistro');

        if (!inputAvatarRegistro || !previewLogoRegistro) return;

        inputAvatarRegistro.addEventListener('change', (event) => {
            const archivo = event.target.files[0];
            if (!archivo) return;

            const reader = new FileReader();
            reader.onload = (e) => {
                previewLogoRegistro.src = e.target.result;
                previewLogoRegistro.style.width = "150px";
                previewLogoRegistro.style.height = "150px";
                previewLogoRegistro.style.objectFit = "cover";
                previewLogoRegistro.style.filter = "none";
            };
            reader.readAsDataURL(archivo);
        });
    };

    // ------------------------------
    // 3. Funciones Auxiliares
    // ------------------------------
    const mostrarError = (contenedor, mensaje) => {
        contenedor.innerHTML = `
            <div class="alert alert-danger alert-dismissible fade show shadow-sm mb-3">
                <i class="bi bi-exclamation-triangle-fill me-2"></i> ${mensaje}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>`;
    };

    const cerrarModalRegistro = () => {
        const modalElement = document.getElementById('registroModal');
        const modalInstancia = bootstrap.Modal.getInstance(modalElement);
        if (modalInstancia) modalInstancia.hide();
    };

    const mostrarExitoYRedirigir = () => {
        Swal.fire({
            icon: 'success',
            title: '¡Registro Exitoso!',
            text: 'Bienvenida a la élite de RebexTech',
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: true,
            zIndex: 10000
        }).then(() => {
            window.location.href = 'FrontController';
        });
    };

    const mostrarErrorServidor = () => {
        Swal.fire({
            icon: 'error',
            title: 'Error de servidor',
            text: 'No pudimos conectar con el servidor, intenta más tarde.',
            confirmButtonColor: '#6f42c1'
        });
    };

    // ------------------------------
    // ¡EJECUTAMOS LAS FUNCIONES!
    // ------------------------------
    inicializarRegistro();
    inicializarAvatarRegistro();
});