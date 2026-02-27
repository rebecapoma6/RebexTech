const AJAX_CARRITO = 'CarritoController';

async function cambiarCantidad(idProductoSeleccionado, valorCambio) {
    const etiquetaCantidadActual = document.getElementById(`cant-${idProductoSeleccionado}`);
    const cantidadActual = parseInt(etiquetaCantidadActual.innerText);
    let cantidadCalculada = cantidadActual + valorCambio;

    if (cantidadCalculada < 1) return;

    const parametrosPeticion = new URLSearchParams();
    parametrosPeticion.append("accionCarrito", "actualizarCantidadCarrito");
    parametrosPeticion.append("id", idProductoSeleccionado);
    parametrosPeticion.append("cantidad", cantidadCalculada);

    try {
        const respuestaServidor = await fetch(AJAX_CARRITO, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: parametrosPeticion
        });

        const datosActualizados = await respuestaServidor.json();

        if (datosActualizados.status === "success") {
            // Recargamos la página para que se actualice todo el carrito
            // Es lo más seguro y limpio (Estilo Profe)
            window.location.reload(); 
        }
    } catch (error) {
        console.error("Error en la actualización del carrito:", error);
    }
}