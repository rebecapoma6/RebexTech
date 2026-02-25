const URL_REGISTRO = 'RegistroController';


document.addEventListener("DOMContentLoaded", () => {
    const formRegistro = document.getElementById('formRegistro');
    const cajaErrores = document.getElementById('cajaErrores');

    formRegistro?.addEventListener('submit', async (e) => {
        e.preventDefault();
        let datosFormulario = new FormData(formRegistro);

        try {
            let respuesta = await fetch(URL_REGISTRO, {
                method: 'POST',
                body: datosFormulario
            });

            let resultado = await respuesta.json();

            if (resultado.status === 'error') {
                cajaErrores.innerHTML = `
                    <div class="alert alert-danger alert-dismissible fade show shadow-sm mb-3">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i> ${resultado.mensaje}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>`;
            } else if (resultado.status === 'success') {
                // 1. Cerramos el modal usando la API de Bootstrap
                const modalElement = document.getElementById('registroModal');
                const modalInstancia = bootstrap.Modal.getInstance(modalElement);

                if (modalInstancia) {
                    modalInstancia.hide();
                }
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
            }
        } catch (error) {
            Swal.fire({
                icon: 'error',
                title: 'Error de servidor',
                text: 'No pudimos conectar con el servidor, intenta más tarde.',
                confirmButtonColor: '#6f42c1'
            });
        }
    });
});