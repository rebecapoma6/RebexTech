const URL_AJAX = 'AjaxController';
const URL_REGISTRO = 'RegistroController';

document.addEventListener("DOMContentLoaded", () => {

    const campoDNI = document.getElementById('nifInput');
    const campoEmail = document.getElementById('emailInput'); // Añadido para el inciso A
    const formRegistro = document.getElementById('formRegistro');
    const cajaErrores = document.getElementById('cajaErrores');

    // Función auxiliar para pintar errores al estilo Bootstrap
    const mostrarError = (mensaje) => {
        cajaErrores.innerHTML = `
            <div class="alert alert-danger alert-dismissible fade show shadow-sm mb-3">
                <i class="bi bi-exclamation-triangle-fill me-2"></i> ${mensaje}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>`;
    };

    // ==========================================
    // 1. AJAX: VALIDAR NIF (Inciso D del PDF)
    // ==========================================
    if (campoDNI) {
        campoDNI.addEventListener('blur', async () => {
            let dni = campoDNI.value.trim();

            // Si tiene 8 dígitos y son números
            if (dni.length === 8 && !isNaN(dni)) {
                const data = new URLSearchParams();
                data.append('accion', 'validarDni');
                data.append('dni', dni);

                try {
                    let respuesta = await fetch(URL_AJAX, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
                        },
                        body: data.toString()
                    });

                    let resultado = await respuesta.json();

                    if (resultado.tipo === 'warning') {
                        campoDNI.value = '';
                        mostrarError(resultado.letra);
                    } else {
                        // Asigna la letra automáticamente como pide el PDF
                        campoDNI.value = dni + resultado.letra;
                        cajaErrores.innerHTML = ''; 
                    }
                } catch (e) { console.error("Error validando NIF"); }
            }
        });
    }

    // ==========================================
    // 2. AJAX: COMPROBAR EMAIL (Inciso A del PDF)
    // ==========================================
    if (campoEmail) {
        campoEmail.addEventListener('blur', async () => {
            let email = campoEmail.value.trim();
            
            if (email.length > 5 && email.includes('@')) {
                const data = new URLSearchParams();
                data.append('accion', 'comprobarEmail');
                data.append('email', email);

                try {
                    let respuesta = await fetch(URL_AJAX, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
                        },
                        body: data.toString()
                    });

                    let resultado = await respuesta.json();

                    if (resultado.status === 'ocupado') {
                        campoEmail.classList.add('is-invalid');
                        mostrarError(resultado.mensaje);
                    } else {
                        campoEmail.classList.remove('is-invalid');
                        campoEmail.classList.add('is-valid');
                    }
                } catch (e) { console.error("Error comprobando email"); }
            }
        });
    }

    // ==========================================
    // 3. AJAX: ENVIAR FORMULARIO (Sin parpadeo)
    // ==========================================
    if (formRegistro) {
        formRegistro.addEventListener('submit', async (e) => {
            console.log("¡El JS sí está escuchando el clic!"); // <--- AÑADE ESTO
        e.preventDefault();
            e.preventDefault(); // Mata el parpadeo de la página
            
            let datosFormulario = new FormData(formRegistro); 

            try {
                let respuesta = await fetch(URL_REGISTRO, {
                    method: 'POST',
                    body: datosFormulario // Incluye el avatar_file
                });

                let resultado = await respuesta.json();

                if (resultado.status === 'error') {
                    mostrarError(resultado.mensaje);
                } else if (resultado.status === 'success') {
                    // Redirección limpia tras éxito
                    window.location.href = 'FrontController';
                }
            } catch (error) {
                mostrarError("Error de conexión con el servidor.");
            }
        });
    }
});