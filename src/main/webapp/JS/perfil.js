document.addEventListener("DOMContentLoaded", () => {

    const inicializarAvatarPerfil = () => {
        const inputAvatar = document.getElementById('inputAvatar');
        const avatarPreview = document.getElementById('avatarPreview');

        if (!inputAvatar || !avatarPreview) return;

        inputAvatar.addEventListener('change', (event) => {
            const archivo = event.target.files[0];
            if (!archivo) return;

            const reader = new FileReader();
            reader.onload = (e) => {
                avatarPreview.src = e.target.result;

                const navAvatar = document.querySelector('img.rounded-circle[width="30"]') ||
                                  document.querySelector('.navbar img.rounded-circle');
                if (navAvatar) navAvatar.src = e.target.result;
            };
            reader.readAsDataURL(archivo);

            const formData = new FormData();
            formData.append("accion", "updateAvatar");
            formData.append("avatar_file", archivo);

            fetch(URL_PERFIL, { method: 'POST', body: formData })
                .then(r => r.json())
                .then(res => {
                    if (res.status === "success") {
                        Swal.fire({
                            toast: true,
                            position: 'top-end',
                            icon: 'success',
                            title: 'Imagen de perfil actualizada',
                            showConfirmButton: false,
                            timer: 3000
                        });
                    }
                })
                .catch(err => console.error("Error avatar", err));
        });
    };

    const inicializarFormularioDatos = () => {
        const formDatos = document.getElementById('formUpdateDatos');
        if (!formDatos) return;

        formDatos.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(formDatos);
            formData.append('accion', 'updateDatos');

            try {
                const resp = await fetch(URL_PERFIL, { method: 'POST', body: formData });
                const data = await resp.json();

                if (data.status === 'success') {
                    Swal.fire({
                        icon: 'success',
                        title: '¡Actualizado!',
                        text: 'Tus datos se han guardado correctamente.',
                        timer: 1500,
                        timerProgressBar: true
                    }).then(() => window.location.reload());
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
    };

    const inicializarFormularioPass = () => {
        const formPass = document.getElementById('formUpdatePass');
        if (!formPass) return;

        formPass.addEventListener('submit', async (e) => {
            e.preventDefault();

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
                const resp = await fetch(URL_PERFIL, { method: 'POST', body: formData });
                const data = await resp.json();

                if (data.status === 'success') {
                    Swal.fire({
                        icon: 'success',
                        title: '¡Contraseña cambiada!',
                        text: 'Tu seguridad es lo primero.',
                        confirmButtonColor: '#6f42c1',
                        timer: 1500,
                        showConfirmButton: false
                    }).then(() => window.location.reload());
                } else {
                    Swal.fire('Error', data.mensaje, 'error');
                }
            } catch (err) {
                Swal.fire('Error', 'Fallo en la comunicación con el servidor', 'error');
            }
        });
    };

    // Ejecutar funciones del perfil
    inicializarAvatarPerfil();
    inicializarFormularioDatos();
    inicializarFormularioPass();
});