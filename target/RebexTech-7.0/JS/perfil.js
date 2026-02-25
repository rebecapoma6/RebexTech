document.addEventListener("DOMContentLoaded", () => {

    // --- 1. CONSTANTES (Estilo Profe) ---
    const inputAvatar = document.getElementById('inputAvatar');
    const avatarPreview = document.getElementById('avatarPreview');
    const formDatos = document.getElementById('formUpdateDatos');
    const formPass = document.getElementById('formUpdatePass');

    // --- 2. LÓGICA DE LA IMAGEN (VISTA PREVIA Y SUBIDA) ---
    if (inputAvatar) {
        inputAvatar.addEventListener('change', function (event) {
            const archivo = event.target.files[0];
            if (archivo) {
                // VISTA PREVIA (Como la del profe)
                const reader = new FileReader();
                reader.onload = function (e) {
                    avatarPreview.src = e.target.result;
                    // Opcional: Cambiar también el mini avatar del Navbar si existe
                    const navAvatar = document.querySelector('img.rounded-circle[width="30"]') ||
                            document.querySelector('.navbar img.rounded-circle');
                    if (navAvatar)
                        navAvatar.src = e.target.result;
                };
                reader.readAsDataURL(archivo);

                // SUBIDA AUTOMÁTICA (AJAX)
                const formData = new FormData();
                formData.append("accion", "updateAvatar");
                formData.append("avatar_file", archivo);

                fetch('../UsuarioController', {
                    method: 'POST',
                    body: formData
                })
                        .then(response => response.json())
                        .then(resultado => {
                            if (resultado.status === "success") {
                                // Alerta pequeña (Toast) para que no interrumpa
                                const Toast = Swal.mixin({
                                    toast: true,
                                    position: 'top-end',
                                    showConfirmButton: false,
                                    timer: 3000,
                                    timerProgressBar: true
                                });
                                Toast.fire({
                                    icon: 'success',
                                    title: 'Imagen de perfil actualizada'
                                });
                            }
                        })
                        .catch(error => console.error("Error avatar", error));
            }
        });
    }

    // --- 3. BOTÓN GUARDAR CAMBIOS ---
    if (formDatos) {
        formDatos.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(formDatos);
            formData.append('accion', 'updateDatos');

            try {
                const resp = await fetch('../UsuarioController', {method: 'POST', body: formData});
                const data = await resp.json();

                if (data.status === 'success') {
                    Swal.fire({
                        icon: 'success',
                        title: '¡Actualizado!',
                        text: 'Tus datos se han guardado correctamente.',
                        timer: 1500,
                        timerProgressBar: true
                    }).then(() => {
                        window.location.reload();

                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: data.mensaje || "No se pudo actualizar los datos.",
                        confirmButtonColor: '#d33'
                    });
                }
            } catch (err) {
                Swal.fire('Error', 'Error de conexión con el servidor', 'error');
            }
        });
    }

    // --- 4. BOTÓN ACTUALIZAR CONTRASEÑA ---
    if (formPass) {
        formPass.addEventListener('submit', async (e) => {
            e.preventDefault();

            // Sacamos los valores directamente de los campos del form
            const nPass = formPass.querySelector('input[name="newPass"]').value;
            const cPass = formPass.querySelector('input[name="confirmPass"]').value;

            if (nPass !== cPass) {
                Swal.fire({
                    icon: 'warning',
                    title: 'Atención',
                    text: 'Las nuevas contraseñas no coinciden',
                    confirmButtonColor: '#6f42c1'
                });
                return;
            }

            const formData = new FormData(formPass);
            formData.append('accion', 'updatePass');

            try {
                const resp = await fetch('../UsuarioController', {method: 'POST', body: formData});
                const data = await resp.json();

                if (data.status === 'success') {
                    Swal.fire({
                        icon: 'success',
                        title: '¡Contraseña cambiada!',
                        text: 'Tu seguridad es lo primero.',
                        confirmButtonColor: '#6f42c1',
                        timer: 1500,
                        showConfirmButton: false
                    }).then(() => {
                        // Esto refresca la página donde estás parado. 
                        // Si ya estás en perfil.jsp, se queda ahí pero limpia los campos.
                        window.location.reload();
                    });
                } else {
                    Swal.fire('Error', data.mensaje, 'error');
                }
            } catch (err) {
                Swal.fire('Error', 'Fallo en la comunicación con el servidor', 'error');
            }
        });
    }
});