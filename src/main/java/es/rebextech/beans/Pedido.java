package es.rebextech.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Mapea la tabla 'pedidos' y maneja constantes estáticas (ESTADO_CARRITO, ESTADO_FINALIZADO)
 * para transicionar el estado del carrito a una factura confirmada.
 * * @author Rebeca
 */
public class Pedido implements Serializable {

    // Constantes para manejar el ENUM('c', 'f') de MySQL de forma segura
    public static final char ESTADO_CARRITO = 'c';
    public static final char ESTADO_FINALIZADO = 'f';

    private short idpedido;
    private Date fecha;
    private char estado;
    private short idusuario;
    private double importe;
    private double iva;

    // Constructor vacío obligatorio para JavaBeans
    public Pedido() {

        this.estado = ESTADO_CARRITO;
    }

    // Getters y Setters
    public short getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(short idpedido) {
        this.idpedido = idpedido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public short getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(short idusuario) {
        this.idusuario = idusuario;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }
}
