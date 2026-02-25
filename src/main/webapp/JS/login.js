// login.js
const LOGIN_AJAX = 'UsuarioController';

document.addEventListener("DOMContentLoaded", () => {
    const formLogin = document.getElementById('formLogin');

    formLogin?.addEventListener('submit', async (e) => {
        e.preventDefault();

        // Obtenemos los datos del formulario
        const formData = new FormData(formLogin);

        // Convertimos el FormData a una cadena tipo URL: email=user@test.com&password=123&accion=login
        const dataURL = new URLSearchParams(formData).toString();

        try {
            const resp = await fetch(LOGIN_AJAX, {
                method: 'POST',
                body: dataURL, // Mandamos la cadena de texto
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded' // Estilo Profe
                }
            });

            const data = await resp.json();

            if (data.status === 'success') {
                Swal.fire({
                    icon: 'success',
                    title: '¡Acceso Concedido!',
                    text: 'Bienvenida a RebexTech, estamos cargando tu sesión...',
                    showConfirmButton: false,
                    timer: 2000, // 2 segundos de gloria
                    timerProgressBar: true
                }).then(() => {
                    // Recién cuando termina el tiempo, nos vamos al FrontController
                    window.location.href = 'FrontController';
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: '¡Acceso Denegado!',
                    text: data.mensaje,
                    confirmButtonColor: '#6f42c1'
                });
            }
        } catch (error) {
            console.error("Error en login:", error);
        }
    });
});