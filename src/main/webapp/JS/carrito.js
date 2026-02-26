async function cambiarCantidad(idProductoSeleccionado, valorCambio) {
   const etiquetaCantidadActual = document.getElementById(`cant-${idProductoSeleccionado}`);
    const celdaSubtotalLinea = document.getElementById(`subtotal-${idProductoSeleccionado}`);
    const etiquetaTotalResumenDerecha = document.getElementById("totalCarrito");
    const etiquetaTotalFinalPago = document.getElementById("totalFinal");
    const circuloNotificacionNavbar = document.getElementById("cantidadProductosNavbar");

    let cantidadCalculada = parseInt(etiquetaCantidadActual.innerText) + valorCambio;
    if (cantidadCalculada < 1) return;

    const parametrosPeticion = new URLSearchParams();
    parametrosPeticion.append("accion", "verCarrito"); 
    parametrosPeticion.append("accionCarrito", "actualizarCantidadCarrito");
    parametrosPeticion.append("id", idProductoSeleccionado);
    parametrosPeticion.append("cantidad", cantidadCalculada);

    try {
        const respuestaServidor = await fetch('CarritoController', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: parametrosPeticion
        });
        
        // LEEMOS DIRECTO COMO JSON (Solo una vez)
        const datosActualizados = await respuestaServidor.json();

        if (datosActualizados.status === "success") {
            etiquetaCantidadActual.innerText = cantidadCalculada;
            
            if (celdaSubtotalLinea) celdaSubtotalLinea.innerText = datosActualizados.nuevoSubtotal + "€";
            if (etiquetaTotalResumenDerecha) etiquetaTotalResumenDerecha.innerText = datosActualizados.nuevoTotal + "€";
            if (etiquetaTotalFinalPago) etiquetaTotalFinalPago.innerText = datosActualizados.nuevoTotal + "€";
            
            if (circuloNotificacionNavbar) {
                circuloNotificacionNavbar.innerText = datosActualizados.nuevoContador;
            }
        }
    } catch (errorConexion) {
        console.error("Hubo un error en la actualización:", errorConexion);
    }
}