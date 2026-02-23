document.addEventListener("DOMContentLoaded", () => {
    
    // --- 1. CONSTANTES (Estilo Profe) ---
    const inputAvatar = document.getElementById('inputAvatar');
    const avatarPreview = document.getElementById('avatarPreview');
    const formDatos = document.getElementById('formUpdateDatos');
    const formPass = document.getElementById('formUpdatePass');

    // --- 2. LÓGICA DE LA IMAGEN (VISTA PREVIA Y SUBIDA) ---
    if (inputAvatar) {
        inputAvatar.addEventListener('change', function(event) {
            const archivo = event.target.files[0];
            if (archivo) {
                // VISTA PREVIA (Como la del profe)
                const reader = new FileReader();
                reader.onload = function (e) {
                    avatarPreview.src = e.target.result;
                    // Opcional: Cambiar también el mini avatar del Navbar si existe
                    const navAvatar = document.querySelector('img.rounded-circle[width="30"]') || 
                      document.querySelector('.navbar img.rounded-circle');
                    if(navAvatar) navAvatar.src = e.target.result;
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
                    if (resultado.status === "success") console.log("Avatar guardado en DB");
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
                const resp = await fetch('../UsuarioController', { method: 'POST', body: formData });
                const data = await resp.json();
                
                if (data.status === 'success') {
                    alert("¡Datos actualizados con éxito!");
                } else {
                    alert("Error: " + (data.mensaje || "No se pudo actualizar"));
                }
            } catch (err) { alert("Error de conexión"); }
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
                alert("Las nuevas contraseñas no coinciden");
                return;
            }

            const formData = new FormData(formPass);
            formData.append('accion', 'updatePass');

            try {
                const resp = await fetch('../UsuarioController', { method: 'POST', body: formData });
                const data = await resp.json();
                
                if (data.status === 'success') {
                    alert("Contraseña actualizada");
                    formPass.reset(); // Limpia los campos
                } else {
                    alert("Error: " + data.mensaje);
                }
            } catch (err) { alert("Error al conectar con el servidor"); }
        });
    }
});