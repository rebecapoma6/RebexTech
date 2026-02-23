
package es.rebextech.beans;

import java.io.Serializable;

/**
 *
 * @author User
 */
public class LineaPedido implements Serializable {

    private short idlinea;
    private short idpedido;
    private short idproducto;
    private byte cantidad;

    private Producto producto;
    
    public LineaPedido() {
    }
    
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        if (producto != null) {
            this.idproducto = producto.getIdproducto(); // Sincronizamos el ID
        }
    }

    public short getIdlinea() {
        return idlinea;
    }

    public void setIdlinea(short idlinea) {
        this.idlinea = idlinea;
    }

    public short getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(short idpedido) {
        this.idpedido = idpedido;
    }

    public short getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(short idproducto) {
        this.idproducto = idproducto;
    }

    public byte getCantidad() {
        return cantidad;
    }

    public void setCantidad(byte cantidad) {
        this.cantidad = cantidad;
    }
    
    public double getSubtotal() {
        if (this.producto != null) {
            return this.producto.getPrecio() * this.cantidad;
        }
        return 0;
    }
}
