

document.addEventListener("DOMContentLoaded", () => {
    const campoDNI = document.getElementById('nifInput');
    const campoEmail = document.getElementById('emailInput');
    const campoCP = document.getElementsByName('codigo_postal')[0];
    const campoTlf = document.getElementsByName('telefono')[0];
    const cajaErrores = document.getElementById('cajaErrores');

    const mostrarError = (mensaje) => {
        cajaErrores.innerHTML = `
            <div class="alert alert-danger alert-dismissible fade show shadow-sm mb-3">
                <i class="bi bi-exclamation-triangle-fill me-2"></i> ${mensaje}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>`;
    };

    // --- 1. VALIDACIÓN NIF (AJAX) ---
    campoDNI?.addEventListener('blur', async () => {
        let dniNumeros = campoDNI.value.trim();
        const campoLetra = document.getElementById('letraNif');
        const campoFinal = document.getElementById('nifFinal');

       if (dniNumeros.length === 8 && !isNaN(dniNumeros)) {
        try {
            // USAMOS ESTO PARA QUE EL JAVA LO RECONOZCA SÍ O SÍ
            const datosEnvio = new URLSearchParams();
            datosEnvio.append('accion', 'validarDni');
            datosEnvio.append('nif', dniNumeros);

            const res = await fetch(URL_AJAX, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: datosEnvio.toString() // Esto formatea el texto perfecto para el Java
            });
            
            const data = await res.json();

            if (data.tipo === 'success') {
                campoLetra.value = data.letra;
                campoFinal.value = dniNumeros + data.letra;
                campoDNI.classList.replace('is-invalid', 'is-valid');
                cajaErrores.innerHTML = '';
            } else {
                mostrarError(data.letra); // Aquí saldrá el "Faltan dígitos" si el Java sigue sin verlo
                campoDNI.classList.add('is-invalid');
            }
        } catch (e) {
            console.error("Error en la conexión");
        }
    } else {
        campoLetra.value = '';
        campoFinal.value = '';
        if(dniNumeros.length > 0) {
            mostrarError("Ingresa exactamente 8 números.");
            campoDNI.classList.add('is-invalid');
        }
    }
    });

    // --- 2. COMPROBAR EMAIL (AJAX) ---
    campoEmail?.addEventListener('blur', async () => {
        let email = campoEmail.value.trim();
        if (email.includes('@')) {
            try {
                const res = await fetch(URL_AJAX, {
                    method: 'POST',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    body: `accion=comprobarEmail&email=${email}`
                });
                const data = await res.json();
                if (data.status === 'ocupado') {
                    campoEmail.classList.add('is-invalid');
                    mostrarError(data.mensaje);
                } else {
                    campoEmail.classList.remove('is-invalid');
                    campoEmail.classList.add('is-valid');
                }
            } catch (e) {
                console.error("Error Email");
            }
        }
    });

    // --- 3. VALIDACIÓN CP (RegEx España) ---
    campoCP?.addEventListener('blur', () => {
        const regexCP = /^(0[1-9]|[1-4][0-9]|5[0-2])[0-9]{3}$/;
        if (!regexCP.test(campoCP.value)) {
            campoCP.classList.add('is-invalid');
            mostrarError("El Código Postal no es válido en España.");
        } else {
            campoCP.classList.remove('is-invalid');
            campoCP.classList.add('is-valid');
        }
    });

    // --- 4. VALIDACIÓN TELÉFONO (RegEx España) ---
    campoTlf?.addEventListener('blur', () => {
        const regexTlf = /^[6789][0-9]{8}$/;
        if (!regexTlf.test(campoTlf.value)) {
            campoTlf.classList.add('is-invalid');
            mostrarError("Teléfono inválido. Debe tener 9 dígitos y empezar por 6, 7, 8 o 9.");
        } else {
            campoTlf.classList.remove('is-invalid');
            campoTlf.classList.add('is-valid');
        }
    });
});