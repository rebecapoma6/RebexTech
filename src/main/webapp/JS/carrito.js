async function cambiarCantidad(idProductoSeleccionado, valorCambio) {
    const etiquetaCantidadActual = document.getElementById(`cant-${idProductoSeleccionado}`);
    const cantidadActual = parseInt(etiquetaCantidadActual.innerText);
    let cantidadCalculada = cantidadActual + valorCambio;

    // 1. Evitamos que la cantidad sea menor a 1
    if (cantidadCalculada < 1)
        return;

    // 2. Preparamos los datos para el Servlet
    const parametrosPeticion = new URLSearchParams();
    parametrosPeticion.append("accionCarrito", "actualizarCantidadCarrito");
    parametrosPeticion.append("id", idProductoSeleccionado);
    parametrosPeticion.append("cantidad", cantidadCalculada);

    try {
        // 3. Usamos la constante URL_PROYECTO que definimos en el JSP
        const respuestaServidor = await fetch(RUTA_CARRITO, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: parametrosPeticion
        });

        const datosActualizados = await respuestaServidor.json();

        if (datosActualizados.status === "success") {

            etiquetaCantidadActual.innerText = cantidadCalculada;

            // Actualizamos el subtotal de la línea
            const celdaSubtotal = document.getElementById(`subtotal-${idProductoSeleccionado}`);
            if (celdaSubtotal)
                celdaSubtotal.innerText = datosActualizados.nuevoSubtotal + "€";

            // Actualizamos el resumen de la derecha
            const etiquetaTotalResumen = document.getElementById("totalCarrito");
            const etiquetaTotalFinal = document.getElementById("totalFinal");
            if (etiquetaTotalResumen)
                etiquetaTotalResumen.innerText = datosActualizados.nuevoTotal + "€";
            if (etiquetaTotalFinal)
                etiquetaTotalFinal.innerText = datosActualizados.nuevoTotal + "€";

            // Actualizamos el contador del Navbar
            const iconoNavbar = document.getElementById("cantidadProductosNavbar");
            if (iconoNavbar)
                iconoNavbar.innerText = datosActualizados.nuevoContador;
        }
    } catch (error) {
        console.error("Error en la actualización AJAX:", error);
    }
}