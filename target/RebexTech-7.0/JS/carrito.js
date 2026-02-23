async function cambiarCantidad(idProd, cambio) {
    // 1. DECLARAMOS TODOS LOS ELEMENTOS QUE VAMOS A TOCAR
    const spanCant = document.getElementById(`cant-${idProd}`);
    const cellSubtotal = document.getElementById(`subtotal-${idProd}`);
    const labelTotalResumen = document.getElementById("totalCarrito");
    const labelTotalFinal = document.getElementById("totalFinal");
    const iconoNavbar = document.getElementById("cantidadProductosNavbar"); // Ajusta este ID según tu navbar

    // 2. LÓGICA DE CÁLCULO PREVIA
    let nuevaCant = parseInt(spanCant.innerText) + cambio;
    if (nuevaCant < 1) return; // No permitimos menos de 1 unidad

    // 3. PREPARAMOS LA PETICIÓN
    const formData = new FormData();
    formData.append("accion", "verCarrito"); 
    formData.append("accionCarrito", "actualizarCantidadCarrito");
    formData.append("id", idProd);
    formData.append("cantidad", nuevaCant);

    try {
        const resp = await fetch('FrontController', { 
            method: 'POST', 
            body: formData 
        });
        
        const data = await resp.json();

        if (data.status === "success") {
            // 4. ACTUALIZAMOS LA VISTA CON LOS DATOS QUE NOS DA EL SERVLET
            
            // Actualizamos el número de la cantidad
            spanCant.innerText = nuevaCant;
            
            // Actualizamos el subtotal de la fila (ej: 100.00€)
            if (cellSubtotal) {
                cellSubtotal.innerText = data.nuevoSubtotal + "€";
            }
            
            // Actualizamos los totales del resumen de la derecha
            if (labelTotalResumen) {
                labelTotalResumen.innerText = data.nuevoTotal + "€";
            }
            if (labelTotalFinal) {
                labelTotalFinal.innerText = data.nuevoTotal + "€";
            }
            
            // Actualizamos el numerito rojo del carrito en el navbar
            if (iconoNavbar) {
                iconoNavbar.innerText = data.nuevoContador; 
            }
        }
    } catch (error) {
        console.error("Error al conectar con el servidor:", error);
    }
}