
document.addEventListener("DOMContentLoaded", () => {
    const formLogin = document.getElementById('formLogin');

    formLogin?.addEventListener('submit', async (e) => {
        e.preventDefault(); 
        const formData = new FormData(formLogin);
        const dataURL = new URLSearchParams(formData).toString();

        try {
            const resp = await fetch(RUTA_LOGIN, {
                method: 'POST',
                body: dataURL,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });

            // Si el servidor responde con JSON, lo atrapamos aquí
            const data = await resp.json();

            if (data.status === 'success') {
                Swal.fire({
                    icon: 'success',
                    title: '¡Acceso Concedido!',
                    text: 'Bienvenida a RebexTech...',
                    showConfirmButton: false,
                    timer: 2000,
                    timerProgressBar: true
                }).then(() => {
                    // Refrescamos al FrontController para que cargue la sesión
                    window.location.href = 'FrontController';
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: '¡Acceso Denegado!',
                    text: data.mensaje || 'Credenciales incorrectas',
                    confirmButtonColor: '#6f42c1'
                });
            }
        } catch (error) {
            // Si el fetch falla (error 404, 500), entrará aquí
            console.error("Error en login:", error);
        }
    });
});