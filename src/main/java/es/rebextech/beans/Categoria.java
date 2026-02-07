/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.rebextech.beans;

import java.io.Serializable;

/**
 *
 * @author User
 */
public class Categoria implements Serializable {

    private byte idcategoria; // TINYINT (1 byte)
    private String nombre;
    private String imagen;

    public Categoria() {
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
