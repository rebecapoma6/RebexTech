package es.rebextech.beans;

import java.io.Serializable;


public class Producto implements Serializable {

    private short idproducto;
    private byte idcategoria; // FK a categorías
    private String nombre;
    private String descripcion;
    private double precio; // DECIMAL(6,2)
    private String marca;
    private String imagen;

    public Producto() {
    }

    public short getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(short idproducto) {
        this.idproducto = idproducto;
    }

    public byte getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(byte idcategoria) {
        this.idcategoria = idcategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
